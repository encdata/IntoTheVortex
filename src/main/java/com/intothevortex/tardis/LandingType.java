package com.intothevortex.tardis;

public enum LandingType {
    NONE,
    FLOOR,
    CEILING,
    MEDIAN;

    public static LandingType fromValue(float value) {
        return values()[Math.clamp(Math.round(value), 0, values().length - 1)];
    }
}
