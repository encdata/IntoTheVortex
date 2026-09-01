package com.intothevortex.mixin.client;

import com.intothevortex.client.render.TardisSkyRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.world.level.MoonPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
    @Inject(method = "renderSkyDisc", at = @At("HEAD"), cancellable = true)
    private void intothevortex$renderTardisSky(int color, CallbackInfo ci) {
        if (!TardisSkyRenderer.shouldRender()) return;
        TardisSkyRenderer.render();
        ci.cancel();
    }

    @Inject(method = "renderSunriseAndSunset", at = @At("HEAD"), cancellable = true)
    private void intothevortex$hideSunrise(PoseStack matrices, float alpha, int color, CallbackInfo ci) {
        if (TardisSkyRenderer.shouldRender()) ci.cancel();
    }

    @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
    private void intothevortex$hideCelestials(PoseStack matrices, float timeOfDay, float rainGradient, float starBrightness, MoonPhase moonPhase, float alpha, float brightness, CallbackInfo ci) {
        if (TardisSkyRenderer.shouldRender()) ci.cancel();
    }

    @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
    private void intothevortex$hideDarkDisc(CallbackInfo ci) {
        if (TardisSkyRenderer.shouldRender()) ci.cancel();
    }
}
