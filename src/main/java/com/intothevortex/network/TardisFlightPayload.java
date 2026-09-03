package com.intothevortex.network;

import com.intothevortex.tardis.TardisTravelState;
import com.intothevortex.tardis.FlightCondition;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.UUID;

public record TardisFlightPayload(UUID id, int throttleStage, boolean handbrakeEngaged, double fuel, double maxFuel, boolean refueling, TardisTravelState travelState, int phaseTicks, int flightTicks, int targetFlightTicks, FlightCondition flightCondition, String failureType, String activeEvent, String activeEventControl, int eventRemaining, int eventElapsed, int eventStep) implements CustomPacketPayload {
    public static final Type<TardisFlightPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("intothevortex", "tardis_flight"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TardisFlightPayload> CODEC = StreamCodec.of(
            (buf, payload) -> { buf.writeUUID(payload.id()); buf.writeVarInt(payload.throttleStage()); buf.writeBoolean(payload.handbrakeEngaged()); buf.writeDouble(payload.fuel()); buf.writeDouble(payload.maxFuel()); buf.writeBoolean(payload.refueling()); buf.writeEnum(payload.travelState()); buf.writeVarInt(payload.phaseTicks()); buf.writeVarInt(payload.flightTicks()); buf.writeVarInt(payload.targetFlightTicks()); buf.writeEnum(payload.flightCondition()); buf.writeUtf(payload.failureType(), 64); buf.writeUtf(payload.activeEvent(), 128); buf.writeUtf(payload.activeEventControl(), 64); buf.writeVarInt(payload.eventRemaining()); buf.writeVarInt(payload.eventElapsed()); buf.writeVarInt(payload.eventStep()); },
            buf -> new TardisFlightPayload(buf.readUUID(), buf.readVarInt(), buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readBoolean(), buf.readEnum(TardisTravelState.class), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readEnum(FlightCondition.class), buf.readUtf(64), buf.readUtf(128), buf.readUtf(64), buf.readVarInt(), buf.readVarInt(), buf.readVarInt())
    );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
