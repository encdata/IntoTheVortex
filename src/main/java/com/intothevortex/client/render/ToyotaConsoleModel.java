package com.intothevortex.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class ToyotaConsoleModel extends EntityModel<TardisExteriorRenderState> {
    private final ModelPart toyota;

    public ToyotaConsoleModel(ModelPart root) { super(root, RenderTypes::entityCutout); this.toyota = root.getChild("toyota"); }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        PartDefinition toyota = root.addOrReplaceChild("toyota", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition panel1 = toyota.addOrReplaceChild("panel1", CubeListBuilder.create().texOffs(107, 185).addBox(-14.0F,
                -14.9306F, -25.1225F, 28.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition cube_r1 = panel1.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(62, 184).addBox(-1.0F, 0.9F, -1.1F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(9.0F, -17.0F, -23.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r2 = panel1.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(186, 16).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(8.0F, -17.5609F, -17.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r3 = panel1
                .addOrReplaceChild("cube_r3",
                        CubeListBuilder.create().texOffs(149, 156).addBox(-1.0F, 5.5F, -28.0F, 2.0F, 2.0F, 22.0F,
                                new CubeDeformation(-0.001F)),
                        PartPose.offsetAndRotation(0.0F, -11.9609F, 0.0F, -0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r4 = panel1.addOrReplaceChild("cube_r4",
                CubeListBuilder.create().texOffs(25, 180).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-14.0582F, -13.4805F, -24.3496F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r5 = panel1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 181).addBox(-1.0F, -7.5F,
                -28.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r6 = panel1
                .addOrReplaceChild(
                        "cube_r6", CubeListBuilder.create().texOffs(108, 83).addBox(-14.0F, -4.0F, -1.0F, 28.0F, 18.0F,
                                0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -11.8613F, -21.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r7 = panel1
                .addOrReplaceChild(
                        "cube_r7", CubeListBuilder.create().texOffs(108, 102).addBox(-14.0F, -14.0F, -1.0F, 28.0F, 18.0F,
                                0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -15.0F, -21.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition controls = panel1.addOrReplaceChild("controls", CubeListBuilder.create(),
                PartPose.offset(0.0F, -11.9609F, 0.0F));

        PartDefinition cube_r8 = controls.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(77, 184).addBox(-1.0F, -0.5F,
                -1.75F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -6.6222F, -11.8592F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r9 = controls
                .addOrReplaceChild("cube_r9",
                        CubeListBuilder.create().texOffs(27, 138).addBox(-7.0F, 0.35F, -1.0F, 8.0F, 0.0F, 2.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(3.0F, -3.9015F, -23.3038F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r10 = controls.addOrReplaceChild("cube_r10",
                CubeListBuilder.create().texOffs(7, 194).addBox(5.0F, -11.25F, -21.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(142, 194).addBox(-7.0F, -11.25F, -21.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.5F, -0.1F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition powerlights = controls.addOrReplaceChild("powerlights", CubeListBuilder.create(),
                PartPose.offset(-7.0F, -3.975F, -20.5665F));

        PartDefinition cube_r11 = powerlights.addOrReplaceChild("cube_r11",
                CubeListBuilder.create().texOffs(199, 128)
                        .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(50, 201)
                        .addBox(-2.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition powerlights2 = powerlights.addOrReplaceChild("powerlights2", CubeListBuilder.create(),
                PartPose.offset(1.0F, 0.9478F, -0.014F));

        PartDefinition rightlight1 = powerlights2.addOrReplaceChild("rightlight1", CubeListBuilder.create(),
                PartPose.offset(13.0F, 0.0522F, 0.014F));

        PartDefinition cube_r12 = rightlight1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(199, 103).addBox(-0.5F,
                -1.0541F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition leftlight1 = powerlights2.addOrReplaceChild("leftlight1", CubeListBuilder.create(),
                PartPose.offset(13.0F, 0.0522F, 0.014F));

        PartDefinition cube_r13 = leftlight1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 196).addBox(-2.5F,
                -1.0541F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition dooropenlights = controls.addOrReplaceChild("dooropenlights", CubeListBuilder.create(),
                PartPose.offset(-7.0F, -3.975F, -20.5665F));

        PartDefinition cube_r14 = dooropenlights.addOrReplaceChild("cube_r14",
                CubeListBuilder.create().texOffs(0, 204).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F))
                        .texOffs(203, 175).addBox(-2.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition doorlights2 = dooropenlights.addOrReplaceChild("doorlights2", CubeListBuilder.create(),
                PartPose.offset(1.0F, 0.9478F, -0.014F));

        PartDefinition cube_r15 = doorlights2.addOrReplaceChild("cube_r15",
                CubeListBuilder.create().texOffs(55, 201)
                        .addBox(-2.5F, -1.0541F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(60, 201)
                        .addBox(-0.5F, -1.0541F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(1.0F, 0.0522F, 0.014F, 0.2618F, 0.0F, 0.0F));

        PartDefinition dooropen = controls.addOrReplaceChild("dooropen", CubeListBuilder.create(),
                PartPose.offset(-5.5F, -4.8F, -22.0F));

        PartDefinition cube_r16 = dooropen.addOrReplaceChild("cube_r16",
                CubeListBuilder.create().texOffs(142, 178)
                        .addBox(-6.5F, -10.75F, -20.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(193, 173)
                        .addBox(-4.75F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(186, 193)
                        .addBox(-6.25F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(181, 195)
                        .addBox(-6.25F, -10.75F, -21.5F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(192, 110)
                        .addBox(-6.0F, -10.75F, -24.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, 4.7F, 22.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition power = controls.addOrReplaceChild("power", CubeListBuilder.create(),
                PartPose.offset(5.5F, -4.8F, -22.0F));

        PartDefinition cube_r17 = power.addOrReplaceChild("cube_r17",
                CubeListBuilder.create().texOffs(180, 36)
                        .addBox(4.5F, -10.75F, -20.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(182, 127)
                        .addBox(4.75F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(97, 187)
                        .addBox(6.25F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(167, 195)
                        .addBox(4.25F, -10.75F, -21.5F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(192, 90)
                        .addBox(5.0F, -10.75F, -24.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, 4.7F, 22.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition geigercounter = controls.addOrReplaceChild("geigercounter", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-7.0F, -4.0F, -19.0F, 0.3365F, 0.6699F, 0.2139F));

        PartDefinition cube_r18 = geigercounter.addOrReplaceChild("cube_r18",
                CubeListBuilder.create().texOffs(107, 189)
                        .addBox(-1.5F, -4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.4F)).texOffs(120, 189)
                        .addBox(-1.5F, -4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.2F)).texOffs(62, 191)
                        .addBox(-1.5F, -4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)),
                PartPose.offsetAndRotation(0.0F, 2.2109F, 0.75F, 0.0F, 0.0873F, 0.0F));

        PartDefinition needle = geigercounter
                .addOrReplaceChild(
                        "needle", CubeListBuilder.create().texOffs(164, 181).addBox(-1.7401F, -0.15F, -0.735F, 2.0F, 0.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.5F, -1.2891F, 0.25F, 0.0F, 0.8727F, 0.0F));

        PartDefinition lockernob1 = controls
                .addOrReplaceChild("lockernob1",
                        CubeListBuilder.create().texOffs(207, 112).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-3.0F, -3.9015F, -23.3038F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lockernob2 = controls.addOrReplaceChild("lockernob2",
                CubeListBuilder.create().texOffs(207, 91).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, -3.9015F, -23.3038F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lockernob3 = controls.addOrReplaceChild("lockernob3",
                CubeListBuilder.create().texOffs(207, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -3.9015F, -23.3038F, 0.2793F, 0.0F, 0.0F));

        PartDefinition lockernob4 = controls.addOrReplaceChild("lockernob4",
                CubeListBuilder.create().texOffs(207, 75).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, -3.9015F, -23.3038F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lockernob5 = controls.addOrReplaceChild("lockernob5",
                CubeListBuilder.create().texOffs(207, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, -3.9015F, -23.3038F, 0.2618F, 0.0F, 0.0F));

        PartDefinition faucettaps1 = controls.addOrReplaceChild("faucettaps1", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-3.225F, -6.2617F, -13.3693F, 0.2618F, 0.0F, 0.0F));

        PartDefinition pivot2 = faucettaps1.addOrReplaceChild("pivot2",
                CubeListBuilder.create().texOffs(128, 178)
                        .addBox(-1.475F, -0.7975F, 0.0637F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 198)
                        .addBox(-0.525F, -0.556F, -0.4513F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition faucettaps2 = controls.addOrReplaceChild("faucettaps2",
                CubeListBuilder.create().texOffs(135, 178)
                        .addBox(-0.475F, -0.7975F, 0.0637F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(201, 152)
                        .addBox(-0.525F, -0.556F, -0.4513F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(3.275F, -6.2617F, -13.3693F, 0.2618F, 0.0F, 0.0F));

        PartDefinition redknob = controls.addOrReplaceChild("redknob",
                CubeListBuilder.create().texOffs(207, 62).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -4.0309F, -20.8209F, 0.2618F, 0.0F, 0.0F));

        PartDefinition largefaucettap = controls
                .addOrReplaceChild(
                        "largefaucettap", CubeListBuilder.create().texOffs(15, 204).addBox(0.25F, -0.7823F, -2.1733F, 1.0F,
                                2.0F, 1.0F, new CubeDeformation(-0.2F)),
                        PartPose.offsetAndRotation(-3.25F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

        PartDefinition largefaucettaplever = largefaucettap.addOrReplaceChild("largefaucettaplever",
                CubeListBuilder.create().texOffs(207, 59)
                        .addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(40, 207)
                        .addBox(-0.5F, -0.5F, -1.15F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(25, 198)
                        .addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(195, 158)
                        .addBox(-0.5F, -0.5F, -2.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(13, 198)
                        .addBox(-0.5F, -0.5F, -2.35F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.75F, -0.8823F, -1.6233F));

        PartDefinition largefaucettap2 = controls
                .addOrReplaceChild(
                        "largefaucettap2", CubeListBuilder.create().texOffs(90, 201).addBox(0.25F, -0.7823F, -2.1733F, 1.0F,
                                2.0F, 1.0F, new CubeDeformation(-0.2F)),
                        PartPose.offsetAndRotation(-2.25F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

        PartDefinition largefaucettaplever2 = largefaucettap2.addOrReplaceChild("largefaucettaplever2",
                CubeListBuilder.create().texOffs(205, 41)
                        .addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(205, 38)
                        .addBox(-0.5F, -0.5F, -1.15F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(122, 195)
                        .addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(117, 195)
                        .addBox(-0.5F, -0.5F, -2.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(112, 195)
                        .addBox(-0.5F, -0.5F, -2.35F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.75F, -0.8823F, -1.6233F));

        PartDefinition largefaucettap3 = controls
                .addOrReplaceChild(
                        "largefaucettap3", CubeListBuilder.create().texOffs(85, 201).addBox(0.25F, -0.7823F, -2.1733F, 1.0F,
                                2.0F, 1.0F, new CubeDeformation(-0.2F)),
                        PartPose.offsetAndRotation(-1.25F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

        PartDefinition largefaucettaplever3 = largefaucettap3.addOrReplaceChild("largefaucettaplever3",
                CubeListBuilder.create().texOffs(205, 31)
                        .addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(205, 28)
                        .addBox(-0.5F, -0.5F, -1.15F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(107, 195)
                        .addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(72, 191)
                        .addBox(-0.5F, -0.5F, -2.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(152, 189)
                        .addBox(-0.5F, -0.5F, -2.35F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.75F, -0.8823F, -1.6233F));

        PartDefinition largefaucettap4 = controls
                .addOrReplaceChild(
                        "largefaucettap4", CubeListBuilder.create().texOffs(80, 201).addBox(0.25F, -0.7823F, -2.1733F, 1.0F,
                                2.0F, 1.0F, new CubeDeformation(-0.2F)),
                        PartPose.offsetAndRotation(-0.25F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

        PartDefinition largefaucettaplever4 = largefaucettap4.addOrReplaceChild("largefaucettaplever4",
                CubeListBuilder.create().texOffs(205, 25)
                        .addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(204, 127)
                        .addBox(-0.5F, -0.5F, -1.15F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(130, 189)
                        .addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(117, 189)
                        .addBox(-0.5F, -0.5F, -2.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(189, 13)
                        .addBox(-0.5F, -0.5F, -2.35F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.75F, -0.8823F, -1.6233F));

        PartDefinition largefaucettap5 = controls
                .addOrReplaceChild(
                        "largefaucettap5", CubeListBuilder.create().texOffs(75, 201).addBox(0.25F, -0.7823F, -2.1733F, 1.0F,
                                2.0F, 1.0F, new CubeDeformation(-0.2F)),
                        PartPose.offsetAndRotation(0.75F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

        PartDefinition largefaucettaplever5 = largefaucettap5.addOrReplaceChild("largefaucettaplever5",
                CubeListBuilder.create().texOffs(204, 102)
                        .addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(40, 204)
                        .addBox(-0.5F, -0.5F, -1.15F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(185, 188)
                        .addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(171, 188)
                        .addBox(-0.5F, -0.5F, -2.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(185, 175)
                        .addBox(-0.5F, -0.5F, -2.35F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.75F, -0.8823F, -1.6233F));

        PartDefinition largefaucettap6 = controls
                .addOrReplaceChild(
                        "largefaucettap6", CubeListBuilder.create().texOffs(65, 201).addBox(0.25F, -0.7823F, -2.1733F, 1.0F,
                                2.0F, 1.0F, new CubeDeformation(-0.2F)),
                        PartPose.offsetAndRotation(1.75F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

        PartDefinition largefaucettaplever6 = largefaucettap6.addOrReplaceChild("largefaucettaplever6",
                CubeListBuilder.create().texOffs(35, 204)
                        .addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(30, 204)
                        .addBox(-0.5F, -0.5F, -1.15F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(184, 135)
                        .addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(184, 13)
                        .addBox(-0.5F, -0.5F, -2.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(171, 181)
                        .addBox(-0.5F, -0.5F, -2.35F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.75F, -0.8823F, -1.6233F));

        PartDefinition smalllockernob = controls.addOrReplaceChild("smalllockernob", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-5.5F, -5.7927F, -14.7259F, 0.2618F, 0.0F, 0.0F));

        PartDefinition pivot3 = smalllockernob.addOrReplaceChild("pivot3",
                CubeListBuilder.create().texOffs(5, 204).addBox(-0.5F, -0.375F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(207, 56).addBox(-0.5F, -1.125F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition smallswitch = controls.addOrReplaceChild("smallswitch",
                CubeListBuilder.create().texOffs(172, 142)
                        .addBox(0.0F, -1.6136F, -0.5033F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.001F)).texOffs(91, 153)
                        .addBox(-1.0F, -2.4136F, -1.0033F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(6.5F, -5.4356F, -16.9125F, 0.2618F, 0.0F, 0.0F));

        PartDefinition tinylever = controls.addOrReplaceChild("tinylever", CubeListBuilder.create(),
                PartPose.offset(9.25F, -4.1268F, -22.8931F));

        PartDefinition cube_r19 = tinylever.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(165, 85).addBox(-0.5F,
                -1.5088F, 0.0328F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition tinytinyswitch = controls.addOrReplaceChild("tinytinyswitch", CubeListBuilder.create().texOffs(69, 178)
                .addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-3.5F, -4.0F, -21.25F));

        PartDefinition tinytinyswitch2 = controls.addOrReplaceChild("tinytinyswitch2",
                CubeListBuilder.create().texOffs(178, 6).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-2.5F, -4.0F, -21.25F));

        PartDefinition tinytinyswitch3 = controls.addOrReplaceChild("tinytinyswitch3",
                CubeListBuilder.create().texOffs(178, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-1.5F, -4.0F, -21.25F));

        PartDefinition tinytinyswitch4 = controls.addOrReplaceChild("tinytinyswitch4", CubeListBuilder.create().texOffs(175, 65)
                .addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(1.5F, -4.0F, -21.25F));

        PartDefinition tinytinyswitch5 = controls.addOrReplaceChild("tinytinyswitch5", CubeListBuilder.create().texOffs(172, 110)
                .addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(2.5F, -4.0F, -21.25F));

        PartDefinition tinytinyswitch6 = controls.addOrReplaceChild("tinytinyswitch6", CubeListBuilder.create().texOffs(73, 157)
                .addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(3.5F, -4.0F, -21.25F));

        PartDefinition panel1lights = panel1.addOrReplaceChild("panel1lights", CubeListBuilder.create(),
                PartPose.offset(0.0F, -15.9359F, -20.5665F));

        PartDefinition panel2 = toyota
                .addOrReplaceChild(
                        "panel2", CubeListBuilder.create().texOffs(164, 184).addBox(-14.0F, -14.9306F, -25.1225F, 28.0F,
                                3.0F, 0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r20 = panel2
                .addOrReplaceChild("cube_r20",
                        CubeListBuilder.create().texOffs(175, 0).addBox(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F,
                                new CubeDeformation(-0.001F)),
                        PartPose.offsetAndRotation(0.0F, 2.0391F, 0.0F, -0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r21 = panel2
                .addOrReplaceChild("cube_r21",
                        CubeListBuilder.create().texOffs(40, 180).addBox(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-14.0582F, 0.5195F, -24.3496F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r22 = panel2
                .addOrReplaceChild(
                        "cube_r22", CubeListBuilder.create().texOffs(180, 25).addBox(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F,
                                20.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r23 = panel2
                .addOrReplaceChild(
                        "cube_r23", CubeListBuilder.create().texOffs(57, 0).addBox(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F,
                                0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, 2.1387F, -21.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r24 = panel2.addOrReplaceChild("cube_r24",
                CubeListBuilder.create().texOffs(104, 57)
                        .addBox(-7.0F, -0.1235F, -14.523F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(0, 135)
                        .addBox(-7.0F, -14.1235F, -14.523F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(0, 138)
                        .addBox(-7.0F, -0.1235F, -14.523F, 14.0F, 0.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(51, 76)
                        .addBox(7.0F, -14.1235F, -14.523F, 0.0F, 14.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(51, 93)
                        .addBox(-7.0F, -14.1235F, -14.523F, 0.0F, 14.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(57, 142)
                        .addBox(-7.0F, -14.1235F, -12.523F, 14.0F, 14.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(0, 161)
                        .addBox(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -21.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition controls3 = panel2.addOrReplaceChild("controls3", CubeListBuilder.create(),
                PartPose.offset(1.25F, -4.5F, -8.0F));

        PartDefinition gears = controls3.addOrReplaceChild("gears", CubeListBuilder.create(),
                PartPose.offsetAndRotation(1.25F, -8.75F, -14.5F, 2.618F, 0.0F, 3.1416F));

        PartDefinition largegear1 = gears.addOrReplaceChild("largegear1", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.5F, -0.9F, -1.75F, -1.309F, 0.0F, 0.0F));

        PartDefinition bone7 = largegear1.addOrReplaceChild("bone7",
                CubeListBuilder.create().texOffs(207, 0).addBox(-0.5F, 0.7F, -1.4F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                        .texOffs(175, 85).addBox(-0.5F, 0.7415F, -0.6353F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.001F))
                        .texOffs(75, 194).addBox(1.5F, 0.7074F, -0.6765F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-2.0F, -1.2683F, -1.3528F));

        PartDefinition bone8 = bone7.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(192, 69).addBox(-2.0F, -3.0F, 0.9F,
                4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offset(2.0F, 2.2774F, 0.396F));

        PartDefinition largegear2 = gears.addOrReplaceChild("largegear2", CubeListBuilder.create(),
                PartPose.offsetAndRotation(6.0F, 0.1F, -5.15F, -1.309F, 0.0F, 0.0F));

        PartDefinition bone6 = largegear2.addOrReplaceChild("bone6",
                CubeListBuilder.create().texOffs(207, 0).addBox(-0.5F, 0.7F, -1.4F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                        .texOffs(175, 85).addBox(-0.5F, 0.7415F, -0.6353F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.001F))
                        .texOffs(75, 194).addBox(1.5F, 0.7074F, -0.6765F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-2.0F, -1.2683F, -1.3528F));

        PartDefinition bone9 = bone6.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(192, 69).addBox(-2.0F, -3.0F, 0.9F,
                4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offset(2.0F, 2.2774F, 0.396F));

        PartDefinition largegear3 = gears.addOrReplaceChild("largegear3", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.5F, 1.1F, -9.05F, -1.309F, 0.0F, 0.0F));

        PartDefinition bone10 = largegear3.addOrReplaceChild("bone10",
                CubeListBuilder.create().texOffs(207, 0).addBox(-0.5F, 0.7F, -1.4F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                        .texOffs(175, 85).addBox(-0.5F, 0.7415F, -0.6353F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.001F))
                        .texOffs(75, 194).addBox(1.5F, 0.7074F, -0.6765F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-2.0F, -1.2683F, -1.3528F));

        PartDefinition bone11 = bone10.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(192, 69).addBox(-2.0F, -3.0F,
                0.9F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offset(2.0F, 2.2774F, 0.396F));

        PartDefinition tinygear1 = gears.addOrReplaceChild("tinygear1", CubeListBuilder.create(),
                PartPose.offset(0.25F, -0.402F, -5.5029F));

        PartDefinition cube_r25 = tinygear1.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(207, 19).addBox(-1.0F,
                -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition tinygear2 = gears.addOrReplaceChild("tinygear2", CubeListBuilder.create(),
                PartPose.offset(3.75F, 0.3392F, -8.3922F));

        PartDefinition cube_r26 = tinygear2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(207, 16).addBox(-1.0F,
                -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition tinygear3 = gears.addOrReplaceChild("tinygear3", CubeListBuilder.create(),
                PartPose.offset(3.75F, -1.2432F, -2.8137F));

        PartDefinition cube_r27 = tinygear3.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(207, 13).addBox(-1.0F,
                -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition tapnobs = controls3.addOrReplaceChild("tapnobs",
                CubeListBuilder.create().texOffs(121, 178)
                        .addBox(-1.0F, -0.5F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(206, 148)
                        .addBox(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(1.0F, -12.6228F, -9.958F, 0.2618F, 0.0F, 0.0F));

        PartDefinition tapnobs2 = controls3.addOrReplaceChild("tapnobs2",
                CubeListBuilder.create().texOffs(114, 178)
                        .addBox(-1.0F, -0.5F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(140, 206)
                        .addBox(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(-2.5F, -13.3728F, -7.058F, 0.2618F, 0.0F, 0.0F));

        PartDefinition tapnobs3 = controls3.addOrReplaceChild("tapnobs3",
                CubeListBuilder.create().texOffs(107, 178)
                        .addBox(-1.0F, -0.5F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(206, 138)
                        .addBox(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(-2.5F, -11.7728F, -12.808F, 0.2618F, 0.0F, 0.0F));

        PartDefinition keyhole = controls3.addOrReplaceChild("keyhole", CubeListBuilder.create().texOffs(135, 202).addBox(-0.5F,
                -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.25F, -13.8F, -2.15F));

        PartDefinition tinytapnob = controls3.addOrReplaceChild("tinytapnob",
                CubeListBuilder.create().texOffs(206, 135)
                        .addBox(-0.5F, -0.9167F, -0.6667F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(130, 202)
                        .addBox(-0.5F, -0.1667F, -0.6667F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(135, 206)
                        .addBox(-0.5F, -0.9167F, -0.1667F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, -14.6333F, -2.0833F));

        PartDefinition tinytapnob2 = controls3.addOrReplaceChild("tinytapnob2",
                CubeListBuilder.create().texOffs(130, 206)
                        .addBox(-0.5F, -0.9167F, -0.6667F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(125, 202)
                        .addBox(-0.5F, -0.1667F, -0.6667F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(125, 206)
                        .addBox(-0.5F, -0.9167F, -0.1667F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-9.0F, -12.4333F, -12.0833F, 0.2618F, 0.0F, 0.0F));

        PartDefinition tinytapnob3 = controls3.addOrReplaceChild("tinytapnob3", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-11.0F, -11.4512F, -15.2875F, 0.2618F, 0.0F, 0.0F));

        PartDefinition pivot4 = tinytapnob3.addOrReplaceChild("pivot4",
                CubeListBuilder.create().texOffs(206, 7)
                        .addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(20, 204)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(176, 174)
                        .addBox(-1.0F, -1.25F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tinytapnob4 = controls3.addOrReplaceChild("tinytapnob4",
                CubeListBuilder.create().texOffs(202, 17)
                        .addBox(-0.5F, -0.3749F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(175, 18)
                        .addBox(-1.0F, -1.1251F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(6.5F, -11.0891F, -15.1904F, 0.2618F, 0.0F, 0.0F));

        PartDefinition siegemode = controls3.addOrReplaceChild("siegemode",
                CubeListBuilder.create().texOffs(104, 60)
                        .addBox(-1.6226F, -9.1265F, -25.2794F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(137, 57)
                        .addBox(-1.6226F, -10.1265F, -24.7794F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.25F, 6.5F, 8.0F, 0.0F, -2.0944F, 0.0F));

        PartDefinition cube_r28 = siegemode
                .addOrReplaceChild("cube_r28",
                        CubeListBuilder.create().texOffs(91, 147).addBox(12.25F, -15.2617F, -17.7352F, 3.0F, 0.0F, 5.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(-13.8726F, -10.8765F, 0.0554F, 0.7854F, 0.0F, 0.0F));

        PartDefinition siegemodehandle = siegemode.addOrReplaceChild("siegemodehandle",
                CubeListBuilder.create().texOffs(175, 60)
                        .addBox(1.0F, -3.0333F, -0.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(183, 110)
                        .addBox(-1.0F, -3.0333F, -0.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 135)
                        .addBox(-1.5F, -3.5333F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-0.1226F, -9.5932F, -24.2794F));

        PartDefinition panel3 = toyota.addOrReplaceChild("panel3",
                CubeListBuilder.create().texOffs(118, 60)
                        .addBox(-13.8726F, -7.6806F, -17.4019F, 28.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(183, 115)
                        .addBox(2.35F, -8.0962F, -17.6985F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(113, 31)
                        .addBox(0.35F, -5.0962F, -17.6985F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(48, 138)
                        .addBox(0.35F, -8.0962F, -17.6985F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(6.75F, -9.25F, 3.75F, 0.0F, -2.0944F, 0.0F));

        PartDefinition cube_r29 = panel3.addOrReplaceChild("cube_r29",
                CubeListBuilder.create().texOffs(146, 57)
                        .addBox(0.6F, -12.4F, -15.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.001F)).texOffs(113, 46)
                        .addBox(2.6F, -12.4F, -15.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(165, 148)
                        .addBox(2.35F, -12.9F, -14.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(100, 153)
                        .addBox(-4.85F, -13.9F, -12.35F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.001F)).texOffs(165, 142)
                        .addBox(-7.1F, -13.9F, -11.1F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(19, 139)
                        .addBox(5.85F, -13.91F, -8.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(66, 58)
                        .addBox(6.35F, -13.9F, -7.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r30 = panel3
                .addOrReplaceChild("cube_r30",
                        CubeListBuilder.create().texOffs(153, 35).addBox(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F,
                                new CubeDeformation(-0.001F)),
                        PartPose.offsetAndRotation(0.1274F, 9.2891F, 7.7207F, -0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r31 = panel3.addOrReplaceChild("cube_r31",
                CubeListBuilder.create().texOffs(171, 0).addBox(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-13.9308F, 7.7695F, -16.6289F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r32 = panel3
                .addOrReplaceChild("cube_r32",
                        CubeListBuilder.create().texOffs(176, 135).addBox(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F, 20.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.1274F, 6.25F, 7.7207F, 0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r33 = panel3
                .addOrReplaceChild("cube_r33",
                        CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -17.9761F, -0.0344F, 28.0F, 18.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.1274F, -0.0613F, -0.0293F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r34 = panel3
                .addOrReplaceChild("cube_r34",
                        CubeListBuilder.create().texOffs(108, 64).addBox(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.1274F, 6.25F, -13.2793F, -1.309F, 0.0F, 0.0F));

        PartDefinition lights = panel3.addOrReplaceChild("lights", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone2 = lights.addOrReplaceChild("bone2", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.5F, -1.75F));

        PartDefinition cube_r35 = bone2.addOrReplaceChild("cube_r35",
                CubeListBuilder.create().texOffs(113, 42).addBox(6.1F, -12.9F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lights2 = bone2.addOrReplaceChild("lights2", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r36 = lights2.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(202, 123).addBox(6.1F,
                -10.9681F, -4.5176F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone = lights.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r37 = bone.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(100, 201).addBox(6.1F, -12.9F,
                -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lights1 = bone.addOrReplaceChild("lights1", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r38 = lights1.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(25, 204).addBox(6.1F,
                -10.9681F, -4.5176F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition controls5 = panel3.addOrReplaceChild("controls5", CubeListBuilder.create(),
                PartPose.offset(0.1274F, 9.2891F, 7.7207F));

        PartDefinition sonicport = controls5.addOrReplaceChild("sonicport",
                CubeListBuilder.create().texOffs(180, 39)
                        .addBox(-7.0F, -20.0F, -18.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(171, 6)
                        .addBox(-6.5F, -21.0F, -17.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r39 = sonicport
                .addOrReplaceChild("cube_r39",
                        CubeListBuilder.create().texOffs(33, 135).addBox(6.15F, -1.0F, 1.5974F, 4.0F, 2.0F, 0.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-12.4067F, -20.5F, -11.8481F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r40 = sonicport
                .addOrReplaceChild(
                        "cube_r40", CubeListBuilder.create().texOffs(165, 135).addBox(0.35F, -1.0F, -0.95F, 0.0F, 2.0F,
                                4.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-6.5F, -20.5F, -17.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition speaker = controls5.addOrReplaceChild("speaker", CubeListBuilder.create(),
                PartPose.offset(4.5F, -19.6609F, -15.1033F));

        PartDefinition cube_r41 = speaker.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(171, 13).addBox(-3.0F, -0.1F,
                -1.9F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r42 = speaker
                .addOrReplaceChild(
                        "cube_r42", CubeListBuilder.create().texOffs(180, 149).addBox(-2.25F, -1.0F, -2.15F, 3.0F, 2.0F,
                                3.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3622F, 0.7519F, 0.2533F));

        PartDefinition spinnything1 = controls5.addOrReplaceChild("spinnything1",
                CubeListBuilder.create().texOffs(40, 186).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, -21.2016F, -11.2982F, 0.2618F, 0.0F, 0.0F));

        PartDefinition spinnything2 = controls5.addOrReplaceChild("spinnything2",
                CubeListBuilder.create().texOffs(25, 186).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -21.2016F, -11.2982F, 0.2618F, 0.0F, 0.0F));

        PartDefinition spinnything3 = controls5
                .addOrReplaceChild("spinnything3",
                        CubeListBuilder.create().texOffs(175, 148).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-3.0F, -21.2016F, -11.2982F, 0.2618F, 0.0F, 0.0F));

        PartDefinition sliders = spinnything3.addOrReplaceChild("sliders",
                CubeListBuilder.create().texOffs(207, 115)
                        .addBox(0.75F, -21.4516F, -13.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(85, 205)
                        .addBox(-0.25F, -21.4516F, -13.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(90, 205)
                        .addBox(-1.25F, -21.4516F, -13.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(100, 205)
                        .addBox(-2.25F, -21.4516F, -13.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(80, 205)
                        .addBox(-4.25F, -21.4516F, -13.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(105, 205)
                        .addBox(-3.25F, -21.4516F, -13.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offset(3.0F, 21.2016F, 11.2982F));

        PartDefinition slider1 = sliders.addOrReplaceChild("slider1", CubeListBuilder.create().texOffs(50, 205).addBox(-4.25F,
                -21.4516F, -14.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition slider2 = sliders.addOrReplaceChild("slider2", CubeListBuilder.create().texOffs(55, 205).addBox(-3.25F,
                -21.4516F, -14.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition slider3 = sliders.addOrReplaceChild("slider3", CubeListBuilder.create().texOffs(60, 205).addBox(-2.25F,
                -21.4516F, -14.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition slider4 = sliders.addOrReplaceChild("slider4", CubeListBuilder.create().texOffs(65, 205).addBox(-1.25F,
                -21.4516F, -14.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition slider5 = sliders.addOrReplaceChild("slider5", CubeListBuilder.create().texOffs(75, 205).addBox(-0.25F,
                -21.4516F, -14.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition slider6 = sliders.addOrReplaceChild("slider6", CubeListBuilder.create().texOffs(207, 165).addBox(0.75F,
                -21.4516F, -14.9982F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rotaryswitch1 = controls5.addOrReplaceChild("rotaryswitch1",
                CubeListBuilder.create().texOffs(180, 142)
                        .addBox(-0.75F, -0.0647F, -0.0085F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(205, 142)
                        .addBox(-0.75F, -0.4353F, -0.4915F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(5.6226F, -18.631F, -20.0501F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rotaryswitch2 = controls5.addOrReplaceChild("rotaryswitch2",
                CubeListBuilder.create().texOffs(165, 80)
                        .addBox(-0.75F, 0.0F, -0.017F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(115, 205)
                        .addBox(-0.75F, -0.3706F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(5.6226F, -18.2427F, -21.499F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rotaryswitch3 = controls5.addOrReplaceChild("rotaryswitch3",
                CubeListBuilder.create().texOffs(66, 157)
                        .addBox(-0.75F, 0.0F, -0.017F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(110, 205)
                        .addBox(-0.75F, -0.3706F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(5.6226F, -17.8545F, -22.9479F, 0.2618F, 0.0F, 0.0F));

        PartDefinition panel4 = toyota.addOrReplaceChild("panel4",
                CubeListBuilder.create().texOffs(107, 181)
                        .addBox(-14.0F, -14.9306F, -25.1225F, 28.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(201, 139)
                        .addBox(-3.5F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(201, 135)
                        .addBox(-2.0F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(115, 201)
                        .addBox(-0.5F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(110, 201)
                        .addBox(1.0F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(105, 201)
                        .addBox(2.5F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r43 = panel4.addOrReplaceChild("cube_r43",
                CubeListBuilder.create().texOffs(186, 0).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(186, 5).addBox(-8.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, -17.65F, -16.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r44 = panel4.addOrReplaceChild("cube_r44",
                CubeListBuilder.create().texOffs(192, 98).addBox(6.0F, -22.4F, -19.4F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(86, 142).addBox(-3.0F, -21.4F, -16.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(192, 123)
                        .addBox(-9.0F, -21.9F, -18.9F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r45 = panel4
                .addOrReplaceChild("cube_r45",
                        CubeListBuilder.create().texOffs(165, 110).addBox(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F,
                                new CubeDeformation(-0.001F)),
                        PartPose.offsetAndRotation(0.0F, 2.0391F, 0.0F, -0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r46 = panel4
                .addOrReplaceChild("cube_r46",
                        CubeListBuilder.create().texOffs(77, 178).addBox(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-14.0582F, 0.5195F, -24.3496F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r47 = panel4
                .addOrReplaceChild(
                        "cube_r47", CubeListBuilder.create().texOffs(178, 161).addBox(-1.0F, -21.023F, -24.3765F, 2.0F,
                                2.0F, 20.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r48 = panel4
                .addOrReplaceChild(
                        "cube_r48", CubeListBuilder.create().texOffs(0, 57).addBox(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F,
                                0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, 2.1387F, -21.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r49 = panel4
                .addOrReplaceChild(
                        "cube_r49", CubeListBuilder.create().texOffs(114, 159).addBox(-14.0F, -17.6235F, -14.523F, 28.0F,
                                18.0F, 0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -1.0F, -21.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition controls4 = panel4.addOrReplaceChild("controls4", CubeListBuilder.create(),
                PartPose.offset(-1.0F, 0.0F, 0.0F));

        PartDefinition tinyswitchcover = controls4
                .addOrReplaceChild("tinyswitchcover",
                        CubeListBuilder.create().texOffs(192, 103).addBox(-1.0F, -1.0262F, -1.9978F, 1.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(12.0F, -15.6585F, -22.2118F, 0.2618F, 0.0F, 0.0F));

        PartDefinition tinyswitch = controls4.addOrReplaceChild("tinyswitch", CubeListBuilder.create(),
                PartPose.offset(11.5F, -15.1847F, -23.2096F));

        PartDefinition cube_r50 = tinyswitch.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(105, 142).addBox(1.25F,
                -1.15F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-1.5F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition throttle = controls4.addOrReplaceChild("throttle", CubeListBuilder.create(),
                PartPose.offsetAndRotation(8.0F, -15.9197F, -23.197F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r51 = throttle.addOrReplaceChild("cube_r51",
                CubeListBuilder.create().texOffs(174, 129)
                        .addBox(-1.25F, -23.5F, -18.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 178)
                        .addBox(1.25F, -23.5F, -18.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(47, 186)
                        .addBox(2.75F, -22.5F, -18.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).texOffs(187, 138)
                        .addBox(-0.75F, -22.5F, -18.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).texOffs(113, 60)
                        .addBox(-0.75F, -21.0F, -18.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.001F)).texOffs(98, 153)
                        .addBox(1.75F, -21.0F, -18.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.001F)).texOffs(149, 194)
                        .addBox(2.25F, -21.0F, -18.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).texOffs(195, 161)
                        .addBox(-0.25F, -21.0F, -18.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-1.0F, 13.6722F, 22.2068F, 0.2618F, 0.0F, 0.0F));

        PartDefinition handbrake = controls4.addOrReplaceChild("handbrake", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-7.45F, -16.4308F, -22.5199F, 0.2618F, 0.0F, 0.0F));

        PartDefinition pivot = handbrake.addOrReplaceChild("pivot", CubeListBuilder.create(),
                PartPose.offset(-0.05F, -0.25F, 0.0F));

        PartDefinition cube_r52 = pivot.addOrReplaceChild("cube_r52",
                CubeListBuilder.create().texOffs(57, 157)
                        .addBox(0.6F, -0.058F, -0.5647F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.001F)).texOffs(142, 189)
                        .addBox(-2.5F, 0.041F, -1.5647F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition tinyswitch2 = controls4.addOrReplaceChild("tinyswitch2",
                CubeListBuilder.create().texOffs(54, 76).addBox(0.25F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F))
                        .texOffs(54, 93).addBox(-0.75F, -0.983F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-5.25F, -17.1495F, -17.0152F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lockernob = controls4.addOrReplaceChild("lockernob", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r53 = lockernob.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(202, 13).addBox(-2.5F,
                -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lockernob6 = controls4.addOrReplaceChild("lockernob6", CubeListBuilder.create(),
                PartPose.offset(1.5F, 0.0F, 0.0F));

        PartDefinition cube_r54 = lockernob6.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(202, 4).addBox(-2.0F,
                -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lockernob7 = controls4.addOrReplaceChild("lockernob7", CubeListBuilder.create(),
                PartPose.offset(3.0F, 0.0F, 0.0F));

        PartDefinition cube_r55 = lockernob7.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(202, 0).addBox(-1.5F,
                -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lockernob8 = controls4.addOrReplaceChild("lockernob8", CubeListBuilder.create(),
                PartPose.offset(5.0F, 0.0F, 0.0F));

        PartDefinition cube_r56 = lockernob8.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(201, 148).addBox(-1.5F,
                -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rotatingclockthing = controls4.addOrReplaceChild("rotatingclockthing",
                CubeListBuilder.create().texOffs(181, 188)
                        .addBox(-1.5F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.001F)).texOffs(167, 188)
                        .addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.001F)).texOffs(180, 135)
                        .addBox(1.5F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(1.0F, -17.5F, -16.5F, 0.2618F, 0.0F, 0.0F));

        PartDefinition coloredlever = controls4.addOrReplaceChild("coloredlever", CubeListBuilder.create().texOffs(165, 135)
                .addBox(-0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-1.75F, -19.25F, -12.0F));

        PartDefinition coloredlever2 = controls4.addOrReplaceChild("coloredlever2", CubeListBuilder.create().texOffs(171, 13)
                .addBox(-0.75F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(0.0F, -19.25F, -12.0F));

        PartDefinition coloredlever3 = controls4.addOrReplaceChild("coloredlever3", CubeListBuilder.create().texOffs(183, 120)
                .addBox(-0.75F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(1.5F, -19.25F, -12.0F));

        PartDefinition coloredlever4 = controls4.addOrReplaceChild("coloredlever4", CubeListBuilder.create().texOffs(184, 101)
                .addBox(-0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(2.75F, -19.25F, -12.0F));

        PartDefinition coloredlever5 = controls4.addOrReplaceChild("coloredlever5", CubeListBuilder.create().texOffs(32, 186)
                .addBox(-0.75F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offset(4.5F, -19.25F, -12.0F));

        PartDefinition flightlights = panel4.addOrReplaceChild("flightlights", CubeListBuilder.create(),
                PartPose.offset(-6.0F, -15.9359F, -20.5665F));

        PartDefinition cube_r57 = flightlights.addOrReplaceChild("cube_r57",
                CubeListBuilder.create().texOffs(195, 41).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(127, 195).addBox(-2.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition flightlights2 = flightlights.addOrReplaceChild("flightlights2", CubeListBuilder.create(),
                PartPose.offset(1.0F, 0.9478F, -0.014F));

        PartDefinition leftlight = flightlights2.addOrReplaceChild("leftlight", CubeListBuilder.create(),
                PartPose.offset(13.0F, 0.0522F, 0.014F));

        PartDefinition cube_r58 = leftlight.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(132, 194).addBox(-2.5F,
                -1.0541F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rightlight = flightlights2.addOrReplaceChild("rightlight", CubeListBuilder.create(),
                PartPose.offset(13.0F, 0.0522F, 0.014F));

        PartDefinition cube_r59 = rightlight.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(194, 192).addBox(-0.5F,
                -1.0541F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition handbrakelights = flightlights.addOrReplaceChild("handbrakelights", CubeListBuilder.create(),
                PartPose.offset(-11.0F, 0.25F, 0.25F));

        PartDefinition cube_r60 = handbrakelights.addOrReplaceChild("cube_r60",
                CubeListBuilder.create().texOffs(14, 194).addBox(-2.0F, -1.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(165, 155).addBox(-5.5F, -1.0F, 1.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(170, 135)
                        .addBox(-4.0F, -1.0F, 0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(96, 194)
                        .addBox(-2.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition handbrakelights2 = handbrakelights.addOrReplaceChild("handbrakelights2", CubeListBuilder.create(),
                PartPose.offset(1.0F, 0.9478F, -0.014F));

        PartDefinition red2 = handbrakelights2.addOrReplaceChild("red2", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r61 = red2
                .addOrReplaceChild(
                        "cube_r61", CubeListBuilder.create().texOffs(113, 27).addBox(-5.5F, -1.0541F, 1.25F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(13.0F, 0.0522F, 0.014F, 0.2618F, 0.0F, 0.0F));

        PartDefinition red1 = handbrakelights2.addOrReplaceChild("red1", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r62 = red1
                .addOrReplaceChild(
                        "cube_r62", CubeListBuilder.create().texOffs(175, 30).addBox(-4.0F, -1.0541F, 0.75F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(13.0F, 0.0522F, 0.014F, 0.2618F, 0.0F, 0.0F));

        PartDefinition yellow2 = handbrakelights2.addOrReplaceChild("yellow2", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r63 = yellow2
                .addOrReplaceChild(
                        "cube_r63", CubeListBuilder.create().texOffs(186, 25).addBox(-2.5F, -1.0541F, -0.5F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(13.0F, 0.0522F, 0.014F, 0.2618F, 0.0F, 0.0F));

        PartDefinition yellow1 = handbrakelights2.addOrReplaceChild("yellow1", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r64 = yellow1
                .addOrReplaceChild(
                        "cube_r64", CubeListBuilder.create().texOffs(158, 189).addBox(-2.0F, -1.0541F, -2.0F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(13.0F, 0.0522F, 0.014F, 0.2618F, 0.0F, 0.0F));

        PartDefinition yellow6 = panel4.addOrReplaceChild("yellow6", CubeListBuilder.create(),
                PartPose.offset(-14.0F, -15.7382F, -20.3305F));

        PartDefinition cube_r65 = yellow6
                .addOrReplaceChild(
                        "cube_r65", CubeListBuilder.create().texOffs(51, 119).addBox(-2.5F, -0.0881F, -0.7588F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(19.0F, 0.8022F, -1.236F, 0.2618F, 0.0F, 0.0F));

        PartDefinition yellow5 = panel4.addOrReplaceChild("yellow5", CubeListBuilder.create(),
                PartPose.offset(-14.0F, -15.7382F, -20.3305F));

        PartDefinition cube_r66 = yellow5
                .addOrReplaceChild(
                        "cube_r66", CubeListBuilder.create().texOffs(91, 147).addBox(-2.5F, -0.0881F, -0.7588F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(17.0F, 0.8022F, -1.236F, 0.2618F, 0.0F, 0.0F));

        PartDefinition yellow4 = panel4.addOrReplaceChild("yellow4", CubeListBuilder.create(),
                PartPose.offset(-16.0F, -15.7382F, -20.3305F));

        PartDefinition cube_r67 = yellow4
                .addOrReplaceChild(
                        "cube_r67", CubeListBuilder.create().texOffs(103, 147).addBox(-2.5F, -0.0881F, -0.7588F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(17.0F, 0.8022F, -1.236F, 0.2618F, 0.0F, 0.0F));

        PartDefinition yellow3 = panel4.addOrReplaceChild("yellow3", CubeListBuilder.create(),
                PartPose.offset(-16.0F, -15.7382F, -20.3305F));

        PartDefinition cube_r68 = yellow3
                .addOrReplaceChild(
                        "cube_r68", CubeListBuilder.create().texOffs(165, 148).addBox(-2.5F, -0.0881F, -0.7588F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(15.0F, 0.8022F, -1.236F, 0.2618F, 0.0F, 0.0F));

        PartDefinition panel5 = toyota
                .addOrReplaceChild(
                        "panel5", CubeListBuilder.create().texOffs(180, 48).addBox(-14.0F, -14.9306F, -25.1225F, 28.0F,
                                3.0F, 0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

        PartDefinition cube_r69 = panel5
                .addOrReplaceChild("cube_r69",
                        CubeListBuilder.create().texOffs(165, 60).addBox(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F,
                                new CubeDeformation(-0.001F)),
                        PartPose.offsetAndRotation(0.0F, 2.0391F, 0.0F, -0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r70 = panel5
                .addOrReplaceChild("cube_r70",
                        CubeListBuilder.create().texOffs(62, 178).addBox(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-14.0582F, 0.5195F, -24.3496F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r71 = panel5
                .addOrReplaceChild(
                        "cube_r71", CubeListBuilder.create().texOffs(37, 178).addBox(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F,
                                20.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r72 = panel5
                .addOrReplaceChild(
                        "cube_r72", CubeListBuilder.create().texOffs(0, 19).addBox(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F,
                                0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, 2.1387F, -21.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r73 = panel5.addOrReplaceChild("cube_r73",
                CubeListBuilder.create().texOffs(118, 38)
                        .addBox(-14.0F, -17.6235F, -13.273F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(108, 121)
                        .addBox(-14.0F, -17.6235F, -13.523F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(51, 123)
                        .addBox(-14.0F, -17.6235F, -13.773F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(108, 140)
                        .addBox(-14.0F, -17.6235F, -14.023F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(118, 19)
                        .addBox(-14.0F, -17.6235F, -14.273F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(0, 142)
                        .addBox(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -21.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition panel6 = toyota
                .addOrReplaceChild("panel6",
                        CubeListBuilder.create().texOffs(180, 52).addBox(-9.3365F, -1.0376F, -6.4336F, 28.0F, 3.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(-18.5168F, -15.8931F, -5.3058F, 0.0F, 1.0472F, 0.0F));

        PartDefinition cube_r74 = panel6.addOrReplaceChild("cube_r74",
                CubeListBuilder.create().texOffs(165, 85).addBox(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F,
                        new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(4.6635F, 15.9321F, 18.6889F, -0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r75 = panel6
                .addOrReplaceChild("cube_r75",
                        CubeListBuilder.create().texOffs(178, 76).addBox(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-9.3948F, 14.4126F, -5.6607F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r76 = panel6
                .addOrReplaceChild("cube_r76",
                        CubeListBuilder.create().texOffs(82, 178).addBox(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F, 20.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(4.6635F, 12.8931F, 18.6889F, 0.2618F, 0.5236F, 0.0F));

        PartDefinition cube_r77 = panel6
                .addOrReplaceChild("cube_r77",
                        CubeListBuilder.create().texOffs(0, 38).addBox(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(4.6635F, 16.0318F, -2.3111F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r78 = panel6.addOrReplaceChild("cube_r78",
                CubeListBuilder.create().texOffs(114, 0)
                        .addBox(-14.0F, -17.6235F, -14.023F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(57, 159)
                        .addBox(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(4.6635F, 12.8931F, -2.3111F, -1.309F, 0.0F, 0.0F));

        PartDefinition controls2 = panel6.addOrReplaceChild("controls2", CubeListBuilder.create(),
                PartPose.offset(4.6635F, 13.8931F, 18.6889F));

        PartDefinition gallifreyanball = controls2.addOrReplaceChild("gallifreyanball", CubeListBuilder.create(),
                PartPose.offset(8.0F, -15.9606F, -21.1224F));

        PartDefinition cube_r79 = gallifreyanball.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(192, 85)
                .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(0.0F, 0.0259F, -0.0966F, 0.2618F, 0.0F, 0.0F));

        PartDefinition gallifreyanball2 = controls2.addOrReplaceChild("gallifreyanball2", CubeListBuilder.create(),
                PartPose.offset(-8.0F, -15.9606F, -21.1224F));

        PartDefinition cube_r80 = gallifreyanball2.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(192, 74)
                .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(0.0F, 0.0259F, -0.0966F, 0.2618F, 0.0F, 0.0F));

        PartDefinition smallnob = controls2.addOrReplaceChild("smallnob",
                CubeListBuilder.create().texOffs(35, 207).addBox(-0.75F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(104, 153).addBox(-0.25F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-4.25F, -18.7839F, -12.7475F, 0.3054F, 0.0F, 0.0F));

        PartDefinition smallnob2 = controls2.addOrReplaceChild("smallnob2", CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.5F, -18.7839F, -12.7475F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r81 = smallnob2.addOrReplaceChild("cube_r81",
                CubeListBuilder.create().texOffs(165, 64)
                        .addBox(-0.75F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)).texOffs(30, 207)
                        .addBox(-0.25F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition tinyswitches = controls2.addOrReplaceChild("tinyswitches", CubeListBuilder.create(),
                PartPose.offset(0.0F, -18.1802F, -12.5857F));

        PartDefinition tinyswitches1 = tinyswitches.addOrReplaceChild("tinyswitches1", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r82 = tinyswitches1
                .addOrReplaceChild("cube_r82",
                        CubeListBuilder.create().texOffs(165, 129).addBox(-9.0F, 3.0F, 4.5F, 4.0F, 2.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(7.0F, -2.8198F, -5.4143F, 0.2618F, 0.0F, 0.0F));

        PartDefinition smallnob3 = controls2
                .addOrReplaceChild("smallnob3",
                        CubeListBuilder.create().texOffs(203, 171).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-5.5F, -15.5606F, -22.6996F, 0.2618F, 0.0F, 0.0F));

        PartDefinition smallnob4 = controls2
                .addOrReplaceChild("smallnob4",
                        CubeListBuilder.create().texOffs(203, 162).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(5.5F, -15.5606F, -22.6996F, 0.2618F, 0.0F, 0.0F));

        PartDefinition smallnob5 = controls2
                .addOrReplaceChild("smallnob5",
                        CubeListBuilder.create().texOffs(203, 158).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-3.5F, -15.3106F, -23.6996F, 0.2618F, 0.0F, 0.0F));

        PartDefinition smallnob6 = controls2
                .addOrReplaceChild("smallnob6",
                        CubeListBuilder.create().texOffs(140, 202).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(3.5F, -15.3106F, -23.6996F, 0.2618F, 0.0F, 0.0F));

        PartDefinition gallifreyan = panel6.addOrReplaceChild("gallifreyan", CubeListBuilder.create(),
                PartPose.offset(18.5168F, 15.8931F, 5.3058F));

        PartDefinition middlegallifreyan = gallifreyan
                .addOrReplaceChild("middlegallifreyan",
                        CubeListBuilder.create().texOffs(104, 49).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(-13.8533F, -18.4362F, -5.1547F, -1.309F, 0.0F, 0.0F));

        PartDefinition rightgallifreyan = gallifreyan
                .addOrReplaceChild("rightgallifreyan",
                        CubeListBuilder.create().texOffs(194, 188).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(-9.1033F, -18.4362F, -5.1547F, -1.309F, 0.0F, 0.0F));

        PartDefinition leftgallifreyan = gallifreyan
                .addOrReplaceChild("leftgallifreyan",
                        CubeListBuilder.create().texOffs(89, 194).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(-18.6033F, -18.4362F, -5.1547F, -1.309F, 0.0F, 0.0F));

        PartDefinition switchlights2 = panel6.addOrReplaceChild("switchlights2", CubeListBuilder.create(),
                PartPose.offset(-2.0865F, -1.0428F, -1.6276F));

        PartDefinition cube_r83 = switchlights2.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(66, 44).addBox(6.25F,
                -1.8591F, 7.8848F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition switchlights3 = switchlights2.addOrReplaceChild("switchlights3", CubeListBuilder.create(),
                PartPose.offset(1.0F, 0.9478F, -0.014F));

        PartDefinition cube_r84 = switchlights3
                .addOrReplaceChild(
                        "cube_r84", CubeListBuilder.create().texOffs(66, 29).addBox(4.25F, -1.9132F, 7.8848F, 1.0F, 2.0F,
                                1.0F, new CubeDeformation(-0.25F)),
                        PartPose.offsetAndRotation(1.0F, 0.0522F, 0.014F, 0.2618F, 0.0F, 0.0F));

        PartDefinition top = toyota.addOrReplaceChild("top",
                CubeListBuilder.create().texOffs(57, 49)
                        .addBox(-8.0F, -46.0F, -7.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.001F)).texOffs(57, 34)
                        .addBox(-8.0F, -84.0F, -7.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.001F)),
                PartPose.offset(0.0F, 23.0F, 0.0F));

        PartDefinition cube_r85 = top.addOrReplaceChild("cube_r85",
                CubeListBuilder.create().texOffs(31, 214)
                        .addBox(-7.75F, -27.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(0, 204)
                        .addBox(-8.65F, -26.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r86 = top.addOrReplaceChild("cube_r86",
                CubeListBuilder.create().texOffs(212, 203)
                        .addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(203, 158)
                        .addBox(-8.65F, -29.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -17.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r87 = top.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(188, 212).addBox(-7.75F, -30.0F,
                -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -17.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r88 = top
                .addOrReplaceChild(
                        "cube_r88", CubeListBuilder.create().texOffs(211, 190).addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F,
                                9.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -17.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r89 = top.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(174, 209).addBox(-7.75F, -30.0F,
                -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -17.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r90 = top
                .addOrReplaceChild("cube_r90",
                        CubeListBuilder.create().texOffs(151, 209).addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -17.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r91 = top.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(207, 75).addBox(-8.65F, -31.0F,
                -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r92 = top
                .addOrReplaceChild(
                        "cube_r92", CubeListBuilder.create().texOffs(207, 59).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r93 = top
                .addOrReplaceChild("cube_r93",
                        CubeListBuilder.create().texOffs(15, 207).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r94 = top
                .addOrReplaceChild(
                        "cube_r94", CubeListBuilder.create().texOffs(205, 25).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition toprotor = top.addOrReplaceChild("toprotor", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, -114.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition cube_r95 = toprotor
                .addOrReplaceChild(
                        "cube_r95", CubeListBuilder.create().texOffs(77, 148).addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F,
                                9.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r96 = toprotor
                .addOrReplaceChild(
                        "cube_r96", CubeListBuilder.create().texOffs(167, 188).addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F,
                                9.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r97 = toprotor
                .addOrReplaceChild(
                        "cube_r97", CubeListBuilder.create().texOffs(165, 135).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r98 = toprotor
                .addOrReplaceChild(
                        "cube_r98", CubeListBuilder.create().texOffs(171, 0).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r99 = toprotor.addOrReplaceChild("cube_r99", CubeListBuilder.create().texOffs(194, 188).addBox(-7.75F,
                -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r100 = toprotor.addOrReplaceChild("cube_r100", CubeListBuilder.create().texOffs(62, 178).addBox(-8.65F,
                -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r101 = toprotor.addOrReplaceChild("cube_r101",
                CubeListBuilder.create().texOffs(207, 101)
                        .addBox(-7.75F, -27.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(25, 180)
                        .addBox(-8.65F, -26.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r102 = toprotor.addOrReplaceChild("cube_r102",
                CubeListBuilder.create().texOffs(208, 89)
                        .addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(77, 181)
                        .addBox(-8.65F, -29.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r103 = toprotor.addOrReplaceChild("cube_r103", CubeListBuilder.create().texOffs(208, 114).addBox(-7.75F,
                -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r104 = toprotor.addOrReplaceChild("cube_r104", CubeListBuilder.create().texOffs(127, 189).addBox(-8.65F,
                -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition monitors = top.addOrReplaceChild("monitors", CubeListBuilder.create(),
                PartPose.offset(0.0F, -47.0F, 0.0F));

        PartDefinition monitor1 = monitors.addOrReplaceChild("monitor1", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r105 = monitor1.addOrReplaceChild("cube_r105",
                CubeListBuilder.create().texOffs(41, 193).addBox(7.25F, -48.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(179, 188).addBox(14.0F, -54.4F, -6.0F, 1.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(113, 34)
                        .addBox(14.5F, -53.4F, -7.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(99, 178)
                        .addBox(14.5F, -53.4F, 6.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 47.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r106 = monitor1.addOrReplaceChild("cube_r106",
                CubeListBuilder.create().texOffs(165, 104)
                        .addBox(13.5287F, 7.75F, -0.6609F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(165, 126)
                        .addBox(13.5287F, 7.75F, -0.6609F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.25F, 0.0F, -4.0F, 0.281F, -0.4478F, -0.588F));

        PartDefinition gallifreyan2 = monitor1
                .addOrReplaceChild("gallifreyan2",
                        CubeListBuilder.create().texOffs(180, 25).addBox(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(13.0198F, -3.45F, 7.517F, 0.0F, -0.5236F, 0.0F));

        PartDefinition monitor2 = monitors.addOrReplaceChild("monitor2", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r107 = monitor2.addOrReplaceChild("cube_r107",
                CubeListBuilder.create().texOffs(34, 193).addBox(7.25F, -48.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(152, 188).addBox(14.0F, -54.4F, -6.0F, 1.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(54, 110)
                        .addBox(14.5F, -53.4F, -7.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(113, 19)
                        .addBox(14.5F, -53.4F, 6.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 47.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r108 = monitor2.addOrReplaceChild("cube_r108",
                CubeListBuilder.create().texOffs(0, 139)
                        .addBox(13.5287F, 7.75F, -0.6609F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(165, 101)
                        .addBox(13.5287F, 7.75F, -0.6609F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.25F, 0.0F, -4.0F, 0.281F, -0.4478F, -0.588F));

        PartDefinition gallifreyan3 = monitor2
                .addOrReplaceChild("gallifreyan3",
                        CubeListBuilder.create().texOffs(152, 189).addBox(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(13.0198F, -3.45F, 7.517F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bottom = toyota.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(57, 19).addBox(-8.0F, -27.0F,
                -7.0F, 16.0F, 0.0F, 14.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 27.0F, 0.0F));

        PartDefinition cube_r109 = bottom
                .addOrReplaceChild("cube_r109",
                        CubeListBuilder.create().texOffs(218, 155).addBox(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, -27.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r110 = bottom.addOrReplaceChild("cube_r110",
                CubeListBuilder.create().texOffs(92, 178).addBox(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -27.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r111 = bottom.addOrReplaceChild("cube_r111", CubeListBuilder.create().texOffs(183, 220).addBox(0.0F,
                -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -27.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r112 = bottom.addOrReplaceChild("cube_r112", CubeListBuilder.create().texOffs(176, 220).addBox(0.0F,
                -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -27.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r113 = bottom.addOrReplaceChild("cube_r113",
                CubeListBuilder.create().texOffs(221, 22).addBox(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -27.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r114 = bottom.addOrReplaceChild("cube_r114", CubeListBuilder.create().texOffs(221, 125).addBox(0.0F,
                -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -27.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r115 = bottom
                .addOrReplaceChild(
                        "cube_r115", CubeListBuilder.create().texOffs(146, 215).addBox(-0.1F, -27.0F, -5.75F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-0.25F, -16.0F, 0.5F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r116 = bottom
                .addOrReplaceChild(
                        "cube_r116", CubeListBuilder.create().texOffs(191, 25).addBox(-0.5F, -27.0F, -6.0F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-0.25F, -16.0F, 0.5F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r117 = bottom
                .addOrReplaceChild("cube_r117",
                        CubeListBuilder.create().texOffs(190, 158).addBox(-0.9F, -27.0F, -5.25F, 1.0F, 15.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-0.25F, -16.0F, 0.5F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r118 = bottom
                .addOrReplaceChild(
                        "cube_r118", CubeListBuilder.create().texOffs(190, 135).addBox(-0.9F, -27.0F, -5.75F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-0.25F, -16.0F, 0.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r119 = bottom
                .addOrReplaceChild(
                        "cube_r119", CubeListBuilder.create().texOffs(185, 158).addBox(-0.1F, -27.0F, -5.25F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-0.25F, -16.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r120 = bottom
                .addOrReplaceChild("cube_r120",
                        CubeListBuilder.create().texOffs(50, 180).addBox(-0.5F, -27.0F, -5.0F, 1.0F, 15.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-0.25F, -16.0F, 0.5F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r121 = bottom.addOrReplaceChild("cube_r121", CubeListBuilder.create().texOffs(20, 220).addBox(-0.1F,
                -27.0F, -5.75F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -16.0F, 0.5F, 0.0F, 1.0472F, 0.0F));

        PartDefinition cube_r122 = bottom.addOrReplaceChild("cube_r122", CubeListBuilder.create().texOffs(15, 220).addBox(-0.5F,
                -27.0F, -6.0F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -16.0F, 0.5F, 0.0F, 0.0F, 0.0F));

        PartDefinition cube_r123 = bottom
                .addOrReplaceChild("cube_r123",
                        CubeListBuilder.create().texOffs(10, 217).addBox(-0.9F, -27.0F, -5.25F, 1.0F, 15.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -16.0F, 0.5F, -3.1416F, -1.0472F, 3.1416F));

        PartDefinition cube_r124 = bottom.addOrReplaceChild("cube_r124", CubeListBuilder.create().texOffs(5, 217).addBox(-0.9F,
                -27.0F, -5.75F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -16.0F, 0.5F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r125 = bottom
                .addOrReplaceChild(
                        "cube_r125", CubeListBuilder.create().texOffs(0, 217).addBox(-0.1F, -27.0F, -5.25F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -16.0F, 0.5F, 3.1416F, 1.0472F, 3.1416F));

        PartDefinition cube_r126 = bottom
                .addOrReplaceChild(
                        "cube_r126", CubeListBuilder.create().texOffs(216, 125).addBox(-0.5F, -27.0F, -5.0F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -16.0F, 0.5F, -3.1416F, 0.0F, 3.1416F));

        PartDefinition cube_r127 = bottom.addOrReplaceChild("cube_r127",
                CubeListBuilder.create().texOffs(192, 85)
                        .addBox(-8.65F, -28.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(214, 179)
                        .addBox(-7.75F, -29.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(135, 202)
                        .addBox(-8.65F, -35.5F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r128 = bottom.addOrReplaceChild("cube_r128", CubeListBuilder.create().texOffs(192, 69).addBox(-8.65F,
                -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r129 = bottom.addOrReplaceChild("cube_r129", CubeListBuilder.create().texOffs(202, 214).addBox(-7.75F,
                -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r130 = bottom
                .addOrReplaceChild(
                        "cube_r130", CubeListBuilder.create().texOffs(192, 56).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r131 = bottom
                .addOrReplaceChild(
                        "cube_r131", CubeListBuilder.create().texOffs(123, 215).addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F,
                                9.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r132 = bottom
                .addOrReplaceChild(
                        "cube_r132", CubeListBuilder.create().texOffs(54, 214).addBox(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F,
                                9.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r133 = bottom
                .addOrReplaceChild(
                        "cube_r133", CubeListBuilder.create().texOffs(35, 201).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r134 = bottom.addOrReplaceChild("cube_r134", CubeListBuilder.create().texOffs(192, 110).addBox(-8.65F,
                -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r135 = bottom.addOrReplaceChild("cube_r135",
                CubeListBuilder.create().texOffs(196, 199)
                        .addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(100, 214)
                        .addBox(-7.75F, -32.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(60, 201)
                        .addBox(-8.65F, -38.5F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r136 = bottom.addOrReplaceChild("cube_r136", CubeListBuilder.create().texOffs(77, 214).addBox(-7.75F,
                -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r137 = bottom.addOrReplaceChild("cube_r137", CubeListBuilder.create().texOffs(202, 0).addBox(-8.65F,
                -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r138 = bottom
                .addOrReplaceChild(
                        "cube_r138", CubeListBuilder.create().texOffs(201, 135).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r139 = bottom
                .addOrReplaceChild(
                        "cube_r139", CubeListBuilder.create().texOffs(110, 201).addBox(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F,
                                10.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r140 = bottom.addOrReplaceChild("cube_r140", CubeListBuilder.create().texOffs(85, 201).addBox(-8.65F,
                -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition glass = toyota.addOrReplaceChild("glass", CubeListBuilder.create().texOffs(34, 76).addBox(-4.0F, -59.0F, -6.9F,
                8.0F, 58.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition cube_r141 = glass.addOrReplaceChild("cube_r141", CubeListBuilder.create().texOffs(0, 76).addBox(-4.0F, -75.0F,
                -6.9F, 8.0F, 58.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

        PartDefinition cube_r142 = glass
                .addOrReplaceChild("cube_r142",
                        CubeListBuilder.create().texOffs(74, 64).addBox(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

        PartDefinition cube_r143 = glass
                .addOrReplaceChild("cube_r143",
                        CubeListBuilder.create().texOffs(57, 64).addBox(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F, 0.0F,
                                new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

        PartDefinition cube_r144 = glass
                .addOrReplaceChild(
                        "cube_r144", CubeListBuilder.create().texOffs(17, 76).addBox(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F,
                                0.0F, new CubeDeformation(0.001F)),
                        PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r145 = glass.addOrReplaceChild("cube_r145", CubeListBuilder.create().texOffs(91, 64).addBox(-4.0F, -75.0F,
                -6.9F, 8.0F, 58.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

        PartDefinition rotor = toyota.addOrReplaceChild("rotor", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r146 = rotor.addOrReplaceChild("cube_r146",
                CubeListBuilder.create().texOffs(0, 180).addBox(-1.0F, 7.0F, -5.25F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r147 = rotor
                .addOrReplaceChild("cube_r147",
                        CubeListBuilder.create().texOffs(178, 85).addBox(-1.0F, 7.0F, -5.25F, 2.0F, 13.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 3.1416F, -0.5236F, -3.1416F));

        PartDefinition cube_r148 = rotor
                .addOrReplaceChild(
                        "cube_r148", CubeListBuilder.create().texOffs(178, 60).addBox(-1.0F, 7.0F, -5.25F, 2.0F, 13.0F,
                                2.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 3.1416F, 0.5236F, -3.1416F));

        PartDefinition cube_r149 = rotor.addOrReplaceChild("cube_r149", CubeListBuilder.create().texOffs(176, 158).addBox(-1.0F, 7.0F,
                -5.25F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r150 = rotor.addOrReplaceChild("cube_r150", CubeListBuilder.create().texOffs(174, 110).addBox(-1.0F, 7.0F,
                -5.25F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r151 = rotor.addOrReplaceChild("cube_r151", CubeListBuilder.create().texOffs(165, 110).addBox(-1.0F, 7.0F,
                -5.25F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r152 = rotor.addOrReplaceChild("cube_r152",
                CubeListBuilder.create().texOffs(9, 180).addBox(-1.0F, 8.0F, -5.25F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, 1.5708F, -3.1416F));

        PartDefinition cube_r153 = rotor
                .addOrReplaceChild(
                        "cube_r153", CubeListBuilder.create().texOffs(104, 34).addBox(-1.0F, 8.0F, -5.25F, 2.0F, 11.0F,
                                2.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, 0.5236F, 3.1416F));

        PartDefinition cube_r154 = rotor
                .addOrReplaceChild(
                        "cube_r154", CubeListBuilder.create().texOffs(104, 19).addBox(-1.0F, 8.0F, -5.25F, 2.0F, 11.0F,
                                2.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, -0.5236F, 3.1416F));

        PartDefinition cube_r155 = rotor.addOrReplaceChild("cube_r155",
                CubeListBuilder.create().texOffs(57, 49).addBox(-1.0F, 8.0F, -5.25F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

        PartDefinition cube_r156 = rotor.addOrReplaceChild("cube_r156",
                CubeListBuilder.create().texOffs(57, 34).addBox(-1.0F, 8.0F, -5.25F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, -3.1416F, -0.5236F, 0.0F));

        PartDefinition cube_r157 = rotor.addOrReplaceChild("cube_r157",
                CubeListBuilder.create().texOffs(57, 19).addBox(-1.0F, 8.0F, -5.25F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 3.1416F, 0.5236F, 0.0F));

        PartDefinition rotorlights = rotor.addOrReplaceChild("rotorlights", CubeListBuilder.create(),
                PartPose.offset(0.0F, -21.0F, 0.5F));

        PartDefinition cube_r158 = rotorlights
                .addOrReplaceChild("cube_r158",
                        CubeListBuilder.create().texOffs(151, 220).addBox(-0.533F, -8.0F, -4.75F, 1.0F, 15.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -21.0F, -0.5F, -3.1416F, 0.5236F, 3.1416F));

        PartDefinition cube_r159 = rotorlights.addOrReplaceChild("cube_r159", CubeListBuilder.create().texOffs(25, 220)
                .addBox(-0.533F, -8.0F, -4.75F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -21.0F, -0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r160 = rotorlights
                .addOrReplaceChild(
                        "cube_r160", CubeListBuilder.create().texOffs(156, 220).addBox(-0.467F, -8.0F, -4.75F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -21.0F, -0.5F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r161 = rotorlights
                .addOrReplaceChild(
                        "cube_r161", CubeListBuilder.create().texOffs(161, 220).addBox(-0.467F, -8.0F, -4.75F, 1.0F, 15.0F,
                                1.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -21.0F, -0.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r162 = rotorlights
                .addOrReplaceChild("cube_r162",
                        CubeListBuilder.create().texOffs(171, 220).addBox(-0.5F, -8.0F, -4.75F, 1.0F, 15.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, -21.0F, -0.5F, -3.1416F, -0.5236F, 3.1416F));

        PartDefinition cube_r163 = rotorlights.addOrReplaceChild("cube_r163", CubeListBuilder.create().texOffs(166, 220).addBox(-0.5F,
                -8.0F, -4.75F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -21.0F, -0.5F, 0.0F, 0.5236F, 0.0F));

        PartDefinition rotorgizmo = rotor.addOrReplaceChild("rotorgizmo",
                CubeListBuilder.create().texOffs(165, 85)
                        .addBox(-1.5F, -35.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(25, 193)
                        .addBox(-1.0F, -37.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r164 = rotorgizmo.addOrReplaceChild("cube_r164",
                CubeListBuilder.create().texOffs(192, 115)
                        .addBox(-1.0F, -37.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(165, 64)
                        .addBox(-1.5F, -35.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -85.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

        PartDefinition uppertimepiece = rotorgizmo.addOrReplaceChild("uppertimepiece", CubeListBuilder.create(),
                PartPose.offset(0.0F, -48.0F, 0.0F));

        PartDefinition cube_r165 = uppertimepiece.addOrReplaceChild("cube_r165",
                CubeListBuilder.create().texOffs(66, 49).addBox(-0.5F, -42.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.001F))
                        .texOffs(66, 19).addBox(0.0F, -42.0F, -0.5F, 0.0F, 8.0F, 1.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -37.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        PartDefinition lowertimepiece = rotorgizmo.addOrReplaceChild("lowertimepiece", CubeListBuilder.create(),
                PartPose.offset(0.0F, -37.0F, 0.0F));

        PartDefinition cube_r166 = lowertimepiece.addOrReplaceChild("cube_r166",
                CubeListBuilder.create().texOffs(66, 34).addBox(0.0F, -42.0F, -0.5F, 0.0F, 8.0F, 1.0F, new CubeDeformation(0.001F))
                        .texOffs(51, 110).addBox(-0.5F, -42.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 37.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        return LayerDefinition.create(meshDefinition, 256, 256);
    }


    @Override
    public void setupAnim(TardisExteriorRenderState state) {
        super.setupAnim(state);
        ModelPart throttle = toyota.getChild("panel4").getChild("controls4").getChild("throttle");
        ModelPart handbrake = toyota.getChild("panel4").getChild("controls4").getChild("handbrake").getChild("pivot");
        throttle.xRot = -0.5F + state.consoleThrottle * 1.5F;
        handbrake.yRot = state.consoleHandbrake * -1.57F;
    }
}




