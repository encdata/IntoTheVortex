package com.intothevortex.client.render;

import com.intothevortex.entity.TardisExteriorEntity;
import com.intothevortex.exterior.ExteriorRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.Map;

public final class TardisExteriorRenderer extends EntityRenderer<TardisExteriorEntity, TardisExteriorRenderState> {
    private final Map<Identifier, Model<TardisExteriorRenderState>> models = new HashMap<>();

    public TardisExteriorRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.8F;
    }

    @Override
    public TardisExteriorRenderState createRenderState() {
        return new TardisExteriorRenderState();
    }

    @Override
    public void extractRenderState(TardisExteriorEntity entity, TardisExteriorRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.yaw = entity.getYRot(tickDelta);
        state.doorOpen = entity.isDoorOpen();
        state.doorProgress = entity.getDoorProgress();
        state.tardisId = entity.getTardisId();
        state.exterior = entity.getExterior();
    }

    @Override
    public void submit(TardisExteriorRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        super.submit(state, poseStack, collector, cameraState);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yaw));
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        var definition = ExteriorRegistry.get(Identifier.parse(state.exterior));
        var model = models.computeIfAbsent(definition.model(), TardisModelRegistry::exterior);
        collector.submitModel(model, state, poseStack, definition.texture(), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
        if (definition.emission() != null) {
            collector.submitModel(model, state, poseStack, definition.emission(), 15728880, OverlayTexture.NO_OVERLAY, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
        }
        poseStack.popPose();
    }
}
