package com.intothevortex;

import com.intothevortex.command.IntoTheVortexCommands;
import com.intothevortex.network.RuntimeDimensionPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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

        ServerTickEvents.END_SERVER_TICK.register(TardisDimensionManager::tick);
    }
}

