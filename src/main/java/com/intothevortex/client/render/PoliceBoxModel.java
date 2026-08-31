package com.intothevortex.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class PoliceBoxModel extends EntityModel<TardisExteriorRenderState> {
    private final ModelPart leftDoor;
    private final ModelPart rightDoor;

    public PoliceBoxModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        ModelPart doors = root.getChild("TARDIS").getChild("Doors");
        leftDoor = doors.getChild("left_door");
        rightDoor = doors.getChild("right_door");
    }

    @Override
    public void setupAnim(TardisExteriorRenderState state) {
        super.setupAnim(state);
        if (state.doorOpen) {
            leftDoor.yRot = -1.5708F;
            rightDoor.yRot = 1.5708F;
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition tardis = root.addOrReplaceChild("TARDIS", CubeListBuilder.create().texOffs(0, 0).addBox(-19.0F, -4.0F, -19.0F, 38.0F, 4.0F, 38.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition posts = tardis.addOrReplaceChild("Posts", CubeListBuilder.create().texOffs(46, 223).addBox(-18.0F, -66.0F, -18.0F, 4.0F, 62.0F, 4.0F, CubeDeformation.NONE), PartPose.ZERO);
        posts.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(29, 198).addBox(-18.0F, -66.0F, -18.0F, 4.0F, 62.0F, 4.0F, CubeDeformation.NONE), PartPose.rotation(0.0F, 1.5708F, 0.0F));
        posts.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(210, 177).addBox(-18.0F, -66.0F, -18.0F, 4.0F, 62.0F, 4.0F, CubeDeformation.NONE), PartPose.rotation(0.0F, 3.1416F, 0.0F));
        posts.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(218, 41).addBox(-18.0F, -66.0F, -18.0F, 4.0F, 62.0F, 4.0F, CubeDeformation.NONE), PartPose.rotation(0.0F, -1.5708F, 0.0F));
        PartDefinition doors = tardis.addOrReplaceChild("Doors", CubeListBuilder.create(), PartPose.ZERO);
        doors.addOrReplaceChild("right_door", CubeListBuilder.create().texOffs(181, 177).addBox(0.5F, -29.5F, -0.5F, 13.0F, 55.0F, 1.0F, CubeDeformation.NONE).texOffs(0, 198).addBox(0.5F, -29.5F, -1.0F, 14.0F, 55.0F, 0.0F, CubeDeformation.NONE).texOffs(0, 10).addBox(9.5F, -9.5F, -1.5F, 1.0F, 2.0F, 1.0F, CubeDeformation.NONE).texOffs(5, 51).addBox(2.5F, -9.5F, -1.5F, 1.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.offset(-13.5F, -29.5F, -15.5F));
        doors.addOrReplaceChild("left_door", CubeListBuilder.create().texOffs(189, 41).addBox(-13.5F, -29.5F, -0.5F, 13.0F, 55.0F, 1.0F, CubeDeformation.NONE).texOffs(0, 0).addBox(-12.5F, -10.5F, -1.5F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE).texOffs(0, 51).addBox(-12.5F, -4.5F, -1.5F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE), PartPose.offset(13.5F, -29.5F, -15.5F));
        PartDefinition walls = tardis.addOrReplaceChild("Walls", CubeListBuilder.create().texOffs(129, 15).addBox(-16.0F, -60.0F, -14.0F, 1.0F, 56.0F, 28.0F, CubeDeformation.NONE).texOffs(59, 142).addBox(-16.5F, -60.0F, -14.0F, 0.0F, 56.0F, 28.0F, CubeDeformation.NONE).texOffs(63, 227).addBox(-14.0F, -60.0F, -16.0F, 1.0F, 56.0F, 1.0F, CubeDeformation.NONE).texOffs(116, 170).addBox(13.0F, -60.0F, -16.0F, 1.0F, 56.0F, 1.0F, CubeDeformation.NONE).texOffs(115, 0).addBox(-13.0F, -60.0F, -16.0F, 26.0F, 1.0F, 1.0F, CubeDeformation.NONE).texOffs(59, 113).addBox(13.0F, -60.0F, -16.5F, 1.0F, 56.0F, 0.0F, CubeDeformation.NONE).texOffs(115, 3).addBox(-13.0F, -60.0F, -16.5F, 26.0F, 1.0F, 0.0F, CubeDeformation.NONE).texOffs(62, 113).addBox(-14.0F, -60.0F, -16.5F, 1.0F, 56.0F, 0.0F, CubeDeformation.NONE), PartPose.ZERO);
        walls.addOrReplaceChild("Wall_r1", CubeListBuilder.create().texOffs(160, 72).addBox(-16.5F, -60.0F, -14.0F, 0.0F, 56.0F, 28.0F, CubeDeformation.NONE).texOffs(93, 85).addBox(-16.0F, -60.0F, -14.0F, 1.0F, 56.0F, 28.0F, CubeDeformation.NONE), PartPose.rotation(0.0F, 3.1416F, 0.0F));
        walls.addOrReplaceChild("Wall_r2", CubeListBuilder.create().texOffs(124, 142).addBox(-16.75F, -60.0F, -14.0F, 0.0F, 56.0F, 28.0F, CubeDeformation.NONE).texOffs(0, 113).addBox(-16.0F, -60.0F, -14.0F, 1.0F, 56.0F, 28.0F, CubeDeformation.NONE), PartPose.rotation(0.0F, 1.5708F, 0.0F));
        PartDefinition pcb = tardis.addOrReplaceChild("PCB", CubeListBuilder.create().texOffs(181, 167).addBox(-17.0F, -64.0F, -19.0F, 34.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -1.0F, 0.0F));
        pcb.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(153, 157).addBox(-17.0F, -61.0F, -19.0F, 34.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        pcb.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(160, 21).addBox(-17.0F, -61.0F, -19.0F, 34.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        pcb.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(160, 31).addBox(-17.0F, -61.0F, -19.0F, 34.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition roof = tardis.addOrReplaceChild("Roof", CubeListBuilder.create().texOffs(0, 43).addBox(-16.0F, -68.0F, -16.0F, 32.0F, 4.0F, 32.0F, CubeDeformation.NONE).texOffs(0, 43).addBox(-17.0F, -67.5F, -17.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(22, 7).addBox(-17.0F, -67.5F, 14.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(0, 30).addBox(14.0F, -67.5F, -17.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(17, 26).addBox(14.0F, -67.5F, 14.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(0, 80).addBox(-15.0F, -70.0F, -15.0F, 30.0F, 2.0F, 30.0F, CubeDeformation.NONE).texOffs(0, 0).addBox(-3.0F, -72.0F, -3.0F, 6.0F, 3.0F, 6.0F, CubeDeformation.NONE).texOffs(0, 10).addBox(-3.0F, -78.0F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -1.0F, 0.0F));
        roof.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(17, 18).addBox(-2.0F, -70.75F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.4F)).texOffs(0, 18).addBox(-2.0F, -73.75F, -2.0F, 4.0F, 7.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -5.25F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition alternate = tardis.addOrReplaceChild("TARDIS_t", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition alternatePcb = alternate.addOrReplaceChild("PCB_t", CubeListBuilder.create().texOffs(0, 394).addBox(-16.0F, -64.0F, -19.0F, 32.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -1.0F, 0.0F));
        alternatePcb.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 404).addBox(-16.0F, -61.0F, -19.0F, 32.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        alternatePcb.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 414).addBox(-16.0F, -61.0F, -19.0F, 32.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        alternatePcb.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 424).addBox(-16.0F, -61.0F, -19.0F, 32.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition alternateRoof = alternate.addOrReplaceChild("Roof_t", CubeListBuilder.create().texOffs(0, 294).addBox(-17.0F, -66.5F, -17.0F, 34.0F, 3.0F, 34.0F, CubeDeformation.NONE).texOffs(0, 332).addBox(-15.0F, -68.25F, -15.0F, 30.0F, 2.0F, 30.0F, CubeDeformation.NONE).texOffs(0, 365).addBox(-13.0F, -70.0F, -13.0F, 26.0F, 2.0F, 26.0F, CubeDeformation.NONE).texOffs(13, 65).addBox(-17.5F, -65.75F, -17.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(0, 58).addBox(14.5F, -65.75F, -17.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(13, 58).addBox(-17.5F, -65.75F, 14.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(0, 65).addBox(14.5F, -65.75F, 14.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.05F)).texOffs(0, 295).addBox(-3.0F, -71.0F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE).texOffs(0, 303).addBox(-2.0F, -72.0F, -2.0F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE).texOffs(0, 309).addBox(-2.0F, -78.0F, -2.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE).texOffs(9, 315).addBox(0.0F, -76.5F, -3.0F, 0.0F, 5.0F, 6.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -1.25F, 0.0F));
        alternateRoof.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(9, 316).addBox(-1.0F, -75.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)).texOffs(0, 321).addBox(-1.0F, -76.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.2F)).texOffs(0, 316).addBox(-1.0F, -79.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.rotation(0.0F, -0.7854F, 0.0F));
        alternateRoof.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(9, 315).addBox(0.0F, -76.5F, -3.0F, 0.0F, 5.0F, 6.0F, CubeDeformation.NONE), PartPose.rotation(0.0F, 1.5708F, 0.0F));
        return LayerDefinition.create(mesh, 512, 512);
    }
}
