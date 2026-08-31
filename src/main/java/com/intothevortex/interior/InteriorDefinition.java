package com.intothevortex.interior;

import com.intothevortex.exterior.AnimationDefinition;
import net.minecraft.resources.Identifier;

public record InteriorDefinition(Identifier id, Identifier model, Identifier texture, Identifier emission, AnimationDefinition animation) {
}
