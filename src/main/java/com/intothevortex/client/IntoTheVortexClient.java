package com.intothevortex.client;

import com.intothevortex.client.render.TardisExteriorRenderer;
import com.intothevortex.entity.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import com.intothevortex.network.RuntimeDimensionPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class IntoTheVortexClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntityTypes.TARDIS_EXTERIOR, TardisExteriorRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(RuntimeDimensionPayload.TYPE, (payload, context) -> context.client().execute(() -> {
            if (context.client().getConnection() != null) {
                context.client().getConnection().levels().add(ResourceKey.create(Registries.DIMENSION, payload.id()));
            }
        }));
    }
}
