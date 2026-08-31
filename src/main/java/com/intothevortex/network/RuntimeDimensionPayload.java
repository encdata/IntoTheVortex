package com.intothevortex.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record RuntimeDimensionPayload(Identifier id) implements CustomPacketPayload {
    public static final Type<RuntimeDimensionPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "runtime_dimension"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RuntimeDimensionPayload> CODEC = StreamCodec.of(
            (buf, payload) -> buf.writeUtf(payload.id().toString()),
            buf -> new RuntimeDimensionPayload(Identifier.parse(buf.readUtf())));
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
