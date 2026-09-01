package com.intothevortex.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public final class ModBlocks {

    public static final Block GRATE_BLOCK = register(
            ModBlockIds.GRATE_BLOCK,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block RUSTY_GRATE_BLOCK = register(
            ModBlockIds.RUSTY_GRATE_BLOCK,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block THICK_GRATE_BLOCK = register(
            ModBlockIds.THICK_GRATE_BLOCK,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block RUSTY_THICK_GRATE_BLOCK = register(
            ModBlockIds.RUSTY_THICK_GRATE_BLOCK,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block HARTNELL_A = register(
            ModBlockIds.HARTNELL_A,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block HARTNELL_B = register(
            ModBlockIds.HARTNELL_B,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block HARTNELL_C = register(
            ModBlockIds.HARTNELL_C,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block ECAT = register(
            ModBlockIds.ECAT,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    public static final Block GOOD_HEAVENS = register(
            ModBlockIds.GOOD_HEAVENS,
            Block::new,
            BlockBehaviour.Properties.of()
    );

    private static Block register(
            ResourceKey<Block> key,
            Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties properties
    ) {
        return Registry.register(
                BuiltInRegistries.BLOCK,
                key,
                factory.apply(properties.setId(key))
        );
    }

    public static void initialize() {
    }

    private ModBlocks() {
    }
}
