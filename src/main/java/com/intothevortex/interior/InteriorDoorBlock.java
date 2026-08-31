package com.intothevortex.interior;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class InteriorDoorBlock extends Block {
    public InteriorDoorBlock(BlockBehaviour.Properties properties) { super(properties.noCollision().noOcclusion().strength(-1.0F, 3600000.0F)); }

    @Override protected void entityInside(BlockState state, Level level, net.minecraft.core.BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effects, boolean moving) {
        if (!(level instanceof ServerLevel world) || !(entity instanceof ServerPlayer player)) return;
        java.util.UUID id = TardisDimensionManager.id(world.dimension());
        if (id == null) return;
        TardisData data = TardisManager.get(world.getServer(), id);
        if (data == null || data.locked() || !data.doorOpen()) return;
        ServerLevel exterior = world.getServer().getLevel(TardisDimensionManager.parseDimension(data.dimension()));
        if (exterior == null) return;
        double yaw = Math.toRadians(data.yaw());
        double x = data.position().getX() + 0.5D + Math.sin(yaw) * 1.8D;
        double z = data.position().getZ() + 0.5D - Math.cos(yaw) * 1.8D;
        player.teleport(new net.minecraft.world.level.portal.TeleportTransition(exterior, new net.minecraft.world.phys.Vec3(x, data.position().getY(), z), net.minecraft.world.phys.Vec3.ZERO, data.yaw(), 0.0F, net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING));
    }
}
