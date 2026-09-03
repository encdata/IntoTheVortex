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
import java.util.Optional;
import net.minecraft.core.BlockPos;

public final class ControlHitboxEntity extends Entity {
    private static final EntityDataAccessor<BlockPos> CONSOLE_POS = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<String> CONTROL_ID = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> CONSOLE_UUID = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> CONSOLE_DEFINITION = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> CONTROL_TYPE = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> CONTROL_MINIMUM = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CONTROL_MAXIMUM = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CONTROL_WIDTH = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CONTROL_HEIGHT = SynchedEntityData.defineId(ControlHitboxEntity.class, EntityDataSerializers.FLOAT);
    private BlockPos consolePos = BlockPos.ZERO;
    private String controlId = "";
    private UUID consoleEntityId;
    private String consoleDefinition = "intothevortex:toyota";
    private float controlWidth = 0.125F;
    private float controlHeight = 0.125F;
    private float value;

    public ControlHitboxEntity(EntityType<? extends ControlHitboxEntity> type, Level level) { super(type, level); setNoGravity(true); }

    public ControlHitboxEntity(ServerLevel level, BlockPos consolePos, ConsoleControlDefinition definition, UUID consoleEntityId, String consoleDefinition) {
        this(ModEntityTypes.CONTROL_HITBOX, level);
        this.consolePos = consolePos;
        this.controlId = definition.id();
        this.consoleEntityId = consoleEntityId;
        this.consoleDefinition = consoleDefinition;
        this.controlWidth = definition.width();
        this.controlHeight = definition.height();
        entityData.set(CONSOLE_POS, consolePos);
        entityData.set(CONTROL_ID, controlId);
        entityData.set(CONSOLE_UUID, consoleEntityId == null ? "" : consoleEntityId.toString());
        entityData.set(CONSOLE_DEFINITION, consoleDefinition);
        entityData.set(CONTROL_TYPE, definition.inputType().ordinal());
        entityData.set(CONTROL_MINIMUM, definition.minimum());
        entityData.set(CONTROL_MAXIMUM, definition.maximum());
        entityData.set(CONTROL_WIDTH, controlWidth);
        entityData.set(CONTROL_HEIGHT, controlHeight);
        setPos(consolePos.getX() + 0.5D + definition.position().x(), consolePos.getY() + 0.5D + definition.position().y(), consolePos.getZ() + 0.5D + definition.position().z());
        refreshDimensions();
    }

    public BlockPos consolePos() { return entityData.get(CONSOLE_POS); }
    public String controlId() { return entityData.get(CONTROL_ID); }
    public UUID consoleUuid() {
        String value = entityData.get(CONSOLE_UUID);
        return value.isEmpty() ? null : UUID.fromString(value);
    }
    public ConsoleControlDefinition definition() {
        String identifier = entityData.get(CONSOLE_DEFINITION);
        try {
            ConsoleDefinition console = ConsoleRegistry.get(net.minecraft.resources.Identifier.parse(identifier));
            if (console != null) {
                ConsoleControlDefinition registered = console.controls().stream().filter(control -> control.id().equals(controlId)).findFirst().orElse(null);
                if (registered != null) return registered;
            }
        } catch (RuntimeException exception) {
        }
        int ordinal = entityData.get(CONTROL_TYPE);
        ConsoleInputType[] types = ConsoleInputType.values();
        if (ordinal < 0 || ordinal >= types.length) return null;
        return new ConsoleControlDefinition(controlId, types[ordinal], new org.joml.Vector3f(), entityData.get(CONTROL_WIDTH), entityData.get(CONTROL_HEIGHT), controlId, entityData.get(CONTROL_MINIMUM), entityData.get(CONTROL_MAXIMUM));
    }
    public float value() {
        if (!(level().getBlockEntity(consolePos) instanceof com.intothevortex.interior.ConsoleBlockEntity console)) return value;
        return console.controlValue(controlId);
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CONSOLE_POS, BlockPos.ZERO);
        builder.define(CONTROL_ID, "");
        builder.define(CONSOLE_UUID, "");
        builder.define(CONSOLE_DEFINITION, "intothevortex:toyota");
        builder.define(CONTROL_TYPE, ConsoleInputType.BUTTON.ordinal());
        builder.define(CONTROL_MINIMUM, 0.0F);
        builder.define(CONTROL_MAXIMUM, 1.0F);
        builder.define(CONTROL_WIDTH, 0.125F);
        builder.define(CONTROL_HEIGHT, 0.125F);
    }
    @Override protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
        consolePos = input.read("console_pos", BlockPos.CODEC).orElse(BlockPos.ZERO);
        controlId = input.getString("control").orElse("");
        controlWidth = input.getFloatOr("width", 0.125F);
        controlHeight = input.getFloatOr("height", 0.125F);
        value = input.getFloatOr("value", 0.0F);
        consoleEntityId = input.read("console_uuid", net.minecraft.core.UUIDUtil.CODEC).orElse(null);
        consoleDefinition = input.getStringOr("console_definition", "intothevortex:toyota");
        int type = input.getIntOr("control_type", ConsoleInputType.BUTTON.ordinal());
        entityData.set(CONTROL_TYPE, type);
        entityData.set(CONTROL_MINIMUM, input.getFloatOr("control_minimum", 0.0F));
        entityData.set(CONTROL_MAXIMUM, input.getFloatOr("control_maximum", 1.0F));
        entityData.set(CONSOLE_POS, consolePos);
        entityData.set(CONTROL_ID, controlId);
        entityData.set(CONSOLE_UUID, consoleEntityId == null ? "" : consoleEntityId.toString());
        entityData.set(CONSOLE_DEFINITION, consoleDefinition);
        entityData.set(CONTROL_WIDTH, controlWidth);
        entityData.set(CONTROL_HEIGHT, controlHeight);
        refreshDimensions();
    }
    @Override protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
        output.store("console_pos", BlockPos.CODEC, consolePos);
        output.putString("control", controlId);
        output.putFloat("width", controlWidth);
        output.putFloat("height", controlHeight);
        output.putFloat("value", value);
        if (consoleEntityId != null) output.store("console_uuid", net.minecraft.core.UUIDUtil.CODEC, consoleEntityId);
        output.putString("console_definition", consoleDefinition);
        output.putInt("control_type", entityData.get(CONTROL_TYPE));
        output.putFloat("control_minimum", entityData.get(CONTROL_MINIMUM));
        output.putFloat("control_maximum", entityData.get(CONTROL_MAXIMUM));
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
        if (!(level() instanceof ServerLevel server) || !(server.getBlockEntity(consolePos) instanceof com.intothevortex.interior.ConsoleBlockEntity console) || !console.consoleUuid().equals(consoleEntityId)) {
            discard();
            return;
        }
        ConsoleControlDefinition current = console.definition(controlId);
        if (current != null) {
            String currentConsole = console.console();
            if (!currentConsole.equals(entityData.get(CONSOLE_DEFINITION))) entityData.set(CONSOLE_DEFINITION, currentConsole);
            if (current.inputType().ordinal() != entityData.get(CONTROL_TYPE)) entityData.set(CONTROL_TYPE, current.inputType().ordinal());
            if (current.minimum() != entityData.get(CONTROL_MINIMUM)) entityData.set(CONTROL_MINIMUM, current.minimum());
            if (current.maximum() != entityData.get(CONTROL_MAXIMUM)) entityData.set(CONTROL_MAXIMUM, current.maximum());
        }
    }
}
