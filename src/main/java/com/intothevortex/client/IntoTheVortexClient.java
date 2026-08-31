package com.intothevortex.client;

import com.intothevortex.client.render.TardisExteriorRenderer;
import com.intothevortex.entity.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public final class IntoTheVortexClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntityTypes.TARDIS_EXTERIOR, TardisExteriorRenderer::new);
    }
}
