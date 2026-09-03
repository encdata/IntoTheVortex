package com.intothevortex.tardis;

public record FlightFailure(FlightFailureType type, FlightFailureSeverity severity, boolean recoverable, String details) {
    public FlightFailure {
        type = type == null ? FlightFailureType.PERSISTED_STATE_INVALID : type;
        severity = severity == null ? FlightFailureSeverity.TERMINAL : severity;
        details = details == null ? "" : details;
    }
}
