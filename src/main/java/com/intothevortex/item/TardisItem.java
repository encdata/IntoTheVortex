package com.intothevortex.item;

import com.intothevortex.tardis.TardisManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public final class TardisItem extends Item {
    public TardisItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getPlayer() instanceof ServerPlayer player) || !(context.getLevel() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        if (!player.getAbilities().instabuild) {
            return InteractionResult.FAIL;
        }
        BlockPos position = context.getClickedPos().relative(context.getClickedFace());
        TardisManager.create(player, level, position);
        return InteractionResult.SUCCESS;
    }
}
