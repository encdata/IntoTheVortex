package com.intothevortex.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ControlValueRequestPayload(BlockPos consolePos, String controlId) implements CustomPacketPayload {
    public static final Type<ControlValueRequestPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "control_value_request"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ControlValueRequestPayload> CODEC = StreamCodec.of(
    (buf, payload) -> { buf.writeBlockPos(payload.consolePos()); buf.writeUtf(payload.controlId()); },
    buf -> new ControlValueRequestPayload(buf.readBlockPos(), buf.readUtf(64)));

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
