package com.intothevortex.network;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record OpenMonitorPayload(UUID tardisId, BlockPos monitorPos) implements CustomPacketPayload {
    public static final Type<OpenMonitorPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "open_monitor"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenMonitorPayload> CODEC = StreamCodec.of(
        (buf, payload) -> { buf.writeUUID(payload.tardisId()); buf.writeBlockPos(payload.monitorPos()); },
        buf -> new OpenMonitorPayload(buf.readUUID(), buf.readBlockPos())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
