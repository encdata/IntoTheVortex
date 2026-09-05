package com.intothevortex.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record RwfExitPayload() implements CustomPacketPayload {
    public static final Type<RwfExitPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "rwf_exit"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RwfExitPayload> CODEC = StreamCodec.unit(new RwfExitPayload());

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
