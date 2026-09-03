package com.intothevortex.client.render;

import com.intothevortex.exterior.ExteriorRegistry;
import com.intothevortex.interior.InteriorDoorBlock;
import com.intothevortex.interior.InteriorDoorBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.model.Model;
import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.Map;

public final class InteriorDoorRenderer implements BlockEntityRenderer<InteriorDoorBlockEntity, InteriorDoorRenderState> {
    private final Map<Identifier, Model<TardisExteriorRenderState>> models = new HashMap<>();

    @Override
    public InteriorDoorRenderState createRenderState() {
        return new InteriorDoorRenderState();
    }

    @Override
    public void extractRenderState(InteriorDoorBlockEntity blockEntity, InteriorDoorRenderState state, float tickDelta, net.minecraft.world.phys.Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickDelta, cameraPos, breakProgress);
        state.facing = blockEntity.getBlockState().getValue(InteriorDoorBlock.FACING);
        state.modelState.exterior = blockEntity.exterior();
        state.modelState.doorOpen = blockEntity.getBlockState().getValue(InteriorDoorBlock.OPEN);
        state.modelState.doorProgress = blockEntity.doorProgress();
        state.modelState.doorAnimation = blockEntity.doorAnimation();
        state.powered = blockEntity.powered();
        state.modelState.lightCoords = state.lightCoords;
        state.modelState.outlineColor = 0;
    }

    @Override
    public void submit(InteriorDoorRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        var definition = ExteriorRegistry.get(Identifier.parse(state.modelState.exterior));
        var model = models.computeIfAbsent(definition.interiorModel(), TardisModelRegistry::interior);
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.facing.toYRot()));
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        collector.submitModel(model, state.modelState, poseStack, definition.interiorTexture(), state.lightCoords, OverlayTexture.NO_OVERLAY, 0, (ModelFeatureRenderer.CrumblingOverlay) null);
        if (state.powered && definition.interiorEmission() != null) collector.submitModel(model, state.modelState, poseStack, definition.interiorEmission(), 15728880, OverlayTexture.NO_OVERLAY, 0, (ModelFeatureRenderer.CrumblingOverlay) null);
        poseStack.popPose();
    }
}
