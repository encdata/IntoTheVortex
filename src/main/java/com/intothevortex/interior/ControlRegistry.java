package com.intothevortex.interior;

import com.intothevortex.IntoTheVortex;
import net.minecraft.resources.Identifier;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ControlRegistry {
    public record RegisteredControl(Identifier id, Set<ConsoleInputType> supportedTypes) {}

    private static final Map<Identifier, RegisteredControl> CONTROLS = new LinkedHashMap<>();

    private ControlRegistry() {}

    public static void initialize() {
        register("anti_gravs", ConsoleInputType.SWITCH);
        register("auto_pilot", ConsoleInputType.SWITCH);
        register("cloak", ConsoleInputType.SWITCH);
        register("dimension", ConsoleInputType.KNOB);
        register("direction", ConsoleInputType.KNOB);
        register("door", ConsoleInputType.BUTTON, ConsoleInputType.SWITCH);
        register("door_lock", ConsoleInputType.KEY_SWITCH, ConsoleInputType.BUTTON);
        register("fast_return", ConsoleInputType.BUTTON);
        register("hads", ConsoleInputType.SWITCH);
        register("hail_mary", ConsoleInputType.BUTTON);
        register("handbrake", ConsoleInputType.LEVER, ConsoleInputType.SWITCH);
        register("land_type", ConsoleInputType.SWITCH, ConsoleInputType.BUTTON);
        register("monitor", ConsoleInputType.BUTTON);
        register("monitor_upper", ConsoleInputType.BUTTON);
        register("monitor_left", ConsoleInputType.BUTTON);
        register("power", ConsoleInputType.SWITCH);
        register("randomiser", ConsoleInputType.KNOB);
        register("refueler", ConsoleInputType.BUTTON);
        register("security", ConsoleInputType.SWITCH);
        register("siege_mode", ConsoleInputType.SWITCH);
        register("sonic_port", ConsoleInputType.BUTTON);
        register("telepathic", ConsoleInputType.BUTTON);
        register("throttle", ConsoleInputType.LEVER);
        register("visualiser", ConsoleInputType.BUTTON);
        register("engine_overload", ConsoleInputType.BUTTON);
        register("electrical_discharge", ConsoleInputType.BUTTON);
        register("shields", ConsoleInputType.SWITCH);
        register("console_port", ConsoleInputType.BUTTON);
        register("save_waypoint", ConsoleInputType.BUTTON);
        register("load_waypoint", ConsoleInputType.BUTTON);
        register("increment", ConsoleInputType.KNOB);
        register("x", ConsoleInputType.KNOB);
        register("y", ConsoleInputType.KNOB);
        register("z", ConsoleInputType.KNOB);
    }

    public static RegisteredControl register(String path, ConsoleInputType... types) {
        Identifier id = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, path);
        RegisteredControl control = new RegisteredControl(id, Set.copyOf(EnumSet.of(types[0], types)));
        CONTROLS.put(id, control);
        return control;
    }

    public static RegisteredControl get(String path) {
        return CONTROLS.get(Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, path));
    }

    public static boolean supports(String path, ConsoleInputType type) {
        RegisteredControl control = get(path);
        return control != null && control.supportedTypes().contains(type);
    }
}
