package com.intothevortex.interior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

public final class InteriorDoorBlockEntity extends BlockEntity {
    private String exterior = "intothevortex:default";
    private String doorAnimation = "intothevortex:door_swing";
    private float doorProgress;
    private boolean powered;

    public InteriorDoorBlockEntity(BlockPos pos, BlockState state) {
        super(InteriorRegistry.DOOR_ENTITY, pos, state);
    }

    public String exterior() {
        return exterior;
    }

    public String doorAnimation() { return doorAnimation; }

    public void setExterior(String value) {
        if (exterior.equals(value)) return;
        exterior = value;
        setChanged();
        if (level != null && !level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public void setDoorAnimation(String value) {
        doorAnimation = value;
        setChanged();
    }

    public float doorProgress() {
        float target = getBlockState().getValue(InteriorDoorBlock.OPEN) ? 1.0F : 0.0F;
        doorProgress += (target - doorProgress) * 0.18F;
        return doorProgress;
    }
    public boolean powered() { return powered; }
    public void setPowered(boolean value) { powered = value; setChanged(); }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        exterior = input.getString("exterior").orElse("intothevortex:default");
        doorAnimation = input.getString("door_animation").orElse("intothevortex:door_swing");
        powered = input.getBooleanOr("powered", false);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("exterior", exterior);
        output.putString("door_animation", doorAnimation);
        output.putBoolean("powered", powered);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
