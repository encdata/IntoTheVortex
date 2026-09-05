package com.intothevortex;

import com.intothevortex.command.IntoTheVortexCommands;
import com.intothevortex.network.RuntimeDimensionPayload;
import com.intothevortex.network.ControlValuePayload;
import com.intothevortex.network.ControlActivatePayload;
import com.intothevortex.network.ControlStepPayload;
import com.intothevortex.network.ControlValueRequestPayload;
import com.intothevortex.network.ControlValueSyncPayload;
import com.intothevortex.network.TardisFlightPayload;
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
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import com.intothevortex.block.ModBlocks;
import com.intothevortex.sound.ModSounds;
import com.intothevortex.sound.ControlSoundManager;
import com.intothevortex.exterior.TardisAnimationManager;
import com.intothevortex.tardis.TardisTravelManager;
import com.intothevortex.tardis.TardisFuelManager;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.tardis.TardisFlightEventManager;
import com.intothevortex.tardis.RwfFlightManager;
import com.intothevortex.network.RwfExitPayload;
import com.intothevortex.network.RwfStatePayload;
import com.intothevortex.interior.InteriorDoorBlock;
import com.intothevortex.interior.ConsoleRegistry;
import com.intothevortex.interior.ControlRegistry;

public final class IntoTheVortex implements ModInitializer {
    public static final String MOD_ID = "intothevortex";

    @Override
    public void onInitialize() {
        ModSounds.initialize();
        ControlSoundManager.initialize();
        ExteriorRegistry.initialize();
        TardisAnimationManager.initializeTravel();
        ModEntityTypes.initialize();
        InteriorRegistry.initialize();
        ControlRegistry.initialize();
        TardisFlightEventManager.initialize();
        ConsoleRegistry.initialize();
        IntoTheVortexCommands.initialize();
        ModBlocks.initialize();

        ModItems.initialize();

        ServerChunkEvents.CHUNK_LOAD.register((level, chunk, generated) -> TardisManager.reconcileLoadedChunk(level, chunk));

        PayloadTypeRegistry.clientboundPlay().register(RuntimeDimensionPayload.TYPE, RuntimeDimensionPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ControlValuePayload.TYPE, ControlValuePayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ControlActivatePayload.TYPE, ControlActivatePayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ControlStepPayload.TYPE, ControlStepPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ControlValueRequestPayload.TYPE, ControlValueRequestPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ControlValueSyncPayload.TYPE, ControlValueSyncPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(TardisFlightPayload.TYPE, TardisFlightPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(RwfExitPayload.TYPE, RwfExitPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(RwfStatePayload.TYPE, RwfStatePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ControlValuePayload.TYPE, (payload, context) -> context.server().execute(() -> {
            if (!Float.isFinite(payload.value()) || payload.controlId().length() > 64) return;
            if (context.player().distanceToSqr(payload.consolePos().getCenter()) > 36.0D) return;
            if (context.player().level().getBlockEntity(payload.consolePos()) instanceof com.intothevortex.interior.ConsoleBlockEntity console) console.setControlValue(context.player(), payload.controlId(), payload.value(), payload.released());
        }));
        ServerPlayNetworking.registerGlobalReceiver(ControlActivatePayload.TYPE, (payload, context) -> context.server().execute(() -> {
            if (payload.controlId().length() > 64) return;
            if (context.player().distanceToSqr(payload.consolePos().getCenter()) > 36.0D) return;
            if (context.player().level().getBlockEntity(payload.consolePos()) instanceof com.intothevortex.interior.ConsoleBlockEntity console) console.beginControlInput(context.player(), payload.controlId());
        }));
        ServerPlayNetworking.registerGlobalReceiver(ControlStepPayload.TYPE, (payload, context) -> context.server().execute(() -> {
            if ((payload.direction() != -1.0F && payload.direction() != 1.0F) || payload.controlId().length() > 64) return;
            if (context.player().distanceToSqr(payload.consolePos().getCenter()) > 36.0D) return;
            if (context.player().level().getBlockEntity(payload.consolePos()) instanceof com.intothevortex.interior.ConsoleBlockEntity console) console.stepControl(context.player(), payload.controlId(), payload.direction());
        }));
        ServerPlayNetworking.registerGlobalReceiver(ControlValueRequestPayload.TYPE, (payload, context) -> context.server().execute(() -> {
            if (payload.controlId().length() > 64 || context.player().distanceToSqr(payload.consolePos().getCenter()) > 36.0D) return;
            if (context.player().level().getBlockEntity(payload.consolePos()) instanceof com.intothevortex.interior.ConsoleBlockEntity console) {
                com.intothevortex.interior.ControlUseContext control = com.intothevortex.interior.ControlUseContext.resolve(context.player(), console, payload.controlId());
                if (control != null && control.validate() == com.intothevortex.interior.InteractionResult.SUCCESS) {
                    if (payload.controlId().equals("throttle") && control.tardis() != null) com.intothevortex.tardis.RwfFlightManager.start(context.player(), control.tardis().id());
                    ServerPlayNetworking.send(context.player(), new ControlValueSyncPayload(payload.consolePos(), payload.controlId(), console.controlValue(payload.controlId())));
                }
            }
        }));
        ServerPlayNetworking.registerGlobalReceiver(RwfExitPayload.TYPE, (payload, context) -> context.server().execute(() -> RwfFlightManager.exit(context.player())));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            RwfFlightManager.onPlayerJoin(handler.getPlayer());
            server.execute(() -> server.getAllLevels().forEach(level -> {
                if (TardisDimensionManager.id(level.dimension()) != null) RuntimeDimensionSync.sendCreate(server, handler.getPlayer(), level.dimension());
            }));
            server.execute(() -> TardisManager.ids(server).forEach(id -> {
                var data = TardisManager.get(server, id);
                if (data != null) com.intothevortex.network.TardisFlightSync.sendIfChanged(server, data);
            }));
            java.util.UUID tardisId = RuntimeDimensionRestoration.consume(handler.getPlayer().getUUID());
            if (tardisId == null) return;
            server.execute(() -> TardisDimensionManager.whenInteriorReady(server, tardisId, level -> {
                var player = handler.getPlayer();
                if (player.connection != null && player.level() == server.overworld()) player.teleportTo(level, player.getX(), player.getY(), player.getZ(), java.util.Set.of(), player.getYRot(), player.getXRot(), false);
            }));
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            TardisManager.tickReconciliations(server);
            if (server.getTickCount() % 20 == 0) TardisManager.tickLoadedExteriorRecovery(server);
            TardisDimensionManager.tick(server);
            InteriorDoorBlock.tickExits(server);
            TardisFuelManager.tick(server);
            TardisTravelManager.tick(server);
            RwfFlightManager.tick(server);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> server.execute(() -> RwfFlightManager.onPlayerDisconnect(server, handler.getPlayer().getUUID())));
    }
}
