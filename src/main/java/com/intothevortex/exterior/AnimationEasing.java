package com.intothevortex.exterior;

public enum AnimationEasing {
    LINEAR,
    SMOOTHSTEP,
    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT,
    BOUNCE,
    CUBIC;

    public float apply(float value) {
        value = Math.max(0.0F, Math.min(1.0F, value));
        return switch (this) {
            case LINEAR -> value;
            case SMOOTHSTEP -> value * value * (3.0F - 2.0F * value);
            case EASE_IN -> value * value;
            case EASE_OUT -> 1.0F - (1.0F - value) * (1.0F - value);
            case EASE_IN_OUT -> value < 0.5F ? 2.0F * value * value : 1.0F - (float) Math.pow(-2.0F * value + 2.0F, 2.0F) / 2.0F;
            case BOUNCE -> bounce(value);
            case CUBIC -> value;
        };
    }

    private static float bounce(float value) {
        float n = 7.5625F;
        float d = 2.75F;
        if (value < 1.0F / d) return n * value * value;
        if (value < 2.0F / d) { value -= 1.5F / d; return n * value * value + 0.75F; }
        if (value < 2.5F / d) { value -= 2.25F / d; return n * value * value + 0.9375F; }
        value -= 2.625F / d;
        return n * value * value + 0.984375F;
    }
}
