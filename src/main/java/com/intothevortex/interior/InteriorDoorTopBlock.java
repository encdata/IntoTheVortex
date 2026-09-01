package com.intothevortex.interior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

public final class InteriorDoorTopBlock extends Block {
    public InteriorDoorTopBlock(BlockBehaviour.Properties properties) {
        super(properties.noOcclusion().strength(-1.0F, 3600000.0F));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && level.getBlockState(pos.below()).is(InteriorRegistry.DOOR)) level.removeBlock(pos.below(), false);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteriorDoorBlock.interact(level, pos.below(), player, ItemStack.EMPTY);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteriorDoorBlock.interact(level, pos.below(), player, stack);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effects, boolean moving) {
        BlockPos lower = pos.below();
        if (level.getBlockState(lower).is(InteriorRegistry.DOOR)) InteriorDoorBlock.tryExit(level.getBlockState(lower), level, lower, entity, moving);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        BlockState lower = level.getBlockState(pos.below());
        return lower.is(InteriorRegistry.DOOR) ? InteriorDoorBlock.shape(lower.getValue(InteriorDoorBlock.FACING)) : net.minecraft.world.phys.shapes.Shapes.empty();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getCollisionShape(state, level, pos, context);
    }
}
