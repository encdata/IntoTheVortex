package com.intothevortex.item;

import com.intothevortex.IntoTheVortex;
import java.util.function.Function;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public final class ModItems {
    public static final Item TARDIS = register("tardis", TardisItem::new, new Item.Properties().stacksTo(1));
    public static final Item TARDIS_KEY = register("tardis_key", TardisKeyItem::new, new Item.Properties());
    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "main"));
    public static final CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.intothevortex.main"))
            .icon(() -> new ItemStack(TARDIS))
            .displayItems((parameters, output) -> {
                output.accept(TARDIS);
                output.accept(TARDIS_KEY);
                output.accept(com.intothevortex.interior.InteriorRegistry.DOOR_ITEM);
                output.accept(com.intothevortex.interior.InteriorRegistry.CONSOLE_ITEM);
                output.accept(com.intothevortex.interior.InteriorRegistry.WALL_MONITOR_ITEM);
            })
            .build());

    private ModItems() {
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
    }

    public static void initialize() {
    }
}
