package com.intothevortex.mixin;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MappedRegistry.class)
public interface MappedRegistryAccessor<T> {
    @Accessor("frozenTags") Map<TagKey<T>, HolderSet.Named<T>> tags();
    @Accessor("unregisteredIntrusiveHolders") Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders();
    @Accessor("byId") ObjectList<Holder.Reference<T>> byId();
    @Accessor("toId") Reference2IntMap<T> toId();
    @Accessor("byLocation") Map<Identifier, Holder.Reference<T>> byLocation();
    @Accessor("byKey") Map<ResourceKey<T>, Holder.Reference<T>> byKey();
    @Accessor("byValue") Map<T, Holder.Reference<T>> byValue();
    @Accessor("registrationInfos") Map<ResourceKey<T>, RegistrationInfo> registrationInfos();
    @Accessor("frozen") boolean frozen();
    @Accessor("frozen") void frozen(boolean value);
    @Accessor("registryLifecycle") void registryLifecycle(Lifecycle value);
}
