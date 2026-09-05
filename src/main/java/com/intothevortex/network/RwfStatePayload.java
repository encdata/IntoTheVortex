package com.intothevortex.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record RwfStatePayload(boolean active) implements CustomPacketPayload {
    public static final Type<RwfStatePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "rwf_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RwfStatePayload> CODEC = StreamCodec.of((buf, payload) -> buf.writeBoolean(payload.active()), buf -> new RwfStatePayload(buf.readBoolean()));

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
