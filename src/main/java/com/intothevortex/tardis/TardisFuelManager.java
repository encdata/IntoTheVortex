package com.intothevortex.tardis;

import net.minecraft.server.MinecraftServer;

public final class TardisFuelManager {
    public static final double DEFAULT_MAX_FUEL = 1000.0D;
    public static final double REFUEL_RATE = 2.0D;

    private TardisFuelManager() {}

    public static double getFuel(TardisData data) { return data.sanitized().fuel(); }
    public static double getMaxFuel(TardisData data) { return data.sanitized().maxFuel(); }
    public static boolean hasFuel(TardisData data, double required) { return Double.isFinite(required) && required >= 0.0D && getFuel(data) >= required; }
    public static TardisData setFuel(TardisData data, double value) { return data.withFuel(value).sanitized(); }
    public static TardisData addFuel(TardisData data, double amount) { return !Double.isFinite(amount) || amount < 0.0D ? data.sanitized() : setFuel(data, getFuel(data) + amount); }
    public static TardisData consumeFuel(TardisData data, double amount) { return !hasFuel(data, amount) ? null : setFuel(data, getFuel(data) - amount); }

    public static void tick(MinecraftServer server) {
        if (server.overworld().getGameTime() % 10L != 0L) return;
        for (java.util.UUID id : TardisManager.ids(server)) {
            TardisData data = TardisManager.get(server, id);
            if (data == null || !data.refueling() || !data.powered() || data.travelState() != TardisTravelState.LANDED) continue;
            TardisData updated = addFuel(data, REFUEL_RATE * 10.0D);
            if (updated.fuel() >= updated.maxFuel()) updated = updated.withRefueling(false);
            if (updated.fuel() != data.fuel() || updated.refueling() != data.refueling()) TardisManager.save(server, updated);
        }
    }
}
