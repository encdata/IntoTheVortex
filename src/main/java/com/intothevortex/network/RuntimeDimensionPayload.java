package com.intothevortex.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.nbt.CompoundTag;

public record RuntimeDimensionPayload(Identifier id, CompoundTag dimensionType) implements CustomPacketPayload {
    public static final Type<RuntimeDimensionPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "runtime_dimension"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RuntimeDimensionPayload> CODEC = StreamCodec.of(
            (buf, payload) -> { buf.writeUtf(payload.id().toString()); buf.writeBoolean(payload.dimensionType() != null); if (payload.dimensionType() != null) buf.writeNbt(payload.dimensionType()); },
            buf -> new RuntimeDimensionPayload(Identifier.parse(buf.readUtf()), buf.readBoolean() ? buf.readNbt() : null));
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
