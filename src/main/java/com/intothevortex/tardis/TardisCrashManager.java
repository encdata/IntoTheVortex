package com.intothevortex.tardis;

public final class TardisCrashManager {
    private TardisCrashManager() {
    }

    public static TardisData transition(TardisData data, FlightCondition next) {
        FlightCondition current = data.flightCondition();
        if (current == next) return data;
        if (!isAllowed(current, next)) return data;
        return data.withFlightCondition(next);
    }

    public static TardisData fail(TardisData data, FlightFailure failure) {
        FlightCondition condition = switch (failure.severity()) {
            case RECOVERABLE -> FlightCondition.UNSTABLE;
            case EMERGENCY -> FlightCondition.EMERGENCY_LANDING;
            case TERMINAL -> FlightCondition.CRASHED;
        };
        TardisData updated = transition(data, condition);
        return updated.withFailure(failure.type(), failure.details());
    }

    public static boolean canUseNormalFlightControls(TardisData data) {
        return data.flightCondition() == FlightCondition.NORMAL && data.travelState() == TardisTravelState.LANDED;
    }

    public static boolean isAllowed(FlightCondition from, FlightCondition to) {
        return switch (from) {
            case NORMAL -> to == FlightCondition.UNSTABLE || to == FlightCondition.CRASHING || to == FlightCondition.EMERGENCY_LANDING || to == FlightCondition.NORMAL;
            case UNSTABLE -> to == FlightCondition.CRASHING || to == FlightCondition.EMERGENCY_LANDING || to == FlightCondition.NORMAL;
            case CRASHING -> to == FlightCondition.EMERGENCY_LANDING || to == FlightCondition.CRASHED;
            case EMERGENCY_LANDING -> to == FlightCondition.CRASHED || to == FlightCondition.NORMAL;
            case CRASHED -> to == FlightCondition.NORMAL;
        };
    }
}
