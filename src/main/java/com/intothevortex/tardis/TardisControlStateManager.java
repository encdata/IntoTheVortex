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
import java.util.WeakHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

public final class TardisControlStateManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<MinecraftServer, Map<UUID, Map<String, Float>>> STATES = new WeakHashMap<>();

    private TardisControlStateManager() {}

    public static synchronized float get(MinecraftServer server, UUID id, String control) {
        return states(server, id).getOrDefault(control, 0.0F);
    }

    public static synchronized boolean enabled(MinecraftServer server, UUID id, String control) {
        return get(server, id, control) >= 0.5F || (control.equals("anti_gravs") && !states(server, id).containsKey(control));
    }

    public static synchronized void set(MinecraftServer server, UUID id, String control, float value) {
        if (!Float.isFinite(value)) return;
        states(server, id).put(control, value);
        save(server, id);
    }

    public static synchronized Map<String, Float> snapshot(MinecraftServer server, UUID id) {
        return Map.copyOf(states(server, id));
    }

    public static synchronized void remove(MinecraftServer server, UUID id) {
        Map<UUID, Map<String, Float>> serverStates = STATES.get(server);
        if (serverStates != null) serverStates.remove(id);
        try {
            Files.deleteIfExists(path(server, id));
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static Map<String, Float> states(MinecraftServer server, UUID id) {
        Map<UUID, Map<String, Float>> serverStates = STATES.computeIfAbsent(server, ignored -> new HashMap<>());
        return serverStates.computeIfAbsent(id, ignored -> load(server, id));
    }

    private static Map<String, Float> load(MinecraftServer server, UUID id) {
        Path file = path(server, id);
        if (!Files.isRegularFile(file)) return new HashMap<>();
        try {
            StoredState stored = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), StoredState.class);
            Map<String, Float> values = new HashMap<>();
            if (stored != null && stored.values != null) stored.values.forEach((key, value) -> { if (value != null && Float.isFinite(value)) values.put(key, value); });
            return values;
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static void save(MinecraftServer server, UUID id) {
        try {
            Files.createDirectories(path(server, id).getParent());
            Files.writeString(path(server, id), GSON.toJson(new StoredState(states(server, id))), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static Path path(MinecraftServer server, UUID id) {
        return server.getWorldPath(LevelResource.ROOT).resolve("IntoTheVortex").resolve(id + ".controls.json");
    }

    private record StoredState(Map<String, Float> values) {}
}
