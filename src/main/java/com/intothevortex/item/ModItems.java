package com.intothevortex.item;

import com.intothevortex.IntoTheVortex;
import com.intothevortex.block.ModBlocks;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

public final class ModItems {

    public static final Item TARDIS = register(
            "tardis",
            TardisItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static final Item TARDIS_KEY = register(
            "tardis_key",
            TardisKeyItem::new,
            new Item.Properties()
    );

    // Blocks
    public static final Item GRATE_BLOCK = registerBlock(
            "grate_block",
            ModBlocks.GRATE_BLOCK
    );

    public static final Item RUSTY_GRATE_BLOCK = registerBlock(
            "rusty_grate_block",
            ModBlocks.RUSTY_GRATE_BLOCK
    );

    public static final Item THICK_GRATE_BLOCK = registerBlock(
            "thick_grate_block",
            ModBlocks.THICK_GRATE_BLOCK
    );

    public static final Item RUSTY_THICK_GRATE_BLOCK = registerBlock(
            "rusty_thick_grate_block",
            ModBlocks.RUSTY_THICK_GRATE_BLOCK
    );

    public static final Item HARTNELL_ROUNDEL = registerBlock(
            "hartnell_roundel",
            ModBlocks.HARTNELL_ROUNDEL
    );

    public static final Item HARTNELL_WALL = registerBlock(
            "hartnell_wall",
            ModBlocks.HARTNELL_WALL
    );

    public static final Item ECAT = registerBlock(
            "ecat",
            ModBlocks.ECAT
    );

    public static final Item GOOD_HEAVENS = registerBlock(
            "good_heavens",
            ModBlocks.GOOD_HEAVENS
    );

    public static final ResourceKey<CreativeModeTab> TAB_KEY =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    Identifier.fromNamespaceAndPath(
                            IntoTheVortex.MOD_ID,
                            "main"
                    )
            );

    public static final CreativeModeTab TAB =
            Registry.register(
                    BuiltInRegistries.CREATIVE_MODE_TAB,
                    TAB_KEY,
                    CreativeModeTab.builder(
                                    CreativeModeTab.Row.TOP,
                                    0
                            )
                            .title(Component.translatable(
                                    "itemGroup.intothevortex.main"
                            ))
                            .icon(() -> new ItemStack(TARDIS))
                            .displayItems((parameters, output) -> {

                                output.accept(TARDIS);
                                output.accept(TARDIS_KEY);

                                // Grates
                                output.accept(GRATE_BLOCK);
                                output.accept(RUSTY_GRATE_BLOCK);
                                output.accept(THICK_GRATE_BLOCK);
                                output.accept(RUSTY_THICK_GRATE_BLOCK);

                                // Hartnell
                                output.accept(HARTNELL_ROUNDEL);
                                output.accept(HARTNELL_WALL);

                                // Meme blocks
                                output.accept(ECAT);
                                output.accept(GOOD_HEAVENS);

                                // Existing interior items
                                output.accept(
                                        com.intothevortex.interior.InteriorRegistry.DOOR_ITEM
                                );
                                output.accept(
                                        com.intothevortex.interior.InteriorRegistry.CONSOLE_ITEM
                                );
                                output.accept(
                                        com.intothevortex.interior.InteriorRegistry.WALL_MONITOR_ITEM
                                );
                            })
                            .build()
            );

    private ModItems() {
    }

    private static <T extends Item> T register(
            String name,
            Function<Item.Properties, T> factory,
            Item.Properties properties
    ) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(
                        IntoTheVortex.MOD_ID,
                        name
                )
        );

        return Registry.register(
                BuiltInRegistries.ITEM,
                key,
                factory.apply(properties.setId(key))
        );
    }

    private static Item registerBlock(
            String name,
            net.minecraft.world.level.block.Block block
    ) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(
                        IntoTheVortex.MOD_ID,
                        name
                )
        );

        return Registry.register(
                BuiltInRegistries.ITEM,
                key,
                new BlockItem(
                        block,
                        new Item.Properties().setId(key)
                )
        );
    }

    public static void initialize() {
    }
}
