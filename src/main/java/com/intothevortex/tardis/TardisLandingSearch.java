package com.intothevortex.tardis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public final class TardisLandingSearch {
    public static final int NORMAL_SEARCH_RADIUS = 32;
    public static final int EMERGENCY_SEARCH_RADIUS = 64;
    public static final int NORMAL_VERTICAL_RANGE = 8;
    public static final int EMERGENCY_VERTICAL_RANGE = 16;
    public static final int NORMAL_MAX_CANDIDATES = 4096;
    public static final int EMERGENCY_MAX_CANDIDATES = 8192;
    public static final double EXTERIOR_HALF_WIDTH = 0.75D;
    public static final double EXTERIOR_HEIGHT = 3.1D;

    private TardisLandingSearch() {
    }

    public static LandingResult resolve(ServerLevel level, TardisTravelDestination requested, LandingSearchMode mode) {
        if (level == null || requested == null) return LandingResult.failure(LandingReason.INVALID_DIMENSION, requested);
        int radius = mode == LandingSearchMode.NORMAL ? NORMAL_SEARCH_RADIUS : EMERGENCY_SEARCH_RADIUS;
        int vertical = mode == LandingSearchMode.NORMAL ? NORMAL_VERTICAL_RANGE : EMERGENCY_VERTICAL_RANGE;
        int limit = mode == LandingSearchMode.NORMAL ? NORMAL_MAX_CANDIDATES : EMERGENCY_MAX_CANDIDATES;
        BlockPos origin = requested.position();
        if (origin == null) return LandingResult.failure(LandingReason.INVALID_DIMENSION, requested);
        boolean border = false;
        boolean height = false;
        boolean blocked = false;
        boolean support = false;
        int tested = 0;
        LandingCandidate best = null;
        for (int radiusValue = 0; radiusValue <= radius && tested < limit; radiusValue++) {
            for (int dx = -radiusValue; dx <= radiusValue && tested < limit; dx++) {
                for (int dz = -radiusValue; dz <= radiusValue && tested < limit; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != radiusValue) continue;
                    for (int dy = vertical; dy >= -vertical && tested < limit; dy--) {
                        tested++;
                        BlockPos candidate = origin.offset(dx, dy, dz);
                        AABB box = box(candidate);
                        int minBuildHeight = level.dimensionType().minY();
                        int maxBuildHeight = minBuildHeight + level.dimensionType().height();
                        if (candidate.getY() < minBuildHeight || candidate.getY() + EXTERIOR_HEIGHT > maxBuildHeight) {
                            height = true;
                            continue;
                        }
                        if (!level.getWorldBorder().isWithinBounds(box)) {
                            border = true;
                            continue;
                        }
                        if (!level.hasChunkAt(candidate)) continue;
                        if (dangerous(level, candidate)) continue;
                        if (!level.getBlockState(candidate.below()).isFaceSturdy(level, candidate.below(), Direction.UP)) {
                            support = true;
                            continue;
                        }
                        if (!level.noCollision(box)) {
                            blocked = true;
                            continue;
                        }
                        double horizontal = Math.sqrt((double) dx * dx + (double) dz * dz);
                        double score = horizontal + Math.abs(dy) * 2.0D + (radiusValue == 0 ? 0.0D : 0.01D);
                        if (best == null || score < best.score()) best = new LandingCandidate(candidate, score);
                    }
                }
            }
        }
        if (best == null) {
            LandingReason reason = tested >= limit ? LandingReason.SEARCH_LIMIT_REACHED : border ? LandingReason.OUTSIDE_WORLD_BORDER : height ? LandingReason.OUTSIDE_BUILD_HEIGHT : blocked ? LandingReason.BLOCKED : support ? LandingReason.NO_SOLID_FLOOR : LandingReason.NO_VALID_LOCATION;
            return LandingResult.failure(reason, requested);
        }
        TardisTravelDestination resolved = new TardisTravelDestination(requested.dimension(), best.position(), requested.yaw());
        double fallback = Math.sqrt(best.position().distSqr(origin));
        return new LandingResult(true, fallback == 0.0D ? LandingReason.SUCCESS_EXACT : LandingReason.SUCCESS_NEARBY, requested, resolved, best.score(), fallback);
    }

    public static AABB box(BlockPos pos) {
        return new AABB(pos.getX() - EXTERIOR_HALF_WIDTH, pos.getY(), pos.getZ() - EXTERIOR_HALF_WIDTH, pos.getX() + EXTERIOR_HALF_WIDTH, pos.getY() + EXTERIOR_HEIGHT, pos.getZ() + EXTERIOR_HALF_WIDTH);
    }

    private static boolean dangerous(ServerLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        var below = level.getBlockState(pos.below());
        return state.is(Blocks.LAVA) || below.is(Blocks.LAVA) || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE) || below.is(Blocks.MAGMA_BLOCK) || below.is(Blocks.CACTUS);
    }

    private record LandingCandidate(BlockPos position, double score) {
    }
}
