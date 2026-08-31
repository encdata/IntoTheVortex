package com.intothevortex.mixin;

import com.intothevortex.dimension.TardisDimensionServer;
import com.intothevortex.dimension.TardisDimensionManager;
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
    @Unique private final List<ServerLevel> intothevortex$pendingLevels = new ArrayList<>();

    @Override
    public void intothevortex$queueLevel(ServerLevel level) {
        intothevortex$pendingLevels.add(level);
    }

    @Override
    public boolean intothevortex$removeLevel(ResourceKey<Level> key) {
        return levels.remove(key) != null;
    }

    @Override
    public net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess intothevortex$storageSource() {
        return storageSource;
    }

    @Inject(method = "tickChildren", at = @At("HEAD"))
    private void intothevortex$registerPending(BooleanSupplier shouldKeepTicking, CallbackInfo callback) {
        for (ServerLevel level : intothevortex$pendingLevels) {
            levels.put(level.dimension(), level);
            level.tick(() -> true);
            TardisDimensionManager.initializeRegistered((MinecraftServer) (Object) this, level);
        }
        intothevortex$pendingLevels.clear();
    }
}
