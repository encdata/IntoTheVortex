package com.intothevortex.exterior;

import java.util.List;

public record AnimationDefinition(float closedLeft, float closedRight, float openLeft, float openRight, List<DoorKeyframe> keyframes, AnimationEasing easing) {
    public AnimationDefinition(float closedLeft, float closedRight, float openLeft, float openRight) {
        this(closedLeft, closedRight, openLeft, openRight, List.of(new DoorKeyframe(0, closedLeft, closedRight), new DoorKeyframe(1, openLeft, openRight)), AnimationEasing.SMOOTHSTEP);
    }

    public AnimationDefinition(float closedLeft, float closedRight, float openLeft, float openRight, List<DoorKeyframe> keyframes) {
        this(closedLeft, closedRight, openLeft, openRight, keyframes, AnimationEasing.SMOOTHSTEP);
    }
    public static final AnimationDefinition DOOR_SWING = new AnimationDefinition(0.0F, 0.0F, 1.5708F, -1.5708F);

    public float left(float progress) { return sample(progress, true); }
    public float right(float progress) { return sample(progress, false); }

    private float sample(float progress, boolean left) {
        if (progress <= keyframes.get(0).tick()) return left ? keyframes.get(0).left() : keyframes.get(0).right();
        for (int i = 1; i < keyframes.size(); i++) {
            DoorKeyframe next = keyframes.get(i);
            DoorKeyframe previous = keyframes.get(i - 1);
            if (progress <= next.tick()) {
                float value = (progress - previous.tick()) / (next.tick() - previous.tick());
                value = easing.apply(value);
                float from = left ? previous.left() : previous.right();
                float to = left ? next.left() : next.right();
                return from + (to - from) * value;
            }
        }
        DoorKeyframe last = keyframes.get(keyframes.size() - 1);
        return left ? last.left() : last.right();
    }
}
