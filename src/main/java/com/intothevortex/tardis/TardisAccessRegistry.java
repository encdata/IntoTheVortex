package com.intothevortex.tardis;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TardisAccessRegistry {
    private static final ConcurrentHashMap<UUID, Set<UUID>> ACCESS = new ConcurrentHashMap<>();

    private TardisAccessRegistry() {
    }

    public static void register(UUID tardisId, UUID... playerIds) {
        ACCESS.put(tardisId, Set.copyOf(Arrays.asList(playerIds)));
    }

    public static void unregister(UUID tardisId) {
        ACCESS.remove(tardisId);
    }

    public static boolean canUse(UUID tardisId, UUID playerId, UUID ownerId) {
        Set<UUID> users = ACCESS.get(tardisId);
        return ownerId.equals(playerId) || users == null || users.contains(playerId);
    }
}
