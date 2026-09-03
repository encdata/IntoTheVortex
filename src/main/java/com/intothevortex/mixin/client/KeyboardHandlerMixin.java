package com.intothevortex.mixin.client;

import com.intothevortex.client.ControlInputManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public final class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", ordinal = 0, opcode = 180), cancellable = true)
    private void intothevortex$controlCameraKey(long window, int action, KeyEvent event, CallbackInfo info) {
        if (Minecraft.getInstance().player != null && ControlInputManager.beforeKeyInput(event.key(), action)) info.cancel();
    }
}
