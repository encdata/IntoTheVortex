package com.intothevortex.tardis;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.entity.TardisExteriorEntity;
import com.intothevortex.exterior.TardisAnimationManager;
import com.intothevortex.exterior.TardisPhaseAnimation;
import com.intothevortex.sound.ModSounds;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

public final class TardisTravelManager {
    private static final int LANDING_RADIUS = 64;
    private TardisTravelManager() {
    }

    public static boolean summon(ServerPlayer player, UUID id) {
        if (TardisDimensionManager.id(player.level().dimension()) != null) return false;
        MinecraftServer server = player.level().getServer();
        if (server == null) return false;
        TardisData data = TardisManager.get(server, id);
        if (data == null || data.travelState() != TardisTravelState.LANDED) return false;
        ServerLevel level = (ServerLevel) player.level();
        float yaw = Mth.wrapDegrees(player.getYRot() + 180.0F);
        BlockPos landing = findSafeLanding(level, player.blockPosition(), yaw);
        return landing != null && startTravel(server, id, new TardisTravelDestination(level.dimension().identifier().toString(), landing, yaw));
    }

    public static boolean startTravel(MinecraftServer server, UUID id, TardisTravelDestination requestedDestination) {
        TardisData data = TardisManager.get(server, id);
        PreflightResult preflight = TardisPreflightValidator.validate(server, id, requestedDestination);
        if (!preflight.success()) return false;
        ServerLevel destinationLevel = server.getLevel(TardisDimensionManager.parseDimension(requestedDestination.dimension()));
        BlockPos landing = findSafeLanding(destinationLevel, requestedDestination.position(), requestedDestination.yaw());
        if (landing == null) return false;
        TardisTravelDestination source = new TardisTravelDestination(data.dimension(), data.position(), data.yaw());
        TardisTravelDestination destination = new TardisTravelDestination(requestedDestination.dimension(), landing, requestedDestination.yaw());
        TardisFlightParameters parameters = TardisFlightParameters.calculate(source, destination, data.getThrottleStage());
        TardisData charged = TardisFuelManager.consumeFuel(data, parameters.fuelCost());
        if (charged == null) return false;
        TardisData updated = charged.withDoorOpen(false).withTravel(TardisTravelState.DEMAT, 0, 0, parameters.flightTicks(), source, destination).withTravelFuel(parameters.fuelCost(), true);
        TardisManager.save(server, updated);
        playTransitionSound(server, updated, ModSounds.TARDIS_DEMAT);
        return true;
    }

    public static void tick(MinecraftServer server) {
        for (UUID id : TardisManager.ids(server)) {
            TardisData data = TardisManager.get(server, id);
            if (data == null || data.travelState() == TardisTravelState.LANDED) continue;
            tick(server, data);
        }
    }

    public static int progress(TardisData data) {
        return data.targetFlightTicks() <= 0 ? 0 : Mth.clamp((data.flightTicks() * 100) / data.targetFlightTicks(), 0, 100);
    }

    public static TardisData status(MinecraftServer server, UUID id) {
        return TardisManager.get(server, id);
    }

    public static BlockPos findSafeLanding(ServerLevel level, BlockPos requested, float yaw) {
        for (int radius = 0; radius <= LANDING_RADIUS; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != radius) continue;
                    for (int dy = 8; dy >= -8; dy--) {
                        BlockPos candidate = requested.offset(dx, dy, dz);
                        if (isSafeLanding(level, candidate)) return candidate;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isSafeLanding(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) return false;
        return level.noCollision(new AABB(pos.getX() - 0.75D, pos.getY(), pos.getZ() - 0.75D, pos.getX() + 0.75D, pos.getY() + 3.1D, pos.getZ() + 0.75D));
    }

    private static void tick(MinecraftServer server, TardisData data) {
        if (!validActiveTravel(data)) {
            recoverInvalidTravel(server, data);
            return;
        }
        String animationId = data.travelState() == TardisTravelState.MAT ? data.matAnimation() : data.dematAnimation();
        TardisPhaseAnimation animation = TardisAnimationManager.getPhase(Identifier.parse(animationId));
        switch (data.travelState()) {
            case DEMAT -> {
                TardisData updated = data.withTravel(TardisTravelState.DEMAT, data.phaseTicks() + 1, data.flightTicks(), data.targetFlightTicks(), source(data), destination(data));
                if (updated.phaseTicks() >= animation.ticks()) finishDemat(server, updated); else TardisManager.save(server, updated);
            }
            case FLIGHT -> {
                TardisData updated = data.withTravel(TardisTravelState.FLIGHT, 0, data.flightTicks() + 1, data.targetFlightTicks(), source(data), destination(data));
                if (updated.flightTicks() >= updated.targetFlightTicks()) beginMaterialization(server, updated); else TardisManager.save(server, updated);
            }
            case MAT -> {
                TardisData updated = data.withTravel(TardisTravelState.MAT, data.phaseTicks() + 1, data.flightTicks(), data.targetFlightTicks(), source(data), destination(data));
                if (updated.phaseTicks() >= animation.ticks()) finishMaterialization(server, updated); else TardisManager.save(server, updated);
            }
            case LANDED -> { }
        }
    }

    private static void finishDemat(MinecraftServer server, TardisData data) {
        discardExterior(server, data);
        TardisManager.save(server, data.withTravel(TardisTravelState.FLIGHT, 0, 0, data.targetFlightTicks(), source(data), destination(data)));
    }

    private static void beginMaterialization(MinecraftServer server, TardisData data) {
        TardisTravelDestination destination = destination(data);
        ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(destination.dimension()));
        BlockPos landing = level == null ? null : findSafeLanding(level, destination.position(), destination.yaw());
        if (landing == null) {
            TardisManager.save(server, data.withTravel(TardisTravelState.FLIGHT, 0, data.flightTicks(), data.targetFlightTicks(), source(data), destination));
            return;
        }
        TardisTravelDestination safeDestination = new TardisTravelDestination(destination.dimension(), landing, destination.yaw());
        TardisData updated = data.withExteriorLocation(safeDestination).withTravel(TardisTravelState.MAT, 0, data.flightTicks(), data.targetFlightTicks(), source(data), safeDestination);
        updated = TardisManager.spawnExterior(server, updated);
        TardisManager.save(server, updated);
        playTransitionSound(server, updated, ModSounds.TARDIS_MAT);
    }

    private static void finishMaterialization(MinecraftServer server, TardisData data) {
        TardisManager.save(server, data.withLanded(destination(data)));
    }

    private static void discardExterior(MinecraftServer server, TardisData data) {
        ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(data.dimension()));
        if (level == null) return;
        var entity = level.getEntity(data.exteriorId());
        if (entity instanceof TardisExteriorEntity exterior) exterior.discard();
    }

    private static void playTransitionSound(MinecraftServer server, TardisData data, net.minecraft.sounds.SoundEvent sound) {
        ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(data.dimension()));
        if (level != null) level.playSound(null, data.position(), sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        ServerLevel interior = TardisDimensionManager.ensureLoaded(server, data.id());
        if (interior != null) {
            BlockPos door = TardisDimensionManager.interiorDoor(interior);
            if (door != null) interior.playSound(null, door, sound, SoundSource.AMBIENT, 1.0F, 1.0F);
        }
    }

    private static boolean validActiveTravel(TardisData data) {
        if (data.travelState() == TardisTravelState.LANDED) return true;
        if (data.travelSourceDimension() == null || data.travelDestinationDimension() == null || data.travelSourcePosition() == null || data.travelDestinationPosition() == null) return false;
        return data.targetFlightTicks() > 0 && data.phaseTicks() >= 0 && data.flightTicks() >= 0 && Double.isFinite(data.travelFuelCost()) && data.travelFuelCost() >= 0.0D && data.fuelCommitted();
    }

    private static void recoverInvalidTravel(MinecraftServer server, TardisData data) {
        java.util.logging.Logger.getLogger("IntoTheVortex/TardisTravel").warning("Recovering invalid persisted travel state for TARDIS " + data.id());
        TardisManager.save(server, data.withLanded(new TardisTravelDestination(data.dimension(), data.position(), data.yaw())).withTravelFuel(0.0D, false));
    }

    private static TardisTravelDestination source(TardisData data) {
        return new TardisTravelDestination(data.travelSourceDimension(), data.travelSourcePosition(), data.travelSourceYaw());
    }

    private static TardisTravelDestination destination(TardisData data) {
        return new TardisTravelDestination(data.travelDestinationDimension(), data.travelDestinationPosition(), data.travelDestinationYaw());
    }
}
