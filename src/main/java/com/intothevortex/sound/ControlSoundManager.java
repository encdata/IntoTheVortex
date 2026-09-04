package com.intothevortex.sound;

import com.intothevortex.IntoTheVortex;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class ControlSoundManager {
    private ControlSoundManager() {}
    public static void initialize() {
        ControlSoundProfile door = new ControlSoundProfile(ModSounds.DOOR_OPEN, ModSounds.DOOR_CLOSE, ModSounds.DOOR_OPEN, ModSounds.DOOR_CLOSE);
        ControlSoundProfile lock = new ControlSoundProfile(ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK, ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK);
        ControlSoundRegistry.register(id("door"), door);
        ControlSoundRegistry.register(id("door_lock"), lock);
        ControlSoundRegistry.register(id("autopilot"), new ControlSoundProfile(ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK, ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK));
        ControlSoundRegistry.register(id("power"), new ControlSoundProfile(ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK, ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK));
        ControlSoundRegistry.register(id("handbrake"), new ControlSoundProfile(ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK, ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK));
        ControlSoundRegistry.register(id("refueler"), new ControlSoundProfile(ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK, ModSounds.KEY_UNLOCK, ModSounds.KEY_LOCK));
        ControlSoundRegistry.register(id("default"), new ControlSoundProfile(null, null, null, null));
    }
    public static void play(ServerLevel level, BlockPos pos, String profileId, boolean active) {
        ControlSoundProfile profile = ControlSoundRegistry.get(Identifier.parse(profileId));
        if (profile == null) return;
        SoundEvent sound = active ? profile.activation() : profile.deactivation();
        if (sound != null) level.playSound(null, pos, sound, SoundSource.BLOCKS, 0.7F, 1.0F);
    }
    public static void playForControl(ServerLevel level, BlockPos pos, String controlId, boolean active) {
        play(level, pos, id(controlId).toString(), active);
    }
    private static Identifier id(String path) { return Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, path); }
}
