package com.intothevortex.client;

import com.intothevortex.client.render.TardisExteriorRenderer;
import com.intothevortex.client.render.InteriorDoorRenderer;
import com.intothevortex.client.render.ConsoleBlockEntityRenderer;
import com.intothevortex.client.render.ControlHitboxRenderer;
import com.intothevortex.client.render.TardisModelRegistry;
import com.intothevortex.entity.ModEntityTypes;
import com.intothevortex.interior.InteriorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import com.intothevortex.network.RuntimeDimensionPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.intothevortex.network.TardisFlightPayload;
import com.intothevortex.network.ControlValueSyncPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import org.lwjgl.glfw.GLFW;
import com.intothevortex.network.RwfExitPayload;
import com.intothevortex.network.RwfStatePayload;

public final class IntoTheVortexClient implements ClientModInitializer {
    private static final KeyMapping RWF_EXIT = new KeyMapping("key.intothevortex.rwf_exit", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_TAB, ControlInputManager.CATEGORY);
    private static boolean rwfActive;
    private static net.minecraft.client.CameraType previousCamera;
    private static boolean pilotVisibilityCaptured;
    private static boolean pilotWasInvisible;

    public static boolean isRwfActive() {
        return rwfActive;
    }

    @Override
    public void onInitializeClient() {
        TardisModelRegistry.initialize();
        ControlInputManager.initialize();
        KeyMappingHelper.registerKeyMapping(RWF_EXIT);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ControlInputManager.tick();
            if (RWF_EXIT.consumeClick()) ClientPlayNetworking.send(new RwfExitPayload());
            if (rwfActive && client.player != null) {
                if (!pilotVisibilityCaptured) {
                    pilotWasInvisible = client.player.isInvisible();
                    pilotVisibilityCaptured = true;
                }
                client.player.setInvisible(true);
            } else if (!rwfActive && pilotVisibilityCaptured && client.player != null) {
                client.player.setInvisible(pilotWasInvisible);
                pilotVisibilityCaptured = false;
            }
            if (rwfActive && client.options.getCameraType() != net.minecraft.client.CameraType.THIRD_PERSON_BACK) client.options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_BACK);
        });
        EntityRenderers.register(ModEntityTypes.TARDIS_EXTERIOR, TardisExteriorRenderer::new);
        EntityRenderers.register(ModEntityTypes.CONTROL_HITBOX, ControlHitboxRenderer::new);
        BlockEntityRendererRegistry.register(InteriorRegistry.DOOR_ENTITY, context -> new InteriorDoorRenderer());
        BlockEntityRendererRegistry.register(InteriorRegistry.CONSOLE_ENTITY, context -> new ConsoleBlockEntityRenderer());
        ClientPlayNetworking.registerGlobalReceiver(RuntimeDimensionPayload.TYPE, (payload, context) -> context.client().execute(() -> {
            if (context.client().getConnection() != null) {
                if (payload.dimensionType() != null) ClientRegistryAccess.register(context.client().getConnection(), payload.id(), payload.dimensionType());
                else ClientRegistryAccess.unregister(context.client().getConnection(), payload.id());
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(TardisFlightPayload.TYPE, (payload, context) -> context.client().execute(() -> ClientTardisFlightState.accept(payload)));
        ClientPlayNetworking.registerGlobalReceiver(RwfStatePayload.TYPE, (payload, context) -> context.client().execute(() -> {
            if (payload.active()) {
                if (!rwfActive) previousCamera = context.client().options.getCameraType();
                rwfActive = true;
                context.client().options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_BACK);
            } else {
                rwfActive = false;
                if (context.client().player != null && pilotVisibilityCaptured) context.client().player.setInvisible(pilotWasInvisible);
                pilotVisibilityCaptured = false;
                if (previousCamera != null) context.client().options.setCameraType(previousCamera);
                previousCamera = null;
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(ControlValueSyncPayload.TYPE, (payload, context) -> context.client().execute(() -> {
            if (context.client().level != null && context.client().level.getBlockEntity(payload.consolePos()) instanceof com.intothevortex.interior.ConsoleBlockEntity console) console.applySyncedControlValue(payload.controlId(), payload.value());
        }));
    }
}
