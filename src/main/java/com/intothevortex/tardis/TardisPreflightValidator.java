package com.intothevortex.tardis;

import com.intothevortex.dimension.TardisDimensionManager;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public final class TardisPreflightValidator {
    private TardisPreflightValidator() {}

    public static PreflightResult validate(MinecraftServer server, UUID id, TardisTravelDestination requestedDestination) {
        TardisData data = TardisManager.get(server, id);
        if (data == null) return PreflightResult.failure(PreflightReason.MISSING_TARDIS_DATA, "TARDIS data was not found");
        data = data.sanitized();
        if (data.isCrashed()) return PreflightResult.failure(PreflightReason.CRASHED, "The TARDIS requires recovery");
        if (data.travelState() == TardisTravelState.DEMAT) return PreflightResult.failure(PreflightReason.ALREADY_DEMATERIALIZING, "The TARDIS is dematerializing");
        if (data.travelState() == TardisTravelState.FLIGHT) return PreflightResult.failure(PreflightReason.ALREADY_IN_FLIGHT, "The TARDIS is already in flight");
        if (data.travelState() == TardisTravelState.MAT) return PreflightResult.failure(PreflightReason.ALREADY_MATERIALIZING, "The TARDIS is materializing");
        if (data.travelState() != TardisTravelState.LANDED) return PreflightResult.failure(PreflightReason.INVALID_TRAVEL_STATE, "The TARDIS has an invalid travel state");
        if (!data.powered()) return PreflightResult.failure(PreflightReason.NO_POWER, "The TARDIS is not powered");
        if (data.isHandbrakeEngaged()) return PreflightResult.failure(PreflightReason.HANDBRAKE_ENGAGED, "The handbrake is engaged");
        if (data.getThrottleStage() <= 0) return PreflightResult.failure(PreflightReason.THROTTLE_IDLE, "The throttle is idle");
        if (requestedDestination == null) return PreflightResult.failure(PreflightReason.NO_DESTINATION, "No destination was provided");
        if (!validDestination(requestedDestination)) return PreflightResult.failure(PreflightReason.INVALID_DESTINATION, "The destination coordinates are invalid");
        try {
            if (server.getLevel(TardisDimensionManager.parseDimension(requestedDestination.dimension())) == null) return PreflightResult.failure(PreflightReason.INVALID_DIMENSION, "The destination dimension is unavailable");
        } catch (RuntimeException exception) {
            return PreflightResult.failure(PreflightReason.INVALID_DIMENSION, "The destination dimension is malformed");
        }
        TardisTravelDestination source = new TardisTravelDestination(data.dimension(), data.position(), data.yaw());
        TardisFlightParameters parameters = TardisFlightParameters.calculate(source, requestedDestination, data.getThrottleStage());
        if (!TardisFuelManager.hasFuel(data, parameters.fuelCost())) return new PreflightResult(PreflightReason.INSUFFICIENT_FUEL, "Insufficient fuel", parameters.fuelCost(), parameters.flightTicks());
        return new PreflightResult(PreflightReason.SUCCESS, "", parameters.fuelCost(), parameters.flightTicks());
    }

    private static boolean validDestination(TardisTravelDestination destination) {
        if (destination.dimension() == null || destination.dimension().isBlank()) return false;
        return Float.isFinite(destination.yaw()) && destination.position() != null;
    }
}
