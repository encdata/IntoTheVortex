package com.intothevortex.mixin.client;

import com.intothevortex.client.ControlInputManager;
import com.intothevortex.entity.ControlHitboxEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ControlHitboxEntity.class)
public final class ControlHitboxEntityMixin {
    @Inject(method = "interact", at = @At("HEAD"))
    private void intothevortex$startHold(Player player, InteractionHand hand, Vec3 hit, CallbackInfoReturnable<InteractionResult> info) {
        if (player.level().isClientSide() && player.isLocalPlayer()) ControlInputManager.startFromEntity((ControlHitboxEntity) (Object) this);
    }
}
