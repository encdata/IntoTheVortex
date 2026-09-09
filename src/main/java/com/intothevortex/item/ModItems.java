package com.intothevortex.item;

import com.intothevortex.IntoTheVortex;
import com.intothevortex.block.ModBlocks;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
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

    public static final Item ROUNDEL_MOLD = register(
    "roundel_mold",
    Item::new,
    new Item.Properties()
    );

    public static final java.util.Map<String, Item> ROUNDEL_ITEMS = createRoundelItems();
   // public static final java.util.Map<String, Item> PREHISTORIC_ITEMS = createPrehistoricItems();

    private static java.util.Map<String, Item> createRoundelItems() {
        java.util.Map<String, Item> items = new java.util.LinkedHashMap<>();
        items.put("hartnell_roundel", HARTNELL_ROUNDEL);
        items.put("hartnell_wall", HARTNELL_WALL);
        for (var entry : ModBlocks.ROUNDELS.entrySet()) {
            if (!items.containsKey(entry.getKey())) items.put(entry.getKey(), registerBlock(entry.getKey(), entry.getValue()));
        }
        return java.util.Collections.unmodifiableMap(items);
    }

   // private static java.util.Map<String, Item> createPrehistoricItems() {
  //      java.util.Map<String, Item> items = new java.util.LinkedHashMap<>();
  //      items.put("prehistoric_log", PREHISTORIC_LOG);
   //     items.put("prehistoric_leaves", PREHISTORIC_LEAVES);
   //     for (var entry : ModBlocks.ROUNDELS.entrySet()) {
    //        if (!items.containsKey(entry.getKey())) items.put(entry.getKey(), registerBlock(entry.getKey(), entry.getValue()));
    //    }
   //     return java.util.Collections.unmodifiableMap(items);
  // }

    public static final Item ECAT = registerBlock(
    "ecat",
    ModBlocks.ECAT
    );

    public static final Item GOOD_HEAVENS = registerBlock(
    "good_heavens",
    ModBlocks.GOOD_HEAVENS
    );

    public static final Item PREHISTORIC_LOG = registerBlock(
            "prehistoric_log",
            ModBlocks.PREHISTORIC_LOG
    );

    public static final Item PREHISTORIC_LEAVES = registerBlock(
            "prehistoric_leaves",
            ModBlocks.PREHISTORIC_LEAVES
    );

    public static final Item PREHISTORIC_PLANKS = registerBlock(
            "prehistoric_planks",
            ModBlocks.PREHISTORIC_PLANKS
    );

    public static final Item PREHISTORIC_STAIRS = registerBlock(
            "prehistoric_stairs",
            ModBlocks.PREHISTORIC_STAIRS
    );

    public static final Item PREHISTORIC_SLAB = registerBlock(
            "prehistoric_slab",
            ModBlocks.PREHISTORIC_SLAB
    );

    public static final Item PREHISTORIC_BLOCK = registerBlock(
            "prehistoric_block",
            ModBlocks.PREHISTORIC_BLOCK
    );

    public static final Item PREHISTORIC_ORE = registerBlock(
            "prehistoric_ore",
            ModBlocks.PREHISTORIC_ORE
    );

    public static final Item DEEPSLATE_PREHISTORIC_ORE = registerBlock(
            "deepslate_prehistoric_ore",
            ModBlocks.DEEPSLATE_PREHISTORIC_ORE
    );

    public static final ResourceKey<CreativeModeTab> TAB_KEY =
    ResourceKey.create(
    Registries.CREATIVE_MODE_TAB,
    Identifier.fromNamespaceAndPath(
    IntoTheVortex.MOD_ID,
    "main"
    )
    );

    public static final ResourceKey<CreativeModeTab> ROUNDELS_TAB_KEY = ResourceKey.create(
    Registries.CREATIVE_MODE_TAB,
    Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "roundels")
    );

    public static final ResourceKey<CreativeModeTab> DECORATIVE_TAB_KEY = ResourceKey.create(
    Registries.CREATIVE_MODE_TAB,
    Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "decorative")
    );

    public static final ResourceKey<CreativeModeTab> PREHISTORIC_TAB_KEY = ResourceKey.create(
    Registries.CREATIVE_MODE_TAB,
    Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "prehistoric")
    );

    public static final CreativeModeTab TAB =
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, FabricCreativeModeTab.builder()
    .icon(() -> new ItemStack(TARDIS))
    .title(Component.translatable(
    "itemGroup.intothevortex.main"
    ))
    .displayItems((parameters, output) -> {

        // TARDIS
        output.accept(TARDIS);
        output.accept(TARDIS_KEY);

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
    .build());

    public static final CreativeModeTab ROUNDELS_TAB =
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ROUNDELS_TAB_KEY, FabricCreativeModeTab.builder()
    .icon(() -> new ItemStack(HARTNELL_ROUNDEL))
    .title(Component.translatable(
    "itemGroup.intothevortex.roundels"
    ))
    .displayItems((parameters, output) -> {
        output.accept(ROUNDEL_MOLD);
        ROUNDEL_ITEMS.values().forEach(output::accept);
    })
    .build());

    public static final CreativeModeTab DECORATIVE_TAB =
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, DECORATIVE_TAB_KEY, FabricCreativeModeTab.builder()
    .icon(() -> new ItemStack(GRATE_BLOCK))
    .title(Component.translatable(
    "itemGroup.intothevortex.decorative"
    ))
    .displayItems((parameters, output) -> {

        // Grates
        output.accept(GRATE_BLOCK);
        output.accept(RUSTY_GRATE_BLOCK);
        output.accept(THICK_GRATE_BLOCK);
        output.accept(RUSTY_THICK_GRATE_BLOCK);
    })
    .build());

    public static final CreativeModeTab PREHISTORIC_TAB =
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PREHISTORIC_TAB_KEY, FabricCreativeModeTab.builder()
    .icon(() -> new ItemStack(PREHISTORIC_PLANKS))
    .title(Component.translatable("itemGroup.intothevortex.prehistoric"))
    .displayItems((parameters, output) -> {
        output.accept(PREHISTORIC_LOG);
        output.accept(PREHISTORIC_LEAVES);
        output.accept(PREHISTORIC_PLANKS);
        output.accept(PREHISTORIC_STAIRS);
        output.accept(PREHISTORIC_SLAB);
        output.accept(PREHISTORIC_BLOCK);
        output.accept(PREHISTORIC_ORE);
        output.accept(DEEPSLATE_PREHISTORIC_ORE);
    })
    .build());

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
