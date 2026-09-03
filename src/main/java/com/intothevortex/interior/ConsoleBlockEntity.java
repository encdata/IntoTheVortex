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

public final class ConsoleBlockEntity extends BlockEntity {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("IntoTheVortex/Console");
    private String console = ConsoleRegistry.TOYOTA.toString();
    private UUID consoleUuid;
    private final Map<String, Float> controlValues = new HashMap<>();
    private boolean powered;
    private boolean refueling;
    private boolean hitboxesCreated;
    private boolean hitboxesScheduled;

    public ConsoleBlockEntity(BlockPos pos, BlockState state) { super(InteriorRegistry.CONSOLE_ENTITY, pos, state); }

    public String console() { return console; }
    public UUID consoleUuid() {
        if (consoleUuid == null && level instanceof ServerLevel server) consoleUuid = UUID.nameUUIDFromBytes((server.dimension().identifier().toString() + worldPosition).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return consoleUuid;
    }
    public float controlValue(String id) { return controlValues.getOrDefault(id, 0.0F); }
    public boolean powered() { return powered; }
    public boolean refueling() { return refueling; }
    public ConsoleControlDefinition definition(String id) { return ConsoleRegistry.get(net.minecraft.resources.Identifier.parse(console)).controls().stream().filter(control -> control.id().equals(id)).findFirst().orElse(null); }

    public void setControlValue(Player player, String id, float value, boolean released) {
        if (!(level instanceof ServerLevel server)) return;
        ConsoleControlDefinition definition = definition(id);
        if (definition == null || !ControlRegistry.supports(id, definition.inputType())) return;
        float bounded = Math.clamp(value, definition.minimum(), definition.maximum());
        if (released && definition.inputType() == ConsoleInputType.MOMENTARY_BUTTON) bounded = 0.0F;
        controlValues.put(id, bounded);
        setChanged();
        server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal(id + ": " + String.format(java.util.Locale.ROOT, "%.2f", bounded)), true);
    }

    public void beginControlInput(Player player, String id) {
        if (!(level instanceof ServerLevel server)) return;
        ConsoleControlDefinition definition = definition(id);
        if (definition == null || !ControlRegistry.supports(id, definition.inputType())) return;
        if (id.equals("door")) {
            InteriorPropBlock.toggleDoor(server, worldPosition, player);
            return;
        }
        if (id.equals("door_lock")) {
            if (player.getMainHandItem().is(com.intothevortex.item.ModItems.TARDIS_KEY)) {
                java.util.UUID tardisId = com.intothevortex.dimension.TardisDimensionManager.id(server.dimension());
                TardisData data = tardisId == null ? null : TardisManager.get(server.getServer(), tardisId);
                if (data != null && data.ownerId().equals(player.getUUID())) {
                    boolean locked = !data.locked();
                    TardisManager.save(server.getServer(), data.withLocked(locked).withDoorOpen(locked ? false : data.doorOpen()));
                    if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal("Door lock: " + (locked ? "Locked" : "Unlocked")), true);
                }
            }
            return;
        }
        if (id.equals("power")) {
            powered = !powered;
            controlValues.put(id, powered ? 1.0F : 0.0F);
            java.util.UUID tardisId = com.intothevortex.dimension.TardisDimensionManager.id(server.dimension());
            TardisData data = tardisId == null ? null : TardisManager.get(server.getServer(), tardisId);
            if (data != null) {
                TardisManager.save(server.getServer(), data.withPowered(powered));
                InteriorDoorBlock.syncPower(server, tardisId, powered);
            }
            setChanged();
            server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal("Power: " + (powered ? "On" : "Off")), true);
            return;
        }
        if (id.equals("refueler")) {
            java.util.UUID tardisId = com.intothevortex.dimension.TardisDimensionManager.id(server.dimension());
            TardisData data = tardisId == null ? null : TardisManager.get(server.getServer(), tardisId);
            if (data != null && data.travelState() == com.intothevortex.tardis.TardisTravelState.LANDED && controlValue("handbrake") >= 0.5F) {
                refueling = !refueling;
                controlValues.put(id, refueling ? 1.0F : 0.0F);
                TardisManager.save(server.getServer(), data.withRefueling(refueling));
                setChanged();
                server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal("Refueling: " + (refueling ? "On" : "Off")), true);
            }
            return;
        }
        float oldValue = controlValues.getOrDefault(id, 0.0F);
        float value = definition.inputType() == ConsoleInputType.SWITCH || definition.inputType() == ConsoleInputType.KEY_SWITCH ? (oldValue >= 0.5F ? 0.0F : 1.0F) : oldValue;
        controlValues.put(id, value);
        setChanged();
        server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.literal(id + ": " + String.format(java.util.Locale.ROOT, "%.2f", value)), true);
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
                }
                createHitboxes();
            });
        }
    }
    @Override protected void loadAdditional(ValueInput input) { super.loadAdditional(input); console = input.getString("console").orElse(ConsoleRegistry.TOYOTA.toString()); consoleUuid = input.read("console_uuid", net.minecraft.core.UUIDUtil.CODEC).orElse(null); powered = input.getBooleanOr("powered", false); refueling = input.getBooleanOr("refueling", false); for (ConsoleControlDefinition control : ConsoleRegistry.get(net.minecraft.resources.Identifier.parse(console)).controls()) controlValues.put(control.id(), input.getFloatOr("control_" + control.id(), 0.0F)); }
    @Override protected void saveAdditional(ValueOutput output) { super.saveAdditional(output); output.putString("console", console); if (consoleUuid != null) output.store("console_uuid", net.minecraft.core.UUIDUtil.CODEC, consoleUuid); output.putBoolean("powered", powered); output.putBoolean("refueling", refueling); controlValues.forEach((id, value) -> output.putFloat("control_" + id, value)); }
    @Override public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveWithoutMetadata(registries); }
}
