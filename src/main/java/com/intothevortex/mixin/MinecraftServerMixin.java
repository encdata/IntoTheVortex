package com.intothevortex.mixin;

import com.intothevortex.dimension.TardisDimensionServer;
import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.dimension.RuntimeRegistry;
import com.intothevortex.network.RuntimeDimensionSync;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements TardisDimensionServer {
    @Shadow @Final private Map<ResourceKey<Level>, ServerLevel> levels;
    @Shadow @Final protected net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess storageSource;
    @Shadow @Final private java.util.concurrent.Executor executor;
    @Unique private final List<ServerLevel> intothevortex$pendingLevels = new ArrayList<>();

    @Override
    public void intothevortex$queueLevel(ServerLevel level) {
        if (levels.containsKey(level.dimension())) return;
        for (ServerLevel pending : intothevortex$pendingLevels) if (pending.dimension().equals(level.dimension())) return;
        intothevortex$pendingLevels.add(level);
    }

    @Override
    public boolean intothevortex$removeLevel(ResourceKey<Level> key) {
        ServerLevel level = levels.remove(key);
        if (level == null) return false;
        RuntimeRegistry.unregister(((MinecraftServer) (Object) this).registries().compositeAccess().lookup(net.minecraft.core.registries.Registries.LEVEL_STEM).orElseThrow(), key.identifier());
        RuntimeRegistry.unregister(((MinecraftServer) (Object) this).registries().compositeAccess().lookup(net.minecraft.core.registries.Registries.DIMENSION_TYPE).orElseThrow(), key.identifier());
        RuntimeDimensionSync.sendRemoveToAll((MinecraftServer) (Object) this, key.identifier());
        return true;
    }

    @Override
    public net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess intothevortex$storageSource() {
        return storageSource;
    }

    @Override
    public java.util.concurrent.Executor intothevortex$executor() {
        return executor;
    }

    @Inject(method = "tickChildren", at = @At("HEAD"))
    private void intothevortex$registerPending(BooleanSupplier shouldKeepTicking, CallbackInfo callback) {
        for (ServerLevel level : intothevortex$pendingLevels) {
            levels.put(level.dimension(), level);
            RuntimeDimensionSync.sendCreateToAll((MinecraftServer) (Object) this, level.dimension());
            level.tick(() -> true);
            TardisDimensionManager.initializeRegistered((MinecraftServer) (Object) this, level);
        }
        intothevortex$pendingLevels.clear();
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void intothevortex$saveRuntimeDimensions(CallbackInfo callback) {
        TardisDimensionManager.shutdown((MinecraftServer) (Object) this);
    }
}
