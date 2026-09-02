package com.intothevortex.tardis;

import net.minecraft.core.BlockPos;

public record TardisTravelDestination(String dimension, BlockPos position, float yaw) {
}
