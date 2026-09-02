package com.intothevortex.network;

import com.intothevortex.dimension.TardisDimensionManager;
import com.mojang.serialization.JsonOps;
import java.util.ArrayList;
import net.minecraft.resources.RegistryOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.DimensionType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class RuntimeDimensionSync {
    private RuntimeDimensionSync() {}

    public static void sendCreate(MinecraftServer server, ServerPlayer player, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> key) {
        var level = server.getLevel(key);
        if (level == null) return;
        CompoundTag type = (CompoundTag) DimensionType.DIRECT_CODEC.encode(level.dimensionTypeRegistration().value(), RegistryOps.create(NbtOps.INSTANCE, server.registryAccess()), new CompoundTag()).getOrThrow();
        ServerPlayNetworking.send(player, new RuntimeDimensionPayload(key.identifier(), type));
    }

    public static void sendCreateToAll(MinecraftServer server, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> key) {
        for (ServerPlayer player : new ArrayList<>(server.getPlayerList().getPlayers())) sendCreate(server, player, key);
    }

    public static void sendRemoveToAll(MinecraftServer server, net.minecraft.resources.Identifier id) {
        for (ServerPlayer player : new ArrayList<>(server.getPlayerList().getPlayers())) ServerPlayNetworking.send(player, new RuntimeDimensionPayload(id, null));
    }
}
