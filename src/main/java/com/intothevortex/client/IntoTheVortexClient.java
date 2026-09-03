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
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class IntoTheVortexClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TardisModelRegistry.initialize();
        ControlInputManager.initialize();
        ClientTickEvents.END_CLIENT_TICK.register(client -> ControlInputManager.tick());
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
    }
}
