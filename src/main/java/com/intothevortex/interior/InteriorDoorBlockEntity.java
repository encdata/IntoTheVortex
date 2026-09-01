package com.intothevortex.interior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public final class InteriorDoorBlockEntity extends BlockEntity {
    public InteriorDoorBlockEntity(BlockPos pos, BlockState state) {
        super(InteriorRegistry.DOOR_ENTITY, pos, state);
    }
}
