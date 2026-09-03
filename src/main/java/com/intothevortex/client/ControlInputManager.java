package com.intothevortex.client;

import com.intothevortex.entity.ControlHitboxEntity;
import com.intothevortex.interior.ConsoleControlDefinition;
import com.intothevortex.interior.ConsoleInputType;
import com.intothevortex.network.ControlValuePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public final class ControlInputManager {
    private static ControlHitboxEntity active;
    private static ConsoleControlDefinition definition;
    private static float value;
    private static boolean allowCamera;

    private ControlInputManager() {}

    public static void initialize() {
    }

    public static boolean beforeMouseInput(int button, int action) {
        if (!(Minecraft.getInstance().player instanceof LocalPlayer) || Minecraft.getInstance().screen != null) return false;
        if (action == GLFW.GLFW_RELEASE) {
            if (active != null && (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT || button == GLFW.GLFW_MOUSE_BUTTON_LEFT)) release();
            return active != null;
        }
        if (action != GLFW.GLFW_PRESS || active != null || !(Minecraft.getInstance().hitResult instanceof EntityHitResult hit) || !(hit.getEntity() instanceof ControlHitboxEntity control)) return active != null;
        ConsoleControlDefinition next = control.definition();
        if (next == null || (next.inputType() != ConsoleInputType.LEVER && next.inputType() != ConsoleInputType.KNOB && next.inputType() != ConsoleInputType.JOYSTICK && next.inputType() != ConsoleInputType.MOMENTARY_BUTTON && next.inputType() != ConsoleInputType.KEY_SWITCH)) return false;
        begin(control);
        return true;
    }

    public static boolean onMouseMove(double dx, double dy) {
        if (active == null || definition == null) return false;
        if (allowCamera) return false;
        float old = value;
        if (definition.inputType() == ConsoleInputType.LEVER) value = clamp(value - (float) dy / 160.0F, definition);
        if (definition.inputType() == ConsoleInputType.KNOB || definition.inputType() == ConsoleInputType.KEY_SWITCH) value = clamp(value + (float) dx / 160.0F, definition);
        if (definition.inputType() == ConsoleInputType.JOYSTICK) value = clamp(value + (float) dx / 160.0F, definition);
        if (old != value) send(active, value, false);
        return true;
    }

    public static boolean beforeKeyInput(int key, int action) {
        if (active == null || key != GLFW.GLFW_KEY_R) return false;
        allowCamera = action != GLFW.GLFW_RELEASE;
        return true;
    }

    private static void begin(ControlHitboxEntity control) {
        active = control;
        definition = control.definition();
        value = control.value();
    }

    private static void release() {
        if (active == null) return;
        if (definition != null && (definition.inputType() == ConsoleInputType.MOMENTARY_BUTTON || definition.inputType() == ConsoleInputType.JOYSTICK)) send(active, definition.inputType() == ConsoleInputType.JOYSTICK ? 0.0F : value, true);
        active = null;
        definition = null;
        allowCamera = false;
    }

    private static void send(ControlHitboxEntity control, float value, boolean released) {
        ClientPlayNetworking.send(new ControlValuePayload(control.consolePos(), control.controlId(), value, released));
    }

    private static float clamp(float value, ConsoleControlDefinition definition) {
        return Math.clamp(value, definition.minimum(), definition.maximum());
    }

}
