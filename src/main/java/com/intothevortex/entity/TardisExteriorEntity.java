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
import com.intothevortex.sound.ModSounds;
import com.intothevortex.tardis.DoorEvents;
import com.intothevortex.tardis.TardisTravelState;
import com.intothevortex.tardis.TardisTeleportCooldowns;
import net.minecraft.sounds.SoundSource;
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
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("IntoTheVortex/TardisEntry");
    private static final EntityDataAccessor<Boolean> DOOR_OPEN = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DOOR_PROGRESS = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<String> EXTERIOR = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> TRAVEL_STATE = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> TRAVEL_PROGRESS = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<String> TRAVEL_ANIMATION = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DOOR_ANIMATION = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(TardisExteriorEntity.class, EntityDataSerializers.BOOLEAN);
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
    public TardisTravelState getTravelState() { return TardisTravelState.valueOf(entityData.get(TRAVEL_STATE)); }
    public float getTravelProgress() { return entityData.get(TRAVEL_PROGRESS); }
    public String getTravelAnimation() { return entityData.get(TRAVEL_ANIMATION); }
    public String getDoorAnimation() { return entityData.get(DOOR_ANIMATION); }
    public boolean isPowered() { return entityData.get(POWERED); }
    public void syncPowered(boolean value) { entityData.set(POWERED, value); }

    public void syncDoorState(boolean open) {
        entityData.set(DOOR_OPEN, open);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DOOR_OPEN, false);
        builder.define(DOOR_PROGRESS, 0.0F);
        builder.define(EXTERIOR, "intothevortex:default");
        builder.define(TRAVEL_STATE, TardisTravelState.LANDED.name());
        builder.define(TRAVEL_PROGRESS, 0.0F);
        builder.define(TRAVEL_ANIMATION, "intothevortex:default");
        builder.define(DOOR_ANIMATION, "intothevortex:door_swing");
        builder.define(POWERED, false);
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
        if (tardis.travelState() != TardisTravelState.LANDED) {
            serverPlayer.sendSystemMessage(Component.literal("The TARDIS is currently in flight."));
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
            DoorEvents.fire(updated, false);
            playDoorSound(serverPlayer.level().getServer(), updated.locked() ? ModSounds.KEY_LOCK : ModSounds.KEY_UNLOCK);
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
            DoorEvents.fire(updated, updated.doorOpen());
            playDoorSound(serverPlayer.level().getServer(), updated.locked() ? ModSounds.KEY_LOCK : ModSounds.KEY_UNLOCK);
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
        DoorEvents.fire(updated, updated.doorOpen());
        playDoorSound(serverPlayer.level().getServer(), updated.doorOpen() ? ModSounds.DOOR_OPEN : ModSounds.DOOR_CLOSE);
        entityData.set(DOOR_OPEN, updated.doorOpen());
        InteriorDoorBlock.syncState((ServerLevel) serverPlayer.level(), tardisId, updated.doorOpen());
        return InteractionResult.SUCCESS;
    }

    private void playDoorSound(net.minecraft.server.MinecraftServer server, net.minecraft.sounds.SoundEvent sound) {
        level().playSound(null, blockPosition(), sound, SoundSource.BLOCKS, 0.8F, 1.0F);
        ServerLevel interior = TardisDimensionManager.ensureLoaded(server, tardisId);
        if (interior != null) {
            net.minecraft.core.BlockPos door = TardisDimensionManager.interiorDoor(interior);
            if (door != null) interior.playSound(null, door, sound, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
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
        if (tardis == null) return;
        entityData.set(TRAVEL_STATE, tardis.travelState().name());
        entityData.set(TRAVEL_ANIMATION, tardis.travelState() == TardisTravelState.MAT ? tardis.matAnimation() : tardis.dematAnimation());
        entityData.set(DOOR_ANIMATION, tardis.doorAnimation());
        entityData.set(POWERED, tardis.powered());
        float travelProgress = switch (tardis.travelState()) {
            case DEMAT -> {
                var animation = com.intothevortex.exterior.TardisAnimationManager.getPhase(net.minecraft.resources.Identifier.parse(tardis.dematAnimation()));
                yield Math.min(1.0F, tardis.phaseTicks() / (float) animation.ticks());
            }
            case MAT -> {
                var animation = com.intothevortex.exterior.TardisAnimationManager.getPhase(net.minecraft.resources.Identifier.parse(tardis.matAnimation()));
                yield Math.min(1.0F, tardis.phaseTicks() / (float) animation.ticks());
            }
            default -> 0.0F;
        };
        entityData.set(TRAVEL_PROGRESS, travelProgress);
        if (tardis.travelState() == TardisTravelState.FLIGHT || (tardis.travelState() == TardisTravelState.MAT && !getUUID().equals(tardis.exteriorId()))) {
            discard();
            return;
        }
        if (tardis != null) entityData.set(EXTERIOR, tardis.exterior());
        boolean open = tardis != null && tardis.travelState() == TardisTravelState.LANDED && tardis.doorOpen() && !tardis.locked();
        entityData.set(DOOR_OPEN, open);
        if (!open) {
            playersInDoorway.clear();
            return;
        }
        Set<UUID> present = new HashSet<>();
        for (ServerPlayer player : serverLevel.getEntitiesOfClass(ServerPlayer.class, doorArea())) {
            present.add(player.getUUID());
            if (TardisTeleportCooldowns.active(serverLevel.getServer(), player.getUUID())) continue;
            TardisData accessData = TardisManager.get(serverLevel.getServer(), tardisId);
            if (accessData == null || !TardisAccessRegistry.canUse(tardisId, player.getUUID(), accessData.ownerId())) continue;
            if (playersInDoorway.add(player.getUUID())) {
                LOGGER.info("Queued exterior entry for {} into TARDIS {}", player.getGameProfile().name(), tardisId);
                TardisDimensionManager.whenInteriorReady(serverLevel.getServer(), tardisId, targetLevel -> {
                    TardisData readyData = TardisManager.get(serverLevel.getServer(), tardisId);
                    if (player.isRemoved() || player.connection == null || readyData == null || readyData.locked() || !readyData.doorOpen() || !TardisAccessRegistry.canUse(tardisId, player.getUUID(), readyData.ownerId())) {
                        LOGGER.info("Cancelled exterior entry for {} into TARDIS {}", player.getGameProfile().name(), tardisId);
                        playersInDoorway.remove(player.getUUID());
                        return;
                    }
                    net.minecraft.core.BlockPos doorPos = TardisDimensionManager.interiorDoor(targetLevel);
                    if (doorPos == null) {
                        LOGGER.info("Interior doorway was unavailable for TARDIS {}", tardisId);
                        playersInDoorway.remove(player.getUUID());
                        return;
                    }
                    net.minecraft.core.Direction doorFacing = targetLevel.getBlockState(doorPos).getValue(InteriorDoorBlock.FACING);
                    float arrivalYaw = doorFacing.toYRot();
                    LOGGER.info("Teleporting {} into TARDIS {} at {}", player.getGameProfile().name(), tardisId, doorPos);
                    try {
                        var arrival = TardisDimensionManager.interiorArrival(targetLevel, doorPos);
                        player.teleport(new net.minecraft.world.level.portal.TeleportTransition(targetLevel, arrival, net.minecraft.world.phys.Vec3.ZERO, arrivalYaw, 0.0F, java.util.Set.of(), net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING));
                        InteriorDoorBlock.markArrival(player);
                        playersInDoorway.remove(player.getUUID());
                    } catch (RuntimeException exception) {
                        playersInDoorway.remove(player.getUUID());
                        TardisTeleportCooldowns.clear(player.getUUID());
                        LOGGER.error("Exterior entry failed for {} into TARDIS {}", player.getGameProfile().name(), tardisId, exception);
                    }
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
