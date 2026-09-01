package com.intothevortex.item;

import java.util.UUID;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class TardisLinking {
    private TardisLinking() {}
    public static void link(ItemStack stack, UUID id) {
        link(stack, "tardis", id);
    }
    public static void link(ItemStack stack, String key, UUID id) {
        CompoundTag tag = new CompoundTag();
        tag.putString(key, id.toString());
        CustomData.set(DataComponents.CUSTOM_DATA, stack, tag);
    }
    public static UUID get(ItemStack stack) {
        return get(stack, "tardis");
    }
    public static UUID get(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        try { return UUID.fromString(data.copyTag().getString(key).orElseThrow()); } catch (IllegalArgumentException | java.util.NoSuchElementException exception) { return null; }
    }
    public static void unlink(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;
        CompoundTag tag = data.copyTag();
        tag.remove(key);
        CustomData.set(DataComponents.CUSTOM_DATA, stack, tag);
    }
    public static boolean isLinked(ItemStack stack, String key) {
        return get(stack, key) != null;
    }
    public static com.intothevortex.tardis.TardisData resolve(net.minecraft.server.MinecraftServer server, ItemStack stack, String key) {
        UUID id = get(stack, key);
        return id == null ? null : com.intothevortex.tardis.TardisManager.get(server, id);
    }
}
