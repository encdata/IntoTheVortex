package com.intothevortex.mixin.client;

import com.intothevortex.client.IntoTheVortexClient;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void intothevortex$hideRwfPilot(Avatar avatar, AvatarRenderState state, float tickDelta, CallbackInfo callbackInfo) {
        if (IntoTheVortexClient.isRwfActive() && avatar == net.minecraft.client.Minecraft.getInstance().player) state.isInvisibleToPlayer = true;
    }
}
