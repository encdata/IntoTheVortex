package com.intothevortex.tardis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;

public final class TardisLandingSearch {
    public static final int NORMAL_SEARCH_RADIUS = 32;
    public static final int EMERGENCY_SEARCH_RADIUS = 64;
    public static final double EXTERIOR_HALF_WIDTH = 0.75D;
    public static final double EXTERIOR_HEIGHT = 3.1D;

    private TardisLandingSearch() {
    }

    public static LandingResult resolve(ServerLevel level, TardisTravelDestination requested, LandingSearchMode mode) {
        return resolve(level, requested, mode, LandingType.MEDIAN);
    }

    public static LandingResult resolve(ServerLevel level, TardisTravelDestination requested, LandingSearchMode mode, LandingType landingType) {
        if (level == null || requested == null) return LandingResult.failure(LandingReason.INVALID_DIMENSION, requested);
        int radius = mode == LandingSearchMode.NORMAL ? NORMAL_SEARCH_RADIUS : EMERGENCY_SEARCH_RADIUS;
        BlockPos origin = requested.position();
        if (origin == null) return LandingResult.failure(LandingReason.INVALID_DIMENSION, requested);
        int minBuildHeight = level.dimensionType().minY();
        int maxBuildHeight = minBuildHeight + level.dimensionType().height();
        boolean border = false;
        boolean height = false;
        boolean blocked = false;
        boolean support = false;
        LandingCandidate best = null;
        Set<ChunkPos> loadedChunks = new HashSet<>();
        for (int radiusValue = 0; radiusValue <= radius; radiusValue++) {
            for (int dx = -radiusValue; dx <= radiusValue; dx++) {
                for (int dz = -radiusValue; dz <= radiusValue; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != radiusValue) continue;
                    int chunkX = (origin.getX() + dx) >> 4;
                    int chunkZ = (origin.getZ() + dz) >> 4;
                    ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
                    if (loadedChunks.add(chunkPos)) level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);
                    for (int candidateY : candidateHeights(level, origin, landingType, minBuildHeight, maxBuildHeight)) {
                        BlockPos candidate = new BlockPos(origin.getX() + dx, candidateY, origin.getZ() + dz);
                        AABB box = box(candidate);
                        if (candidate.getY() < minBuildHeight || candidate.getY() + EXTERIOR_HEIGHT > maxBuildHeight) {
                            height = true;
                            continue;
                        }
                        if (!level.getWorldBorder().isWithinBounds(box)) {
                            border = true;
                            continue;
                        }
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
                        double score = horizontal + Math.abs(candidate.getY() - origin.getY()) * 2.0D + (radiusValue == 0 ? 0.0D : 0.01D);
                        if (best == null || score < best.score()) best = new LandingCandidate(candidate, score);
                    }
                }
            }
        }
        if (best == null) {
            LandingReason reason = border ? LandingReason.OUTSIDE_WORLD_BORDER : height ? LandingReason.OUTSIDE_BUILD_HEIGHT : blocked ? LandingReason.BLOCKED : support ? LandingReason.NO_SOLID_FLOOR : LandingReason.NO_VALID_LOCATION;
            return LandingResult.failure(reason, requested);
        }
        TardisTravelDestination resolved = new TardisTravelDestination(requested.dimension(), best.position(), requested.yaw());
        double fallback = Math.sqrt(best.position().distSqr(origin));
        return new LandingResult(true, fallback == 0.0D ? LandingReason.SUCCESS_EXACT : LandingReason.SUCCESS_NEARBY, requested, resolved, best.score(), fallback);
    }

    public static LandingType configuredType(net.minecraft.server.MinecraftServer server, java.util.UUID tardisId) {
        java.util.Map<String, Float> values = TardisControlStateManager.snapshot(server, tardisId);
        return values.containsKey("land_type") ? LandingType.fromValue(values.get("land_type")) : LandingType.MEDIAN;
    }

    private static int[] candidateHeights(ServerLevel level, BlockPos origin, LandingType type, int minBuildHeight, int maxBuildHeight) {
        if (type == LandingType.NONE) return new int[] { origin.getY() };
        if (type == LandingType.CEILING) {
            int height = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, origin.getX(), origin.getZ()) + 1;
            return new int[] { height };
        }
        int size = maxBuildHeight - minBuildHeight;
        int[] values = new int[size];
        int index = 0;
        if (type == LandingType.FLOOR) {
            for (int y = minBuildHeight; y < maxBuildHeight; y++) values[index++] = y;
        } else {
            int maxOffset = Math.max(origin.getY() - minBuildHeight, maxBuildHeight - 1 - origin.getY());
            for (int offset = 0; offset <= maxOffset; offset++) {
                int up = origin.getY() + offset;
                if (up >= minBuildHeight && up < maxBuildHeight) values[index++] = up;
                if (offset > 0) {
                    int down = origin.getY() - offset;
                    if (down >= minBuildHeight && down < maxBuildHeight) values[index++] = down;
                }
            }
        }
        return java.util.Arrays.copyOf(values, index);
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
