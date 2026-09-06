package com.intothevortex.network;

import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.tardis.TardisStatusSnapshot;
import com.intothevortex.tardis.TardisLoyalty;
import com.intothevortex.tardis.TardisLoyaltyManager;
import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.exterior.ExteriorDefinition;
import com.intothevortex.exterior.ExteriorRegistry;
import com.intothevortex.interior.InteriorRegistry;
import net.minecraft.resources.Identifier;
import java.util.UUID;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public final class MonitorServer {
    private MonitorServer() {
    }

    public static boolean open(ServerPlayer player, UUID tardisId, BlockPos monitorPos) {
        if (player == null || tardisId == null || monitorPos == null) {
            return false;
        }
        TardisData data = TardisManager.get(player.level().getServer(), tardisId);
        if (data == null || !data.powered()) {
            return false;
        }
        if (!data.ownerId().equals(player.getUUID()) && !TardisLoyaltyManager.get(player.level().getServer(), tardisId, player.getUUID()).isAtLeast(TardisLoyalty.Rank.COMPANION)) {
            return false;
        }
        if (player.distanceToSqr(monitorPos.getCenter()) > 36.0D) {
            return false;
        }
        ServerPlayNetworking.send(player, MonitorStatePayload.from(TardisStatusSnapshot.from(data)));
        ServerPlayNetworking.send(player, new OpenMonitorPayload(tardisId, monitorPos));
        return true;
    }

    public static boolean applySelection(ServerPlayer player, UUID tardisId, BlockPos monitorPos, String target, String value) {
        if (player == null || tardisId == null || monitorPos == null || (!target.equals("exterior") && !target.equals("interior"))) return false;
        var server = player.level().getServer();
        TardisData data = TardisManager.get(server, tardisId);
        if (data == null || !data.powered() || data.locked() || player.distanceToSqr(monitorPos.getCenter()) > 36.0D) return false;
        if (!data.ownerId().equals(player.getUUID()) && !TardisLoyaltyManager.get(server, tardisId, player.getUUID()).isAtLeast(TardisLoyalty.Rank.PILOT)) return false;
        try {
            Identifier id = Identifier.parse(value);
            if (target.equals("exterior")) {
                ExteriorDefinition definition = ExteriorRegistry.values().stream().filter(entry -> entry.id().equals(id)).findFirst().orElse(null);
                if (definition == null) return false;
                TardisManager.save(server, data.withExteriorType(id.toString()));
                TardisManager.spawnExterior(server, TardisManager.get(server, tardisId));
                return true;
            }
            if (!InteriorRegistry.registered().contains(id) || data.interior().equals(id.toString())) return false;
            TardisManager.switchInterior(server, tardisId, id.toString());
            TardisDimensionManager.replaceInterior(server, tardisId);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
