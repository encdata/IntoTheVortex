package com.intothevortex.interior;

import com.intothevortex.IntoTheVortex;
import net.minecraft.resources.Identifier;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ControlRegistry {
    public record RegisteredControl(Identifier id, Set<ConsoleInputType> supportedTypes, Set<ControlMode> modes, Set<ControlCapability> capabilities, ControlBehavior behavior) {}

    private static final Map<Identifier, RegisteredControl> CONTROLS = new LinkedHashMap<>();

    private ControlRegistry() {}

    public static void initialize() {
        register("anti_gravs", ControlBehaviors.TOGGLE, ConsoleInputType.SWITCH);
        register("auto_pilot", ControlBehaviors.AUTOPILOT, defaultModes(ConsoleInputType.SWITCH), defaultCapabilities(ConsoleInputType.SWITCH), ConsoleInputType.SWITCH);
        register("cloak", ControlBehaviors.TOGGLE, ConsoleInputType.SWITCH);
        register("dimension", ControlBehaviors.DIMENSION, ConsoleInputType.KNOB);
        register("direction", ControlBehaviors.DIRECTION, ConsoleInputType.KNOB);
        register("door", ControlBehaviors.DOOR, defaultModes(ConsoleInputType.BUTTON, ConsoleInputType.SWITCH), defaultCapabilities(ConsoleInputType.BUTTON, ConsoleInputType.SWITCH), ConsoleInputType.BUTTON, ConsoleInputType.SWITCH);
        register("door_lock", ControlBehaviors.DOOR_LOCK, defaultModes(ConsoleInputType.KEY_SWITCH, ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.KEY_SWITCH, ConsoleInputType.BUTTON), ConsoleInputType.KEY_SWITCH, ConsoleInputType.BUTTON);
        register("fast_return", ControlBehaviors.FAST_RETURN, defaultModes(ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.BUTTON), ConsoleInputType.BUTTON);
        register("hads", ControlBehaviors.TOGGLE, ConsoleInputType.SWITCH);
        register("hail_mary", ControlBehaviors.HAIL_MARY, defaultModes(ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.BUTTON), ConsoleInputType.BUTTON);
        register("handbrake", ControlBehaviors.HANDBRAKE, defaultModes(ConsoleInputType.LEVER, ConsoleInputType.SWITCH), defaultCapabilities(ConsoleInputType.LEVER, ConsoleInputType.SWITCH), ConsoleInputType.LEVER, ConsoleInputType.SWITCH);
        register("land_type", ConsoleInputType.SWITCH, ConsoleInputType.BUTTON);
        register("monitor", ControlBehaviors.MONITOR, defaultModes(ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.BUTTON), ConsoleInputType.BUTTON);
        register("monitor_upper", ConsoleInputType.BUTTON);
        register("monitor_left", ConsoleInputType.BUTTON);
        register("power", ControlBehaviors.POWER, defaultModes(ConsoleInputType.SWITCH), defaultCapabilities(ConsoleInputType.SWITCH), ConsoleInputType.SWITCH);
        register("randomiser", ConsoleInputType.KNOB);
        register("refueler", ControlBehaviors.REFUELER, defaultModes(ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.BUTTON), ConsoleInputType.BUTTON);
        register("security", ConsoleInputType.SWITCH);
        register("siege_mode", ConsoleInputType.SWITCH);
        register("sonic_port", ConsoleInputType.BUTTON);
        register("telepathic", ConsoleInputType.BUTTON);
        register("throttle", ControlBehaviors.THROTTLE, defaultModes(ConsoleInputType.LEVER), defaultCapabilities(ConsoleInputType.LEVER), ConsoleInputType.LEVER);
        register("visualiser", ConsoleInputType.BUTTON);
        register("engine_overload", ControlBehaviors.TOGGLE, ConsoleInputType.BUTTON);
        register("electrical_discharge", ControlBehaviors.ELECTRICAL_DISCHARGE, defaultModes(ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.BUTTON), ConsoleInputType.BUTTON);
        register("shields", ControlBehaviors.TOGGLE, ConsoleInputType.SWITCH);
        register("console_port", ConsoleInputType.BUTTON);
        register("save_waypoint", ControlBehaviors.SAVE_WAYPOINT, defaultModes(ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.BUTTON), ConsoleInputType.BUTTON);
        register("load_waypoint", ControlBehaviors.LOAD_WAYPOINT, defaultModes(ConsoleInputType.BUTTON), defaultCapabilities(ConsoleInputType.BUTTON), ConsoleInputType.BUTTON);
        register("increment", ControlBehaviors.INCREMENT, ConsoleInputType.KNOB);
        register("x", ControlBehaviors.COORDINATE, ConsoleInputType.KNOB);
        register("y", ControlBehaviors.COORDINATE, ConsoleInputType.KNOB);
        register("z", ControlBehaviors.COORDINATE, ConsoleInputType.KNOB);
    }

    public static RegisteredControl register(String path, ConsoleInputType... types) {
        return register(path, ControlBehaviors.DEFAULT, defaultModes(types), defaultCapabilities(types), types);
    }

    public static RegisteredControl register(String path, ControlBehavior behavior, ConsoleInputType... types) {
        return register(path, behavior, defaultModes(types), defaultCapabilities(types), types);
    }

    public static RegisteredControl register(String path, ModeCallback callback, ConsoleInputType... types) {
        return register(path, behavior(callback), defaultModes(types), defaultCapabilities(types), types);
    }

    public static RegisteredControl register(String path, ModeCallback callback, Set<ControlMode> modes, ConsoleInputType... types) {
        return register(path, behavior(callback), modes, defaultCapabilities(types), types);
    }

    public static RegisteredControl register(String path, ControlBehavior behavior, Set<ControlMode> modes, Set<ControlCapability> capabilities, ConsoleInputType... types) {
        if (types.length == 0) throw new IllegalArgumentException("A control requires at least one input type");
        if (behavior == null) throw new IllegalArgumentException("A control requires a behavior");
        Identifier id = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, path);
        RegisteredControl control = new RegisteredControl(id, Set.copyOf(EnumSet.of(types[0], types)), Set.copyOf(modes), Set.copyOf(capabilities), behavior);
        CONTROLS.put(id, control);
        return control;
    }

    public static RegisteredControl register(String path, ControlBehavior behavior, Set<ControlCapability> capabilities, ConsoleInputType... types) {
        return register(path, behavior, defaultModes(types), capabilities, types);
    }

    public static RegisteredControl get(String path) {
        return CONTROLS.get(Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, path));
    }

    public static boolean supports(String path, ConsoleInputType type) {
        RegisteredControl control = get(path);
        return control != null && control.supportedTypes().contains(type);
    }

    public static boolean supports(String path, ControlMode mode) {
        RegisteredControl control = get(path);
        return control != null && control.modes().contains(mode);
    }

    private static Set<ControlCapability> defaultCapabilities(ConsoleInputType... types) {
        EnumSet<ControlCapability> capabilities = EnumSet.of(ControlCapability.BUTTON);
        for (ConsoleInputType type : types) {
            if (type == ConsoleInputType.SWITCH || type == ConsoleInputType.KEY_SWITCH || type == ConsoleInputType.TOGGLE) capabilities.add(ControlCapability.TOGGLE);
            if (type == ConsoleInputType.LEVER) {
                capabilities.add(ControlCapability.DRAG);
                capabilities.add(ControlCapability.MULTI_STATE);
            }
            if (type == ConsoleInputType.KNOB || type == ConsoleInputType.KEY_SWITCH || type == ConsoleInputType.DIAL || type == ConsoleInputType.VALVE || type == ConsoleInputType.JOYSTICK) capabilities.add(ControlCapability.DRAG);
            if (type == ConsoleInputType.MOMENTARY_BUTTON) {
                capabilities.add(ControlCapability.PRESS_DOWN);
                capabilities.add(ControlCapability.RELEASE);
            }
        }
        return capabilities;
    }

    private static Set<ControlMode> defaultModes(ConsoleInputType... types) {
        EnumSet<ControlMode> modes = EnumSet.of(ControlMode.RIGHT_CLICK_UP, ControlMode.LEFT_CLICK_DOWN);
        for (ConsoleInputType type : types) {
            if (type == ConsoleInputType.LEVER) modes.add(ControlMode.DRAG_VERTICAL);
            if (type == ConsoleInputType.KNOB || type == ConsoleInputType.KEY_SWITCH || type == ConsoleInputType.DIAL || type == ConsoleInputType.VALVE) modes.add(ControlMode.DRAG_HORIZONTAL);
        }
        return modes;
    }

    private static ControlBehavior behavior(ModeCallback callback) {
        return new ControlBehavior() {
            @Override public InteractionResult onPress(ControlUseContext context) {
                callback.apply(context.console(), context.player(), context.definition().id(), context.inputDelta() == 0.0F ? 1.0F : context.inputDelta());
                return InteractionResult.SUCCESS;
            }

            @Override public InteractionResult onSecondaryPress(ControlUseContext context) {
                callback.apply(context.console(), context.player(), context.definition().id(), -1.0F);
                return InteractionResult.SUCCESS;
            }

            @Override public InteractionResult onDrag(ControlUseContext context, float value) {
                context.console().setAuthoritativeValue(context.player(), context.definition().id(), value, false);
                return InteractionResult.SUCCESS;
            }
        };
    }
}
