package com.intothevortex.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ControlValueSyncPayload(BlockPos consolePos, String controlId, float value) implements CustomPacketPayload {
    public static final Type<ControlValueSyncPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "control_value_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ControlValueSyncPayload> CODEC = StreamCodec.of(
    (buf, payload) -> { buf.writeBlockPos(payload.consolePos()); buf.writeUtf(payload.controlId()); buf.writeFloat(payload.value()); },
    buf -> new ControlValueSyncPayload(buf.readBlockPos(), buf.readUtf(64), buf.readFloat()));

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
