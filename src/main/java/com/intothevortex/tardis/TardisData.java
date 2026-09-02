package com.intothevortex.tardis;

import java.util.UUID;
import net.minecraft.core.BlockPos;

public record TardisData(UUID id, UUID ownerId, UUID exteriorId, String exterior, String interior, String dimension, BlockPos position, BlockPos interiorDoor, boolean interiorDoorStored, float yaw, boolean locked, boolean doorOpen, boolean interiorInitialized, String doorAnimation, String dematAnimation, String matAnimation, TardisTravelState travelState, int phaseTicks, int flightTicks, int targetFlightTicks, String travelSourceDimension, BlockPos travelSourcePosition, float travelSourceYaw, String travelDestinationDimension, BlockPos travelDestinationPosition, float travelDestinationYaw) {
    public TardisData withExterior(UUID exteriorId) {
        return copy(exteriorId, exterior, interior, dimension, position, interiorDoor, interiorDoorStored, yaw, locked, doorOpen, interiorInitialized, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withExteriorType(String exterior) {
        return copy(exteriorId, exterior, interior, dimension, position, interiorDoor, interiorDoorStored, yaw, locked, doorOpen, interiorInitialized, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withDoorOpen(boolean doorOpen) {
        return copy(exteriorId, exterior, interior, dimension, position, interiorDoor, interiorDoorStored, yaw, locked, doorOpen, interiorInitialized, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withLocked(boolean locked) {
        return copy(exteriorId, exterior, interior, dimension, position, interiorDoor, interiorDoorStored, yaw, locked, doorOpen, interiorInitialized, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withInteriorInitialized(boolean value) {
        return copy(exteriorId, exterior, interior, dimension, position, interiorDoor, interiorDoorStored, yaw, locked, doorOpen, value, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withInterior(String value) {
        return copy(exteriorId, exterior, value, dimension, position, new BlockPos(0, 64, 0), false, yaw, locked, doorOpen, false, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withInteriorDoor(BlockPos value) {
        return copy(exteriorId, exterior, interior, dimension, position, value, true, yaw, locked, doorOpen, interiorInitialized, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withTravel(TardisTravelState state, int phase, int flight, int target, TardisTravelDestination source, TardisTravelDestination destination) {
        return copy(exteriorId, exterior, interior, dimension, position, interiorDoor, interiorDoorStored, yaw, locked, doorOpen, interiorInitialized, doorAnimation, dematAnimation, matAnimation, state, phase, flight, target, source.dimension(), source.position(), source.yaw(), destination.dimension(), destination.position(), destination.yaw());
    }

    public TardisData withAnimations(String door, String demat, String mat) {
        return copy(exteriorId, exterior, interior, dimension, position, interiorDoor, interiorDoorStored, yaw, locked, doorOpen, interiorInitialized, door, demat, mat, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withLanded(TardisTravelDestination destination) {
        return copy(exteriorId, exterior, interior, destination.dimension(), destination.position(), interiorDoor, interiorDoorStored, destination.yaw(), locked, false, interiorInitialized, doorAnimation, dematAnimation, matAnimation, TardisTravelState.LANDED, 0, 0, 0, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    public TardisData withExteriorLocation(TardisTravelDestination destination) {
        return copy(exteriorId, exterior, interior, destination.dimension(), destination.position(), interiorDoor, interiorDoorStored, destination.yaw(), locked, doorOpen, interiorInitialized, doorAnimation, dematAnimation, matAnimation, travelState, phaseTicks, flightTicks, targetFlightTicks, travelSourceDimension, travelSourcePosition, travelSourceYaw, travelDestinationDimension, travelDestinationPosition, travelDestinationYaw);
    }

    private TardisData copy(UUID exteriorEntityId, String exteriorType, String interiorType, String exteriorDimension, BlockPos exteriorPosition, BlockPos door, boolean doorStored, float exteriorYaw, boolean isLocked, boolean isDoorOpen, boolean initialized, String doorId, String dematId, String matId, TardisTravelState state, int phase, int flight, int target, String sourceDimension, BlockPos sourcePosition, float sourceYaw, String destinationDimension, BlockPos destinationPosition, float destinationYaw) {
        return new TardisData(id, ownerId, exteriorEntityId, exteriorType, interiorType, exteriorDimension, exteriorPosition, door, doorStored, exteriorYaw, isLocked, isDoorOpen, initialized, doorId, dematId, matId, state, phase, flight, target, sourceDimension, sourcePosition, sourceYaw, destinationDimension, destinationPosition, destinationYaw);
    }
}
