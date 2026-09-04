package com.intothevortex.interior;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.item.ModItems;
import com.intothevortex.item.TardisLinking;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.tardis.TardisTravelState;
import com.intothevortex.tardis.TardisTravelManager;
import com.intothevortex.tardis.TardisTravelDestination;
import com.intothevortex.tardis.TardisCrashManager;
import com.intothevortex.tardis.FlightFailure;
import com.intothevortex.tardis.FlightFailureType;
import com.intothevortex.tardis.FlightFailureSeverity;
import com.intothevortex.tardis.TardisStatusManager;
import com.intothevortex.sound.ControlSoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import java.util.List;

public final class ControlBehaviors {
    public static final ControlBehavior DEFAULT = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.inputDelta() != 0.0F && context.definition().inputType() != ConsoleInputType.SWITCH && context.definition().inputType() != ConsoleInputType.KEY_SWITCH) {
                float step = context.definition().id().equals("handbrake") ? 1.0F : 0.05F;
                float next = Math.clamp(context.currentValue() + context.inputDelta() * step, context.definition().minimum(), context.definition().maximum());
                context.console().setAuthoritativeValue(context.player(), context.definition().id(), next, false);
                return InteractionResult.SUCCESS;
            }
            float oldValue = context.currentValue();
            float value = context.definition().inputType() == ConsoleInputType.SWITCH || context.definition().inputType() == ConsoleInputType.KEY_SWITCH ? (oldValue >= 0.5F ? 0.0F : 1.0F) : oldValue;
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onSecondaryPress(ControlUseContext context) {
            return onPress(context);
        }

        @Override public InteractionResult onDrag(ControlUseContext context, float value) {
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior DOOR = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            InteriorPropBlock.toggleDoor(context.level(), context.position(), context.player());
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior DOOR_LOCK = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (!context.player().getMainHandItem().is(ModItems.TARDIS_KEY)) return InteractionResult.FAILED_PERMISSION;
            if (context.tardis() == null || !context.tardis().ownerId().equals(context.player().getUUID())) return InteractionResult.FAILED_PERMISSION;
            boolean locked = !context.tardis().locked();
            TardisManager.save(context.level().getServer(), context.tardis().withLocked(locked).withDoorOpen(locked ? false : context.tardis().doorOpen()));
            send(context.player(), "Door lock: " + (locked ? "Locked" : "Unlocked"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior POWER = new ControlBehavior() {
        @Override public InteractionResult validate(ControlUseContext context) {
            return context.tardis() != null && (context.tardis().travelState() != TardisTravelState.LANDED || context.tardis().isCrashed()) ? InteractionResult.FAILED_INVALID_PHASE : InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onPress(ControlUseContext context) {
            boolean powered = !context.console().powered();
            context.console().setPowered(context.player(), powered);
            ControlSoundManager.playForControl(context.level(), context.position(), "power", powered);
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior REFUELER = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || context.tardis().travelState() != TardisTravelState.LANDED || context.tardis().isCrashed() || context.console().controlValue("handbrake") < 0.5F) return InteractionResult.FAILED_INVALID_PHASE;
            context.console().setRefueling(context.player(), !context.console().refueling());
            ControlSoundManager.playForControl(context.level(), context.position(), "refueler", context.console().refueling());
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior HANDBRAKE = new ControlBehavior() {
        @Override public InteractionResult validate(ControlUseContext context) {
            return context.tardis() != null && (context.tardis().travelState() == TardisTravelState.DEMAT || context.tardis().travelState() == TardisTravelState.MAT || context.tardis().isCrashed()) ? InteractionResult.FAILED_INVALID_PHASE : InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            boolean engaged = !context.tardis().isHandbrakeEngaged();
            context.console().setHandbrakeEngaged(context.player(), engaged);
            ControlSoundManager.playForControl(context.level(), context.position(), "handbrake", engaged);
            if (engaged && context.tardis().travelState() == TardisTravelState.FLIGHT) {
                if (context.tardis().autopilot()) TardisTravelManager.requestMaterialization(context.level().getServer(), context.tardis().id());
                else TardisTravelManager.crash(context.level().getServer(), context.tardis().id(), new FlightFailure(FlightFailureType.INVALID_TRAVEL_STATE, FlightFailureSeverity.EMERGENCY, true, "handbrake_during_flight"));
            }
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onDrag(ControlUseContext context, float value) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            boolean engaged = value >= 0.5F;
            context.console().setHandbrakeEngaged(context.player(), engaged);
            if (engaged && context.tardis().travelState() == TardisTravelState.FLIGHT) {
                if (context.tardis().autopilot()) TardisTravelManager.requestMaterialization(context.level().getServer(), context.tardis().id());
                else TardisTravelManager.crash(context.level().getServer(), context.tardis().id(), new FlightFailure(FlightFailureType.INVALID_TRAVEL_STATE, FlightFailureSeverity.EMERGENCY, true, "handbrake_during_flight"));
            }
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior THROTTLE = new ControlBehavior() {
        @Override public InteractionResult validate(ControlUseContext context) {
            return context.tardis() != null && (context.tardis().travelState() == TardisTravelState.DEMAT || context.tardis().travelState() == TardisTravelState.MAT || context.tardis().isCrashed()) ? InteractionResult.FAILED_INVALID_PHASE : InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            int delta = context.inputDelta() < 0.0F ? -1 : 1;
            context.console().setThrottleStage(context.player(), context.tardis().getThrottleStage() + delta);
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onSecondaryPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            context.console().setThrottleStage(context.player(), context.tardis().getThrottleStage() - 1);
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onDrag(ControlUseContext context, float value) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            context.console().setThrottleStage(context.player(), Math.round(Math.clamp(value, 0.0F, 4.0F)));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior AUTOPILOT = new ControlBehavior() {
        @Override public InteractionResult validate(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            if (!context.tardis().powered()) return InteractionResult.FAILED_NO_POWER;
            if (context.tardis().isCrashed()) return InteractionResult.FAILED_CRASH_STATE;
            return context.tardis().travelState() == TardisTravelState.DEMAT || context.tardis().travelState() == TardisTravelState.MAT ? InteractionResult.FAILED_INVALID_PHASE : InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            boolean enabled = !context.tardis().autopilot();
            TardisManager.save(context.level().getServer(), context.tardis().withAutopilot(enabled));
            ControlSoundManager.playForControl(context.level(), context.position(), "autopilot", enabled);
            send(context.player(), "Stabilisers / Autopilot: " + (enabled ? "On" : "Off"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior MONITOR = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            var status = TardisStatusManager.get(context.level().getServer(), context.tardis().id());
            send(context.player(), "TARDIS " + status.id() + " | " + status.travelState() + " | Fuel " + Math.round(status.fuel()) + "/" + Math.round(status.maxFuel()) + " | Throttle " + status.throttleStage() + " | Stabilisers " + (status.autopilot() ? "On" : "Off"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior TOGGLE = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            float value = context.currentValue() >= 0.5F ? 0.0F : 1.0F;
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            send(context.player(), context.definition().id() + ": " + (value >= 0.5F ? "On" : "Off"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior SYSTEM_TOGGLE = new ControlBehavior() {
        @Override public InteractionResult validate(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            if (!context.tardis().powered()) return InteractionResult.FAILED_NO_POWER;
            if (context.tardis().travelState() == TardisTravelState.DEMAT || context.tardis().travelState() == TardisTravelState.MAT || context.tardis().isCrashed()) return InteractionResult.FAILED_INVALID_PHASE;
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onPress(ControlUseContext context) {
            float value = context.currentValue() >= 0.5F ? 0.0F : 1.0F;
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            send(context.player(), context.definition().id() + ": " + (value >= 0.5F ? "On" : "Off"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior SECURITY = new ControlBehavior() {
        @Override public InteractionResult validate(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            if (!context.tardis().ownerId().equals(context.player().getUUID()) || !context.player().getMainHandItem().is(ModItems.TARDIS_KEY)) return InteractionResult.FAILED_PERMISSION;
            if (context.tardis().travelState() != TardisTravelState.LANDED || context.tardis().isCrashed()) return InteractionResult.FAILED_INVALID_PHASE;
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onPress(ControlUseContext context) {
            float value = context.currentValue() >= 0.5F ? 0.0F : 1.0F;
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            send(context.player(), "Security: " + (value >= 0.5F ? "On" : "Off"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior SIEGE_MODE = new ControlBehavior() {
        @Override public InteractionResult validate(ControlUseContext context) {
            return context.tardis() == null ? InteractionResult.FAILED_INVALID_CONTROL : context.tardis().travelState() == TardisTravelState.LANDED && !context.tardis().isCrashed() ? InteractionResult.SUCCESS : InteractionResult.FAILED_INVALID_PHASE;
        }

        @Override public InteractionResult onPress(ControlUseContext context) {
            float value = context.currentValue() >= 0.5F ? 0.0F : 1.0F;
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            send(context.player(), "Siege mode: " + (value >= 0.5F ? "On" : "Off"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior ENGINE_OVERLOAD = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || !context.tardis().powered()) return InteractionResult.FAILED_NO_POWER;
            if (context.currentValue() < 0.5F) {
                context.console().setAuthoritativeValue(context.player(), context.definition().id(), 1.0F, false);
                send(context.player(), "Engine overload armed");
                return InteractionResult.SUCCESS;
            }
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), 0.0F, false);
            if (context.tardis().travelState() == TardisTravelState.FLIGHT) {
                TardisTravelManager.crash(context.level().getServer(), context.tardis().id(), new FlightFailure(FlightFailureType.INVALID_TRAVEL_STATE, FlightFailureSeverity.EMERGENCY, true, "engine_overload"));
            }
            send(context.player(), "Engine overload discharged");
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior LAND_TYPE = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            float value = (Math.round(context.currentValue()) + 1) % com.intothevortex.tardis.LandingType.values().length;
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            send(context.player(), "Landing mode: " + com.intothevortex.tardis.LandingType.fromValue(value).name());
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior MONITOR_NAVIGATION = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            float value = context.currentValue() >= 0.5F ? 0.0F : 1.0F;
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
            send(context.player(), context.definition().id() + ": " + (value >= 0.5F ? "Next" : "Previous"));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior SONIC_PORT = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || !context.tardis().powered()) return InteractionResult.FAILED_NO_POWER;
            context.level().sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, context.position().getX() + 0.5D, context.position().getY() + 1.0D, context.position().getZ() + 0.5D, 8, 0.35D, 0.35D, 0.35D, 0.02D);
            send(context.player(), "Sonic port active");
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior CONSOLE_PORT = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            send(context.player(), "Console port: " + context.console().console());
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior UTILITY = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), context.currentValue() >= 0.5F ? 0.0F : 1.0F, false);
            send(context.player(), context.definition().id() + " activated");
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior COORDINATE = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            return changeCoordinate(context, context.inputDelta() < 0.0F ? -1.0D : 1.0D);
        }

        @Override public InteractionResult onSecondaryPress(ControlUseContext context) {
            return changeCoordinate(context, -1.0D);
        }

        @Override public InteractionResult onDrag(ControlUseContext context, float value) {
            if (!Float.isFinite(value)) return InteractionResult.FAILED_INVALID_CONTROL_STATE;
            return setCoordinate(context, value);
        }
    };

    public static final ControlBehavior INCREMENT = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            float next = context.currentValue() >= 64.0F ? 1.0F : Math.min(512.0F, Math.max(1.0F, context.currentValue() * 2.0F));
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), next, false);
            send(context.player(), "Increment: " + Math.round(next));
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onSecondaryPress(ControlUseContext context) {
            float next = context.currentValue() <= 1.0F ? 512.0F : Math.max(1.0F, context.currentValue() / 2.0F);
            context.console().setAuthoritativeValue(context.player(), context.definition().id(), next, false);
            send(context.player(), "Increment: " + Math.round(next));
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior DIRECTION = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) { return changeDirection(context, 45.0F); }
        @Override public InteractionResult onSecondaryPress(ControlUseContext context) { return changeDirection(context, -45.0F); }
        @Override public InteractionResult onDrag(ControlUseContext context, float value) { return changeDirection(context, value - context.currentValue()); }
    };

    public static final ControlBehavior DIMENSION = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) { return changeDimension(context, 1); }
        @Override public InteractionResult onSecondaryPress(ControlUseContext context) { return changeDimension(context, -1); }
    };

    public static final ControlBehavior RANDOMISER = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            ServerLevel level = context.level().getServer().getLevel(ResourceKeyHelper.dimension(context.tardis().requestedDestinationDimension()));
            if (level == null) return InteractionResult.FAILED_INVALID_CONTROL_STATE;
            java.util.Random random = new java.util.Random(context.tardis().id().getLeastSignificantBits() ^ level.getGameTime());
            BlockPos destination = new BlockPos(random.nextInt(20001) - 10000, 64, random.nextInt(20001) - 10000);
            TardisManager.save(context.level().getServer(), context.tardis().withRequestedDestination(new TardisTravelDestination(level.dimension().identifier().toString(), destination, context.tardis().requestedDestinationYaw())));
            send(context.player(), "Random destination: " + destination);
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior FAST_RETURN = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || context.tardis().travelState() != TardisTravelState.LANDED) return InteractionResult.FAILED_INVALID_PHASE;
            ServerLevel source = context.level().getServer().getLevel(ResourceKeyHelper.dimension(context.tardis().travelSourceDimension()));
            if (source == null) return InteractionResult.FAILED_INVALID_CONTROL_STATE;
            return TardisTravelManager.startTravel(context.level().getServer(), context.tardis().id(), new TardisTravelDestination(context.tardis().travelSourceDimension(), context.tardis().travelSourcePosition(), context.tardis().travelSourceYaw())) ? InteractionResult.SUCCESS : InteractionResult.FAILED_INVALID_CONTROL_STATE;
        }
    };

    public static final ControlBehavior HAIL_MARY = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || context.tardis().travelState() != TardisTravelState.LANDED) return InteractionResult.FAILED_INVALID_PHASE;
            TardisData target = TardisCrashManager.fail(context.tardis(), new FlightFailure(FlightFailureType.LANDING_SEARCH_FAILED, FlightFailureSeverity.EMERGENCY, true, "hail_mary"));
            TardisManager.save(context.level().getServer(), target);
            send(context.player(), "Emergency landing mode armed");
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior ELECTRICAL_DISCHARGE = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || !context.tardis().powered()) return InteractionResult.FAILED_NO_POWER;
            AABB area = new AABB(context.position()).inflate(2.0D);
            context.level().getEntitiesOfClass(LivingEntity.class, area, entity -> entity != context.player()).forEach(entity -> entity.hurtServer(context.level(), context.level().damageSources().lightningBolt(), 2.0F));
            context.level().sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, context.position().getX() + 0.5D, context.position().getY() + 1.0D, context.position().getZ() + 0.5D, 20, 1.0D, 1.0D, 1.0D, 0.05D);
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior VISUALISER = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            send(context.player(), "Destination: " + context.tardis().requestedDestinationDimension() + " " + context.tardis().requestedDestinationPosition());
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior TELEPATHIC = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            ItemStack held = context.player().getMainHandItem();
            if (held.is(ModItems.TARDIS_KEY)) {
                TardisLinking.link(held, context.tardis().id());
                send(context.player(), "TARDIS key linked to " + context.tardis().id());
                return InteractionResult.SUCCESS;
            }
            return VISUALISER.onPress(context);
        }
    };

    public static final ControlBehavior SAVE_WAYPOINT = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            context.console().saveWaypoint(context.tardis());
            send(context.player(), "Waypoint saved: " + context.tardis().dimension() + " " + context.tardis().position());
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior LOAD_WAYPOINT = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || !context.console().hasWaypoint()) return InteractionResult.FAILED_INVALID_CONTROL_STATE;
            TardisData updated = context.tardis().withRequestedDestination(new TardisTravelDestination(context.console().waypointDimension(), context.console().waypointPosition(), context.console().waypointYaw()));
            TardisManager.save(context.level().getServer(), updated);
            send(context.player(), "Waypoint loaded: " + updated.requestedDestinationDimension() + " " + updated.requestedDestinationPosition());
            return InteractionResult.SUCCESS;
        }
    };

    private ControlBehaviors() {}

    private static InteractionResult changeCoordinate(ControlUseContext context, double delta) {
        if (context.tardis() == null || !Double.isFinite(delta)) return InteractionResult.FAILED_INVALID_CONTROL_STATE;
        long amount = Math.max(1L, Math.round(context.console().controlValue("increment")));
        long change = Math.round(delta * amount);
        BlockPos current = context.tardis().requestedDestinationPosition();
        long x = current.getX(), y = current.getY(), z = current.getZ();
        if (context.definition().id().equals("x")) x = Math.clamp(x + change, -29999999L, 29999999L);
        if (context.definition().id().equals("y")) y = Math.clamp(y + change, -2032L, 2031L);
        if (context.definition().id().equals("z")) z = Math.clamp(z + change, -29999999L, 29999999L);
        TardisData updated = context.tardis().withRequestedDestination(new TardisTravelDestination(context.tardis().requestedDestinationDimension(), new BlockPos((int) x, (int) y, (int) z), context.tardis().requestedDestinationYaw()));
        TardisManager.save(context.level().getServer(), updated);
        context.console().syncCoordinateValues(updated.requestedDestinationPosition());
        send(context.player(), context.definition().id().toUpperCase(java.util.Locale.ROOT) + ": " + new BlockPos((int) x, (int) y, (int) z));
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult setCoordinate(ControlUseContext context, float value) {
        if (context.tardis() == null || !Float.isFinite(value)) return InteractionResult.FAILED_INVALID_CONTROL_STATE;
        BlockPos current = context.tardis().requestedDestinationPosition();
        int coordinate = Math.round(Math.clamp(value, context.definition().minimum(), context.definition().maximum()));
        int x = current.getX(), y = current.getY(), z = current.getZ();
        if (context.definition().id().equals("x")) x = coordinate;
        if (context.definition().id().equals("y")) y = coordinate;
        if (context.definition().id().equals("z")) z = coordinate;
        TardisData updated = context.tardis().withRequestedDestination(new TardisTravelDestination(context.tardis().requestedDestinationDimension(), new BlockPos(x, y, z), context.tardis().requestedDestinationYaw()));
        TardisManager.save(context.level().getServer(), updated);
        context.console().syncCoordinateValues(updated.requestedDestinationPosition());
        send(context.player(), context.definition().id().toUpperCase(java.util.Locale.ROOT) + ": " + coordinate);
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult changeDirection(ControlUseContext context, float delta) {
        if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
        float yaw = net.minecraft.util.Mth.wrapDegrees(context.tardis().requestedDestinationYaw() + delta);
        TardisManager.save(context.level().getServer(), context.tardis().withRequestedDestination(new TardisTravelDestination(context.tardis().requestedDestinationDimension(), context.tardis().requestedDestinationPosition(), yaw)));
        send(context.player(), "Direction: " + yaw);
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult changeDimension(ControlUseContext context, int direction) {
        if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
        List<ServerLevel> levels = new java.util.ArrayList<>();
        for (ServerLevel level : context.level().getServer().getAllLevels()) if (TardisDimensionManager.id(level.dimension()) == null) levels.add(level);
        if (levels.isEmpty()) return InteractionResult.FAILED_INVALID_CONTROL_STATE;
        int current = 0;
        for (int i = 0; i < levels.size(); i++) if (levels.get(i).dimension().identifier().toString().equals(context.tardis().requestedDestinationDimension())) current = i;
        ServerLevel selected = levels.get(Math.floorMod(current + direction, levels.size()));
        TardisManager.save(context.level().getServer(), context.tardis().withRequestedDestination(new TardisTravelDestination(selected.dimension().identifier().toString(), context.tardis().requestedDestinationPosition(), context.tardis().requestedDestinationYaw())));
        send(context.player(), "Dimension: " + selected.dimension().identifier());
        return InteractionResult.SUCCESS;
    }

    private static final class ResourceKeyHelper {
        private static net.minecraft.resources.ResourceKey<Level> dimension(String value) {
            return net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, Identifier.parse(value));
        }
    }

    private static void send(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal(message), true);
    }
}
