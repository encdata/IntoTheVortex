package com.intothevortex.tardis;

import net.minecraft.core.BlockPos;

public record TardisStatusSnapshot(String id, String exterior, String interior, String dimension, BlockPos position, String destinationDimension, BlockPos destinationPosition, TardisTravelState travelState, FlightCondition flightCondition, int progress, int throttleStage, boolean handbrakeEngaged, double fuel, double maxFuel, boolean powered, boolean locked, boolean doorOpen, boolean autopilot, String eventId, String eventControl, int eventRemaining, int eventStep, FlightFailureType failureType) {
    public static TardisStatusSnapshot from(TardisData data) {
        return new TardisStatusSnapshot(data.id().toString(), data.exterior(), data.interior(), data.dimension(), data.position(), data.requestedDestinationDimension(), data.requestedDestinationPosition(), data.travelState(), data.flightCondition(), data.targetFlightTicks() <= 0 ? 0 : Math.clamp((data.flightTicks() * 100) / data.targetFlightTicks(), 0, 100), data.getThrottleStage(), data.isHandbrakeEngaged(), data.fuel(), data.maxFuel(), data.powered(), data.locked(), data.doorOpen(), data.autopilot(), data.activeFlightEvent(), data.activeEventControl(), data.activeEventRemaining(), data.activeEventStep(), data.lastFailureType());
    }
}
