package com.intothevortex.interior;

import java.util.List;
import net.minecraft.resources.Identifier;

public record ConsoleDefinition(Identifier id, Identifier model, Identifier texture, Identifier emission, List<ConsoleControlDefinition> controls) {}
