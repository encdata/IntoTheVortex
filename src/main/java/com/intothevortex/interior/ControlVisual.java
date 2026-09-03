package com.intothevortex.interior;

import org.joml.Vector3f;

@FunctionalInterface
public interface ControlVisual {
    ControlVisual IDENTITY = (value, definition) -> new ControlVisualTransform(definition.position(), definition.rotation(), new Vector3f(1.0F));

    ControlVisualTransform transform(float value, ConsoleControlDefinition definition);
}
