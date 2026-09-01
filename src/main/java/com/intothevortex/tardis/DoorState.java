package com.intothevortex.tardis;

public enum DoorState {
    CLOSED,
    LEFT,
    RIGHT,
    BOTH;

    public boolean isOpen() {
        return this != CLOSED;
    }

    public DoorState next(boolean doubleDoor) {
        if (!doubleDoor) return isOpen() ? CLOSED : BOTH;
        return switch (this) {
            case CLOSED -> LEFT;
            case LEFT -> RIGHT;
            case RIGHT -> BOTH;
            case BOTH -> CLOSED;
        };
    }
}
