package com.intothevortex.client;

import com.intothevortex.entity.ControlHitboxEntity;
import com.intothevortex.interior.ConsoleControlDefinition;
import com.intothevortex.interior.ConsoleInputType;
import com.intothevortex.network.ControlValuePayload;
import com.intothevortex.network.ControlActivatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import org.lwjgl.glfw.GLFW;

public final class ControlInputManager {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("IntoTheVortex/ControlInput");
    private static ControlHitboxEntity active;
    private static ConsoleControlDefinition definition;
    private static float value;
    private static boolean allowCamera;
    private static int activeButton = -1;
    private static boolean consumesCamera;
    private static boolean movementLogged;
    private static final KeyMapping MOVE_CAMERA = new KeyMapping("key.intothevortex.hold_move_camera", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyMapping.Category.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("intothevortex", "controls")));

    private ControlInputManager() {}

    public static void initialize() {
        KeyMappingHelper.registerKeyMapping(MOVE_CAMERA);
    }

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        if (active == null && minecraft.player instanceof LocalPlayer && minecraft.screen == null && minecraft.mouseHandler.isRightPressed() && minecraft.crosshairPickEntity instanceof ControlHitboxEntity control) {
            startFromEntity(control);
            if (active != null && isClickControl(definition)) ClientPlayNetworking.send(new ControlActivatePayload(control.consolePos(), control.controlId()));
        }
        if (active == null) return;
        if (!(minecraft.player instanceof LocalPlayer player) || minecraft.screen != null || active.isRemoved() || active.level() != player.level() || player.distanceToSqr(active.consolePos().getCenter()) > 36.0D) stop();
    }

    public static boolean beforeMouseInput(int button, int action) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.player instanceof LocalPlayer) || minecraft.screen != null) return false;
        if (active != null) {
            if (action == GLFW.GLFW_RELEASE && button == activeButton) stop();
            return true;
        }
        if (action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && minecraft.crosshairPickEntity instanceof ControlHitboxEntity control) {
            startFromEntity(control);
            if (active != null && isClickControl(definition)) ClientPlayNetworking.send(new ControlActivatePayload(control.consolePos(), control.controlId()));
            return active != null;
        }
        return false;
    }

    public static boolean onMouseMove(double dx, double dy) {
        if (active == null || definition == null || !consumesCamera) return false;
        if (allowCamera) return false;
        float old = value;
        if (definition.inputType() == ConsoleInputType.LEVER) value = clamp(value - (float) dy / 160.0F, definition);
        if (definition.inputType() == ConsoleInputType.KNOB || definition.inputType() == ConsoleInputType.KEY_SWITCH) value = clamp(value + (float) dx / 160.0F, definition);
        if (definition.inputType() == ConsoleInputType.JOYSTICK) value = clamp(value + (float) dx / 180.0F, definition);
        if (old != value) send(active, value, false);
        if (!movementLogged && (dx != 0.0D || dy != 0.0D)) {
            movementLogged = true;
            LOGGER.info("Control drag movement received: id={}, dx={}, dy={}, value={}", active.controlId(), dx, dy, value);
        }
        return true;
    }

    public static boolean beforeKeyInput(int key, int scanCode, int action, int modifiers) {
        if (active == null || action == GLFW.GLFW_REPEAT) return false;
        if (!MOVE_CAMERA.matches(new net.minecraft.client.input.KeyEvent(key, scanCode, modifiers))) return false;
        allowCamera = action != GLFW.GLFW_RELEASE;
        return true;
    }

    public static void startFromEntity(ControlHitboxEntity control) {
        if (active != null || control.isRemoved()) return;
        ConsoleControlDefinition next = control.definition();
        if (next == null) {
            LOGGER.warn("Control hold rejected because definition is unavailable: id={}, pos={}, dimension={}", control.controlId(), control.consolePos(), control.level().dimension().identifier());
            return;
        }
        active = control;
        definition = next;
        value = control.value();
        activeButton = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
        consumesCamera = isDragControl(definition);
        movementLogged = false;
        LOGGER.info("Control hold started: id={}, type={}, pos={}, drag={}, dimension={}", control.controlId(), definition.inputType(), control.consolePos(), consumesCamera, control.level().dimension().identifier());
        if (definition.inputType() == ConsoleInputType.MOMENTARY_BUTTON) send(active, 1.0F, false);
    }

    private static void release() {
        if (active == null) return;
        if (definition != null && (definition.inputType() == ConsoleInputType.MOMENTARY_BUTTON || definition.inputType() == ConsoleInputType.JOYSTICK)) send(active, definition.inputType() == ConsoleInputType.JOYSTICK ? 0.0F : value, true);
        active = null;
        definition = null;
        allowCamera = false;
        activeButton = -1;
        consumesCamera = false;
    }

    private static void stop() {
        if (active != null) LOGGER.info("Control hold stopped: id={}, value={}", active.controlId(), value);
        release();
    }

    private static boolean isDragControl(ConsoleControlDefinition control) {
        ConsoleInputType type = control.inputType();
        return type == ConsoleInputType.LEVER || type == ConsoleInputType.KNOB || type == ConsoleInputType.JOYSTICK || type == ConsoleInputType.MOMENTARY_BUTTON || type == ConsoleInputType.KEY_SWITCH;
    }

    private static boolean isClickControl(ConsoleControlDefinition control) {
        return !isDragControl(control);
    }

    public static boolean isActive() {
        return active != null;
    }

    private static void send(ControlHitboxEntity control, float value, boolean released) {
        ClientPlayNetworking.send(new ControlValuePayload(control.consolePos(), control.controlId(), value, released));
    }

    private static float clamp(float value, ConsoleControlDefinition definition) {
        return Math.clamp(value, definition.minimum(), definition.maximum());
    }

}
