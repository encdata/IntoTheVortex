package com.intothevortex.exterior;

import net.minecraft.resources.Identifier;

public record ExteriorDefinition(Identifier id, Identifier model, Identifier texture, float width, float height, float doorOffsetX, float doorOffsetY, float doorOffsetZ) {
}
