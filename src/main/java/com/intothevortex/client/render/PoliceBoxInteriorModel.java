// Made with Blockbench 5.1.5
// Exported for Minecraft 26.1.2 / Fabric / Mojang mappings

package com.intothevortex.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class PoliceBoxInteriorModel extends EntityModel<TardisExteriorRenderState> {
	private final ModelPart bone;
	private final ModelPart front;
	private final ModelPart doors;
	private final ModelPart rightdoor;
	private final ModelPart phone;
	private final ModelPart leftdoor;

	public PoliceBoxInteriorModel(ModelPart root) {
		super(root);
		this.bone = root.getChild("bone");
		this.front = this.bone.getChild("front");
		this.doors = this.front.getChild("doors");
		this.rightdoor = this.doors.getChild("rightdoor");
		this.phone = this.rightdoor.getChild("phone");
		this.leftdoor = this.doors.getChild("leftdoor");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition root = meshDefinition.getRoot();

		PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-1.3F, 26.0F, 6.0F));

		PartDefinition front = bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(62 * 2, 135 * 2).addBox(8.7187F, -33.0F, -13.2187F, 1.0F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(131 * 2, 136 * 2).addBox(-7.0813F, -34.0F, -13.2187F, 17.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68 * 2, 135 * 2).addBox(-6.2813F, -33.0F, -13.2187F, 1.0F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0 * 2, 112 * 2).addBox(-8.7349F, -36.9886F, -14.2651F, 21.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = front.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0 * 2, 124 * 2).addBox(-11.7651F, -38.6145F, -11.7651F, 3.0F, 36.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.0464F, 0.6145F, -1.9536F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r2 = front.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(84 * 2, 118 * 2).addBox(-11.7651F, -38.6145F, -11.7651F, 3.0F, 36.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9536F, 0.6145F, -1.9536F, 0.0F, -1.5708F, 0.0F));

		PartDefinition doors = front.addOrReplaceChild("doors", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightdoor = doors.addOrReplaceChild("rightdoor", CubeListBuilder.create().texOffs(126 * 2, 41 * 2).addBox(5.9687F, -30.0F, -0.2687F, 2.0F, 30.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(134 * 2, 73 * 2).addBox(-0.2813F, -30.0F, 0.0313F, 7.0F, 31.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(134 * 2, 41 * 2).addBox(-0.2813F, -30.0F, 0.2813F, 7.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80 * 2, 47 * 2).addBox(5.6687F, -19.7071F, -0.6758F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(80 * 2, 50 * 2).addBox(1.6687F, -19.7071F, -0.6758F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -3.0F, -13.0F));

		PartDefinition cube_r3 = rightdoor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(82 * 2, 50 * 2).addBox(-1.7187F, 2.9719F, -12.0429F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(82 * 2, 47 * 2).addBox(2.2813F, 2.9719F, -12.0429F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3874F, -7.6642F, 4.2961F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r4 = rightdoor.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(84 * 2, 49 * 2).addBox(-1.7187F, -12.3358F, -5.679F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(84 * 2, 46 * 2).addBox(2.2813F, -12.3358F, -5.679F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3874F, -23.3851F, 11.66F, 1.5708F, 0.0F, 0.0F));

		PartDefinition phone = rightdoor.addOrReplaceChild("phone", CubeListBuilder.create().texOffs(88 * 2, 33 * 2).addBox(-1.25F, -4.0F, -1.75F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(94 * 2, 80 * 2).addBox(-2.75F, -4.0F, -1.75F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56 * 2, 81 * 2).addBox(-3.0F, -4.0F, -2.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(91 * 2, 98 * 2).addBox(-3.5F, -4.75F, -1.75F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(1 * 2, 1 * 2).addBox(-1.9F, -4.0F, -2.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1 * 2, 1 * 2).addBox(-3.15F, -4.0F, -2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2 * 2, 7 * 2).addBox(-1.45F, -3.5F, -1.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2 * 2, 7 * 2).addBox(-1.45F, -1.5F, -1.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2 * 2, 2 * 2).addBox(-2.65F, -3.4F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(2 * 2, 2 * 2).addBox(-2.65F, -1.4F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.7187F, -16.25F, 3.0313F));

		PartDefinition cube_r5 = phone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(95 * 2, 77 * 2).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7929F, -0.7071F, -0.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r6 = phone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(76 * 2, 47 * 2).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5429F, -3.5F, -1.2071F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r7 = phone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(72 * 2, 47 * 2).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0429F, -3.5F, -1.2071F, 0.0F, 0.7854F, 0.0F));

		PartDefinition leftdoor = doors.addOrReplaceChild("leftdoor", CubeListBuilder.create().texOffs(132 * 2, 104 * 2).addBox(-7.2813F, -30.0F, -0.7187F, 7.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36 * 2, 135 * 2).addBox(-7.2813F, -30.0F, -0.9687F, 7.0F, 31.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(86 * 2, 46 * 2).addBox(-6.8313F, -19.5071F, -1.6758F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -3.0F, -12.0F));

		PartDefinition cube_r8 = leftdoor.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(86 * 2, 48 * 2).addBox(-1.7187F, 3.9719F, -12.0429F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1126F, -7.2587F, 2.766F, -1.6144F, 0.0F, 0.0F));

		PartDefinition cube_r9 = leftdoor.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(84 * 2, 48 * 2).addBox(-1.7187F, -11.3358F, -5.679F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1126F, -23.1851F, 9.66F, 1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(
meshDefinition,
256 * 2,
256 * 2
		);
	}

	@Override
	public void setupAnim(TardisExteriorRenderState state) {
		super.setupAnim(state);
	}
}

