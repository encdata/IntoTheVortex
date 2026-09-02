package com.intothevortex.exterior;

import net.minecraft.resources.Identifier;
import java.util.List;

public record TardisTravelAnimation(Identifier id, int dematTicks, int matTicks, TravelTransform dematStart, TravelTransform dematEnd, TravelTransform matStart, TravelTransform matEnd, List<TravelKeyframe> dematKeyframes, List<TravelKeyframe> matKeyframes, AnimationEasing easing) {
    public TardisTravelAnimation(Identifier id, int dematTicks, int matTicks, TravelTransform dematStart, TravelTransform dematEnd, TravelTransform matStart, TravelTransform matEnd) {
        this(id, dematTicks, matTicks, dematStart, dematEnd, matStart, matEnd, List.of(new TravelKeyframe(0, dematStart), new TravelKeyframe(dematTicks, dematEnd)), List.of(new TravelKeyframe(0, matStart), new TravelKeyframe(matTicks, matEnd)), AnimationEasing.SMOOTHSTEP);
    }

    public TardisTravelAnimation(Identifier id, int dematTicks, int matTicks, TravelTransform dematStart, TravelTransform dematEnd, TravelTransform matStart, TravelTransform matEnd, List<TravelKeyframe> dematKeyframes, List<TravelKeyframe> matKeyframes) {
        this(id, dematTicks, matTicks, dematStart, dematEnd, matStart, matEnd, dematKeyframes, matKeyframes, AnimationEasing.SMOOTHSTEP);
    }
    public TardisTravelAnimation {
        if (dematTicks <= 0 || matTicks <= 0) throw new IllegalArgumentException("Travel animation durations must be positive");
    }

    public TravelTransform transform(boolean materializing, float progress) {
        List<TravelKeyframe> frames = materializing ? matKeyframes : dematKeyframes;
        float tick = progress * (materializing ? matTicks : dematTicks);
        if (tick <= frames.get(0).tick()) return frames.get(0).value();
        for (int i = 1; i < frames.size(); i++) {
            TravelKeyframe next = frames.get(i);
            TravelKeyframe previous = frames.get(i - 1);
            if (tick <= next.tick()) return TravelTransform.interpolate(previous.value(), next.value(), easing.apply((tick - previous.tick()) / (next.tick() - previous.tick())));
        }
        return frames.get(frames.size() - 1).value();
    }
}
