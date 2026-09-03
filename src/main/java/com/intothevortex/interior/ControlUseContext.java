package com.intothevortex.interior;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public record ControlUseContext(ServerPlayer player, ServerLevel level, ConsoleBlockEntity console, UUID consoleUuid, TardisData tardis, ConsoleControlDefinition definition, ControlRegistry.RegisteredControl registered, float currentValue, float inputDelta) {
    public static ControlUseContext resolve(ServerPlayer player, ConsoleBlockEntity console, String controlId) {
        if (!(console.getLevel() instanceof ServerLevel level)) return null;
        ConsoleControlDefinition definition = console.definition(controlId);
        ControlRegistry.RegisteredControl registered = ControlRegistry.get(controlId);
        if (definition == null || registered == null) return null;
        UUID tardisId = TardisDimensionManager.id(level.dimension());
        MinecraftServer server = level.getServer();
        TardisData tardis = tardisId == null ? null : TardisManager.get(server, tardisId);
        return new ControlUseContext(player, level, console, console.consoleUuid(), tardis, definition, registered, console.controlValue(controlId), 0.0F);
    }

    public ControlUseContext withInputDelta(float value) {
        return new ControlUseContext(player, level, console, consoleUuid, tardis, definition, registered, currentValue, value);
    }

    public BlockPos position() {
        return console.getBlockPos();
    }

    public InteractionResult validate() {
        if (player.level() != level) return InteractionResult.FAILED_WRONG_DIMENSION;
        if (player.distanceToSqr(position().getCenter()) > 36.0D) return InteractionResult.FAILED_TOO_FAR;
        if (!registered.supportedTypes().contains(definition.inputType())) return InteractionResult.FAILED_INVALID_CONTROL;
        return registered.behavior().validate(this);
    }
}
