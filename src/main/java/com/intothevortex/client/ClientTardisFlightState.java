package com.intothevortex.client;

import com.intothevortex.network.TardisFlightPayload;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientTardisFlightState {
    private static final Map<UUID, TardisFlightPayload> STATES = new ConcurrentHashMap<>();

    private ClientTardisFlightState() {}

    public static void accept(TardisFlightPayload payload) { STATES.put(payload.id(), payload); }
    public static TardisFlightPayload get(UUID id) { return STATES.get(id); }
    public static void remove(UUID id) { STATES.remove(id); }
}
