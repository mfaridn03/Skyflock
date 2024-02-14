package dev.farid.skyflock.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
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
            Entity renderEntity = mc.getRenderViewEntity();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();

            double realX = renderEntity.lastTickPosX + (renderEntity.posX - renderEntity.lastTickPosX) * partialTicks;
            double realY = renderEntity.lastTickPosY + (renderEntity.posY - renderEntity.lastTickPosY) * partialTicks;
            double realZ = renderEntity.lastTickPosZ + (renderEntity.posZ - renderEntity.lastTickPosZ) * partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.translate(-realX, -realY, -realZ);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GL11.glDisable(3553);
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
            GlStateManager.enableDepth();

            if (phase)
                GlStateManager.depthMask(true);

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }

        public static void drawArrowToEntity(Entity entityIn, Color colour, float thickness, float partialTicks) {
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

            double[] entXZ = normalisedEntityCoords(entityIn, renderX, renderZ, partialTicks);
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
    }

    private static double[] normalisedEntityCoords(Entity entityIn, double renderX, double renderZ, float partialTicks) {
        double entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        double entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;

        double dX = entityX - renderX;
        double dZ = entityZ - renderZ;
        float d = (float) Math.sqrt(dX * dX + dZ * dZ);

        // returns x z coords of entity, normalised to 2 block away from player
        return new double[]{renderX + (dX / d) * 2, renderZ + (dZ / d) * 2};
    }
}
