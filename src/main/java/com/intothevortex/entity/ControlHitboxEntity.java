package com.intothevortex.entity;

import com.intothevortex.interior.ConsoleControlDefinition;
import com.intothevortex.interior.ConsoleDefinition;
import com.intothevortex.interior.ConsoleInputType;
import com.intothevortex.interior.ConsoleRegistry;
import com.intothevortex.interior.InteriorRegistry;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;
import net.minecraft.core.BlockPos;

public final class ControlHitboxEntity extends Entity {
    private static final EntityDataAccessor<Float> CONTROL_WIDTH = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CONTROL_HEIGHT = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.FLOAT);
    private BlockPos consolePos = BlockPos.ZERO;
    private String controlId = "";
    private UUID consoleEntityId;
    private float controlWidth = 0.125F;
    private float controlHeight = 0.125F;

    public ControlHitboxEntity(EntityType<? extends ControlHitboxEntity> type, Level level) { super(type, level); setNoGravity(true); }

    public ControlHitboxEntity(ServerLevel level, BlockPos consolePos, ConsoleControlDefinition definition, UUID consoleEntityId) {
        this(ModEntityTypes.CONTROL_HITBOX, level);
        this.consolePos = consolePos;
        this.controlId = definition.id();
        this.consoleEntityId = consoleEntityId;
        this.controlWidth = definition.width();
        this.controlHeight = definition.height();
        entityData.set(CONTROL_WIDTH, controlWidth);
        entityData.set(CONTROL_HEIGHT, controlHeight);
        setPos(consolePos.getX() + 0.5D + definition.position().x(), consolePos.getY() + 0.5D + definition.position().y(), consolePos.getZ() + 0.5D + definition.position().z());
        refreshDimensions();
    }

    public BlockPos consolePos() { return consolePos; }
    public String controlId() { return controlId; }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CONTROL_WIDTH, 0.125F);
        builder.define(CONTROL_HEIGHT, 0.125F);
    }
    @Override protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
        consolePos = input.read("console_pos", BlockPos.CODEC).orElse(BlockPos.ZERO);
        controlId = input.getString("control").orElse("");
        controlWidth = input.getFloatOr("width", 0.125F);
        controlHeight = input.getFloatOr("height", 0.125F);
        entityData.set(CONTROL_WIDTH, controlWidth);
        entityData.set(CONTROL_HEIGHT, controlHeight);
        refreshDimensions();
    }
    @Override protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
        output.store("console_pos", BlockPos.CODEC, consolePos);
        output.putString("control", controlId);
        output.putFloat("width", controlWidth);
        output.putFloat("height", controlHeight);
    }
    @Override public boolean isPickable() { return true; }
    @Override public boolean shouldRenderAtSqrDistance(double distance) { return true; }
    @Override public net.minecraft.world.entity.EntityDimensions getDimensions(net.minecraft.world.entity.Pose pose) { return net.minecraft.world.entity.EntityDimensions.fixed(entityData.get(CONTROL_WIDTH), entityData.get(CONTROL_HEIGHT)); }
    @Override public boolean isPushable() { return false; }
    @Override public boolean canBeCollidedWith(Entity entity) { return false; }
    @Override public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float amount) { return false; }

    @Override public InteractionResult interact(Player player, InteractionHand hand, Vec3 hit) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (level().getBlockEntity(consolePos) instanceof com.intothevortex.interior.ConsoleBlockEntity console) console.beginControlInput(player, controlId);
        return InteractionResult.SUCCESS;
    }

    @Override public void tick() {
        super.tick();
        if (level().isClientSide()) return;
        if (!(level() instanceof ServerLevel server) || !(server.getBlockEntity(consolePos) instanceof com.intothevortex.interior.ConsoleBlockEntity)) discard();
    }
}
