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

public final class PoliceBoxModel extends EntityModel<TardisExteriorRenderState> {
	private final ModelPart bone;
	private final ModelPart sides;
	private final ModelPart roof;
	private final ModelPart lantern;
	private final ModelPart PCB;
	private final ModelPart base;
	private final ModelPart posts;
	private final ModelPart front;
	private final ModelPart doors;
	private final ModelPart rightdoor;
	private final ModelPart phone;
	private final ModelPart leftdoor;

	public PoliceBoxModel(ModelPart root) {
		super(root, net.minecraft.client.renderer.rendertype.RenderTypes::entityTranslucent);
		this.bone = root.getChild("bone");
		this.sides = this.bone.getChild("sides");
		this.roof = this.bone.getChild("roof");
		this.lantern = this.roof.getChild("lantern");
		this.PCB = this.roof.getChild("PCB");
		this.base = this.bone.getChild("base");
		this.posts = this.bone.getChild("posts");
		this.front = this.bone.getChild("front");
		this.doors = this.front.getChild("doors");
		this.rightdoor = this.doors.getChild("rightdoor");
		this.phone = this.rightdoor.getChild("phone");
		this.leftdoor = this.doors.getChild("leftdoor");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition root = meshDefinition.getRoot();

		PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-1.3F, 24.0F, 3.0F));

		PartDefinition sides = bone.addOrReplaceChild("sides", CubeListBuilder.create().texOffs(74, 135).addBox(-10.0813F, -33.0F, 4.2813F, 2.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 118).addBox(-10.0813F, -34.0F, -10.7187F, 2.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(64, 49).addBox(-9.5813F, -33.0F, -9.7187F, 1.0F, 31.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 67).addBox(10.4687F, -33.0F, -9.7187F, 0.001F, 31.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(94, 27).addBox(-10.1313F, -33.0F, -9.7187F, 0.001F, 31.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(28, 67).addBox(10.6687F, -33.0F, -9.7187F, 0.001F, 31.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(96, 138).addBox(-10.0813F, -33.0F, -10.7187F, 2.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(102, 138).addBox(8.9187F, -33.0F, -10.7187F, 2.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 24).addBox(8.9187F, -34.0F, -10.7187F, 2.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(108, 138).addBox(8.9187F, -33.0F, 4.2813F, 2.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 135).addBox(10.6687F, -33.0F, -3.7187F, 0.001F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(114, 138).addBox(-10.0813F, -33.0F, -3.7187F, 0.001F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(96, 135).addBox(-7.5813F, -34.0F, 5.7813F, 16.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 135).addBox(7.4187F, -33.0F, 5.7813F, 1.0F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(104, 104).addBox(-6.5813F, -33.0F, 7.7813F, 14.0F, 31.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(104, 72).addBox(-6.5813F, -33.0F, 6.5313F, 14.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 163).addBox(0.45F, -15.0F, 7.0F, 0.001F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(126, 158).addBox(0.45F, -21.0F, 7.0F, 0.001F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(126, 153).addBox(0.45F, -27.0F, 7.0F, 0.001F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(126, 148).addBox(0.45F, -33.0F, 7.0F, 0.001F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(56, 135).addBox(-7.5813F, -33.0F, 5.7813F, 1.0F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(122, 41).addBox(-0.5813F, -33.0F, 7.7813F, 2.0F, 31.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(126, 168).addBox(0.45F, -9.0F, 7.0F, 0.001F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3F, 0.0F, 0.0F));

		PartDefinition roof = bone.addOrReplaceChild("roof", CubeListBuilder.create().texOffs(0, 49).addBox(-6.2813F, -4.5F, -10.8187F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(-7.2813F, -3.5F, -11.8187F, 18.0F, 4.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -37.0F, 0.1F));

		PartDefinition lantern = roof.addOrReplaceChild("lantern", CubeListBuilder.create().texOffs(72, 27).addBox(-13.5813F, -42.5F, 23.2813F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(72, 43).addBox(-12.5813F, -47.749F, 24.2813F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(72, 33).addBox(-13.0813F, -47.5F, 23.7813F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(12.8F, 37.0F, -28.6F));

		PartDefinition cube_r1 = lantern.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(72, 38).addBox(0.0F, -5.0F, -3.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.2026F, -40.5F, 25.7958F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = lantern.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(56, 75).addBox(0.0F, -6.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.8026F, -40.5F, 25.0742F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r3 = lantern.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(56, 86).addBox(-3.0F, -6.0F, -2.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(56, 67).addBox(-1.0F, -6.0F, -4.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.7884F, -40.5F, 27.9026F, 0.0F, -0.7854F, 0.0F));

		PartDefinition PCB = roof.addOrReplaceChild("PCB", CubeListBuilder.create().texOffs(56, 94).addBox(-9.7349F, -40.535F, -13.2651F, 3.0F, 3.0F, 21.0F, new CubeDeformation(0.0F))
		.texOffs(96, 0).addBox(10.2651F, -40.535F, -13.2651F, 3.0F, 3.0F, 21.0F, new CubeDeformation(0.0F))
		.texOffs(0, 118).addBox(-8.7349F, -40.535F, 5.7349F, 21.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 40.5464F, -0.1F));

		PartDefinition base = bone.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-10.2813F, -3.0F, -14.7187F, 24.0F, 3.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition posts = bone.addOrReplaceChild("posts", CubeListBuilder.create(), PartPose.offset(-23.0F, 1.0F, -2.0F));

		PartDefinition cube_r4 = posts.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 124).addBox(-11.7651F, -38.6145F, -11.7651F, 3.0F, 36.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.9536F, -0.3855F, 19.0464F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r5 = posts.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(94, 72).addBox(-10.7651F, -5.6145F, -10.7651F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9536F, -35.3855F, 18.0464F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r6 = posts.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(56, 90).addBox(-10.7651F, -5.6145F, -10.7651F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.9536F, -35.3855F, 18.0464F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r7 = posts.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(12, 124).addBox(-11.7651F, -38.6145F, -11.7651F, 3.0F, 36.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.9536F, -0.3855F, 19.0464F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r8 = posts.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(84, 42).addBox(-10.7651F, -5.6145F, -10.7651F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9536F, -35.3855F, 0.0464F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r9 = posts.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(84, 38).addBox(-10.7651F, -5.6145F, -10.7651F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.9536F, -35.3855F, 0.0464F, 0.0F, -1.5708F, 0.0F));

		PartDefinition front = bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(62, 135).addBox(8.7187F, -33.0F, -13.2187F, 1.0F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(131, 136).addBox(-7.0813F, -34.0F, -13.2187F, 17.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68, 135).addBox(-6.2813F, -33.0F, -13.2187F, 1.0F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 112).addBox(-8.7349F, -36.9886F, -14.2651F, 21.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r10 = front.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 124).addBox(-11.7651F, -38.6145F, -11.7651F, 3.0F, 36.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.0464F, 0.6145F, -1.9536F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r11 = front.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(84, 118).addBox(-11.7651F, -38.6145F, -11.7651F, 3.0F, 36.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9536F, 0.6145F, -1.9536F, 0.0F, -1.5708F, 0.0F));

		PartDefinition doors = front.addOrReplaceChild("doors", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightdoor = doors.addOrReplaceChild("rightdoor", CubeListBuilder.create().texOffs(126, 41).addBox(5.9687F, -30.0F, -0.2687F, 2.0F, 30.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(134, 73).addBox(-0.2813F, -30.0F, 0.0313F, 7.0F, 31.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(134, 41).addBox(-0.2813F, -30.0F, 0.2813F, 7.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 47).addBox(5.6687F, -19.7071F, -0.6758F, 1.0F, 2.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(80, 50).addBox(1.6687F, -19.7071F, -0.6758F, 1.0F, 2.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -3.0F, -13.0F));

		PartDefinition cube_r12 = rightdoor.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(82, 50).addBox(-1.7187F, 2.9719F, -12.043F, 1.0F, 2.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(82, 47).addBox(2.2813F, 2.9719F, -12.043F, 1.0F, 2.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3874F, -7.6642F, 4.2961F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r13 = rightdoor.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(84, 49).addBox(-1.7187F, -12.3358F, -5.679F, 1.0F, 2.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(84, 46).addBox(2.2813F, -12.3358F, -5.679F, 1.0F, 2.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3874F, -23.3851F, 11.66F, 1.5708F, 0.0F, 0.0F));

		PartDefinition phone = rightdoor.addOrReplaceChild("phone", CubeListBuilder.create().texOffs(88, 33).addBox(-1.25F, -4.0F, -1.75F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(94, 80).addBox(-2.75F, -4.0F, -1.75F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 81).addBox(-3.0F, -4.0F, -2.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(91, 98).addBox(-3.5F, -4.75F, -1.75F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.9F, -4.0F, -2.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-3.15F, -4.0F, -2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 7).addBox(-1.45F, -3.5F, -1.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 7).addBox(-1.45F, -1.5F, -1.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 2).addBox(-2.65F, -3.4F, -1.0F, 1.0F, 1.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(2, 2).addBox(-2.65F, -1.4F, -1.0F, 1.0F, 1.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offset(4.7187F, -16.25F, 3.0313F));

		PartDefinition cube_r14 = phone.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(95, 77).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7929F, -0.7071F, -0.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r15 = phone.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(76, 47).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5429F, -3.5F, -1.2071F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r16 = phone.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(72, 47).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0429F, -3.5F, -1.2071F, 0.0F, 0.7854F, 0.0F));

		PartDefinition leftdoor = doors.addOrReplaceChild("leftdoor", CubeListBuilder.create().texOffs(132, 104).addBox(-7.2813F, -30.0F, -0.7187F, 7.0F, 31.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 135).addBox(-7.2813F, -30.0F, -0.9687F, 7.0F, 31.0F, 0.001F, new CubeDeformation(0.0F))
		.texOffs(86, 46).addBox(-6.8313F, -19.5071F, -1.6758F, 1.0F, 2.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -3.0F, -12.0F));

		PartDefinition cube_r17 = leftdoor.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(86, 48).addBox(-1.7187F, 3.9719F, -12.043F, 1.0F, 1.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1126F, -7.2587F, 2.766F, -1.6144F, 0.0F, 0.0F));

		PartDefinition cube_r18 = leftdoor.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(84, 48).addBox(-1.7187F, -11.3358F, -5.679F, 1.0F, 1.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1126F, -23.1851F, 9.66F, 1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(
meshDefinition,
256,
256
		);
	}

	@Override
	public void setupAnim(TardisExteriorRenderState state) {
		super.setupAnim(state);
		com.intothevortex.exterior.AnimationDefinition animation = com.intothevortex.exterior.TardisAnimationManager.getDoor(net.minecraft.resources.Identifier.parse(state.doorAnimation));
		leftdoor.yRot = animation.left(state.doorProgress);
		rightdoor.yRot = animation.right(state.doorProgress);
	}
}
