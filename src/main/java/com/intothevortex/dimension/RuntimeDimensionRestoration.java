package com.intothevortex.dimension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RuntimeDimensionRestoration {
    private static final Map<UUID, UUID> PENDING = new HashMap<>();
    private RuntimeDimensionRestoration() {}
    public static void queue(UUID playerId, UUID tardisId) { PENDING.put(playerId, tardisId); }
    public static UUID consume(UUID playerId) { return PENDING.remove(playerId); }
}
