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

    public static final ResourceKey<Block> PREHISTORIC_LOG = create("prehistoric_log");
    public static final ResourceKey<Block> PREHISTORIC_LEAVES = create("prehistoric_leaves");
    public static final ResourceKey<Block> PREHISTORIC_PLANKS = create("prehistoric_planks");
    public static final ResourceKey<Block> PREHISTORIC_BLOCK = create("prehistoric_block");
    public static final ResourceKey<Block> PREHISTORIC_ORE = create("prehistoric_ore");
    public static final ResourceKey<Block> DEEPSLATE_PREHISTORIC_ORE = create("deepslate_prehistoric_ore");
    public static final ResourceKey<Block> PREHISTORIC_STAIRS = create("prehistoric_stairs");
    public static final ResourceKey<Block> PREHISTORIC_SLAB = create("prehistoric_slab");

    private ModBlockIds() {
    }
}
