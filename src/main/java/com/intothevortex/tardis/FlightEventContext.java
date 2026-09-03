package com.intothevortex.tardis;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public record FlightEventContext(MinecraftServer server, ServerLevel level, TardisData data) {
    public TardisTravelDestination requestedDestination() {
        return new TardisTravelDestination(data.requestedDestinationDimension(), data.requestedDestinationPosition(), data.requestedDestinationYaw());
    }
}
