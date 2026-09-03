package com.intothevortex.network;

import com.intothevortex.tardis.TardisData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TardisFlightSync {
    private static final Map<UUID, String> LAST_SENT = new ConcurrentHashMap<>();

    private TardisFlightSync() {}

    public static void sendIfChanged(MinecraftServer server, TardisData data) {
        String signature = data.getThrottleStage() + ":" + data.isHandbrakeEngaged() + ":" + data.fuel() + ":" + data.maxFuel() + ":" + data.refueling() + ":" + data.travelState() + ":" + data.phaseTicks() + ":" + data.flightTicks() + ":" + data.targetFlightTicks();
        if (signature.equals(LAST_SENT.put(data.id(), signature))) return;
        TardisFlightPayload payload = new TardisFlightPayload(data.id(), data.getThrottleStage(), data.isHandbrakeEngaged(), data.fuel(), data.maxFuel(), data.refueling(), data.travelState(), data.phaseTicks(), data.flightTicks(), data.targetFlightTicks());
        for (ServerPlayer player : new ArrayList<>(server.getPlayerList().getPlayers())) ServerPlayNetworking.send(player, payload);
    }

    public static void forget(UUID id) { LAST_SENT.remove(id); }
}
