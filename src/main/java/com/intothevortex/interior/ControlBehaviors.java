package com.intothevortex.interior;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.item.ModItems;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

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
        @Override public InteractionResult onPress(ControlUseContext context) {
            boolean powered = !context.console().powered();
            context.console().setPowered(context.player(), powered);
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior REFUELER = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null || context.tardis().travelState() != com.intothevortex.tardis.TardisTravelState.LANDED || context.console().controlValue("handbrake") < 0.5F) return InteractionResult.FAILED_INVALID_PHASE;
            context.console().setRefueling(context.player(), !context.console().refueling());
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior HANDBRAKE = new ControlBehavior() {
        @Override public InteractionResult onPress(ControlUseContext context) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            context.console().setHandbrakeEngaged(context.player(), !context.tardis().isHandbrakeEngaged());
            return InteractionResult.SUCCESS;
        }

        @Override public InteractionResult onDrag(ControlUseContext context, float value) {
            if (context.tardis() == null) return InteractionResult.FAILED_INVALID_CONTROL;
            context.console().setHandbrakeEngaged(context.player(), value >= 0.5F);
            return InteractionResult.SUCCESS;
        }
    };

    public static final ControlBehavior THROTTLE = new ControlBehavior() {
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

    private ControlBehaviors() {}

    private static void send(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal(message), true);
    }
}
