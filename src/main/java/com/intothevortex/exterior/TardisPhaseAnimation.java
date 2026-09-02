package com.intothevortex.exterior;

import java.util.List;
import net.minecraft.resources.Identifier;

public record TardisPhaseAnimation(Identifier id, int ticks, List<TravelKeyframe> keyframes, AnimationEasing easing) {
    public TardisPhaseAnimation {
        if (ticks <= 0) throw new IllegalArgumentException("Animation duration must be positive");
        if (keyframes.size() < 2) throw new IllegalArgumentException("Animation requires at least two keyframes");
    }

    public TravelTransform sample(float progress) {
        float tick = Math.max(0.0F, Math.min(1.0F, progress)) * ticks;
        if (tick <= keyframes.get(0).tick()) return keyframes.get(0).value();
        for (int i = 1; i < keyframes.size(); i++) {
            TravelKeyframe next = keyframes.get(i);
            TravelKeyframe previous = keyframes.get(i - 1);
            if (tick <= next.tick()) {
                float value = easing.apply((tick - previous.tick()) / (next.tick() - previous.tick()));
                if (easing == AnimationEasing.CUBIC) {
                    TravelTransform before = i > 1 ? keyframes.get(i - 2).value() : previous.value();
                    TravelTransform after = i + 1 < keyframes.size() ? keyframes.get(i + 1).value() : next.value();
                    return TravelTransform.cubic(before, previous.value(), next.value(), after, value);
                }
                return TravelTransform.interpolate(previous.value(), next.value(), value);
            }
        }
        return keyframes.get(keyframes.size() - 1).value();
    }
}
