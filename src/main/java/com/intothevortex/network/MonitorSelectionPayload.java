package com.intothevortex.network;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MonitorSelectionPayload(UUID tardisId, BlockPos monitorPos, String target, String value) implements CustomPacketPayload {
    public static final Type<MonitorSelectionPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "monitor_selection"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MonitorSelectionPayload> CODEC = StreamCodec.of(
        (buf, payload) -> {
            buf.writeUUID(payload.tardisId());
            buf.writeBlockPos(payload.monitorPos());
            buf.writeUtf(payload.target(), 16);
            buf.writeUtf(payload.value(), 128);
        },
        buf -> new MonitorSelectionPayload(buf.readUUID(), buf.readBlockPos(), buf.readUtf(16), buf.readUtf(128))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
