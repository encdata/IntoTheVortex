package com.intothevortex.interior;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.tardis.DoorEvents;
import com.intothevortex.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;

public final class InteriorPropBlock extends Block implements net.minecraft.world.level.block.EntityBlock {
    private final boolean console;
    public InteriorPropBlock(BlockBehaviour.Properties properties) { this(properties, false); }
    public InteriorPropBlock(BlockBehaviour.Properties properties, boolean console) { super(properties.strength(2.0F, 6.0F).lightLevel(state -> console ? 3 : 0)); this.console = console; }
    @Override public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) { return console ? new ConsoleBlockEntity(pos, state) : null; }
    @Override public net.minecraft.world.level.block.RenderShape getRenderShape(net.minecraft.world.level.block.state.BlockState state) { return console ? net.minecraft.world.level.block.RenderShape.INVISIBLE : super.getRenderShape(state); }
    @Override public net.minecraft.world.level.block.state.BlockState getStateForPlacement(BlockPlaceContext context) { return console ? defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()) : defaultBlockState(); }
    @Override protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, net.minecraft.world.level.block.state.BlockState> builder) { builder.add(BlockStateProperties.HORIZONTAL_FACING); }
    @Override public void onPlace(net.minecraft.world.level.block.state.BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.state.BlockState oldState, boolean movedByPiston) { super.onPlace(state, level, pos, oldState, movedByPiston); if (console && level.getBlockEntity(pos) instanceof ConsoleBlockEntity entity) entity.createHitboxes(); }
    @Override public net.minecraft.world.level.block.state.BlockState rotate(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.block.Rotation rotation) { return state; }
    @Override protected InteractionResult useWithoutItem(net.minecraft.world.level.block.state.BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    public static InteractionResult toggleDoor(Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        java.util.UUID id = TardisDimensionManager.id(level.dimension());
        TardisData data = id == null ? null : TardisManager.get(level.getServer(), id);
        if (data == null) return InteractionResult.FAIL;
        if (data.locked()) return InteractionResult.FAIL;
        boolean open = !data.doorOpen();
        TardisData updated = data.withDoorOpen(open);
        TardisManager.save(level.getServer(), updated);
        DoorEvents.fire(updated, open);
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            InteriorDoorBlock.syncState(serverLevel, id, open);
            serverLevel.playSound(null, pos, open ? ModSounds.DOOR_OPEN : ModSounds.DOOR_CLOSE, SoundSource.BLOCKS, 0.8F, 1.0F);
            var exterior = level.getServer().getLevel(TardisDimensionManager.parseDimension(updated.dimension()));
            if (exterior != null) exterior.playSound(null, updated.position(), open ? ModSounds.DOOR_OPEN : ModSounds.DOOR_CLOSE, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }
}
