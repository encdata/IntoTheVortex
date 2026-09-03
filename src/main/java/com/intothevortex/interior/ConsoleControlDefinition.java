package com.intothevortex.interior;

import org.joml.Vector3f;

public record ConsoleControlDefinition(String id, ConsoleInputType inputType, Vector3f position, Vector3f rotation, float width, float height, float depth, String modelPart, ControlVisual visual, float minimum, float maximum) {
    public ConsoleControlDefinition(String id, ConsoleInputType inputType, Vector3f position, float width, float height, String modelPart) {
        this(id, inputType, position, new Vector3f(), width, height, 0.125F, modelPart, ControlVisual.IDENTITY, 0.0F, 1.0F);
    }

    public ConsoleControlDefinition(String id, ConsoleInputType inputType, Vector3f position, float width, float height, String modelPart, float minimum, float maximum) {
        this(id, inputType, position, new Vector3f(), width, height, 0.125F, modelPart, ControlVisual.IDENTITY, minimum, maximum);
    }

    public ConsoleControlDefinition(String id, ConsoleInputType inputType, Vector3f position, Vector3f rotation, float width, float height, float depth, String modelPart, ControlVisual visual, float minimum, float maximum) {
        this.id = id;
        this.inputType = inputType;
        this.position = new Vector3f(position);
        this.rotation = new Vector3f(rotation);
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.modelPart = modelPart;
        this.visual = visual == null ? ControlVisual.IDENTITY : visual;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public ConsoleControlDefinition withPosition(Vector3f value) {
        return new ConsoleControlDefinition(id, inputType, value, rotation, width, height, depth, modelPart, visual, minimum, maximum);
    }

    public ConsoleControlDefinition withRotation(Vector3f value) {
        return new ConsoleControlDefinition(id, inputType, position, value, width, height, depth, modelPart, visual, minimum, maximum);
    }

    public ConsoleControlDefinition withSize(float newWidth, float newHeight, float newDepth) {
        return new ConsoleControlDefinition(id, inputType, position, rotation, newWidth, newHeight, newDepth, modelPart, visual, minimum, maximum);
    }

    public ConsoleControlDefinition withVisual(ControlVisual value) {
        return new ConsoleControlDefinition(id, inputType, position, rotation, width, height, depth, modelPart, value, minimum, maximum);
    }

    public ConsoleControlDefinition withRange(float newMinimum, float newMaximum) {
        return new ConsoleControlDefinition(id, inputType, position, rotation, width, height, depth, modelPart, visual, newMinimum, newMaximum);
    }

    public ControlVisualTransform visualTransform(float value) {
        return visual.transform(value, this);
    }
}
