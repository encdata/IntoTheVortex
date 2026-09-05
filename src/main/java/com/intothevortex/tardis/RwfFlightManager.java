package com.intothevortex.tardis;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.entity.TardisExteriorEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.Vec3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class RwfFlightManager {
    private static final Map<MinecraftServer, Map<UUID, Flight>> ACTIVE = new java.util.WeakHashMap<>();
    private static final Map<MinecraftServer, java.util.Set<UUID>> RESTORED = new java.util.WeakHashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private RwfFlightManager() {
    }

    public static boolean isActive(MinecraftServer server, UUID tardisId) {
        Map<UUID, Flight> flights = ACTIVE.get(server);
        return flights != null && flights.containsKey(tardisId);
    }

    public static boolean isPilot(MinecraftServer server, UUID playerId) {
        Map<UUID, Flight> flights = ACTIVE.get(server);
        if (flights == null) return false;
        return flights.values().stream().anyMatch(flight -> flight.pilotId().equals(playerId));
    }

    public static void onPlayerJoin(ServerPlayer player) {
        MinecraftServer server = player.level().getServer();
        restore(server);
        for (var entry : new java.util.ArrayList<>(flights(server).entrySet())) {
            Flight flight = entry.getValue();
            if (flight.pilotId().equals(player.getUUID())) {
                stop(server, entry.getKey(), false);
                break;
            }
        }
        player.setNoGravity(false);
        player.setInvisible(false);
        player.onUpdateAbilities();
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, new com.intothevortex.network.RwfStatePayload(false));
    }

    public static void onPlayerDisconnect(MinecraftServer server, UUID playerId) {
        for (var entry : new java.util.ArrayList<>(flights(server).entrySet())) {
            Flight flight = entry.getValue();
            if (!flight.pilotId().equals(playerId)) continue;
            UUID tardisId = entry.getKey();
            ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(flight.dimension()));
            if (level != null && level.getEntity(flight.exteriorId()) instanceof TardisExteriorEntity exterior) {
                exterior.setRwfFlight(false);
                exterior.setRwfPilot(null);
                TardisData data = TardisManager.get(server, tardisId);
                if (data != null) TardisManager.save(server, data.withExteriorLocation(new TardisTravelDestination(flight.dimension(), exterior.blockPosition(), exterior.getYRot())));
            }
            TardisControlStateManager.set(server, tardisId, "cloak", flight.originalCloak() ? 1.0F : 0.0F);
            flights(server).remove(tardisId);
            deleteFile(server, tardisId);
        }
    }

    public static boolean start(ServerPlayer pilot, UUID tardisId) {
        MinecraftServer server = pilot.level().getServer();
        if (isActive(server, tardisId)) return false;
        UUID interiorId = TardisDimensionManager.id(pilot.level().dimension());
        if (!tardisId.equals(interiorId)) return false;
        TardisData data = TardisManager.get(server, tardisId);
        if (data == null || data.travelState() != TardisTravelState.LANDED || data.isCrashed() || !data.powered() || !data.isHandbrakeEngaged()) {
            pilot.sendSystemMessage(net.minecraft.network.chat.Component.literal("RWF requires a powered, landed TARDIS with the handbrake engaged."));
            return false;
        }
        ServerLevel exteriorLevel = server.getLevel(TardisDimensionManager.parseDimension(data.dimension()));
        if (exteriorLevel == null || !data.requestedDestinationDimension().equals(data.dimension())) return false;
        exteriorLevel.getChunkAt(data.position());
        TardisExteriorEntity exterior = findExistingExterior(exteriorLevel, data);
        if (exterior == null) {
            pilot.sendSystemMessage(net.minecraft.network.chat.Component.literal("RWF failed: the canonical exterior is not loaded."));
            return false;
        }
        if (!exterior.getUUID().equals(data.exteriorId())) {
            data = data.withExterior(exterior.getUUID());
            TardisManager.saveCanonicalExterior(server, data);
        }
        boolean originalCloak = TardisControlStateManager.enabled(server, tardisId, "cloak");
        TardisControlStateManager.set(server, tardisId, "cloak", 0.0F);
        Flight flight = new Flight(pilot.getUUID(), exterior.getUUID(), exteriorLevel.dimension().identifier().toString(), exterior.position(), Vec3.atBottomCenterOf(data.requestedDestinationPosition()), exterior.getYRot(), data.requestedDestinationYaw(), 0, pilot.getAbilities().mayfly, pilot.getAbilities().flying, pilot.isNoGravity(), pilot.isInvisible(), originalCloak);
        flights(server).put(tardisId, flight);
        write(server, tardisId, flight);
        exterior.setRwfFlight(true);
        exterior.setRwfPilot(pilot.getUUID());
        exterior.syncRwfTarget(exterior.getX(), exterior.getY(), exterior.getZ());
        pilot.getAbilities().mayfly = true;
        pilot.getAbilities().flying = true;
        pilot.setNoGravity(true);
        pilot.setInvisible(true);
        pilot.onUpdateAbilities();
        pilot.teleport(new net.minecraft.world.level.portal.TeleportTransition(exteriorLevel, exterior.position().add(0.0D, 0.2D, 0.0D), Vec3.ZERO, exterior.getYRot(), pilot.getXRot(), java.util.Set.of(), net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING));
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(pilot, new com.intothevortex.network.RwfStatePayload(true));
        pilot.sendSystemMessage(net.minecraft.network.chat.Component.literal("Real-world flight engaged. Press Tab to land."));
        return true;
    }

    public static void tick(MinecraftServer server) {
        restore(server);
        Map<UUID, Flight> flights = ACTIVE.get(server);
        if (flights == null || flights.isEmpty()) return;
        for (UUID tardisId : new java.util.ArrayList<>(flights.keySet())) {
            Flight flight = flights.get(tardisId);
            TardisData data = TardisManager.get(server, tardisId);
            ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(flight.dimension()));
            if (data == null || level == null) {
                continue;
            }
            level.getChunkAt(BlockPosHelper.from(flight.positionX(), flight.positionY(), flight.positionZ()));
            TardisExteriorEntity exterior = level.getEntity(flight.exteriorId()) instanceof TardisExteriorEntity found ? found : findExistingExterior(level, data);
            if (exterior == null) exterior = recoverExterior(server, data, flight);
            if (exterior == null) continue;
            ServerPlayer pilot = server.getPlayerList().getPlayer(flight.pilotId());
            if (pilot == null || pilot.isRemoved()) {
                continue;
            }
            if (pilot.level() != level) continue;
            int elapsed = flight.elapsed() + 1;
            Vec3 position = pilot.position();
            Vec3 motion = position.subtract(exterior.position());
            exterior.teleportTo(position.x, position.y, position.z);
            exterior.setDeltaMovement(motion);
            exterior.setRwfFlight(true);
            exterior.setRwfPilot(flight.pilotId());
            exterior.syncRwfTarget(position.x, position.y, position.z);
            if (motion.horizontalDistanceSqr() > 0.0001D) {
                exterior.setYRot((float) Math.toDegrees(Math.atan2(motion.x, motion.z)));
            }
            exterior.setYHeadRot(exterior.getYRot());
            Flight updated = flight.withPosition(position).withElapsed(elapsed);
            flights.put(tardisId, updated);
            if (elapsed % 5 == 0) {
                write(server, tardisId, updated);
                TardisManager.save(server, data.withExteriorLocation(new TardisTravelDestination(flight.dimension(), exterior.blockPosition(), exterior.getYRot())));
            }
        }
    }

    public static boolean stop(MinecraftServer server, UUID tardisId, boolean landed) {
        Map<UUID, Flight> flights = ACTIVE.get(server);
        Flight flight = flights == null ? null : flights.remove(tardisId);
        if (flight == null) return false;
        TardisData data = TardisManager.get(server, tardisId);
        ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(flight.dimension()));
        if (data != null && level != null && level.getEntity(flight.exteriorId()) instanceof TardisExteriorEntity exterior) {
            TardisTravelDestination requested = new TardisTravelDestination(flight.dimension(), exterior.blockPosition(), exterior.getYRot());
            LandingResult result = TardisLandingSearch.resolve(level, requested, LandingSearchMode.NORMAL);
            if (!result.success()) result = TardisLandingSearch.resolve(level, requested, LandingSearchMode.EMERGENCY);
            if (result.success()) {
                TardisTravelDestination safe = result.resolvedDestination();
                exterior.setPos(safe.position().getX() + 0.5D, safe.position().getY(), safe.position().getZ() + 0.5D);
                exterior.setYRot(safe.yaw());
                exterior.setYHeadRot(safe.yaw());
                exterior.syncRwfTarget(exterior.getX(), exterior.getY(), exterior.getZ());
                TardisManager.save(server, data.withExteriorLocation(safe).withFlightControls(0, data.isHandbrakeEngaged()));
            } else {
                TardisManager.save(server, data.withExteriorLocation(requested).withFlightControls(0, data.isHandbrakeEngaged()));
            }
            exterior.setRwfFlight(false);
            exterior.setRwfPilot(null);
            exterior.syncRwfTarget(exterior.getX(), exterior.getY(), exterior.getZ());
            TardisControlStateManager.set(server, tardisId, "cloak", flight.originalCloak() ? 1.0F : 0.0F);
        }
        deleteFile(server, tardisId);
        ServerPlayer pilot = server.getPlayerList().getPlayer(flight.pilotId());
        if (pilot != null) {
            pilot.getAbilities().mayfly = flight.originalMayFly();
            pilot.getAbilities().flying = flight.originalFlying();
            pilot.setNoGravity(flight.originalNoGravity());
            pilot.setInvisible(flight.originalInvisible());
            pilot.onUpdateAbilities();
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(pilot, new com.intothevortex.network.RwfStatePayload(false));
            ServerLevel interior = TardisDimensionManager.ensureLoaded(server, tardisId);
            net.minecraft.core.BlockPos door = interior == null ? null : TardisDimensionManager.interiorDoor(interior);
            if (interior != null && door != null && pilot.connection != null) {
                Vec3 arrival = TardisDimensionManager.interiorArrival(interior, door);
                pilot.teleport(new net.minecraft.world.level.portal.TeleportTransition(interior, arrival, Vec3.ZERO, interior.getBlockState(door).getValue(com.intothevortex.interior.InteriorDoorBlock.FACING).toYRot(), 0.0F, java.util.Set.of(), net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING));
            }
            pilot.sendSystemMessage(net.minecraft.network.chat.Component.literal(landed ? "Real-world flight landed." : "Real-world flight ended."));
        }
        return true;
    }

    public static boolean exit(ServerPlayer pilot) {
        MinecraftServer server = pilot.level().getServer();
        Map<UUID, Flight> flights = ACTIVE.getOrDefault(server, Map.of());
        for (var entry : flights.entrySet()) if (entry.getValue().pilotId().equals(pilot.getUUID())) return stop(server, entry.getKey(), true);
        return false;
    }

    public static TardisExteriorEntity exterior(MinecraftServer server, UUID tardisId) {
        Flight flight = ACTIVE.getOrDefault(server, Map.of()).get(tardisId);
        if (flight == null) return null;
        ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(flight.dimension()));
        return level != null && level.getEntity(flight.exteriorId()) instanceof TardisExteriorEntity exterior ? exterior : null;
    }

    private static Map<UUID, Flight> flights(MinecraftServer server) {
        return ACTIVE.computeIfAbsent(server, ignored -> new HashMap<>());
    }

    private static void restore(MinecraftServer server) {
        if (RESTORED.computeIfAbsent(server, ignored -> new java.util.HashSet<>()).isEmpty()) {
            Path folder = server.getWorldPath(LevelResource.ROOT).resolve("IntoTheVortex");
            if (!Files.isDirectory(folder)) return;
            try (var files = Files.list(folder)) {
                files.filter(path -> path.getFileName().toString().startsWith("rwf-") && path.getFileName().toString().endsWith(".json")).forEach(path -> {
                    String name = path.getFileName().toString().substring(4, path.getFileName().toString().length() - 5);
                    try {
                        UUID id = UUID.fromString(name);
                        Flight flight = GSON.fromJson(Files.readString(path, StandardCharsets.UTF_8), Flight.class);
                        if (flight != null) flights(server).put(id, flight);
                        RESTORED.get(server).add(id);
                    } catch (Exception exception) {
                        throw new IllegalStateException("Could not restore RWF state " + path, exception);
                    }
                });
            } catch (java.io.IOException exception) {
                throw new java.io.UncheckedIOException(exception);
            }
        }
    }

    private static TardisExteriorEntity recoverExterior(MinecraftServer server, TardisData data, Flight flight) {
        TardisData positioned = data.withExteriorLocation(new TardisTravelDestination(flight.dimension(), BlockPosHelper.from(flight.positionX(), flight.positionY(), flight.positionZ()), flight.startYaw()));
        TardisManager.save(server, positioned);
        return TardisManager.spawnExterior(server, positioned).exteriorId().equals(flight.exteriorId()) ? find(server, positioned, flight.exteriorId()) : null;
    }

    private static TardisExteriorEntity findExistingExterior(ServerLevel level, TardisData data) {
        if (level.getEntity(data.exteriorId()) instanceof TardisExteriorEntity exterior && exterior.getTardisId().equals(data.id())) return exterior;
        TardisExteriorEntity fallback = null;
        for (var entity : level.getAllEntities()) {
            if (entity instanceof TardisExteriorEntity exterior && exterior.getTardisId().equals(data.id())) {
                if (exterior.getUUID().equals(data.exteriorId())) return exterior;
                if (fallback == null) fallback = exterior;
            }
        }
        return fallback;
    }

    private static TardisExteriorEntity find(MinecraftServer server, TardisData data, UUID entityId) {
        ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(data.dimension()));
        if (level == null) return null;
        return level.getEntity(entityId) instanceof TardisExteriorEntity exterior ? exterior : null;
    }

    private static void write(MinecraftServer server, UUID id, Flight flight) {
        try {
            Path folder = server.getWorldPath(LevelResource.ROOT).resolve("IntoTheVortex");
            Files.createDirectories(folder);
            Files.writeString(folder.resolve("rwf-" + id + ".json"), GSON.toJson(flight), StandardCharsets.UTF_8);
        } catch (java.io.IOException exception) {
            throw new java.io.UncheckedIOException(exception);
        }
    }

    private static void deleteFile(MinecraftServer server, UUID id) {
        try {
            Files.deleteIfExists(server.getWorldPath(LevelResource.ROOT).resolve("IntoTheVortex").resolve("rwf-" + id + ".json"));
        } catch (java.io.IOException exception) {
            throw new java.io.UncheckedIOException(exception);
        }
    }

    private static float interpolateYaw(float start, float end, double progress) {
        return start + net.minecraft.util.Mth.wrapDegrees(end - start) * (float) progress;
    }

    private record Flight(UUID pilotId, UUID exteriorId, String dimension, Vec3 start, Vec3 destination, float startYaw, float destinationYaw, int elapsed, double positionX, double positionY, double positionZ, boolean originalMayFly, boolean originalFlying, boolean originalNoGravity, boolean originalInvisible, boolean originalCloak) {
        private Flight(UUID pilotId, UUID exteriorId, String dimension, Vec3 start, Vec3 destination, float startYaw, float destinationYaw, int elapsed, boolean originalMayFly, boolean originalFlying, boolean originalNoGravity, boolean originalInvisible, boolean originalCloak) {
            this(pilotId, exteriorId, dimension, start, destination, startYaw, destinationYaw, elapsed, start.x, start.y, start.z, originalMayFly, originalFlying, originalNoGravity, originalInvisible, originalCloak);
        }

        private Flight withElapsed(int value) {
            return new Flight(pilotId, exteriorId, dimension, start, destination, startYaw, destinationYaw, value, positionX, positionY, positionZ, originalMayFly, originalFlying, originalNoGravity, originalInvisible, originalCloak);
        }

        private Flight withPosition(Vec3 value) {
            return new Flight(pilotId, exteriorId, dimension, start, destination, startYaw, destinationYaw, elapsed, value.x, value.y, value.z, originalMayFly, originalFlying, originalNoGravity, originalInvisible, originalCloak);
        }
    }

    private static final class BlockPosHelper {
        private static net.minecraft.core.BlockPos from(double x, double y, double z) {
            return new net.minecraft.core.BlockPos(net.minecraft.util.Mth.floor(x), net.minecraft.util.Mth.floor(y), net.minecraft.util.Mth.floor(z));
        }
    }
}
