package com.intothevortex.interior;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.tardis.TardisAccessRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import com.intothevortex.item.ModItems;
import com.intothevortex.item.TardisLinking;
import com.intothevortex.tardis.DoorEvents;
import com.intothevortex.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class InteriorDoorBlock extends Block implements EntityBlock {
    public static final net.minecraft.world.level.block.state.properties.BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final java.util.Map<java.util.UUID, Long> ARRIVAL_COOLDOWNS = new java.util.HashMap<>();

    public InteriorDoorBlock(BlockBehaviour.Properties properties) { super(properties.noOcclusion().strength(-1.0F, 3600000.0F)); registerDefaultState(stateDefinition.any().setValue(OPEN, false).setValue(FACING, Direction.NORTH)); }

    @Override public BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos, BlockState state) { return new InteriorDoorBlockEntity(pos, state); }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(Level level, net.minecraft.core.BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) ensureTop(level, pos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, net.minecraft.core.BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) level.removeBlock(pos.above(), false);
        return super.playerWillDestroy(level, pos, state, player);
    }


    public static void syncState(ServerLevel exteriorWorld, java.util.UUID id, boolean open) {
        ServerLevel interior = TardisDimensionManager.ensureLoaded(exteriorWorld.getServer(), id);
        net.minecraft.core.BlockPos door = interior == null ? null : TardisDimensionManager.interiorDoor(interior);
        if (door != null && interior.getBlockState(door).is(InteriorRegistry.DOOR)) {
            interior.setBlock(door, interior.getBlockState(door).setValue(OPEN, open), 3);
            syncExterior(interior, id);
        }
    }

    public static void syncExterior(ServerLevel interior, java.util.UUID id) {
        net.minecraft.core.BlockPos door = TardisDimensionManager.interiorDoor(interior);
        TardisData data = TardisManager.get(interior.getServer(), id);
        if (door != null && data != null && interior.getBlockEntity(door) instanceof InteriorDoorBlockEntity blockEntity) blockEntity.setExterior(data.exterior());
    }

    public static void ensureTop(Level level, net.minecraft.core.BlockPos pos) {
        if (!level.getBlockState(pos.above()).is(InteriorRegistry.DOOR_TOP)) level.setBlock(pos.above(), InteriorRegistry.DOOR_TOP.defaultBlockState(), 3);
    }

    static InteractionResult interact(Level level, net.minecraft.core.BlockPos pos, Player player, ItemStack stack) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        java.util.UUID id = TardisDimensionManager.id(level.dimension());
        TardisData data = id == null ? null : TardisManager.get(level.getServer(), id);
        if (data == null || !TardisAccessRegistry.canUse(id, player.getUUID(), data.ownerId())) return InteractionResult.FAIL;
        boolean key = stack.is(ModItems.TARDIS_KEY);
        if (key) {
            if (!data.ownerId().equals(player.getUUID())) return InteractionResult.FAIL;
            java.util.UUID linked = TardisLinking.get(stack);
            if (linked != null && !linked.equals(id)) return InteractionResult.FAIL;
            TardisData updated = data.withLocked(!data.locked()).withDoorOpen(false);
            TardisManager.save(level.getServer(), updated);
            DoorEvents.fire(updated, false);
            syncState((ServerLevel) level, id, false);
            level.playSound(null, pos, updated.locked() ? ModSounds.KEY_LOCK : ModSounds.KEY_UNLOCK, SoundSource.BLOCKS, 0.8F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        if (data.locked()) return InteractionResult.FAIL;
        boolean open = !data.doorOpen();
        TardisData updated = data.withDoorOpen(open);
        TardisManager.save(level.getServer(), updated);
        DoorEvents.fire(updated, open);
        syncState((ServerLevel) level, id, open);
        level.playSound(null, pos, open ? ModSounds.DOOR_OPEN : ModSounds.DOOR_CLOSE, SoundSource.BLOCKS, 0.8F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    public static void markArrival(ServerPlayer player) {
        ARRIVAL_COOLDOWNS.put(player.getUUID(), ((ServerLevel) player.level()).getGameTime() + 20L);
    }

    @Override protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN, FACING);
    }

    public static VoxelShape shape(Direction facing) {
        return switch (facing) {
            case NORTH -> Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 4.0D);
            case SOUTH -> Block.box(0.0D, 0.0D, 12.0D, 16.0D, 16.0D, 16.0D);
            case WEST -> Block.box(0.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D);
            case EAST -> Block.box(12.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
            default -> Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 4.0D);
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, net.minecraft.core.BlockPos pos, CollisionContext context) {
        return shape(state.getValue(FACING));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, net.minecraft.core.BlockPos pos, CollisionContext context) {
        return shape(state.getValue(FACING));
    }



    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, net.minecraft.core.BlockPos pos, Player player, BlockHitResult hit) {
        return interact(level, pos, player, ItemStack.EMPTY);
    }

    @Override protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, net.minecraft.core.BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return interact(level, pos, player, stack);
    }

    @Override protected void entityInside(BlockState state, Level level, net.minecraft.core.BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effects, boolean moving) {
        tryExit(state, level, pos, entity, moving);
    }

    static void tryExit(BlockState state, Level level, net.minecraft.core.BlockPos pos, Entity entity, boolean moving) {
        if (!(level instanceof ServerLevel world) || !(entity instanceof ServerPlayer player)) return;
        if (!moving) return;
        Long cooldown = ARRIVAL_COOLDOWNS.get(player.getUUID());
        if (cooldown != null) {
            if (world.getGameTime() < cooldown) return;
            ARRIVAL_COOLDOWNS.remove(player.getUUID());
        }
        java.util.UUID id = TardisDimensionManager.id(world.dimension());
        if (id == null) return;
        TardisData data = TardisManager.get(world.getServer(), id);
        if (data == null || data.locked() || !data.doorOpen()) return;
        if (!TardisAccessRegistry.canUse(id, player.getUUID(), data.ownerId())) return;
        ServerLevel exterior = world.getServer().getLevel(TardisDimensionManager.parseDimension(data.dimension()));
        if (exterior == null) return;
        double yaw = Math.toRadians(data.yaw());
        double x = data.position().getX() + 0.5D - Math.sin(yaw) * 1.8D;
        double z = data.position().getZ() + 0.5D + Math.cos(yaw) * 1.8D;
        markArrival(player);
        net.minecraft.world.level.portal.TeleportTransition transition = new net.minecraft.world.level.portal.TeleportTransition(exterior, new net.minecraft.world.phys.Vec3(x, data.position().getY(), z), net.minecraft.world.phys.Vec3.ZERO, net.minecraft.util.Mth.wrapDegrees(data.yaw() + 180.0F), 0.0F, net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING);
        world.getServer().schedule(new net.minecraft.server.TickTask(world.getServer().getTickCount() + 1, () -> {
            if (!player.isRemoved() && player.connection != null && player.level() == world) player.teleport(transition);
        }));
    }
}
