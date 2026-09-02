package com.intothevortex.exterior;

public record TravelTransform(float scale, float verticalOffset, float yawOffset, float opacity) {
    public static final TravelTransform IDENTITY = new TravelTransform(1.0F, 0.0F, 0.0F, 1.0F);

    public static TravelTransform interpolate(TravelTransform from, TravelTransform to, float progress) {
        return new TravelTransform(
                from.scale() + (to.scale() - from.scale()) * progress,
                from.verticalOffset() + (to.verticalOffset() - from.verticalOffset()) * progress,
                from.yawOffset() + (to.yawOffset() - from.yawOffset()) * progress,
                from.opacity() + (to.opacity() - from.opacity()) * progress
        );
    }

    public static TravelTransform cubic(TravelTransform previous, TravelTransform from, TravelTransform to, TravelTransform next, float progress) {
        return new TravelTransform(cubic(previous.scale(), from.scale(), to.scale(), next.scale(), progress), cubic(previous.verticalOffset(), from.verticalOffset(), to.verticalOffset(), next.verticalOffset(), progress), cubic(previous.yawOffset(), from.yawOffset(), to.yawOffset(), next.yawOffset(), progress), cubic(previous.opacity(), from.opacity(), to.opacity(), next.opacity(), progress));
    }

    private static float cubic(float previous, float from, float to, float next, float progress) {
        return 0.5F * ((2.0F * from) + (-previous + to) * progress + (2.0F * previous - 5.0F * from + 4.0F * to - next) * progress * progress + (-previous + 3.0F * from - 3.0F * to + next) * progress * progress * progress);
    }
}
