package com.intothevortex.tardis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

public final class TardisLoyaltyManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<MinecraftServer, Map<UUID, Map<UUID, Integer>>> STATES = new java.util.WeakHashMap<>();

    private TardisLoyaltyManager() {}

    public static synchronized void initialize(MinecraftServer server, UUID tardisId, UUID ownerId) {
        values(server, tardisId).putIfAbsent(ownerId, TardisLoyalty.Rank.OWNER.level);
        save(server, tardisId);
    }

    public static synchronized TardisLoyalty get(MinecraftServer server, UUID tardisId, UUID playerId) {
        TardisData data = TardisManager.get(server, tardisId);
        if (data != null && data.ownerId().equals(playerId)) return new TardisLoyalty(TardisLoyalty.Rank.OWNER);
        return TardisLoyalty.fromLevel(values(server, tardisId).getOrDefault(playerId, TardisLoyalty.Rank.NEUTRAL.level));
    }

    public static synchronized void set(MinecraftServer server, UUID tardisId, UUID playerId, int level) {
        values(server, tardisId).put(playerId, TardisLoyalty.fromLevel(level).level());
        save(server, tardisId);
    }

    public static synchronized Map<UUID, Integer> snapshot(MinecraftServer server, UUID tardisId) {
        return Map.copyOf(values(server, tardisId));
    }

    public static synchronized void remove(MinecraftServer server, UUID tardisId) {
        Map<UUID, Map<UUID, Integer>> serverStates = STATES.get(server);
        if (serverStates != null) serverStates.remove(tardisId);
        try {
            Files.deleteIfExists(path(server, tardisId));
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static Map<UUID, Integer> values(MinecraftServer server, UUID tardisId) {
        return STATES.computeIfAbsent(server, ignored -> new HashMap<>()).computeIfAbsent(tardisId, ignored -> load(server, tardisId));
    }

    private static Map<UUID, Integer> load(MinecraftServer server, UUID tardisId) {
        Path file = path(server, tardisId);
        if (!Files.isRegularFile(file)) return new HashMap<>();
        try {
            StoredState stored = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), StoredState.class);
            Map<UUID, Integer> result = new HashMap<>();
            if (stored != null && stored.values != null) stored.values.forEach((id, level) -> {
                try { result.put(UUID.fromString(id), TardisLoyalty.fromLevel(level == null ? 0 : level).level()); } catch (IllegalArgumentException ignored) {}
            });
            return result;
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static void save(MinecraftServer server, UUID tardisId) {
        try {
            Files.createDirectories(path(server, tardisId).getParent());
            Map<String, Integer> serialized = new HashMap<>();
            values(server, tardisId).forEach((id, level) -> serialized.put(id.toString(), level));
            Files.writeString(path(server, tardisId), GSON.toJson(new StoredState(serialized)), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static Path path(MinecraftServer server, UUID tardisId) {
        return server.getWorldPath(LevelResource.ROOT).resolve("IntoTheVortex").resolve(tardisId + ".loyalty.json");
    }

    private record StoredState(Map<String, Integer> values) {}
}
