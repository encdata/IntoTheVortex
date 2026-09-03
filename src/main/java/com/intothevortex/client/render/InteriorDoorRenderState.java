package com.intothevortex.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public final class InteriorDoorRenderState extends BlockEntityRenderState {
    public final TardisExteriorRenderState modelState = new TardisExteriorRenderState();
    public Direction facing = Direction.NORTH;
    public boolean powered;
}
