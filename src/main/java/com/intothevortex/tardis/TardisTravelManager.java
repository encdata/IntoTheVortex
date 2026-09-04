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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class TardisTravelManager {
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
        return startTravel(server, id, new TardisTravelDestination(level.dimension().identifier().toString(), player.blockPosition(), yaw));
    }

    public static boolean startTravel(MinecraftServer server, UUID id, TardisTravelDestination requestedDestination) {
        TardisData data = TardisManager.get(server, id);
        PreflightResult preflight = TardisPreflightValidator.validate(server, id, requestedDestination);
        if (!preflight.success()) return false;
        TardisTravelDestination source = new TardisTravelDestination(data.dimension(), data.position(), data.yaw());
        TardisFlightParameters parameters = TardisFlightParameters.calculate(source, requestedDestination, data.getThrottleStage());
        TardisData charged = TardisFuelManager.consumeFuel(data, parameters.fuelCost());
        if (charged == null) return false;
        TardisData updated = charged.withDoorOpen(false).withTravel(TardisTravelState.DEMAT, 0, 0, parameters.flightTicks(), source, requestedDestination).withRequestedDestination(requestedDestination).withTravelFuel(parameters.fuelCost(), true).withFlightCondition(FlightCondition.NORMAL).withFailure(FlightFailureType.NONE, "");
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

    public static boolean tryFly(MinecraftServer server, UUID id) {
        TardisData data = TardisManager.get(server, id);
        if (data == null || data.travelState() != TardisTravelState.LANDED || data.isCrashed() || !data.powered() || data.isHandbrakeEngaged() || data.getThrottleStage() <= 0) return false;
        TardisTravelDestination destination = new TardisTravelDestination(data.requestedDestinationDimension(), data.requestedDestinationPosition(), data.requestedDestinationYaw());
        return startTravel(server, id, destination);
    }

    public static boolean requestMaterialization(MinecraftServer server, UUID id) {
        TardisData data = TardisManager.get(server, id);
        if (data == null || data.travelState() != TardisTravelState.FLIGHT || data.isCrashed()) return false;
        TardisManager.save(server, data.withTravel(TardisTravelState.FLIGHT, 0, data.targetFlightTicks(), data.targetFlightTicks(), source(data), destination(data)));
        return true;
    }

    public static boolean crash(MinecraftServer server, UUID id, FlightFailure failure) {
        TardisData data = TardisManager.get(server, id);
        if (data == null || data.travelState() != TardisTravelState.FLIGHT || data.flightCondition() == FlightCondition.CRASHING || data.flightCondition() == FlightCondition.CRASHED) return false;
        FlightFailure actual = failure == null ? new FlightFailure(FlightFailureType.INVALID_TRAVEL_STATE, FlightFailureSeverity.EMERGENCY, true, "flight_crash") : failure;
        TardisData crashed = TardisCrashManager.transition(data, FlightCondition.CRASHING).withDoorOpen(false).withLocked(true).withFailure(actual.type(), actual.details());
        TardisManager.save(server, crashed);
        ServerLevel interior = TardisDimensionManager.ensureLoaded(server, id);
        if (interior != null) {
            for (ServerPlayer player : interior.players()) {
                long seed = id.getMostSignificantBits() ^ id.getLeastSignificantBits() ^ player.getUUID().getLeastSignificantBits();
                double x = ((seed & 255L) / 255.0D) * 2.0D - 1.0D;
                double z = (((seed >>> 8) & 255L) / 255.0D) * 2.0D - 1.0D;
                player.setDeltaMovement(x, 0.6D, z);
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0, true, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, true, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 80, 0, true, false, false));
                player.hurtServer(interior, interior.damageSources().generic(), 1.0F);
            }
            BlockPos door = TardisDimensionManager.interiorDoor(interior);
            if (door != null) interior.playSound(null, door, net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1.5F, 0.8F);
        }
        return true;
    }

    public static BlockPos findSafeLanding(ServerLevel level, BlockPos requested, float yaw) {
        LandingResult result = TardisLandingSearch.resolve(level, new TardisTravelDestination(level.dimension().identifier().toString(), requested, yaw), LandingSearchMode.NORMAL);
        return result.success() ? result.resolvedDestination().position() : null;
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
                updated = TardisFlightEventManager.tick(server, updated);
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
        TardisTravelDestination requested = requested(data);
        ServerLevel level = server.getLevel(TardisDimensionManager.parseDimension(requested.dimension()));
        LandingResult landing = TardisLandingSearch.resolve(level, requested, LandingSearchMode.NORMAL);
        boolean emergency = false;
        if (!landing.success()) {
            emergency = true;
            FlightFailure failure = new FlightFailure(FlightFailureType.LANDING_SEARCH_FAILED, FlightFailureSeverity.EMERGENCY, true, landing.reason().name());
            TardisData emergencyState = crash(server, data.id(), failure) ? TardisManager.get(server, data.id()) : TardisCrashManager.fail(data, failure);
            LandingResult fallback = TardisLandingSearch.resolve(level, requested, LandingSearchMode.EMERGENCY);
            if (!fallback.success()) {
                ServerLevel sourceLevel = server.getLevel(TardisDimensionManager.parseDimension(data.travelSourceDimension()));
                LandingResult sourceFallback = TardisLandingSearch.resolve(sourceLevel, source(data), LandingSearchMode.EMERGENCY);
                if (!sourceFallback.success()) {
                    TardisData terminal = TardisCrashManager.fail(emergencyState, new FlightFailure(FlightFailureType.LANDING_SEARCH_FAILED, FlightFailureSeverity.TERMINAL, false, fallback.reason().name()));
                    TardisManager.save(server, terminal);
                    return;
                }
                level = sourceLevel;
                landing = sourceFallback;
                requested = source(data);
            } else {
                landing = fallback;
            }
        }
        TardisTravelDestination safeDestination = landing.resolvedDestination();
        TardisData updated = data.withExteriorLocation(safeDestination).withTravel(TardisTravelState.MAT, 0, data.flightTicks(), data.targetFlightTicks(), source(data), safeDestination);
        if (emergency) updated = updated.withFlightCondition(FlightCondition.EMERGENCY_LANDING);
        updated = TardisManager.spawnExterior(server, updated);
        TardisManager.save(server, updated);
        playTransitionSound(server, updated, ModSounds.TARDIS_MAT);
    }

    private static void finishMaterialization(MinecraftServer server, TardisData data) {
        TardisData landed = data.withLanded(destination(data));
        if (landed.flightCondition() == FlightCondition.EMERGENCY_LANDING) landed = landed.withFlightCondition(FlightCondition.CRASHED);
        else landed = landed.withFlightCondition(FlightCondition.NORMAL).withFailure(FlightFailureType.NONE, "");
        TardisManager.save(server, landed);
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

    private static TardisTravelDestination requested(TardisData data) {
        return new TardisTravelDestination(data.requestedDestinationDimension(), data.requestedDestinationPosition(), data.requestedDestinationYaw());
    }
}
