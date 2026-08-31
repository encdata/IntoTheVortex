package com.intothevortex.item;

import com.intothevortex.IntoTheVortex;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {

    public static final ResourceKey<CreativeModeTab> INTO_THE_VORTEX_KEY =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    Identifier.fromNamespaceAndPath(
                            IntoTheVortex.MOD_ID,
                            "into_the_vortex"
                    )
            );

    public static final CreativeModeTab INTO_THE_VORTEX = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            INTO_THE_VORTEX_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable(
                            "itemGroup.intothevortex.into_the_vortex"
                    ))
                    .icon(() -> new ItemStack(ModItems.TARDIS))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.TARDIS);
                        output.accept(ModItems.TARDIS_KEY);
                    })
                    .build()
    );

    private ModCreativeTabs() {
    }

    public static void initialize() {
    }
}

