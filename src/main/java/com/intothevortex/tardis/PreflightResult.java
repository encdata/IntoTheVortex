package com.intothevortex.tardis;

public record PreflightResult(PreflightReason reason, String details, double fuelCost, int flightTicks) {
    public boolean success() { return reason == PreflightReason.SUCCESS; }
    public static PreflightResult failure(PreflightReason reason, String details) { return new PreflightResult(reason, details, 0.0D, 0); }
}
