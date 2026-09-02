package com.intothevortex.sound;

import com.intothevortex.IntoTheVortex;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {
    public static final SoundEvent DOOR_OPEN = register("police_box_door_open");
    public static final SoundEvent DOOR_CLOSE = register("police_box_door_close");
    public static final SoundEvent KEY_LOCK = register("key_lock");
    public static final SoundEvent KEY_UNLOCK = register("key_unlock");
    public static final SoundEvent TARDIS_DEMAT = register("type70demat");
    public static final SoundEvent TARDIS_MAT = register("type70mat");
    private ModSounds() {}
    public static void initialize() {}
    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceKey.create(net.minecraft.core.registries.Registries.SOUND_EVENT, id), SoundEvent.createVariableRangeEvent(id));
    }
}
