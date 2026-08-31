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
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import com.intothevortex.exterior.TardisAnimationManager;
import net.minecraft.resources.Identifier;

public final class TardisManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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
                owner.getYRot() + 180.0F,
                false,
                false,
                false
        );
        save(level.getServer(), tardis);
        return spawnExterior(level.getServer(), tardis);
    }

    public static TardisData spawnExterior(MinecraftServer server, TardisData tardis) {
        ServerLevel level = getLevel(server, tardis.dimension());
        if (level == null) {
            return tardis;
        }
        TardisExteriorEntity entity = new TardisExteriorEntity(level, tardis.id());
        entity.setExterior(tardis.exterior());
        entity.setPos(tardis.position().getX() + 0.5, tardis.position().getY(), tardis.position().getZ() + 0.5);
        entity.setYRot(tardis.yaw());
        entity.setYHeadRot(tardis.yaw());
        level.addFreshEntity(entity);
        TardisData updated = tardis.withExterior(entity.getUUID());
        save(server, updated);
        TardisAnimationManager.register(updated.id(), Identifier.parse(updated.exterior()));
        return updated;
    }

    public static TardisData get(MinecraftServer server, UUID id) {
        Path file = folder(server).resolve(id + ".json");
        if (!Files.isRegularFile(file)) {
            return null;
        }
        try {
            StoredTardis stored = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), StoredTardis.class);
            return stored.toData();
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static void save(MinecraftServer server, TardisData tardis) {
        try {
            Files.createDirectories(folder(server));
            Files.writeString(folder(server).resolve(tardis.id() + ".json"), GSON.toJson(StoredTardis.from(tardis)), StandardCharsets.UTF_8);
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

    private record StoredTardis(UUID id, UUID ownerId, UUID exteriorId, String exterior, String interior, String dimension, int x, int y, int z, float yaw, boolean locked, boolean doorOpen, boolean interiorInitialized) {
        private static StoredTardis from(TardisData data) {
            return new StoredTardis(data.id(), data.ownerId(), data.exteriorId(), data.exterior(), data.interior(), data.dimension(), data.position().getX(), data.position().getY(), data.position().getZ(), data.yaw(), data.locked(), data.doorOpen(), data.interiorInitialized());
        }

        private TardisData toData() {
            return new TardisData(id, ownerId, exteriorId, exterior, interior == null ? "intothevortex:70default" : interior, dimension, new BlockPos(x, y, z), yaw, locked, doorOpen, interiorInitialized);
        }
    }
}
