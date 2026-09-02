package com.intothevortex.client.render;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import java.util.UUID;

public final class TardisExteriorRenderState extends EntityRenderState {
    public float yaw;
    public boolean doorOpen;
    public float doorProgress;
    public UUID tardisId;
    public String exterior = "intothevortex:default";
    public String travelState = "LANDED";
    public float travelProgress;
    public float travelOpacity = 1.0F;
    public String travelAnimation = "intothevortex:default";
    public String doorAnimation = "intothevortex:door_swing";
    public float consoleThrottle;
    public float consoleHandbrake;
}
