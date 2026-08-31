package com.intothevortex.tardis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.core.BlockPos;

public record TardisData(UUID id, UUID ownerId, UUID exteriorId, String exterior, String dimension, BlockPos position, float yaw, boolean locked, boolean doorOpen, boolean interiorInitialized) {
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);

    public static final Codec<TardisData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUID_CODEC.fieldOf("id").forGetter(TardisData::id),
            UUID_CODEC.fieldOf("owner").forGetter(TardisData::ownerId),
            UUID_CODEC.optionalFieldOf("exterior", new UUID(0L, 0L)).forGetter(TardisData::exteriorId),
            Codec.STRING.optionalFieldOf("exterior_type", "intothevortex:default").forGetter(TardisData::exterior),
            Codec.STRING.fieldOf("dimension").forGetter(TardisData::dimension),
            BlockPos.CODEC.fieldOf("position").forGetter(TardisData::position),
            Codec.FLOAT.optionalFieldOf("yaw", 0.0F).forGetter(TardisData::yaw),
            Codec.BOOL.fieldOf("locked").forGetter(TardisData::locked),
            Codec.BOOL.fieldOf("door_open").forGetter(TardisData::doorOpen),
            Codec.BOOL.optionalFieldOf("interior_initialized", false).forGetter(TardisData::interiorInitialized)
    ).apply(instance, TardisData::new));

    public TardisData withExterior(UUID exteriorId) {
        return new TardisData(id, ownerId, exteriorId, exterior, dimension, position, yaw, locked, doorOpen, interiorInitialized);
    }

    public TardisData withDoorOpen(boolean doorOpen) {
        return new TardisData(id, ownerId, exteriorId, exterior, dimension, position, yaw, locked, doorOpen, interiorInitialized);
    }

    public TardisData withLocked(boolean locked) {
        return new TardisData(id, ownerId, exteriorId, exterior, dimension, position, yaw, locked, doorOpen, interiorInitialized);
    }

    public TardisData withInteriorInitialized(boolean value) {
        return new TardisData(id, ownerId, exteriorId, exterior, dimension, position, yaw, locked, doorOpen, value);
    }
}
