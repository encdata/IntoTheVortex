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

public final class InteriorPropBlock extends Block {
    private final boolean console;
    public InteriorPropBlock(BlockBehaviour.Properties properties) { this(properties, false); }
    public InteriorPropBlock(BlockBehaviour.Properties properties, boolean console) { super(properties.strength(2.0F, 6.0F)); this.console = console; }
    @Override protected InteractionResult useWithoutItem(net.minecraft.world.level.block.state.BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!console || level.isClientSide()) return InteractionResult.SUCCESS;
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
