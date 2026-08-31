package com.intothevortex;

import com.intothevortex.command.IntoTheVortexCommands;
import com.intothevortex.network.RuntimeDimensionPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.intothevortex.mixin.ServerPlayerMixin;
import com.intothevortex.dimension.TardisDimensionManager;
import net.minecraft.world.level.portal.TeleportTransition;
import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.entity.ModEntityTypes;
import com.intothevortex.exterior.ExteriorRegistry;
import com.intothevortex.interior.InteriorRegistry;
import com.intothevortex.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public final class IntoTheVortex implements ModInitializer {
    public static final String MOD_ID = "intothevortex";

    @Override
    public void onInitialize() {
        ExteriorRegistry.initialize();
        ModEntityTypes.initialize();
        InteriorRegistry.initialize();
        IntoTheVortexCommands.initialize();

        ModItems.initialize();
        PayloadTypeRegistry.clientboundPlay().register(RuntimeDimensionPayload.TYPE, RuntimeDimensionPayload.CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            java.util.UUID tardisId = ServerPlayerMixin.intothevortex$consumePendingDimension(handler.getPlayer().getUUID());
            if (tardisId == null) return;
            server.execute(() -> {
                var level = TardisDimensionManager.ensureLoaded(server, tardisId);
                if (level != null && handler.getPlayer().level() == server.overworld()) {
                    handler.getPlayer().teleport(new TeleportTransition(level, handler.getPlayer().position(), handler.getPlayer().getDeltaMovement(), handler.getPlayer().getYRot(), handler.getPlayer().getXRot(), TeleportTransition.DO_NOTHING));
                }
            });
        });

        ServerTickEvents.END_SERVER_TICK.register(TardisDimensionManager::tick);
    }
}

