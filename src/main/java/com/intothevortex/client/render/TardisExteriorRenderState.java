package com.intothevortex.client.render;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import java.util.UUID;

public final class TardisExteriorRenderState extends EntityRenderState {
    public float yaw;
    public boolean doorOpen;
    public float doorProgress;
    public UUID tardisId;
    public String exterior = "intothevortex:default";
}
