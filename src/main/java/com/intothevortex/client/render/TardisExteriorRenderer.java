package com.intothevortex.client.render;

import com.intothevortex.entity.TardisExteriorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

public final class TardisExteriorRenderer extends EntityRenderer<TardisExteriorEntity, TardisExteriorRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("intothevortex", "textures/entity/exterior/default.png");
    private final PoliceBoxModel model;

    public TardisExteriorRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new PoliceBoxModel(PoliceBoxModel.createBodyLayer().bakeRoot());
        shadowRadius = 0.8F;
    }

    @Override
    public TardisExteriorRenderState createRenderState() {
        return new TardisExteriorRenderState();
    }

    @Override
    public void extractRenderState(TardisExteriorEntity entity, TardisExteriorRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.yaw = entity.getYRot();
        state.doorOpen = entity.isDoorOpen();
    }

    @Override
    public void submit(TardisExteriorRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        super.submit(state, poseStack, collector, cameraState);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yaw));
        poseStack.scale(0.63F, -0.63F, 0.63F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        collector.submitModel(model, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
        poseStack.popPose();
    }
}
