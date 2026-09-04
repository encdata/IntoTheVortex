package com.intothevortex.sound;

import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.Map;

public final class ControlSoundRegistry {
    private static final Map<Identifier, ControlSoundProfile> PROFILES = new HashMap<>();
    private ControlSoundRegistry() {}
    public static void register(Identifier id, ControlSoundProfile profile) { if (id == null || profile == null) throw new IllegalArgumentException("Sound profile is required"); PROFILES.put(id, profile); }
    public static ControlSoundProfile get(Identifier id) { return PROFILES.get(id); }
    public static Map<Identifier, ControlSoundProfile> entries() { return Map.copyOf(PROFILES); }
}
