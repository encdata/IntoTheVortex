package com.intothevortex.interior;

public interface ControlBehavior {
    InteractionResult onPress(ControlUseContext context);

    default InteractionResult onSecondaryPress(ControlUseContext context) {
        return InteractionResult.FAILED_INVALID_CONTROL_STATE;
    }

    default InteractionResult onPressDown(ControlUseContext context) {
        return InteractionResult.FAILED_INVALID_CONTROL_STATE;
    }

    default InteractionResult onRelease(ControlUseContext context) {
        return InteractionResult.FAILED_INVALID_CONTROL_STATE;
    }

    default InteractionResult onDrag(ControlUseContext context, float value) {
        return InteractionResult.FAILED_INVALID_CONTROL_STATE;
    }

    default InteractionResult validate(ControlUseContext context) {
        return InteractionResult.SUCCESS;
    }
}
