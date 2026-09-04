package com.intothevortex.tardis;

import net.minecraft.server.MinecraftServer;
import java.util.UUID;

public final class TardisStatusManager {
    private TardisStatusManager() {}
    public static TardisStatusSnapshot get(MinecraftServer server, UUID id) {
        TardisData data = TardisManager.get(server, id);
        return data == null ? null : TardisStatusSnapshot.from(data);
    }
}
