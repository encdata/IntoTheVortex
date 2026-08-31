package com.intothevortex.mixin;

import com.intothevortex.dimension.TardisDimensionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    private static final java.util.Map<java.util.UUID, java.util.UUID> PENDING_DIMENSIONS = new java.util.HashMap<>();
    @Shadow private MinecraftServer server;
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void intothevortex$restoreRuntimeDimension(ValueInput input, CallbackInfo info) {
        String dimension = input.getStringOr("Dimension", "");
        if (!dimension.startsWith("intothevortex:")) return;
        ServerPlayer player = (ServerPlayer) (Object) this;
        java.util.UUID id = TardisDimensionManager.id(TardisDimensionManager.parseDimension(dimension));
        if (id == null) return;
        PENDING_DIMENSIONS.put(player.getUUID(), id);
    }

    public static java.util.UUID intothevortex$consumePendingDimension(java.util.UUID playerId) {
        return PENDING_DIMENSIONS.remove(playerId);
    }
}
