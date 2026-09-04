package com.intothevortex.interior;

import com.intothevortex.entity.ControlHitboxEntity;
import com.intothevortex.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.dimension.TardisDimensionManager;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.intothevortex.network.ControlValueSyncPayload;

public final class ConsoleBlockEntity extends BlockEntity {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("IntoTheVortex/Console");
    private String console = ConsoleRegistry.TOYOTA.toString();
    private UUID consoleUuid;
    private final Map<String, Float> controlValues = new HashMap<>();
    private boolean powered;
    private boolean refueling;
    private boolean hitboxesCreated;
    private boolean hitboxesScheduled;
    private String waypointDimension;
    private BlockPos waypointPosition;
    private float waypointYaw;

    public ConsoleBlockEntity(BlockPos pos, BlockState state) { super(InteriorRegistry.CONSOLE_ENTITY, pos, state); }

    public String console() { return console; }
    public UUID consoleUuid() {
        if (consoleUuid == null && level instanceof ServerLevel server) consoleUuid = UUID.nameUUIDFromBytes((server.dimension().identifier().toString() + worldPosition).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return consoleUuid;
    }
    public float controlValue(String id) { return controlValues.getOrDefault(id, 0.0F); }
    public void applySyncedControlValue(String id, float value) {
        if (level == null || !level.isClientSide() || !Float.isFinite(value)) return;
        ConsoleControlDefinition definition = definition(id);
        if (definition != null) controlValues.put(id, Math.clamp(value, definition.minimum(), definition.maximum()));
    }
    public void syncCoordinateValues(BlockPos position) {
        controlValues.put("x", (float) position.getX());
        controlValues.put("y", (float) position.getY());
        controlValues.put("z", (float) position.getZ());
        setChanged();
        if (level instanceof ServerLevel server) {
            sendControlValue(server, "x", controlValues.get("x"));
            sendControlValue(server, "y", controlValues.get("y"));
            sendControlValue(server, "z", controlValues.get("z"));
        }
    }
    public boolean powered() { return powered; }
    public boolean refueling() { return refueling; }
    public void saveWaypoint(TardisData data) { waypointDimension = data.dimension(); waypointPosition = data.position(); waypointYaw = data.yaw(); setChanged(); }
    public boolean hasWaypoint() { return waypointDimension != null && waypointPosition != null; }
    public String waypointDimension() { return waypointDimension; }
    public BlockPos waypointPosition() { return waypointPosition; }
    public float waypointYaw() { return waypointYaw; }
    public ConsoleControlDefinition definition(String id) { return ConsoleRegistry.get(net.minecraft.resources.Identifier.parse(console)).controls().stream().filter(control -> control.id().equals(id)).findFirst().orElse(null); }

    public void setAuthoritativeValue(Player player, String id, float value, boolean released) {
        if (!(level instanceof ServerLevel server)) return;
        ConsoleControlDefinition definition = definition(id);
        if (definition == null || !ControlRegistry.supports(id, definition.inputType())) return;
        float bounded = Math.clamp(value, definition.minimum(), definition.maximum());
        if (id.equals("handbrake")) bounded = bounded >= 0.5F ? 1.0F : 0.0F;
        if (released && definition.inputType() == ConsoleInputType.MOMENTARY_BUTTON) bounded = 0.0F;
        controlValues.put(id, bounded);
        setChanged();
        server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        sendControlValue(server, id, bounded);
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal(id + ": " + String.format(java.util.Locale.ROOT, "%.2f", bounded)), true);
    }

    public void setControlValue(Player player, String id, float value, boolean released) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!Float.isFinite(value)) return;
        ControlUseContext context = ControlUseContext.resolve(serverPlayer, this, id);
        if (context == null || context.validate() != InteractionResult.SUCCESS) return;
        ControlCapability capability = released ? ControlCapability.RELEASE : context.definition().inputType() == ConsoleInputType.MOMENTARY_BUTTON ? ControlCapability.PRESS_DOWN : ControlCapability.DRAG;
        if (!context.registered().capabilities().contains(capability)) return;
        InteractionResult result;
        if (released) {
            result = context.registered().behavior().onRelease(context);
            if (result == InteractionResult.FAILED_INVALID_CONTROL_STATE) result = context.registered().behavior().onDrag(context, value);
        } else if (context.definition().inputType() == ConsoleInputType.MOMENTARY_BUTTON && context.registered().capabilities().contains(ControlCapability.PRESS_DOWN)) {
            result = context.registered().behavior().onPressDown(context);
            if (result == InteractionResult.FAILED_INVALID_CONTROL_STATE) result = context.registered().behavior().onDrag(context, value);
        } else {
            result = context.registered().behavior().onDrag(context, value);
        }
        if (result == InteractionResult.SUCCESS) setAuthoritativeValue(player, id, value, released);
    }

    public void stepControl(Player player, String id, float direction) {
        if (direction != -1.0F && direction != 1.0F) return;
        ControlMode mode = direction > 0.0F ? ControlMode.RIGHT_CLICK_UP : ControlMode.LEFT_CLICK_DOWN;
        if (!ControlRegistry.supports(id, mode) || !(player instanceof ServerPlayer serverPlayer)) return;
        ControlUseContext context = ControlUseContext.resolve(serverPlayer, this, id);
        if (context == null || context.validate() != InteractionResult.SUCCESS) return;
        InteractionResult result = direction > 0.0F ? context.registered().behavior().onPress(context.withInputDelta(direction)) : context.registered().behavior().onSecondaryPress(context.withInputDelta(direction));
        if (result == InteractionResult.SUCCESS) com.intothevortex.tardis.TardisFlightEventManager.onControl(context);
        if (result == InteractionResult.FAILED_INVALID_CONTROL_STATE) result = context.registered().behavior().onPress(context.withInputDelta(direction));
        if (result != InteractionResult.SUCCESS) serverPlayer.sendSystemMessage(Component.literal(result.name()), true);
    }

    public void beginControlInput(Player player, String id) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        ControlUseContext context = ControlUseContext.resolve(serverPlayer, this, id);
        if (context == null || context.validate() != InteractionResult.SUCCESS || !context.registered().capabilities().contains(ControlCapability.BUTTON)) return;
        InteractionResult result = context.registered().behavior().onPress(context);
        if (result == InteractionResult.SUCCESS) com.intothevortex.tardis.TardisFlightEventManager.onControl(context);
        if (result != InteractionResult.SUCCESS) serverPlayer.sendSystemMessage(Component.literal(result.name()), true);
    }

    public void setPowered(Player player, boolean value) {
        if (!(level instanceof ServerLevel server)) return;
        powered = value;
        controlValues.put("power", value ? 1.0F : 0.0F);
        java.util.UUID tardisId = TardisDimensionManager.id(server.dimension());
        TardisData data = tardisId == null ? null : TardisManager.get(server.getServer(), tardisId);
        if (data != null) {
            TardisManager.save(server.getServer(), data.withPowered(value));
            InteriorDoorBlock.syncPower(server, tardisId, value);
        }
        setChanged();
        server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        sendControlValue(server, "power", value ? 1.0F : 0.0F);
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal("Power: " + (value ? "On" : "Off")), true);
    }

    public void setRefueling(Player player, boolean value) {
        if (!(level instanceof ServerLevel server)) return;
        refueling = value;
        controlValues.put("refueler", value ? 1.0F : 0.0F);
        java.util.UUID tardisId = TardisDimensionManager.id(server.dimension());
        TardisData data = tardisId == null ? null : TardisManager.get(server.getServer(), tardisId);
        if (data != null) TardisManager.save(server.getServer(), data.withRefueling(value));
        setChanged();
        server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        sendControlValue(server, "refueler", value ? 1.0F : 0.0F);
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal("Refueling: " + (value ? "On" : "Off")), true);
    }

    public void setHandbrakeEngaged(Player player, boolean value) {
        if (!(level instanceof ServerLevel server)) return;
        controlValues.put("handbrake", value ? 1.0F : 0.0F);
        java.util.UUID tardisId = TardisDimensionManager.id(server.dimension());
        TardisData data = tardisId == null ? null : TardisManager.get(server.getServer(), tardisId);
        if (data != null) {
            TardisData updated = data.withFlightControls(data.getThrottleStage(), value);
            TardisManager.save(server.getServer(), updated);
            if (!value && updated.getThrottleStage() > 0) com.intothevortex.tardis.TardisTravelManager.tryFly(server.getServer(), updated.id());
        }
        setChanged();
        server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        sendControlValue(server, "handbrake", value ? 1.0F : 0.0F);
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal("Handbrake: " + (value ? "Engaged" : "Released")), true);
    }

    public void setThrottleStage(Player player, int value) {
        if (!(level instanceof ServerLevel server)) return;
        int stage = Math.clamp(value, 0, 4);
        controlValues.put("throttle", (float) stage);
        java.util.UUID tardisId = TardisDimensionManager.id(server.dimension());
        TardisData data = tardisId == null ? null : TardisManager.get(server.getServer(), tardisId);
        if (data != null) {
            TardisData updated = data.withFlightControls(stage, data.isHandbrakeEngaged());
            TardisManager.save(server.getServer(), updated);
            if (stage > 0 && !updated.isHandbrakeEngaged()) com.intothevortex.tardis.TardisTravelManager.tryFly(server.getServer(), updated.id());
        }
        setChanged();
        server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        sendControlValue(server, "throttle", stage);
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal("Throttle: " + stage), true);
    }

    public void createHitboxes() {
        if (hitboxesCreated || !(level instanceof ServerLevel server)) return;
        ConsoleDefinition definition = ConsoleRegistry.get(net.minecraft.resources.Identifier.parse(console));
        UUID owner = consoleUuid();
        server.getEntitiesOfClass(ControlHitboxEntity.class, new net.minecraft.world.phys.AABB(worldPosition).inflate(4.0D), entity -> entity.consolePos().equals(worldPosition) || owner.equals(entity.consoleUuid())).forEach(entity -> entity.discard());
        for (ConsoleControlDefinition control : definition.controls()) {
            org.joml.Vector3f offset = rotatedOffset(control.position(), getBlockState().getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING));
            server.addFreshEntity(new ControlHitboxEntity(server, worldPosition, control.withPosition(offset), owner, console));
        }
        hitboxesCreated = true;
        LOGGER.info("Spawned {} controls for {} console at {} in {}", definition.controls().size(), console, worldPosition, server.dimension().identifier());
    }

    private void sendControlValue(ServerLevel server, String id, float value) {
        server.getEntitiesOfClass(ServerPlayer.class, new net.minecraft.world.phys.AABB(worldPosition).inflate(32.0D)).forEach(player -> ServerPlayNetworking.send(player, new ControlValueSyncPayload(worldPosition, id, value)));
    }

    private static org.joml.Vector3f rotatedOffset(org.joml.Vector3f offset, net.minecraft.core.Direction facing) {
        return switch (facing) {
            case EAST -> new org.joml.Vector3f(offset.z(), offset.y(), -offset.x());
            case SOUTH -> new org.joml.Vector3f(offset.x(), offset.y(), offset.z());
            case WEST -> new org.joml.Vector3f(-offset.z(), offset.y(), offset.x());
            default -> new org.joml.Vector3f(-offset.x(), offset.y(), -offset.z());
        };
    }

    @Override public void setLevel(net.minecraft.world.level.Level level) {
        super.setLevel(level);
        if (!level.isClientSide() && level.getServer() != null && !hitboxesCreated && !hitboxesScheduled) {
            hitboxesScheduled = true;
            level.getServer().execute(() -> {
                hitboxesScheduled = false;
                java.util.UUID tardisId = TardisDimensionManager.id(((ServerLevel) level).dimension());
                TardisData data = tardisId == null ? null : TardisManager.get(level.getServer(), tardisId);
                if (data != null) {
                    powered = data.powered();
                    refueling = data.refueling();
                    controlValues.put("throttle", (float) data.getThrottleStage());
                    controlValues.put("handbrake", data.isHandbrakeEngaged() ? 1.0F : 0.0F);
                    controlValues.put("x", (float) data.requestedDestinationPosition().getX());
                    controlValues.put("y", (float) data.requestedDestinationPosition().getY());
                    controlValues.put("z", (float) data.requestedDestinationPosition().getZ());
                }
                createHitboxes();
            });
        }
    }
    @Override protected void loadAdditional(ValueInput input) { super.loadAdditional(input); console = input.getString("console").orElse(ConsoleRegistry.TOYOTA.toString()); consoleUuid = input.read("console_uuid", net.minecraft.core.UUIDUtil.CODEC).orElse(null); powered = input.getBooleanOr("powered", false); refueling = input.getBooleanOr("refueling", false); waypointDimension = input.getString("waypoint_dimension").orElse(null); waypointPosition = input.read("waypoint_position", BlockPos.CODEC).orElse(null); waypointYaw = input.getFloatOr("waypoint_yaw", 0.0F); for (ConsoleControlDefinition control : ConsoleRegistry.get(net.minecraft.resources.Identifier.parse(console)).controls()) controlValues.put(control.id(), input.getFloatOr("control_" + control.id(), 0.0F)); }
    @Override protected void saveAdditional(ValueOutput output) { super.saveAdditional(output); output.putString("console", console); if (consoleUuid != null) output.store("console_uuid", net.minecraft.core.UUIDUtil.CODEC, consoleUuid); output.putBoolean("powered", powered); output.putBoolean("refueling", refueling); if (waypointDimension != null) output.putString("waypoint_dimension", waypointDimension); if (waypointPosition != null) output.store("waypoint_position", BlockPos.CODEC, waypointPosition); output.putFloat("waypoint_yaw", waypointYaw); controlValues.forEach((id, value) -> output.putFloat("control_" + id, value)); }
    @Override public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveWithoutMetadata(registries); }
}
