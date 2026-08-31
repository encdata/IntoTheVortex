package com.intothevortex.entity;

import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.dimension.TardisDimensionManager;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import com.intothevortex.item.ModItems;
import com.intothevortex.item.TardisLinking;
import com.intothevortex.interior.InteriorDoorBlock;
import com.intothevortex.tardis.TardisAccessRegistry;
import com.intothevortex.network.RuntimeDimensionPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

public final class TardisExteriorEntity extends Entity {
    private static final EntityDataAccessor<Boolean> DOOR_OPEN = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DOOR_PROGRESS = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<String> EXTERIOR = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.STRING);
    private UUID tardisId = new UUID(0L, 0L);
    private final Set<UUID> playersInDoorway = new HashSet<>();

    public TardisExteriorEntity(EntityType<? extends TardisExteriorEntity> entityType, Level level) {
        super(entityType, level);
    }

    public TardisExteriorEntity(ServerLevel level, UUID tardisId) {
        this(ModEntityTypes.TARDIS_EXTERIOR, level);
        this.tardisId = tardisId;
    }

    public UUID getTardisId() {
        return tardisId;
    }

    public boolean isDoorOpen() {
        return entityData.get(DOOR_OPEN);
    }

    public float getDoorProgress() { return entityData.get(DOOR_PROGRESS); }

    public String getExterior() { return entityData.get(EXTERIOR); }

    public void setExterior(String exterior) { entityData.set(EXTERIOR, exterior); }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DOOR_OPEN, false);
        builder.define(DOOR_PROGRESS, 0.0F);
        builder.define(EXTERIOR, "intothevortex:default");
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        tardisId = input.getString("tardis").map(UUID::fromString).orElse(new UUID(0L, 0L));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putString("tardis", tardisId.toString());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, net.minecraft.world.phys.Vec3 hitPosition) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }
        TardisData tardis = TardisManager.get(serverPlayer.level().getServer(), tardisId);
        if (tardis == null) {
            return InteractionResult.FAIL;
        }
        boolean owner = tardis.ownerId().equals(serverPlayer.getUUID());
        if (!TardisAccessRegistry.canUse(tardisId, serverPlayer.getUUID(), tardis.ownerId())) {
            serverPlayer.sendSystemMessage(Component.literal("You are not authorized to use this TARDIS."));
            return InteractionResult.FAIL;
        }
        if (serverPlayer.getItemInHand(hand).is(ModItems.TARDIS_KEY)) {
            if (!owner) return InteractionResult.FAIL;
            UUID linked = TardisLinking.get(serverPlayer.getItemInHand(hand));
            if (linked != null && !linked.equals(tardisId)) return InteractionResult.FAIL;
            TardisData updated = tardis.withLocked(!tardis.locked()).withDoorOpen(false);
            TardisManager.save(serverPlayer.level().getServer(), updated);
            entityData.set(DOOR_OPEN, false);
            InteriorDoorBlock.syncState((ServerLevel) serverPlayer.level(), tardisId, false);
            return InteractionResult.SUCCESS;
        }
        if (serverPlayer.isShiftKeyDown()) {
            if (!owner) {
                serverPlayer.sendSystemMessage(Component.literal("Only the owner can lock this TARDIS."));
                return InteractionResult.FAIL;
            }
            TardisData updated = tardis.withLocked(!tardis.locked()).withDoorOpen(tardis.locked() ? tardis.doorOpen() : false);
            TardisManager.save(serverPlayer.level().getServer(), updated);
            entityData.set(DOOR_OPEN, updated.doorOpen());
            InteriorDoorBlock.syncState((ServerLevel) serverPlayer.level(), tardisId, updated.doorOpen());
            serverPlayer.sendSystemMessage(Component.literal(updated.locked() ? "The TARDIS is locked." : "The TARDIS is unlocked."));
            return InteractionResult.SUCCESS;
        }
        if (tardis.locked()) {
            serverPlayer.sendSystemMessage(Component.literal("The TARDIS is locked."));
            return InteractionResult.FAIL;
        }
        TardisData updated = tardis.withDoorOpen(!tardis.doorOpen());
        TardisManager.save(serverPlayer.level().getServer(), updated);
        entityData.set(DOOR_OPEN, updated.doorOpen());
        InteriorDoorBlock.syncState((ServerLevel) serverPlayer.level(), tardisId, updated.doorOpen());
        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        float progress = getDoorProgress();
        float target = isDoorOpen() ? 1.0F : 0.0F;
        entityData.set(DOOR_PROGRESS, progress + (target - progress) * 0.18F);
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }
        TardisData tardis = TardisManager.get(serverLevel.getServer(), tardisId);
        if (tardis != null) entityData.set(EXTERIOR, tardis.exterior());
        boolean open = tardis != null && tardis.doorOpen() && !tardis.locked();
        entityData.set(DOOR_OPEN, open);
        if (!open) {
            playersInDoorway.clear();
            return;
        }
        Set<UUID> present = new HashSet<>();
        for (ServerPlayer player : serverLevel.getEntitiesOfClass(ServerPlayer.class, doorArea())) {
            present.add(player.getUUID());
            TardisData accessData = TardisManager.get(serverLevel.getServer(), tardisId);
            if (accessData == null || !TardisAccessRegistry.canUse(tardisId, player.getUUID(), accessData.ownerId())) continue;
            if (playersInDoorway.add(player.getUUID())) {
                TardisDimensionManager.ensureLoaded(serverLevel.getServer(), tardisId);
                ServerLevel registered = serverLevel.getServer().getLevel(TardisDimensionManager.key(tardisId));
                if (registered == null) {
                    playersInDoorway.remove(player.getUUID());
                    continue;
                }
                float arrivalYaw = getYRot();
                serverLevel.getServer().execute(() -> {
                    if (player.isRemoved() || player.level() != serverLevel) return;
                    ServerLevel targetLevel = serverLevel.getServer().getLevel(TardisDimensionManager.key(tardisId));
                    if (targetLevel == null) {
                        playersInDoorway.remove(player.getUUID());
                        return;
                    }
                    InteriorDoorBlock.markArrival(player);
                    ServerPlayNetworking.send(player, new RuntimeDimensionPayload(TardisDimensionManager.key(tardisId).identifier()));
                    player.teleport(new net.minecraft.world.level.portal.TeleportTransition(targetLevel, new net.minecraft.world.phys.Vec3(0.5D, 64D, 2.0D), net.minecraft.world.phys.Vec3.ZERO, arrivalYaw, 0.0F, net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING));
                });
            }
        }
        playersInDoorway.retainAll(present);
    }

    private AABB doorArea() {
        double radians = Math.toRadians(getYRot());
        double x = getX() - Math.sin(radians) * 0.86D;
        double z = getZ() + Math.cos(radians) * 0.86D;
        return new AABB(x - 0.62D, getY(), z - 0.62D, x + 0.62D, getY() + 2.55D, z + 0.62D);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith(Entity entity) {
        return true;
    }
}
