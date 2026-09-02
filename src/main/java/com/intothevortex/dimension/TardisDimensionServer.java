package com.intothevortex.dimension;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface TardisDimensionServer {
    void intothevortex$queueLevel(ServerLevel level);
    boolean intothevortex$removeLevel(ResourceKey<Level> key);
    net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess intothevortex$storageSource();
    java.util.concurrent.Executor intothevortex$executor();
}
