package com.intothevortex;

import com.intothevortex.command.IntoTheVortexCommands;
import com.intothevortex.network.RuntimeDimensionPayload;
import com.intothevortex.network.ControlValuePayload;
import com.intothevortex.network.RuntimeDimensionSync;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.intothevortex.dimension.RuntimeDimensionRestoration;
import com.intothevortex.dimension.TardisDimensionManager;
import net.minecraft.world.level.portal.TeleportTransition;
import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.entity.ModEntityTypes;
import com.intothevortex.exterior.ExteriorRegistry;
import com.intothevortex.interior.InteriorRegistry;
import com.intothevortex.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import com.intothevortex.block.ModBlocks;
import com.intothevortex.sound.ModSounds;
import com.intothevortex.exterior.TardisAnimationManager;
import com.intothevortex.tardis.TardisTravelManager;
import com.intothevortex.interior.InteriorDoorBlock;
import com.intothevortex.interior.ConsoleRegistry;
import com.intothevortex.interior.ControlRegistry;

public final class IntoTheVortex implements ModInitializer {
    public static final String MOD_ID = "intothevortex";

    @Override
    public void onInitialize() {
        ModSounds.initialize();
        ExteriorRegistry.initialize();
        TardisAnimationManager.initializeTravel();
        ModEntityTypes.initialize();
        InteriorRegistry.initialize();
        ControlRegistry.initialize();
        ConsoleRegistry.initialize();
        IntoTheVortexCommands.initialize();
        ModBlocks.initialize();

        ModItems.initialize();
        ModBlocks.initialize();

        PayloadTypeRegistry.clientboundPlay().register(RuntimeDimensionPayload.TYPE, RuntimeDimensionPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ControlValuePayload.TYPE, ControlValuePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ControlValuePayload.TYPE, (payload, context) -> context.server().execute(() -> {
            if (context.player().level().getBlockEntity(payload.consolePos()) instanceof com.intothevortex.interior.ConsoleBlockEntity console) console.setControlValue(context.player(), payload.controlId(), payload.value(), payload.released());
        }));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            server.execute(() -> server.getAllLevels().forEach(level -> {
                if (TardisDimensionManager.id(level.dimension()) != null) RuntimeDimensionSync.sendCreate(server, handler.getPlayer(), level.dimension());
            }));
            java.util.UUID tardisId = RuntimeDimensionRestoration.consume(handler.getPlayer().getUUID());
            if (tardisId == null) return;
            server.execute(() -> TardisDimensionManager.whenInteriorReady(server, tardisId, level -> {
                var player = handler.getPlayer();
                if (player.connection != null && player.level() == server.overworld()) player.teleportTo(level, player.getX(), player.getY(), player.getZ(), java.util.Set.of(), player.getYRot(), player.getXRot(), false);
            }));
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            TardisDimensionManager.tick(server);
            InteriorDoorBlock.tickExits(server);
            TardisTravelManager.tick(server);
        });
    }
}

