package com.intothevortex.exterior;

public record AnimationDefinition(float closedLeft, float closedRight, float openLeft, float openRight) {
    public static final AnimationDefinition DOOR_SWING = new AnimationDefinition(0.0F, 0.0F, -1.5708F, 1.5708F);
}
