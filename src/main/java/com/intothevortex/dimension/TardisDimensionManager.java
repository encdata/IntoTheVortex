package com.intothevortex.dimension;

import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelResource;
import com.intothevortex.interior.InteriorRegistry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;

public final class TardisDimensionManager {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("IntoTheVortex/TardisDimension");
    private static final Map<UUID, Integer> EMPTY_TICKS = new ConcurrentHashMap<>();
    private static final Map<UUID, java.util.List<java.util.function.Consumer<ServerLevel>>> INTERIOR_READY_CALLBACKS = new ConcurrentHashMap<>();
    private static final net.minecraft.core.BlockPos INTERIOR_ORIGIN = new net.minecraft.core.BlockPos(0, 64, 0);
    private TardisDimensionManager() {}

    public static ResourceKey<Level> key(UUID id) {
        return ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("intothevortex", id.toString()));
    }

    public static ResourceKey<Level> parseDimension(String value) {
        return ResourceKey.create(Registries.DIMENSION, Identifier.parse(value));
    }

    public static ServerLevel ensureLoaded(MinecraftServer server, UUID id) {
        ResourceKey<Level> key = key(id);
        ServerLevel existing = server.getLevel(key);
        if (existing != null) {
            EMPTY_TICKS.remove(id);
            initializeRegistered(server, existing);
            return existing;
        }
        ServerLevel overworld = server.overworld();
        LevelStem overworldStem = server.registries().compositeAccess().lookupOrThrow(Registries.LEVEL_STEM).getOrThrow(LevelStem.OVERWORLD).value();
        var dimensionTypes = server.registries().compositeAccess().lookup(Registries.DIMENSION_TYPE).orElseThrow();
        var stems = server.registries().compositeAccess().lookup(Registries.LEVEL_STEM).orElseThrow();
        var type = RuntimeRegistry.register(dimensionTypes, key.identifier(), copyDimensionType(overworldStem.type().value()));
        LevelStem stem = new LevelStem(type, new VoidChunkGenerator(overworldStem.generator().getBiomeSource()));
        RuntimeRegistry.register(stems, key.identifier(), stem);
        try {
            var access = ((TardisDimensionServer) server).intothevortex$storageSource();
            ServerLevel level = new ServerLevel(server, ((TardisDimensionServer) server).intothevortex$executor(), access, server.getWorldData().overworldData(), key, stem, false, id.getMostSignificantBits(), new ArrayList<>(), false);
            level.getChunkSource().setViewDistance(server.getPlayerList().getViewDistance());
            level.getChunkSource().setSimulationDistance(server.getPlayerList().getSimulationDistance());
            ((TardisDimensionServer) server).intothevortex$queueLevel(level);
            EMPTY_TICKS.remove(id);
            return level;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to create TARDIS dimension " + id, exception);
        }
    }

    public static void initializeRegistered(MinecraftServer server, ServerLevel level) {
        UUID id = id(level.dimension());
        if (id == null) return;
        TardisData data = TardisManager.get(server, id);
        if (data == null) return;
        if (!data.interiorInitialized() || !hasInterior(level)) {
            if (!placeInterior(server, level)) return;
            net.minecraft.core.BlockPos door = findInteriorDoor(level);
            if (door == null) return;
            data = data.withInteriorDoor(door).withInteriorInitialized(true);
            TardisManager.save(server, data);
        }
        net.minecraft.core.BlockPos interiorDoor = findInteriorDoor(level);
        if (interiorDoor != null && level.getBlockState(interiorDoor).is(InteriorRegistry.DOOR)) {
            com.intothevortex.interior.InteriorDoorBlock.ensureTop(level, interiorDoor);
            boolean open = data.doorOpen() && !data.locked();
            if (level.getBlockState(interiorDoor).getValue(com.intothevortex.interior.InteriorDoorBlock.OPEN) != open) level.setBlock(interiorDoor, level.getBlockState(interiorDoor).setValue(com.intothevortex.interior.InteriorDoorBlock.OPEN, open), 3);
        }
        com.intothevortex.interior.InteriorDoorBlock.syncExterior(level, id);
        completeInteriorReady(id, level);
    }

    public static net.minecraft.core.BlockPos interiorDoor(ServerLevel level) {
        UUID id = id(level.dimension());
        TardisData data = id == null ? null : TardisManager.get(level.getServer(), id);
        return data == null || !data.interiorInitialized() ? null : findInteriorDoor(level);
    }

    public static void whenInteriorReady(MinecraftServer server, UUID id, java.util.function.Consumer<ServerLevel> callback) {
        ServerLevel level = ensureLoaded(server, id);
        if (level != null && interiorDoor(level) != null) {
            callback.accept(level);
            return;
        }
        INTERIOR_READY_CALLBACKS.computeIfAbsent(id, ignored -> new ArrayList<>()).add(callback);
    }

    private static void completeInteriorReady(UUID id, ServerLevel level) {
        java.util.List<java.util.function.Consumer<ServerLevel>> callbacks = INTERIOR_READY_CALLBACKS.remove(id);
        if (callbacks != null) callbacks.forEach(callback -> callback.accept(level));
    }

    private static boolean hasInterior(ServerLevel level) {
        int nonAir = 0;
        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 9; y++) {
                for (int z = 0; z < 24; z++) {
                    if (!level.getBlockState(INTERIOR_ORIGIN.offset(x, y, z)).isAir() && ++nonAir >= 128) return true;
                }
            }
        }
        return false;
    }

    private static net.minecraft.core.BlockPos findInteriorDoor(ServerLevel level) {
        for (int x = -32; x <= 32; x++) {
            for (int y = 0; y <= 128; y++) {
                for (int z = -32; z <= 32; z++) {
                    net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(x, y, z);
                    if (level.getBlockState(pos).is(InteriorRegistry.DOOR)) return pos;
                }
            }
        }
        return null;
    }

    public static net.minecraft.world.phys.Vec3 interiorArrival(ServerLevel level, net.minecraft.core.BlockPos door) {
        net.minecraft.core.Direction direction = level.getBlockState(door).getValue(com.intothevortex.interior.InteriorDoorBlock.FACING);
        return new net.minecraft.world.phys.Vec3(door.getX() + 0.5D - direction.getStepX() * 1.2D, door.getY(), door.getZ() + 0.5D - direction.getStepZ() * 1.2D);
    }

    private static boolean placeInterior(MinecraftServer server, ServerLevel level) {
        TardisData data = TardisManager.get(server, id(level.dimension()));
        if (data == null) return false;
        Identifier interior = Identifier.parse(data.interior());
        StructureTemplate template;
        try (var input = server.getResourceManager().open(new FileToIdConverter("structures", ".nbt").idToFile(interior))) {
            CompoundTag tag = net.minecraft.nbt.NbtIo.readCompressed(input, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
            ListTag palette = tag.getListOrEmpty("palette");
            for (int index = 0; index < palette.size(); index++) {
                CompoundTag state = palette.getCompoundOrEmpty(index);
                String name = state.getStringOr("Name", "minecraft:air");
                if (name.startsWith("ait:")) state.putString("Name", name.equals("ait:door_block") ? "intothevortex:interior_door" : name.equals("ait:wall_monitor_block") ? "intothevortex:wall_monitor" : name.equals("ait:console") || name.startsWith("ait:console/") ? "intothevortex:console" : "minecraft:stone");
                else if (!name.startsWith("minecraft:") && !name.startsWith("intothevortex:")) state.putString("Name", "minecraft:stone");
            }
            tag.remove("entities");
            tag.remove("block_entities");
            template = new StructureTemplate();
            template.load(server.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), tag);
        } catch (java.io.IOException exception) {
            return false;
        }
        net.minecraft.core.BlockPos origin = INTERIOR_ORIGIN;
        level.getChunkAt(origin);
        boolean placed = template.placeInWorld(level, origin, origin, new StructurePlaceSettings().setKnownShape(true).setIgnoreEntities(false), RandomSource.create(), 2);
        if (!placed || !hasInterior(level)) return false;
        net.minecraft.core.BlockPos placedDoor = findInteriorDoor(level);
        LOGGER.info("Placed converted interior for TARDIS {} in {}. Door={}", id(level.dimension()), level.dimension().identifier(), placedDoor);
        if (placedDoor != null) {
            var state = level.getBlockState(placedDoor);
            com.intothevortex.interior.InteriorDoorBlock.ensureTop(level, placedDoor);
            return true;
        }
        return false;
    }

    public static void tick(MinecraftServer server) {
        java.util.List<ServerLevel> levels = new java.util.ArrayList<>();
        server.getAllLevels().forEach(levels::add);
        for (ServerLevel level : levels) {
            UUID id = id(level.dimension());
            if (id == null) continue;
            if (level.players().isEmpty()) {
                int ticks = EMPTY_TICKS.merge(id, 1, Integer::sum);
                if (ticks >= 200) unloadIfEmpty(server, id);
            } else EMPTY_TICKS.remove(id);
        }
    }

    public static void unloadIfEmpty(MinecraftServer server, UUID id) {
        ServerLevel level = server.getLevel(key(id));
        if (level == null || !level.players().isEmpty()) return;
        level.save(null, true, false);
        ((TardisDimensionServer) server).intothevortex$removeLevel(key(id));
        try { level.close(); } catch (java.io.IOException exception) { throw new IllegalStateException("Unable to close TARDIS dimension " + id, exception); }
        EMPTY_TICKS.remove(id);
        INTERIOR_READY_CALLBACKS.remove(id);
    }

    private static net.minecraft.world.level.dimension.DimensionType copyDimensionType(net.minecraft.world.level.dimension.DimensionType source) {
        return new net.minecraft.world.level.dimension.DimensionType(source.hasFixedTime(), source.hasSkyLight(), source.hasCeiling(), source.hasEnderDragonFight(), source.coordinateScale(), source.minY(), source.height(), source.logicalHeight(), source.infiniburn(), source.ambientLight(), source.monsterSettings(), source.skybox(), source.cardinalLightType(), source.attributes(), source.timelines(), source.defaultClock());
    }

    public static void shutdown(MinecraftServer server) {
        java.util.List<ServerLevel> levels = new java.util.ArrayList<>();
        server.getAllLevels().forEach(level -> { if (id(level.dimension()) != null) levels.add(level); });
        for (ServerLevel level : levels) {
            UUID tardisId = id(level.dimension());
            if (tardisId == null) continue;
            try {
                level.save(null, true, false);
                ((TardisDimensionServer) server).intothevortex$removeLevel(level.dimension());
                level.close();
            } catch (java.io.IOException exception) {
                LOGGER.error("Unable to close TARDIS dimension {} during server shutdown", tardisId, exception);
            }
        }
        EMPTY_TICKS.clear();
        INTERIOR_READY_CALLBACKS.clear();
    }

    public static boolean replaceInterior(MinecraftServer server, UUID id) {
        TardisData data = TardisManager.get(server, id);
        if (data == null) return false;
        ServerLevel level = server.getLevel(key(id));
        ServerLevel exterior = server.getLevel(parseDimension(data.dimension()));
        if (level != null) {
            if (exterior == null) return false;
            java.util.List<ServerPlayer> players = new java.util.ArrayList<>(level.players());
            for (ServerPlayer player : players) {
                var pos = data.position().getCenter();
                player.teleportTo(exterior, pos.x, pos.y, pos.z + 1.8D, java.util.Set.of(), data.yaw(), 0.0F, false);
            }
            java.util.List<net.minecraft.world.entity.Entity> entities = new java.util.ArrayList<>();
            level.getAllEntities().forEach(entities::add);
            for (var entity : entities) {
                if (!(entity instanceof ServerPlayer)) entity.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
            }
            level.save(null, true, false);
            ((TardisDimensionServer) server).intothevortex$removeLevel(key(id));
            try { level.close(); } catch (java.io.IOException exception) { throw new IllegalStateException("Unable to close TARDIS dimension " + id, exception); }
        }
        try {
            java.nio.file.Path dimensionPath = ((TardisDimensionServer) server).intothevortex$storageSource().getDimensionPath(key(id));
            if (java.nio.file.Files.exists(dimensionPath)) java.nio.file.Files.walk(dimensionPath).sorted(java.util.Comparator.reverseOrder()).forEach(path -> { try { java.nio.file.Files.deleteIfExists(path); } catch (java.io.IOException exception) { throw new java.io.UncheckedIOException(exception); } });
            TardisManager.save(server, data.withInteriorInitialized(false));
            EMPTY_TICKS.remove(id);
            INTERIOR_READY_CALLBACKS.remove(id);
            ensureLoaded(server, id);
            return true;
        } catch (java.io.IOException exception) {
            throw new java.io.UncheckedIOException(exception);
        }
    }

    public static boolean delete(MinecraftServer server, UUID id) {
        ServerLevel level = server.getLevel(key(id));
        if (level != null && !level.players().isEmpty()) return false;
        if (level != null) unloadIfEmpty(server, id);
        try {
            java.nio.file.Path dimensionPath = ((TardisDimensionServer) server).intothevortex$storageSource().getDimensionPath(key(id));
            if (java.nio.file.Files.exists(dimensionPath)) java.nio.file.Files.walk(dimensionPath).sorted(java.util.Comparator.reverseOrder()).forEach(path -> { try { java.nio.file.Files.deleteIfExists(path); } catch (java.io.IOException exception) { throw new java.io.UncheckedIOException(exception); } });
            java.nio.file.Files.deleteIfExists(server.getWorldPath(LevelResource.ROOT).resolve("IntoTheVortex").resolve(id + ".json"));
            EMPTY_TICKS.remove(id);
            INTERIOR_READY_CALLBACKS.remove(id);
            return true;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to delete TARDIS dimension " + id, exception);
        }
    }

    public static UUID id(ResourceKey<Level> key) {
        if (!key.identifier().getNamespace().equals("intothevortex")) return null;
        try { return UUID.fromString(key.identifier().getPath()); } catch (IllegalArgumentException ignored) { return null; }
    }
}
