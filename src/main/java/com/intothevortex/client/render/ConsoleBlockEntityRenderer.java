package com.intothevortex.client.render;

import com.intothevortex.interior.ConsoleDefinition;
import com.intothevortex.interior.ConsoleRegistry;
import com.intothevortex.interior.ConsoleBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class ConsoleBlockEntityRenderer implements BlockEntityRenderer<ConsoleBlockEntity, ConsoleRenderState> {
    private final Map<Identifier, net.minecraft.client.model.Model<TardisExteriorRenderState>> models = new HashMap<>();

    @Override
    public ConsoleRenderState createRenderState() {
        return new ConsoleRenderState();
    }

    @Override
    public void extractRenderState(ConsoleBlockEntity blockEntity, ConsoleRenderState state, float tickDelta, net.minecraft.world.phys.Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickDelta, cameraPos, breakProgress);
        state.console = blockEntity.console();
        state.powered = blockEntity.powered();
        state.facing = blockEntity.getBlockState().getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING);
        state.renderedThrottle = net.minecraft.util.Mth.lerp(0.5F, state.renderedThrottle, blockEntity.controlValue("throttle") / 4.0F);
        state.renderedHandbrake = net.minecraft.util.Mth.lerp(0.5F, state.renderedHandbrake, blockEntity.controlValue("handbrake"));
        state.modelState.consoleThrottle = state.renderedThrottle;
        state.modelState.consoleHandbrake = state.renderedHandbrake;
        state.modelState.lightCoords = state.lightCoords;
        state.modelState.outlineColor = 0;
    }

    @Override
    public void submit(ConsoleRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        ConsoleDefinition definition = ConsoleRegistry.get(Identifier.parse(state.console));
        var model = models.computeIfAbsent(definition.id(), id -> TardisModelRegistry.console(id));
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.translate(0.5F, -1.5F, -0.5F);
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(state.facing.toYRot()));
        collector.submitModel(model, state.modelState, poseStack, RenderTypes.entityCutout(definition.texture()), state.lightCoords, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF, null, 0, (ModelFeatureRenderer.CrumblingOverlay) null);
        if (state.powered && definition.emission() != null) collector.submitModel(model, state.modelState, poseStack, RenderTypes.entityTranslucentEmissive(definition.emission()), 15728880, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF, null, 0, (ModelFeatureRenderer.CrumblingOverlay) null);
        poseStack.popPose();
    }
}
