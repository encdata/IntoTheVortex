package com.intothevortex.dimension;

import com.intothevortex.mixin.MappedRegistryAccessor;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public final class RuntimeRegistry {
    private RuntimeRegistry() {}

    public static <T> Holder.Reference<T> register(Registry<T> registry, Identifier id, T value) {
        if (registry.containsKey(id)) return ((MappedRegistryAccessor<T>) registry).byLocation().get(id);
        if (!(registry instanceof MappedRegistry<T> mapped)) throw new IllegalStateException("Unsupported runtime registry " + registry.key());
        MappedRegistryAccessor<T> access = (MappedRegistryAccessor<T>) mapped;
        boolean frozen = access.frozen();
        if (frozen) access.frozen(false);
        Holder.Reference<T> result = mapped.register(ResourceKey.create(registry.key(), id), value, RegistrationInfo.BUILT_IN);
        if (frozen) access.frozen(true);
        return result;
    }

    public static <T> void unregister(Registry<T> registry, Identifier id) {
        if (!registry.containsKey(id) || !(registry instanceof MappedRegistry<T> mapped)) return;
        MappedRegistryAccessor<T> access = (MappedRegistryAccessor<T>) mapped;
        Holder.Reference<T> holder = access.byLocation().get(id);
        T value = holder.value();
        int rawId = access.toId().removeInt(value);
        ObjectList<Holder.Reference<T>> byId = access.byId();
        byId.remove(rawId);
        access.toId().replaceAll((entry, index) -> index > rawId ? index - 1 : index);
        ResourceKey<T> key = ResourceKey.create(registry.key(), id);
        access.byLocation().remove(id);
        access.byKey().remove(key);
        access.byValue().remove(value);
        access.registrationInfos().remove(key);
        Lifecycle lifecycle = Lifecycle.stable();
        for (RegistrationInfo info : access.registrationInfos().values()) lifecycle = lifecycle.add(info.lifecycle());
        access.registryLifecycle(lifecycle);
        Map<T, Holder.Reference<T>> intrusive = access.unregisteredIntrusiveHolders();
        if (intrusive != null) intrusive.remove(value);
    }
}
