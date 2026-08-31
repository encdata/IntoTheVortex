package com.intothevortex.interior;

import com.intothevortex.IntoTheVortex;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public final class InteriorRegistry {
    private static final ResourceKey<Block> DOOR_KEY = ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "interior_door"));
    public static final Block DOOR = Registry.register(BuiltInRegistries.BLOCK, DOOR_KEY, new InteriorDoorBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().setId(DOOR_KEY)));
    private InteriorRegistry() {}
    public static void initialize() {}
}
