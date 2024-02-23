package dev.farid.skyflock.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class RenderUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static class Render2D {
        public static void drawLine(float x1, float y1, float x2, float y2, float thickness) {
            // gui.drawHorizontalLine ?
            double theta = -Math.atan2(y2 - y1, x2 - x1);
            double i = Math.sin(theta) * (thickness / 2);
            double j = Math.cos(theta) * (thickness / 2);

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

            worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            worldRenderer.pos(x1 + i, y1 + j, 0.0).endVertex();
            worldRenderer.pos(x2 + i, y2 + j, 0.0).endVertex();

            tessellator.draw();

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static class Render3D {
        public static void drawLines(List<Vec3> poses, Color colour, float thickness, float partialTicks, boolean phase) {

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            Entity renderEntity = mc.getRenderViewEntity();

            double realX = renderEntity.lastTickPosX + (renderEntity.posX - renderEntity.lastTickPosX) * partialTicks;
            double realY = renderEntity.lastTickPosY + (renderEntity.posY - renderEntity.lastTickPosY) * partialTicks;
            double realZ = renderEntity.lastTickPosZ + (renderEntity.posZ - renderEntity.lastTickPosZ) * partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.translate(-realX, -realY, -realZ);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GL11.glLineWidth(thickness);

            if (phase)
                GlStateManager.disableDepth();

            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

            worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (Vec3 pos : poses) {
                worldRenderer.pos(
                                pos.xCoord,
                                pos.yCoord,
                                pos.zCoord)
                        .color(
                                colour.getRed() / 255f,
                                colour.getGreen() / 255f,
                                colour.getBlue() / 255f,
                                colour.getAlpha() / 255f)
                        .endVertex();
            }

            Tessellator.getInstance().draw();
            GlStateManager.translate(realX, realY, realZ);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();

            if (phase)
                GlStateManager.enableDepth();

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }

        public static void drawArrowToEntity(Entity entityIn, Color colour, float thickness, double distance, float partialTicks) {
            if (entityIn == null || mc.thePlayer == null)
                return;

            double yawRadians = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) * Math.PI / 180D;
            double renderX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks;
            double renderY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks;
            double renderZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks;

            double leftX = renderX - (0.3 * Math.cos(yawRadians));
            double leftZ = renderZ - (0.3 * Math.sin(yawRadians));

            double rightX = renderX + (0.3 * Math.cos(yawRadians));
            double rightZ = renderZ + (0.3 * Math.sin(yawRadians));

            double[] entXZ = normalisedEntityCoords(entityIn, renderX, renderZ, distance, partialTicks);
            Render3D.drawLines(
                    Arrays.asList(
                            new Vec3(leftX, renderY + 0.5, leftZ),
                            new Vec3(entXZ[0], renderY + 0.5, entXZ[1]),
                            new Vec3(rightX, renderY + 0.5, rightZ)
                    ),
                    colour,
                    thickness,
                    partialTicks,
                    true
            );
        }

        // https://github.com/Moulberry/NotEnoughUpdates/blob/master/src/main/java/io/github/moulberry/notenoughupdates/miscfeatures/CustomItemEffects.java
        public static void drawFilledBoundingBox(AxisAlignedBB aabb, Color c, float partialTicks) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            Entity renderEntity = mc.thePlayer;

            double realX = renderEntity.lastTickPosX + (renderEntity.posX - renderEntity.lastTickPosX) * partialTicks;
            double realY = renderEntity.lastTickPosY + (renderEntity.posY - renderEntity.lastTickPosY) * partialTicks;
            double realZ = renderEntity.lastTickPosZ + (renderEntity.posZ - renderEntity.lastTickPosZ) * partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.translate(-realX, -realY, -realZ);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableTexture2D();

            GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);

            //vertical
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
            tessellator.draw();
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
            tessellator.draw();


            GlStateManager.color(c.getRed() / 255f * 0.8f, c.getGreen() / 255f * 0.8f, c.getBlue() / 255f * 0.8f, c.getAlpha() / 255f);

            //x
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
            tessellator.draw();
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
            tessellator.draw();

            GlStateManager.color(c.getRed() / 255f * 0.9f, c.getGreen() / 255f * 0.9f, c.getBlue() / 255f * 0.9f, c.getAlpha() / 255f);
            //z
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
            worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
            tessellator.draw();
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
            worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        public static void drawFilledBoundingBox(Entity entityIn, Color c, float partialTicks) {
            AxisAlignedBB aabb = getEntityRenderAABB(entityIn, partialTicks);
            drawFilledBoundingBox(aabb, c, partialTicks);
        }

        // https://github.com/Skytils/SkytilsMod/blob/1.x/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt
        public static void drawOutlinedBoundingBox(AxisAlignedBB aabb, Color c, float width, float partialTicks) {
            Entity renderEntity = mc.getRenderViewEntity();

            double realX = renderEntity.lastTickPosX + (renderEntity.posX - renderEntity.lastTickPosX) * partialTicks;
            double realY = renderEntity.lastTickPosY + (renderEntity.posY - renderEntity.lastTickPosY) * partialTicks;
            double realZ = renderEntity.lastTickPosZ + (renderEntity.posZ - renderEntity.lastTickPosZ) * partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.translate(-realX, -realY, -realZ);
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(width);

            RenderGlobal.drawOutlinedBoundingBox(aabb, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

            GlStateManager.translate(realX, realY, realZ);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();

        }
        public static void drawOutlinedBoundingBox(Entity entityIn, Color c, float width, float partialTicks) {
            AxisAlignedBB aabb = getEntityRenderAABB(entityIn, partialTicks);
            drawOutlinedBoundingBox(aabb, c, width, partialTicks);
        }
    }

    public static AxisAlignedBB getEntityRenderAABB(Entity entityIn, float partialTicks) {
        double x = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        double y = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        double z = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        float w = entityIn.width;
        float h = entityIn.height;

        return new AxisAlignedBB(
                x - (w / 2),
                y + 0,
                z - (w / 2),
                x + (w / 2),
                y + h,
                z + (w / 2)
        );
    }

    public static double[] getEntityRenderCoords(Entity entityIn, float partialTicks) {
        double x = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        double y = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        double z = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        return new double[] {x, y, z};
    }

    private static double[] normalisedEntityCoords(Entity entityIn, double renderX, double renderZ, double distance, float partialTicks) {
        double entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        double entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;

        double dX = entityX - renderX;
        double dZ = entityZ - renderZ;
        float d = (float) Math.sqrt(dX * dX + dZ * dZ);

        // returns x z coords of entity, normalised to `distance` blocks away from player
        return new double[]{renderX + (dX / d) * distance, renderZ + (dZ / d) * distance};
    }
}
