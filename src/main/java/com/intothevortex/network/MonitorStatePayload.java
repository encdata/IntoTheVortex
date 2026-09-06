package com.intothevortex.network;

import com.intothevortex.tardis.TardisStatusSnapshot;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MonitorStatePayload(UUID tardisId, String exterior, String interior, String dimension, BlockPos position, String destinationDimension, BlockPos destinationPosition, String travelState, String flightCondition, int progress, int throttle, boolean handbrake, double fuel, double maxFuel, boolean powered, boolean locked, boolean doorOpen, String eventId) implements CustomPacketPayload {
    public static final Type<MonitorStatePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "monitor_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MonitorStatePayload> CODEC = StreamCodec.of(
        (buf, payload) -> {
            buf.writeUUID(payload.tardisId());
            buf.writeUtf(payload.exterior(), 128);
            buf.writeUtf(payload.interior(), 128);
            buf.writeUtf(payload.dimension(), 128);
            buf.writeBlockPos(payload.position());
            buf.writeUtf(payload.destinationDimension(), 128);
            buf.writeBlockPos(payload.destinationPosition());
            buf.writeUtf(payload.travelState(), 32);
            buf.writeUtf(payload.flightCondition(), 32);
            buf.writeVarInt(payload.progress());
            buf.writeVarInt(payload.throttle());
            buf.writeBoolean(payload.handbrake());
            buf.writeDouble(payload.fuel());
            buf.writeDouble(payload.maxFuel());
            buf.writeBoolean(payload.powered());
            buf.writeBoolean(payload.locked());
            buf.writeBoolean(payload.doorOpen());
            buf.writeUtf(payload.eventId(), 128);
        },
        buf -> new MonitorStatePayload(buf.readUUID(), buf.readUtf(128), buf.readUtf(128), buf.readUtf(128), buf.readBlockPos(), buf.readUtf(128), buf.readBlockPos(), buf.readUtf(32), buf.readUtf(32), buf.readVarInt(), buf.readVarInt(), buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readUtf(128))
    );

    public static MonitorStatePayload from(TardisStatusSnapshot status) {
        return new MonitorStatePayload(UUID.fromString(status.id()), status.exterior(), status.interior(), status.dimension(), status.position(), status.destinationDimension(), status.destinationPosition(), status.travelState().name(), status.flightCondition().name(), status.progress(), status.throttleStage(), status.handbrakeEngaged(), status.fuel(), status.maxFuel(), status.powered(), status.locked(), status.doorOpen(), status.eventId());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
