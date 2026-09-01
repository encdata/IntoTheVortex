package com.intothevortex.item;

import java.util.UUID;
import net.minecraft.world.item.Item;

public abstract class LinkableItem extends Item {
    private final String linkKey;
    protected LinkableItem(Properties properties) { this(properties, "tardis"); }
    protected LinkableItem(Properties properties, String linkKey) { super(properties); this.linkKey = linkKey; }
    public final String linkKey() { return linkKey; }
    public final void link(net.minecraft.world.item.ItemStack stack, UUID id) { TardisLinking.link(stack, linkKey, id); }
    public final void unlink(net.minecraft.world.item.ItemStack stack) { TardisLinking.unlink(stack, linkKey); }
    public final boolean isLinked(net.minecraft.world.item.ItemStack stack) { return TardisLinking.isLinked(stack, linkKey); }
    public final UUID getTardisId(net.minecraft.world.item.ItemStack stack) { return TardisLinking.get(stack, linkKey); }
}
