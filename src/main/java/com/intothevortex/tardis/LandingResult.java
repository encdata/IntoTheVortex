package com.intothevortex.tardis;

public record LandingResult(boolean success, LandingReason reason, TardisTravelDestination requestedDestination, TardisTravelDestination resolvedDestination, double score, double fallbackDistance) {
    public static LandingResult failure(LandingReason reason, TardisTravelDestination requested) {
        return new LandingResult(false, reason, requested, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
}
