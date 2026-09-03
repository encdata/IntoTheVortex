package com.intothevortex.tardis;

import net.minecraft.core.BlockPos;

public record TardisFlightParameters(double fuelCost, int flightTicks) {
    public static final int MIN_FLIGHT_TICKS = 20;
    public static final int MAX_FLIGHT_TICKS = 20 * 60 * 60;
    public static final double BASE_FUEL_COST = 10.0D;
    public static final double DISTANCE_FUEL_FACTOR = 0.01D;
    public static final double CROSS_DIMENSION_COST = 50.0D;
    public static final double[] THROTTLE_SPEED = {0.0D, 0.65D, 1.0D, 1.35D, 1.7D};
    public static final double[] THROTTLE_FUEL = {1.0D, 0.9D, 1.0D, 1.2D, 1.5D};

    public static TardisFlightParameters calculate(TardisTravelDestination source, TardisTravelDestination destination, int throttleStage) {
        int stage = Math.clamp(throttleStage, 0, 4);
        double distance = safeDistance(source.position(), destination.position());
        double cost = BASE_FUEL_COST + distance * DISTANCE_FUEL_FACTOR + (source.dimension().equals(destination.dimension()) ? 0.0D : CROSS_DIMENSION_COST);
        cost = finiteNonNegative(cost) * THROTTLE_FUEL[stage];
        double baseTicks = 100.0D + distance / 10.0D + (source.dimension().equals(destination.dimension()) ? 0.0D : 600.0D) + (source.yaw() == destination.yaw() ? 0.0D : 100.0D);
        double speed = THROTTLE_SPEED[stage];
        int ticks = stage == 0 ? MAX_FLIGHT_TICKS : (int) Math.clamp(Math.ceil(baseTicks / speed), MIN_FLIGHT_TICKS, MAX_FLIGHT_TICKS);
        return new TardisFlightParameters(cost, ticks);
    }

    private static double safeDistance(BlockPos source, BlockPos destination) {
        double dx = (double) destination.getX() - source.getX();
        double dy = (double) destination.getY() - source.getY();
        double dz = (double) destination.getZ() - source.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        return Double.isFinite(distance) ? Math.min(distance, 1.0E9D) : 1.0E9D;
    }

    private static double finiteNonNegative(double value) {
        return Double.isFinite(value) && value >= 0.0D ? value : Double.MAX_VALUE / 4.0D;
    }
}
