package dev.farid.skyflock.utils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    public static void guiDrawLine(float x1, float y1, float x2, float y2, float thickness) {
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
