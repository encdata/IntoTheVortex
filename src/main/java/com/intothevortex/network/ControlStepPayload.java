package com.intothevortex.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ControlStepPayload(BlockPos consolePos, String controlId, float direction) implements CustomPacketPayload {
    public static final Type<ControlStepPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "control_step"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ControlStepPayload> CODEC = StreamCodec.of(
            (buf, payload) -> { buf.writeBlockPos(payload.consolePos()); buf.writeUtf(payload.controlId()); buf.writeFloat(payload.direction()); },
            buf -> new ControlStepPayload(buf.readBlockPos(), buf.readUtf(), buf.readFloat()));

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
