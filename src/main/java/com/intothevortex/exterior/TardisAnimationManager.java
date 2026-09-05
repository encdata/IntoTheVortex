package com.intothevortex.exterior;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.List;
import net.minecraft.resources.Identifier;

public final class TardisAnimationManager {
    private static final Map<UUID, AnimationDefinition> ANIMATIONS = new ConcurrentHashMap<>();
    private static final Map<Identifier, AnimationDefinition> DOOR_ANIMATIONS = new ConcurrentHashMap<>();
    private static final Map<Identifier, TardisTravelAnimation> TRAVEL_ANIMATIONS = new ConcurrentHashMap<>();
    private static final Map<Identifier, TardisPhaseAnimation> PHASE_ANIMATIONS = new ConcurrentHashMap<>();
    public static final Identifier DEFAULT_TRAVEL_ID = Identifier.fromNamespaceAndPath("intothevortex", "default");
    public static final Identifier DEFAULT_DEMAT_ID = Identifier.fromNamespaceAndPath("intothevortex", "pulsating_demat");
    public static final Identifier DEFAULT_MAT_ID = Identifier.fromNamespaceAndPath("intothevortex", "pulsating_mat");
    public static final Identifier TYPE70_DEMAT_ID = Identifier.fromNamespaceAndPath("intothevortex", "type70demat");
    public static final Identifier TYPE70_MAT_ID = Identifier.fromNamespaceAndPath("intothevortex", "type70mat");
    private TardisAnimationManager() {}

    public static void register(UUID tardisId, Identifier exteriorId) {
        ExteriorDefinition definition = ExteriorRegistry.get(exteriorId);
        if (definition != null) {
            DOOR_ANIMATIONS.putIfAbsent(Identifier.fromNamespaceAndPath("intothevortex", "door_swing"), definition.animation());
            ANIMATIONS.put(tardisId, definition.animation());
        }
    }

    public static void unregister(UUID tardisId) { ANIMATIONS.remove(tardisId); }

    public static AnimationDefinition get(UUID tardisId, Identifier exteriorId) {
        if (tardisId == null) {
            return ExteriorRegistry.get(exteriorId).animation();
        }
        return ANIMATIONS.computeIfAbsent(tardisId, id -> ExteriorRegistry.get(exteriorId).animation());
    }

    public static AnimationDefinition registerDoor(Identifier id, AnimationDefinition animation) {
        DOOR_ANIMATIONS.put(id, animation);
        return animation;
    }

    public static AnimationDefinition getDoor(Identifier id) {
        return DOOR_ANIMATIONS.getOrDefault(id, ExteriorRegistry.get(ExteriorRegistry.DEFAULT_ID).animation());
    }

    public static boolean hasDoor(Identifier id) {
        return DOOR_ANIMATIONS.containsKey(id);
    }

    public static Collection<Identifier> doorIds() {
        return List.copyOf(DOOR_ANIMATIONS.keySet());
    }

    public static TardisTravelAnimation registerTravel(TardisTravelAnimation animation) {
        TRAVEL_ANIMATIONS.put(animation.id(), animation);
        return animation;
    }

    public static TardisTravelAnimation getTravel(Identifier id) {
        return TRAVEL_ANIMATIONS.getOrDefault(id, TRAVEL_ANIMATIONS.get(DEFAULT_TRAVEL_ID));
    }

    public static boolean hasTravel(Identifier id) {
        return TRAVEL_ANIMATIONS.containsKey(id);
    }

    public static TardisPhaseAnimation registerPhase(TardisPhaseAnimation animation) {
        PHASE_ANIMATIONS.put(animation.id(), animation);
        return animation;
    }

    public static TardisPhaseAnimation getPhase(Identifier id) {
        return PHASE_ANIMATIONS.getOrDefault(id, PHASE_ANIMATIONS.get(DEFAULT_TRAVEL_ID));
    }

    public static boolean hasPhase(Identifier id) {
        return PHASE_ANIMATIONS.containsKey(id);
    }

    public static Collection<TardisPhaseAnimation> phaseValues() {
        return List.copyOf(PHASE_ANIMATIONS.values());
    }

    public static Collection<TardisTravelAnimation> travelValues() {
        return List.copyOf(TRAVEL_ANIMATIONS.values());
    }

    public static void initializeTravel() {
        registerDoor(Identifier.fromNamespaceAndPath("intothevortex", "door_swing"), AnimationDefinition.DOOR_SWING);
        registerPhase(new TardisPhaseAnimation(DEFAULT_TRAVEL_ID, 100, List.of(new TravelKeyframe(0, TravelTransform.IDENTITY), new TravelKeyframe(100, TravelTransform.IDENTITY)), AnimationEasing.SMOOTHSTEP));
        registerPhase(new TardisPhaseAnimation(DEFAULT_DEMAT_ID, 191, List.of(
        new TravelKeyframe(0.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(27.5F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(50.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.75F)),
        new TravelKeyframe(65.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.9F)),
        new TravelKeyframe(85.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.55F)),
        new TravelKeyframe(100.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.75F)),
        new TravelKeyframe(120.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.35F)),
        new TravelKeyframe(133.334F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.5F)),
        new TravelKeyframe(160.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.15F)),
        new TravelKeyframe(170.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.15F)),
        new TravelKeyframe(185.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F))
        ), AnimationEasing.CUBIC));
        registerPhase(new TardisPhaseAnimation(DEFAULT_MAT_ID, 411, List.of(
        new TravelKeyframe(0.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(215.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(231.666F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.25F)),
        new TravelKeyframe(250.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.1F)),
        new TravelKeyframe(260.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.1F)),
        new TravelKeyframe(275.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.45F)),
        new TravelKeyframe(285.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.3F)),
        new TravelKeyframe(310.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.3F)),
        new TravelKeyframe(325.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.65F)),
        new TravelKeyframe(330.834F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.4F)),
        new TravelKeyframe(348.334F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.4F)),
        new TravelKeyframe(360.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.75F)),
        new TravelKeyframe(388.334F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F))
        ), AnimationEasing.CUBIC));
        registerPhase(new TardisPhaseAnimation(TYPE70_MAT_ID, 445, List.of(
        new TravelKeyframe(0.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(185.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(205.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(245.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(270.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(310.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(350.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(380.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(445.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F))
        ), AnimationEasing.CUBIC));
        registerPhase(new TardisPhaseAnimation(TYPE70_DEMAT_ID, 655, List.of(
        new TravelKeyframe(0.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(65.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(90.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(120.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(155.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(190.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(205.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(235.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F)),
        new TravelKeyframe(260.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F)),
        new TravelKeyframe(655.0F, new TravelTransform(1.0F, 0.0F, 0.0F, 0.0F))
        ), AnimationEasing.CUBIC));
        if (!TRAVEL_ANIMATIONS.isEmpty()) return;
        registerTravel(new TardisTravelAnimation(DEFAULT_TRAVEL_ID, 100, 100,
        TravelTransform.IDENTITY, new TravelTransform(0.15F, 1.25F, 180.0F, 0.0F),
        new TravelTransform(0.15F, 1.25F, -180.0F, 0.0F), TravelTransform.IDENTITY));
    }
}
