package com.intothevortex.mixin.client;

import com.intothevortex.client.ControlInputManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public final class MouseHandlerMixin {
    @Shadow private double accumulatedDX;
    @Shadow private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    private void intothevortex$consumeControlDrag(double time, CallbackInfo info) {
        if (Minecraft.getInstance().player != null && ControlInputManager.onMouseMove(accumulatedDX, accumulatedDY)) info.cancel();
    }

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void intothevortex$consumeControlPress(long window, MouseButtonInfo button, int action, CallbackInfo info) {
        if (Minecraft.getInstance().player != null && ControlInputManager.beforeMouseInput(button.button(), action)) info.cancel();
    }
}
