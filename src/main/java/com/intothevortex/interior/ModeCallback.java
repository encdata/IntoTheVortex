package com.intothevortex.interior;

import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface ModeCallback {
    void apply(ConsoleBlockEntity console, Player player, String controlId, float delta);
}
