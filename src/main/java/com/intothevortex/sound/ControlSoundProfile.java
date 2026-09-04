package com.intothevortex.sound;

import net.minecraft.sounds.SoundEvent;

public record ControlSoundProfile(SoundEvent success, SoundEvent failure, SoundEvent activation, SoundEvent deactivation) {
}
