package com.intothevortex.item;

import java.util.UUID;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class TardisLinking {
    private TardisLinking() {}
    public static void link(ItemStack stack, UUID id) {
        CompoundTag tag = new CompoundTag();
        tag.putString("TardisId", id.toString());
        CustomData.set(DataComponents.CUSTOM_DATA, stack, tag);
    }
    public static UUID get(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        try { return UUID.fromString(data.copyTag().getString("TardisId").orElseThrow()); } catch (IllegalArgumentException | java.util.NoSuchElementException exception) { return null; }
    }
}
