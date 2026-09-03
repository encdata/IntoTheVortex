package com.intothevortex.interior;

import org.joml.Vector3f;

public record ConsoleControlDefinition(String id, ConsoleInputType inputType, Vector3f position, float width, float height, String modelPart, float minimum, float maximum) {
    public ConsoleControlDefinition(String id, ConsoleInputType inputType, Vector3f position, float width, float height, String modelPart) {
        this(id, inputType, position, width, height, modelPart, 0.0F, 1.0F);
    }

    public ConsoleControlDefinition withPosition(Vector3f value) {
        return new ConsoleControlDefinition(id, inputType, value, width, height, modelPart, minimum, maximum);
    }
}
