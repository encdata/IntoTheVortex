package com.intothevortex.client;

import com.intothevortex.dimension.RuntimeRegistry;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public final class ClientRegistryAccess {
    private ClientRegistryAccess() {}

    public static void register(ClientPacketListener handler, Identifier id, CompoundTag encoded) {
        DimensionType type = DimensionType.DIRECT_CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, handler.registryAccess()), encoded).getOrThrow().getFirst();
        RuntimeRegistry.register(handler.registryAccess().lookup(Registries.DIMENSION_TYPE).orElseThrow(), id, type);
        handler.levels().add(ResourceKey.create(Registries.DIMENSION, id));
    }

    public static void unregister(ClientPacketListener handler, Identifier id) {
        RuntimeRegistry.unregister(handler.registryAccess().lookup(Registries.DIMENSION_TYPE).orElseThrow(), id);
        handler.levels().remove(ResourceKey.create(Registries.DIMENSION, id));
    }
}
