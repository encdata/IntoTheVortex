package com.intothevortex.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ControlValuePayload(BlockPos consolePos, String controlId, float value, boolean released) implements CustomPacketPayload {
    public static final Type<ControlValuePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "control_value"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ControlValuePayload> CODEC = StreamCodec.of(
            (buf, payload) -> { buf.writeBlockPos(payload.consolePos()); buf.writeUtf(payload.controlId()); buf.writeFloat(payload.value()); buf.writeBoolean(payload.released()); },
            buf -> new ControlValuePayload(buf.readBlockPos(), buf.readUtf(), buf.readFloat(), buf.readBoolean()));

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
