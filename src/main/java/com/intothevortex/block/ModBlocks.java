package com.intothevortex.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public static final Block HARTNELL_ROUNDEL = register(
    ModBlockIds.HARTNELL_ROUNDEL,
    RoundelBlock::new,
    BlockBehaviour.Properties.of()
    );

    public static final Block HARTNELL_WALL = register(
    ModBlockIds.HARTNELL_WALL,
    RoundelBlock::new,
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

    public static final Map<String, Block> ROUNDELS = createRoundels();

    private static Map<String, Block> createRoundels() {
        Map<String, Block> roundels = new LinkedHashMap<>();
        roundels.put("hartnell_roundel", HARTNELL_ROUNDEL);
        roundels.put("hartnell_wall", HARTNELL_WALL);
        String[] names = {
            "moss_roundel", "dirt_roundel", "end_stone_bricks_roundel", "polished_andesite_roundel",
            "polished_deepslate_roundel", "polished_diorite_roundel", "polished_granite_roundel", "quartz_roundel",
            "sandstone_roundel", "stripped_acacia_log_roundel", "stripped_birch_log_roundel", "stripped_cherry_log_roundel",
            "stripped_dark_oak_log_roundel", "stripped_jungle_log_roundel", "stripped_mangrove_log_roundel", "stripped_oak_log_roundel",
            "stripped_spruce_log_roundel", "copper_roundel", "exposed_copper_roundel", "oxidized_copper_roundel",
            "weathered_copper_roundel", "orange_concrete_roundel", "pink_concrete_roundel", "purple_concrete_roundel",
            "red_concrete_roundel", "yellow_concrete_roundel", "white_concrete_roundel", "black_concrete_roundel",
            "cyan_concrete_roundel", "light_blue_concrete_roundel", "lime_concrete_roundel", "magenta_concrete_roundel",
            "blue_concrete_roundel", "brown_concrete_roundel", "gray_concrete_roundel", "green_concrete_roundel",
            "light_gray_concrete_roundel"
        };
        for (String name : names) {
            roundels.put(name, registerRoundel(name));
        }
        return java.util.Collections.unmodifiableMap(roundels);
    }

    private static Block registerRoundel(String name) {
        ResourceKey<Block> key = ResourceKey.create(
        net.minecraft.core.registries.Registries.BLOCK,
        net.minecraft.resources.Identifier.fromNamespaceAndPath(com.intothevortex.IntoTheVortex.MOD_ID, name)
        );
        return register(key, RoundelBlock::new, BlockBehaviour.Properties.of().strength(1.5F, 6.0F));
    }

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
