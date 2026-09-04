package com.intothevortex.tardis;

import com.intothevortex.entity.TardisExteriorEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.LevelResource;
import com.intothevortex.exterior.TardisAnimationManager;
import net.minecraft.resources.Identifier;

public final class TardisManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("IntoTheVortex/TardisExterior");
    private static final Map<MinecraftServer, Map<UUID, TardisData>> ACTIVE_TARDISES = new WeakHashMap<>();
    private static final Map<MinecraftServer, Map<UUID, UUID>> ACTIVE_EXTERIOR_ENTITIES = new WeakHashMap<>();
    private static final Map<MinecraftServer, Map<UUID, Long>> EXTERIOR_SPAWN_PENDING = new WeakHashMap<>();
    private static final Map<MinecraftServer, Map<UUID, UUID>> CANONICAL_EXTERIORS = new WeakHashMap<>();
    private static final Map<MinecraftServer, Set<UUID>> RECONCILIATIONS_QUEUED = new WeakHashMap<>();
    private static final Map<MinecraftServer, Map<UUID, Long>> LAST_RECOVERY_ATTEMPTS = new WeakHashMap<>();
    private static final long RECOVERY_COOLDOWN_TICKS = 200L;
    private TardisManager() {
    }

    public static TardisData create(ServerPlayer owner, ServerLevel level, BlockPos position) {
        TardisData tardis = new TardisData(
                UUID.randomUUID(),
                owner.getUUID(),
                new UUID(0L, 0L),
                "intothevortex:default",
                "intothevortex:70default",
                level.dimension().identifier().toString(),
                position,
                new BlockPos(0, 64, 0),
                false,
                owner.getYRot() + 180.0F,
                false,
                false,
                false,
                false,
                false,
                "intothevortex:door_swing",
                "intothevortex:pulsating_demat",
                "intothevortex:pulsating_mat",
                TardisTravelState.LANDED,
                0,
                0,
                0,
                level.dimension().identifier().toString(),
                position,
                owner.getYRot() + 180.0F,
                level.dimension().identifier().toString(),
                position,
                owner.getYRot() + 180.0F
        );
        save(level.getServer(), tardis);
        TardisLoyaltyManager.initialize(level.getServer(), tardis.id(), tardis.ownerId());
        return spawnExterior(level.getServer(), tardis);
    }

    public static TardisData spawnExterior(MinecraftServer server, TardisData tardis) {
        markExteriorSpawnPending(server, tardis.id());
        ServerLevel level = getLevel(server, tardis.dimension());
        if (level == null) {
            LOGGER.warn("Cannot reconcile exterior for TARDIS {}: dimension {} is not loaded", tardis.id(), tardis.dimension());
            clearExteriorSpawnPending(server, tardis.id());
            return tardis;
        }
        double x = tardis.position().getX() + 0.5D;
        double y = tardis.position().getY();
        double z = tardis.position().getZ() + 0.5D;
        TardisExteriorEntity entity = findExterior(level, tardis);
        boolean existing = entity != null;
        if (!existing) {
            entity = new TardisExteriorEntity(level, tardis.id());
            UUID savedExteriorId = tardis.exteriorId();
            if (!isZero(savedExteriorId)) entity.setUUID(savedExteriorId);
        }
        int duplicates = removeDuplicateExteriors(level, tardis.id(), entity);
        entity.setExterior(tardis.exterior());
        entity.setPos(x, y, z);
        entity.setYRot(tardis.yaw());
        entity.setYHeadRot(tardis.yaw());
        entity.syncDoorState(tardis.doorOpen() && !tardis.locked());
        TardisData updated = tardis.withExterior(entity.getUUID());
        ACTIVE_EXTERIOR_ENTITIES.computeIfAbsent(server, ignored -> new HashMap<>()).put(updated.id(), entity.getUUID());
        saveCanonicalExterior(server, updated);
        if (!existing) level.addFreshEntity(entity);
        clearExteriorSpawnPending(server, tardis.id());
        LOGGER.info("{} exterior entity {} for TARDIS {} at {} in {}{}", existing ? "Reconciled" : "Created", entity.getUUID(), tardis.id(), tardis.position(), tardis.dimension(), duplicates == 0 ? "" : "; removed " + duplicates + " duplicate(s)");
        TardisAnimationManager.register(updated.id(), Identifier.parse(updated.exterior()));
        return updated;
    }

    private static int removeDuplicateExteriors(ServerLevel level, UUID tardisId, TardisExteriorEntity keep) {
        int removed = 0;
        for (var entity : level.getAllEntities()) {
            if (entity instanceof TardisExteriorEntity exterior && exterior != keep && exterior.getTardisId().equals(tardisId)) {
                exterior.discard();
                removed++;
            }
        }
        return removed;
    }

    private static TardisExteriorEntity findExterior(ServerLevel level, TardisData tardis) {
        level.getChunkAt(tardis.position());
        LOGGER.debug("Reconciling exterior chunk for TARDIS {} at {}", tardis.id(), tardis.position());
        UUID savedId = tardis.exteriorId();
        if (savedId != null && !savedId.equals(new UUID(0L, 0L))) {
            var savedEntity = level.getEntity(savedId);
            if (savedEntity instanceof TardisExteriorEntity exterior && exterior.getTardisId().equals(tardis.id())) return exterior;
        }
        if (isZero(savedId)) for (var entity : level.getAllEntities()) {
            if (entity instanceof TardisExteriorEntity exterior && exterior.getTardisId().equals(tardis.id())) return exterior;
        }
        return null;
    }

    public static TardisData get(MinecraftServer server, UUID id) {
        Map<UUID, TardisData> active = ACTIVE_TARDISES.get(server);
        if (active != null) {
            TardisData cached = active.get(id);
            if (cached != null) return cached;
        }
        Path file = folder(server).resolve(id + ".json");
        if (!Files.isRegularFile(file)) {
            return null;
        }
        try {
            StoredTardis stored = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), StoredTardis.class);
            TardisData loaded = stored.toData().sanitized();
            ACTIVE_TARDISES.computeIfAbsent(server, ignored -> new HashMap<>()).put(id, loaded);
            CANONICAL_EXTERIORS.computeIfAbsent(server, ignored -> new HashMap<>()).put(id, loaded.exteriorId());
            if (!isZero(loaded.exteriorId())) ACTIVE_EXTERIOR_ENTITIES.computeIfAbsent(server, ignored -> new HashMap<>()).put(id, loaded.exteriorId());
            return loaded;
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static void save(MinecraftServer server, TardisData tardis) {
        TardisData normalized = tardis.sanitized();
        TardisData active = active(server, normalized.id());
        if (active != null && active != tardis && !active.exteriorId().equals(normalized.exteriorId())) {
            LOGGER.debug("Preserving active exterior {} for TARDIS {} over stale save {}", active.exteriorId(), normalized.id(), normalized.exteriorId());
            normalized = normalized.withExterior(active.exteriorId());
        }
        putActive(server, normalized);
        write(server, normalized);
    }

    public static void saveCanonicalExterior(MinecraftServer server, TardisData tardis) {
        TardisData normalized = tardis.sanitized();
        putActive(server, normalized);
        CANONICAL_EXTERIORS.computeIfAbsent(server, ignored -> new HashMap<>()).put(normalized.id(), normalized.exteriorId());
        write(server, normalized);
    }

    private static TardisData active(MinecraftServer server, UUID id) {
        Map<UUID, TardisData> values = ACTIVE_TARDISES.get(server);
        return values == null ? null : values.get(id);
    }

    private static void putActive(MinecraftServer server, TardisData tardis) {
        ACTIVE_TARDISES.computeIfAbsent(server, ignored -> new HashMap<>()).put(tardis.id(), tardis);
        CANONICAL_EXTERIORS.computeIfAbsent(server, ignored -> new HashMap<>()).put(tardis.id(), tardis.exteriorId());
    }

    private static UUID canonicalExterior(MinecraftServer server, UUID tardisId) {
        Map<UUID, UUID> values = CANONICAL_EXTERIORS.get(server);
        if (values != null && values.containsKey(tardisId)) return values.get(tardisId);
        TardisData current = get(server, tardisId);
        if (current == null) return null;
        UUID exteriorId = current.exteriorId();
        CANONICAL_EXTERIORS.computeIfAbsent(server, ignored -> new HashMap<>()).put(tardisId, exteriorId);
        return exteriorId;
    }

    public static void clearCanonicalExterior(MinecraftServer server, UUID tardisId) {
        Map<UUID, UUID> values = CANONICAL_EXTERIORS.get(server);
        if (values != null) values.remove(tardisId);
        Map<UUID, TardisData> active = ACTIVE_TARDISES.get(server);
        if (active != null) active.remove(tardisId);
        Map<UUID, UUID> entities = ACTIVE_EXTERIOR_ENTITIES.get(server);
        if (entities != null) entities.remove(tardisId);
        Set<UUID> queued = RECONCILIATIONS_QUEUED.get(server);
        if (queued != null) queued.remove(tardisId);
        Map<UUID, Long> attempts = LAST_RECOVERY_ATTEMPTS.get(server);
        if (attempts != null) attempts.remove(tardisId);
        Map<UUID, Long> pending = EXTERIOR_SPAWN_PENDING.get(server);
        if (pending != null) pending.remove(tardisId);
    }

    public static boolean isCanonicalExterior(MinecraftServer server, UUID tardisId, UUID entityId) {
        Map<UUID, UUID> entities = ACTIVE_EXTERIOR_ENTITIES.get(server);
        if (entities != null && entities.containsKey(tardisId)) return entityId.equals(entities.get(tardisId));
        TardisData data = get(server, tardisId);
        return data != null && entityId.equals(data.exteriorId());
    }

    public static void reconcileLoadedChunk(ServerLevel level, LevelChunk chunk) {
        MinecraftServer server = level.getServer();
        String dimension = level.dimension().identifier().toString();
        for (UUID id : ids(server)) {
            TardisData data = get(server, id);
            if (data == null || (data.travelState() != TardisTravelState.LANDED && data.travelState() != TardisTravelState.MAT) || !dimension.equals(data.dimension())) continue;
            if ((data.position().getX() >> 4) != chunk.getPos().x() || (data.position().getZ() >> 4) != chunk.getPos().z()) continue;
            Set<UUID> queued = RECONCILIATIONS_QUEUED.computeIfAbsent(server, ignored -> new HashSet<>());
            queued.add(id);
        }
    }

    public static void tickReconciliations(MinecraftServer server) {
        Set<UUID> queued = RECONCILIATIONS_QUEUED.get(server);
        if (queued == null || queued.isEmpty()) return;
        UUID[] ids = queued.toArray(UUID[]::new);
        for (UUID id : ids) {
            try {
                TardisData data = get(server, id);
                    if (data != null && (data.travelState() == TardisTravelState.LANDED || data.travelState() == TardisTravelState.MAT) && recoveryAttemptAllowed(server, id)) {
                        markRecoveryAttempt(server, id);
                        spawnExterior(server, data);
                    }
            } finally {
                queued.remove(id);
            }
        }
    }

    public static void tickLoadedExteriorRecovery(MinecraftServer server) {
        for (UUID id : ids(server)) {
            TardisData data = get(server, id);
            if (data == null || (data.travelState() != TardisTravelState.LANDED && data.travelState() != TardisTravelState.MAT)) continue;
            if (isExteriorSpawnPending(server, id)) continue;
            ServerLevel level = getLevel(server, data.dimension());
            if (level == null || !level.hasChunkAt(data.position())) continue;
            if (isCanonicalExteriorLoaded(level, data)) continue;
            if (!recoveryAttemptAllowed(server, id)) continue;
            markRecoveryAttempt(server, id);
            spawnExterior(server, data);
        }
    }

    public static void markExteriorSpawnPending(MinecraftServer server, UUID tardisId) {
        EXTERIOR_SPAWN_PENDING.computeIfAbsent(server, ignored -> new HashMap<>()).put(tardisId, (long) server.getTickCount());
    }

    public static void clearExteriorSpawnPending(MinecraftServer server, UUID tardisId) {
        Map<UUID, Long> pending = EXTERIOR_SPAWN_PENDING.get(server);
        if (pending != null) pending.remove(tardisId);
    }

    private static boolean isExteriorSpawnPending(MinecraftServer server, UUID tardisId) {
        Map<UUID, Long> pending = EXTERIOR_SPAWN_PENDING.get(server);
        if (pending == null) return false;
        Long started = pending.get(tardisId);
        if (started == null) return false;
        if (server.getTickCount() - started > RECOVERY_COOLDOWN_TICKS) {
            pending.remove(tardisId);
            return false;
        }
        return true;
    }

    private static boolean isCanonicalExteriorLoaded(ServerLevel level, TardisData data) {
        UUID canonical = data.exteriorId();
        if (!isZero(canonical)) {
            var entity = level.getEntity(canonical);
            if (entity instanceof TardisExteriorEntity exterior && exterior.getTardisId().equals(data.id()) && !exterior.isRemoved()) return true;
        }
        for (var entity : level.getAllEntities()) {
            if (entity instanceof TardisExteriorEntity exterior && exterior.getTardisId().equals(data.id()) && !exterior.isRemoved()) return true;
        }
        return false;
    }

    private static boolean recoveryAttemptAllowed(MinecraftServer server, UUID tardisId) {
        Map<UUID, Long> attempts = LAST_RECOVERY_ATTEMPTS.get(server);
        if (attempts == null) return true;
        Long last = attempts.get(tardisId);
        return last == null || server.getTickCount() - last >= RECOVERY_COOLDOWN_TICKS;
    }

    private static void markRecoveryAttempt(MinecraftServer server, UUID tardisId) {
        LAST_RECOVERY_ATTEMPTS.computeIfAbsent(server, ignored -> new HashMap<>()).put(tardisId, (long) server.getTickCount());
    }

    private static boolean isZero(UUID value) {
        return value == null || value.equals(new UUID(0L, 0L));
    }

    private static void write(MinecraftServer server, TardisData tardis) {
        try {
            Files.createDirectories(folder(server));
            Files.writeString(folder(server).resolve(tardis.id() + ".json"), GSON.toJson(StoredTardis.from(tardis)), StandardCharsets.UTF_8);
            com.intothevortex.network.TardisFlightSync.sendIfChanged(server, tardis);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static TardisData switchInterior(MinecraftServer server, UUID id, String interiorId) {
        TardisData data = get(server, id);
        if (data == null) throw new IllegalArgumentException("Unknown TARDIS " + id);
        if (!interiorId.contains(":")) interiorId = "intothevortex:" + interiorId;
        TardisData updated = data.withInterior(interiorId);
        save(server, updated);
        return updated;
    }

    public static List<UUID> ids(MinecraftServer server) {
        try (Stream<Path> files = Files.list(folder(server))) {
            return files.filter(path -> path.getFileName().toString().endsWith(".json")).map(path -> path.getFileName().toString().replace(".json", "")).map(value -> { try { return UUID.fromString(value); } catch (IllegalArgumentException exception) { return null; } }).filter(java.util.Objects::nonNull).toList();
        } catch (IOException exception) {
            return List.of();
        }
    }

    private static ServerLevel getLevel(MinecraftServer server, String dimension) {
        ResourceKey<Level> key = ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, Identifier.parse(dimension));
        return server.getLevel(key);
    }

    private static Path folder(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("IntoTheVortex");
    }

    private record StoredTardis(UUID id, UUID ownerId, UUID exteriorId, String exterior, String interior, String dimension, int x, int y, int z, int interiorDoorX, int interiorDoorY, int interiorDoorZ, boolean interiorDoorStored, float yaw, boolean locked, boolean doorOpen, boolean powered, boolean refueling, boolean interiorInitialized, String doorAnimation, String dematAnimation, String matAnimation, TardisTravelState travelState, int phaseTicks, int flightTicks, int targetFlightTicks, String travelSourceDimension, int travelSourceX, int travelSourceY, int travelSourceZ, float travelSourceYaw, String travelDestinationDimension, int travelDestinationX, int travelDestinationY, int travelDestinationZ, float travelDestinationYaw, int throttleStage, boolean handbrakeEngaged, Double currentFuel, Double maxFuel, Double travelFuelCost, Boolean fuelCommitted, Integer dataVersion, FlightCondition flightCondition, FlightFailureType lastFailureType, String lastFailureDetails, String requestedDestinationDimension, Integer requestedDestinationX, Integer requestedDestinationY, Integer requestedDestinationZ, Float requestedDestinationYaw, String activeFlightEvent, String activeEventControl, Integer activeEventRemaining, Integer activeEventElapsed, Integer activeEventStep, Boolean activeEventConsequenceApplied, String activeEventData, Boolean autopilot) {
        private static StoredTardis from(TardisData data) {
            return new StoredTardis(data.id(), data.ownerId(), data.exteriorId(), data.exterior(), data.interior(), data.dimension(), data.position().getX(), data.position().getY(), data.position().getZ(), data.interiorDoor().getX(), data.interiorDoor().getY(), data.interiorDoor().getZ(), data.interiorDoorStored(), data.yaw(), data.locked(), data.doorOpen(), data.powered(), data.refueling(), data.interiorInitialized(), data.doorAnimation(), data.dematAnimation(), data.matAnimation(), data.travelState(), data.phaseTicks(), data.flightTicks(), data.targetFlightTicks(), data.travelSourceDimension(), data.travelSourcePosition().getX(), data.travelSourcePosition().getY(), data.travelSourcePosition().getZ(), data.travelSourceYaw(), data.travelDestinationDimension(), data.travelDestinationPosition().getX(), data.travelDestinationPosition().getY(), data.travelDestinationPosition().getZ(), data.travelDestinationYaw(), data.getThrottleStage(), data.isHandbrakeEngaged(), data.fuel(), data.maxFuel(), data.travelFuelCost(), data.fuelCommitted(), data.dataVersion(), data.flightCondition(), data.lastFailureType(), data.lastFailureDetails(), data.requestedDestinationDimension(), data.requestedDestinationPosition().getX(), data.requestedDestinationPosition().getY(), data.requestedDestinationPosition().getZ(), data.requestedDestinationYaw(), data.activeFlightEvent(), data.activeEventControl(), data.activeEventRemaining(), data.activeEventElapsed(), data.activeEventStep(), data.activeEventConsequenceApplied(), data.activeEventData(), data.autopilot());
        }

        private TardisData toData() {
            BlockPos door = interiorDoorY == 0 ? new BlockPos(0, 64, 0) : new BlockPos(interiorDoorX, interiorDoorY, interiorDoorZ);
            String demat = dematAnimation == null || dematAnimation.equals("intothevortex:default") ? "intothevortex:pulsating_demat" : dematAnimation;
            String mat = matAnimation == null || matAnimation.equals("intothevortex:default") ? "intothevortex:pulsating_mat" : matAnimation;
            boolean legacyFuel = currentFuel == null && maxFuel == null;
            double capacity = maxFuel == null ? TardisFuelManager.DEFAULT_MAX_FUEL : maxFuel;
            double amount = currentFuel == null ? (legacyFuel ? TardisFuelManager.DEFAULT_MAX_FUEL : 0.0D) : currentFuel;
            double cost = travelFuelCost == null ? 0.0D : travelFuelCost;
            boolean committed = fuelCommitted != null && fuelCommitted;
            BlockPos requested = requestedDestinationX == null || requestedDestinationY == null || requestedDestinationZ == null ? new BlockPos(travelDestinationX, travelDestinationY, travelDestinationZ) : new BlockPos(requestedDestinationX, requestedDestinationY, requestedDestinationZ);
            String requestedDimension = requestedDestinationDimension == null ? (travelDestinationDimension == null ? dimension : travelDestinationDimension) : requestedDestinationDimension;
            float requestedYaw = requestedDestinationYaw == null ? travelDestinationYaw : requestedDestinationYaw;
            return new TardisData(id, ownerId, exteriorId, exterior, interior == null ? "intothevortex:70default" : interior, dimension, new BlockPos(x, y, z), door, interiorDoorStored, yaw, locked, doorOpen, powered, refueling, interiorInitialized, doorAnimation == null ? "intothevortex:door_swing" : doorAnimation, demat, mat, travelState == null ? TardisTravelState.LANDED : travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension == null ? dimension : travelSourceDimension, new BlockPos(travelSourceX, travelSourceY, travelSourceZ), travelSourceYaw, travelDestinationDimension == null ? dimension : travelDestinationDimension, new BlockPos(travelDestinationX, travelDestinationY, travelDestinationZ), travelDestinationYaw, throttleStage, handbrakeEngaged, amount, capacity, cost, committed, dataVersion == null ? 0 : dataVersion, flightCondition == null ? FlightCondition.NORMAL : flightCondition, lastFailureType == null ? FlightFailureType.NONE : lastFailureType, lastFailureDetails == null ? "" : lastFailureDetails, requestedDimension, requested, requestedYaw, activeFlightEvent == null ? "" : activeFlightEvent, activeEventControl == null ? "" : activeEventControl, activeEventRemaining == null ? 0 : activeEventRemaining, activeEventElapsed == null ? 0 : activeEventElapsed, activeEventStep == null ? 0 : activeEventStep, activeEventConsequenceApplied != null && activeEventConsequenceApplied, activeEventData == null ? "" : activeEventData, autopilot != null && autopilot);
        }
    }
}
