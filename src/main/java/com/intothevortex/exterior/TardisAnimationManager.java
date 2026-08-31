package com.intothevortex.exterior;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;

public final class TardisAnimationManager {
    private static final Map<UUID, AnimationDefinition> ANIMATIONS = new ConcurrentHashMap<>();
    private TardisAnimationManager() {}

    public static void register(UUID tardisId, Identifier exteriorId) {
        ExteriorDefinition definition = ExteriorRegistry.get(exteriorId);
        if (definition != null) ANIMATIONS.put(tardisId, definition.animation());
    }

    public static void unregister(UUID tardisId) { ANIMATIONS.remove(tardisId); }

    public static AnimationDefinition get(UUID tardisId, Identifier exteriorId) {
        if (tardisId == null) {
            return ExteriorRegistry.get(exteriorId).animation();
        }
        return ANIMATIONS.computeIfAbsent(tardisId, id -> ExteriorRegistry.get(exteriorId).animation());
    }
}
