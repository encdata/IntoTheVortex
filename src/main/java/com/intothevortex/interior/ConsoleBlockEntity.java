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

public final class ConsoleBlockEntity extends BlockEntity {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("IntoTheVortex/Console");
    private String console = ConsoleRegistry.TOYOTA.toString();
    private final Map<String, Float> controlValues = new HashMap<>();
    private boolean hitboxesCreated;
    private boolean hitboxesScheduled;

    public ConsoleBlockEntity(BlockPos pos, BlockState state) { super(InteriorRegistry.CONSOLE_ENTITY, pos, state); }

    public String console() { return console; }
    public float controlValue(String id) { return controlValues.getOrDefault(id, 0.0F); }

    public void beginControlInput(Player player, String id) {
        if (!(level instanceof ServerLevel server)) return;
        if (id.equals("door")) InteriorPropBlock.toggleDoor(server, worldPosition, player);
    }

    public void createHitboxes() {
        if (hitboxesCreated || !(level instanceof ServerLevel server)) return;
        ConsoleDefinition definition = ConsoleRegistry.get(net.minecraft.resources.Identifier.parse(console));
        UUID owner = getBlockPos().asLong() == 0 ? UUID.randomUUID() : UUID.nameUUIDFromBytes((server.dimension().identifier().toString() + getBlockPos()).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        server.getEntitiesOfClass(ControlHitboxEntity.class, new net.minecraft.world.phys.AABB(worldPosition).inflate(4.0D), entity -> entity.consolePos().equals(worldPosition)).forEach(entity -> entity.discard());
        for (ConsoleControlDefinition control : definition.controls()) {
            server.addFreshEntity(new ControlHitboxEntity(server, worldPosition, control, owner));
        }
        hitboxesCreated = true;
        LOGGER.info("Spawned {} controls for {} console at {} in {}", definition.controls().size(), console, worldPosition, server.dimension().identifier());
    }

    @Override public void setLevel(net.minecraft.world.level.Level level) {
        super.setLevel(level);
        if (!level.isClientSide() && level.getServer() != null && !hitboxesCreated && !hitboxesScheduled) {
            hitboxesScheduled = true;
            level.getServer().execute(() -> {
                hitboxesScheduled = false;
                createHitboxes();
            });
        }
    }
    @Override protected void loadAdditional(ValueInput input) { super.loadAdditional(input); console = input.getString("console").orElse(ConsoleRegistry.TOYOTA.toString()); }
    @Override protected void saveAdditional(ValueOutput output) { super.saveAdditional(output); output.putString("console", console); }
    @Override public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveWithoutMetadata(registries); }
}
