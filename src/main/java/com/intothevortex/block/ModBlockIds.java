package com.intothevortex.block;

import com.intothevortex.IntoTheVortex;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockIds {

    private static ResourceKey<Block> create(String name) {
        return ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, name)
        );
    }

    public static final ResourceKey<Block> GRATE_BLOCK = create("grate_block");
    public static final ResourceKey<Block> RUSTY_GRATE_BLOCK = create("rusty_grate_block");
    public static final ResourceKey<Block> THICK_GRATE_BLOCK = create("thick_grate_block");
    public static final ResourceKey<Block> RUSTY_THICK_GRATE_BLOCK = create("rusty_thick_grate_block");

    public static final ResourceKey<Block> HARTNELL_ROUNDEL = create("hartnell_roundel");
    public static final ResourceKey<Block> HARTNELL_WALL = create("hartnell_wall");

    public static final ResourceKey<Block> ECAT = create("ecat");
    public static final ResourceKey<Block> GOOD_HEAVENS = create("good_heavens");

    private ModBlockIds() {
    }
}

