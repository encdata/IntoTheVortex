package com.intothevortex.exterior;

import net.minecraft.resources.Identifier;

public record ExteriorDefinition(Identifier id, Identifier model, Identifier texture, Identifier emission, float width, float height, float doorOffsetX, float doorOffsetY, float doorOffsetZ, AnimationDefinition animation) {
    public ExteriorDefinition(Identifier id, Identifier model, Identifier texture, float width, float height, float doorOffsetX, float doorOffsetY, float doorOffsetZ, AnimationDefinition animation) {
        this(id, model, texture, null, width, height, doorOffsetX, doorOffsetY, doorOffsetZ, animation);
    }
}
