package com.intothevortex.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ControlActivatePayload(BlockPos consolePos, String controlId) implements CustomPacketPayload {
    public static final Type<ControlActivatePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "control_activate"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ControlActivatePayload> CODEC = StreamCodec.of(
            (buf, payload) -> { buf.writeBlockPos(payload.consolePos()); buf.writeUtf(payload.controlId()); },
            buf -> new ControlActivatePayload(buf.readBlockPos(), buf.readUtf())
    );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
