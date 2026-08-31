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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelResource;
import com.intothevortex.interior.InteriorRegistry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.util.RandomSource;

public final class TardisDimensionManager {
    private static final Map<UUID, Integer> EMPTY_TICKS = new ConcurrentHashMap<>();
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
            return existing;
        }
        ServerLevel overworld = server.overworld();
        LevelStem overworldStem = server.registries().compositeAccess().lookupOrThrow(Registries.LEVEL_STEM).getOrThrow(LevelStem.OVERWORLD).value();
        LevelStem stem = new LevelStem(overworldStem.type(), new VoidChunkGenerator(overworldStem.generator().getBiomeSource()));
        try {
            var access = ((TardisDimensionServer) server).intothevortex$storageSource();
            ServerLevel level = new ServerLevel(server, java.util.concurrent.ForkJoinPool.commonPool(), access, server.getWorldData().overworldData(), key, stem, false, id.getMostSignificantBits(), new ArrayList<>(), false);
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
        if (data != null && !data.interiorInitialized() && placeInterior(server, level)) TardisManager.save(server, data.withInteriorInitialized(true));
    }

    private static boolean placeInterior(MinecraftServer server, ServerLevel level) {
        var template = server.getStructureManager().get(Identifier.fromNamespaceAndPath("intothevortex", "type_40"));
        if (template.isEmpty()) return false;
        net.minecraft.core.BlockPos origin = new net.minecraft.core.BlockPos(0, 64, 0);
        level.getChunkAt(origin);
        boolean placed = template.get().placeInWorld(level, origin, origin, new StructurePlaceSettings().setKnownShape(true).setIgnoreEntities(false), RandomSource.create(), 2);
        level.setBlock(origin, InteriorRegistry.DOOR.defaultBlockState(), 3);
        return placed && level.getBlockState(origin).is(InteriorRegistry.DOOR);
    }

    public static void tick(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
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
        try { level.getChunkSource().close(); } catch (java.io.IOException exception) { throw new IllegalStateException("Unable to close TARDIS dimension " + id, exception); }
        EMPTY_TICKS.remove(id);
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
