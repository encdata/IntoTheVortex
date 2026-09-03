package com.intothevortex.tardis;

import net.minecraft.resources.Identifier;
import java.util.LinkedHashMap;
import java.util.Map;

public final class FlightEventRegistry {
    private static final Map<Identifier, FlightEventType> EVENTS = new LinkedHashMap<>();
    private FlightEventRegistry() {}
    public static void register(Identifier id, FlightEventType event) {
        if (id == null || event == null) throw new IllegalArgumentException("Flight event id and type are required");
        EVENTS.put(id, event);
    }
    public static FlightEventType get(String id) {
        try { return EVENTS.get(Identifier.parse(id)); } catch (RuntimeException exception) { return null; }
    }
    public static Map<Identifier, FlightEventType> entries() { return Map.copyOf(EVENTS); }
}
