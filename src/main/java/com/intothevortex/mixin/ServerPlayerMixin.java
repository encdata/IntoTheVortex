package com.intothevortex.mixin;

import com.intothevortex.dimension.TardisDimensionManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Shadow private MinecraftServer server;
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void intothevortex$restoreRuntimeDimension(CompoundTag tag, CallbackInfo info) {
        String dimension = tag.getStringOr("Dimension", "");
        if (!dimension.startsWith("intothevortex:")) return;
        ServerPlayer player = (ServerPlayer) (Object) this;
        java.util.UUID id = TardisDimensionManager.id(TardisDimensionManager.parseDimension(dimension));
        if (id == null) return;
        server.execute(() -> {
            var level = TardisDimensionManager.ensureLoaded(server, id);
            if (player.level() == server.overworld() && level != null) {
                player.teleport(new net.minecraft.world.level.portal.TeleportTransition(level, player.position(), player.getDeltaMovement(), player.getYRot(), player.getXRot(), net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING));
            }
        });
    }
}
