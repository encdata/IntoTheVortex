package com.intothevortex.interior;

import com.intothevortex.IntoTheVortex;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;

public final class ConsoleRegistry {
    public static final Identifier TOYOTA = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "toyota");
    private static final Map<Identifier, ConsoleDefinition> DEFINITIONS = new LinkedHashMap<>();

    private ConsoleRegistry() {}

    public static void initialize() {
        register(new ConsoleDefinition(TOYOTA, id("interior/toyota"), id("textures/blockentities/consoles/toyota_default.png"), id("textures/blockentities/consoles/toyota_emission.png"), java.util.List.of(
        control("throttle", ConsoleInputType.LEVER, 0.440625F, 0.575F, 1.435938F, 0.2125F, 0.2F, "throttle", 0.0F, 4.0F),
        control("handbrake", ConsoleInputType.LEVER, -0.525781F, 0.5625F, 1.4125F, 0.2F, 0.15F, "handbrake", 0.0F, 1.0F),
        control("auto_pilot", ConsoleInputType.SWITCH, -0.396094F, 0.6500F, 1.053125F, 0.1125F, 0.1F, "autopilot"),
        control("door", ConsoleInputType.BUTTON, -0.377344F, 0.5875F, -1.398438F, 0.1625F, 0.175F, "door_control"),
        control("door_lock", ConsoleInputType.KEY_SWITCH, 0.348438F, 0.7125F, -0.921875F, 0.075F, 0.0875F, "door_lock"),
        control("anti_gravs", ConsoleInputType.SWITCH, 0.202344F, 0.7375F, -0.83125F, 0.1125F, 0.1F, "antigravs"),
        control("monitor", ConsoleInputType.BUTTON, 0.986719F, 0.5875F, -0.565625F, 0.4375F, 0.1625F, "monitor"),
        control("engine_overload", ConsoleInputType.BUTTON, 1.411719F, 0.5375F, -0.240625F, 0.1F, 0.1625F, "engine_overload"),
        control("fast_return", ConsoleInputType.BUTTON, 0.661719F, 0.575F, 1.459375F, 0.0875F, 0.1125F, "fast_return"),
        control("electrical_discharge", ConsoleInputType.BUTTON, 0.088281F, 0.575F, -1.459375F, 0.075F, 0.0875F, "electrical_discharge"),
        control("monitor_upper", ConsoleInputType.BUTTON, 0.725781F, 1.0F, -0.4625F, 0.4375F, 0.375F, "monitor_upper"),
        control("monitor_left", ConsoleInputType.BUTTON, -0.736719F, 1.0F, 0.4625F, 0.4375F, 0.375F, "monitor_left"),
        control("visualiser", ConsoleInputType.BUTTON, -1.223047F, 0.55F, 0.600782F, 0.2375F, 0.0875F, "visualiser"),
        control("security", ConsoleInputType.SWITCH, 0.191406F, 0.775F, 0.751953F, 0.075F, 0.1625F, "security"),
        control("telepathic", ConsoleInputType.BUTTON, 0.925F, 0.5625F, 0.539063F, 0.8F, 0.1875F, "telepathic"),
        control("land_type", ConsoleInputType.BUTTON, -0.399219F, 0.6775F, -1.06875F, 0.1625F, 0.15F, "land_type", 0.0F, 3.0F),
        control("increment", ConsoleInputType.KNOB, -0.8875F, 0.6500F, -0.773438F, 0.1125F, 0.1F, "increment"),
        control("x", ConsoleInputType.KNOB, -0.786719F, 0.7F, -0.545313F, 0.0875F, 0.0875F, "x", -29999999.0F, 29999999.0F),
        control("y", ConsoleInputType.KNOB, -1.046875F, 0.65F, -0.435938F, 0.0875F, 0.0875F, "y", -2032.0F, 2031.0F),
        control("z", ConsoleInputType.KNOB, -1.0875F, 0.6F, -0.723438F, 0.0875F, 0.0875F, "z", -29999999.0F, 29999999.0F),
        control("randomiser", ConsoleInputType.KNOB, 0.585156F, 0.575F, -1.425F, 0.1375F, 0.075F, "randomiser"),
        control("direction", ConsoleInputType.KNOB, 0.5375F, 0.75F, -0.626563F, 0.1125F, 0.1125F, "direction"),
        control("hail_mary", ConsoleInputType.BUTTON, -0.958594F, 0.575F, -1.2625F, 0.1125F, 0.125F, "hail_mary"),
        control("dimension", ConsoleInputType.KNOB, 0.911719F, 0.595F, -1.101563F, 0.1125F, 0.1125F, "dimension"),
        control("refueler", ConsoleInputType.BUTTON, -1.5F, 0.575F, -0.304688F, 0.1F, 0.1125F, "refueler"),
        control("power", ConsoleInputType.SWITCH, 0.376563F, 0.5875F, -1.385938F, 0.175F, 0.175F, "power"),
        control("siege_mode", ConsoleInputType.SWITCH, -0.004687F, 0.1375F, 1.513281F, 0.2F, 0.225F, "siege_mode"),
        control("hads", ConsoleInputType.SWITCH, -0.095312F, 0.7625F, 0.76875F, 0.075F, 0.175F, "hads"),
        control("save_waypoint", ConsoleInputType.BUTTON, 0.188281F, 0.575F, -1.459375F, 0.075F, 0.0875F, "save_waypoint"),
        control("load_waypoint", ConsoleInputType.BUTTON, 0.150781F, 0.7F, -1.060938F, 0.0625F, 0.1125F, "load_waypoint"),
        control("console_port", ConsoleInputType.BUTTON, 0.001562F, 0.7375F, -0.795313F, 0.125F, 0.1125F, "console_port"),
        control("cloak", ConsoleInputType.SWITCH, 0.827344F, 0.7500F, -0.160938F, 0.1F, 0.1125F, "cloak"),
        control("sonic_port", ConsoleInputType.BUTTON, -1.060547F, 0.7625F, 0.213282F, 0.125F, 0.0875F, "sonic_port"),
        control("shields", ConsoleInputType.SWITCH, -0.210937F, 0.7375F, -0.825F, 0.125F, 0.1F, "shields")
        )));
    }

    private static ConsoleControlDefinition control(String id, ConsoleInputType type, float x, float y, float z, float width, float height, String modelPart) {
        return control(id, type, x, y, z, width, height, modelPart, 0.0F, 1.0F);
    }

    private static ConsoleControlDefinition control(String id, ConsoleInputType type, float x, float y, float z, float width, float height, String modelPart, float minimum, float maximum) {
        if (!ControlRegistry.supports(id, type)) throw new IllegalArgumentException("Unsupported control type " + type + " for " + id);
        return new ConsoleControlDefinition(id, type, new Vector3f(x, y, z), width, height, modelPart, minimum, maximum);
    }

    private static Identifier id(String path) { return Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, path); }

    public static void register(ConsoleDefinition definition) { DEFINITIONS.put(definition.id(), definition); }
    public static ConsoleDefinition get(Identifier id) { return DEFINITIONS.getOrDefault(id, DEFINITIONS.get(TOYOTA)); }
    public static Collection<ConsoleDefinition> values() { return java.util.List.copyOf(DEFINITIONS.values()); }
}
