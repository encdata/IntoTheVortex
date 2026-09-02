package com.intothevortex.tardis;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.MinecraftServer;

public final class TardisTeleportCooldowns {
    private static final long DURATION_TICKS = 100L;
    private static final ConcurrentHashMap<UUID, Long> EXPIRY = new ConcurrentHashMap<>();

    private TardisTeleportCooldowns() {
    }

    public static boolean active(MinecraftServer server, UUID playerId) {
        long now = server.getTickCount();
        Long expiry = EXPIRY.get(playerId);
        if (expiry == null) return false;
        if (now >= expiry) {
            EXPIRY.remove(playerId, expiry);
            return false;
        }
        return true;
    }

    public static void arm(MinecraftServer server, UUID playerId) {
        EXPIRY.put(playerId, server.getTickCount() + DURATION_TICKS);
    }

    public static void clear(UUID playerId) {
        EXPIRY.remove(playerId);
    }
}
