package com.intothevortex.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

import java.util.UUID;

public final class TardisSkyRenderer {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("intothevortex", "textures/environment/tardis_sky.png");
    private static final RenderType RENDER_TYPE = RenderTypes.armorCutoutNoCull(TEXTURE);
    private static final float DISTANCE = 100.0F;

    private TardisSkyRenderer() {
    }

    public static boolean shouldRender() {
        if (Minecraft.getInstance().level == null) return false;
        Identifier dimension = Minecraft.getInstance().level.dimension().identifier();
        if (!"intothevortex".equals(dimension.getNamespace())) return false;
        try {
            UUID.fromString(dimension.getPath());
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public static void render() {
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.ENTITY);
        face(builder, -DISTANCE, -DISTANCE, -DISTANCE, DISTANCE, -DISTANCE, -DISTANCE, DISTANCE, DISTANCE, -DISTANCE, -DISTANCE, DISTANCE, -DISTANCE);
        face(builder, DISTANCE, -DISTANCE, DISTANCE, -DISTANCE, -DISTANCE, DISTANCE, -DISTANCE, DISTANCE, DISTANCE, DISTANCE, DISTANCE, DISTANCE);
        face(builder, -DISTANCE, -DISTANCE, DISTANCE, -DISTANCE, -DISTANCE, -DISTANCE, -DISTANCE, DISTANCE, -DISTANCE, -DISTANCE, DISTANCE, DISTANCE);
        face(builder, DISTANCE, -DISTANCE, -DISTANCE, DISTANCE, -DISTANCE, DISTANCE, DISTANCE, DISTANCE, DISTANCE, DISTANCE, DISTANCE, -DISTANCE);
        face(builder, -DISTANCE, DISTANCE, -DISTANCE, DISTANCE, DISTANCE, -DISTANCE, DISTANCE, DISTANCE, DISTANCE, -DISTANCE, DISTANCE, DISTANCE);
        face(builder, -DISTANCE, -DISTANCE, DISTANCE, DISTANCE, -DISTANCE, DISTANCE, DISTANCE, -DISTANCE, -DISTANCE, -DISTANCE, -DISTANCE, -DISTANCE);
        RENDER_TYPE.draw(builder.buildOrThrow());
    }

    private static void face(VertexConsumer vertices, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        vertex(vertices, x1, y1, z1, 0.0F, 0.0F);
        vertex(vertices, x2, y2, z2, 16.0F, 0.0F);
        vertex(vertices, x3, y3, z3, 16.0F, 16.0F);
        vertex(vertices, x4, y4, z4, 0.0F, 16.0F);
    }

    private static void vertex(VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.addVertex(x, y, z).setColor(40, 40, 40, 255).setUv(u, v).setOverlay(0).setLight(15728880).setNormal(0.0F, 1.0F, 0.0F);
    }
}
