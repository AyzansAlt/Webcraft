package net.discraft.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Gui Utilities
 * <p>
 * A simple utility class for rendering various different graphics, such as text, shapes and more
 * <p>
 * Created by ScottehBoeh
 */
public class GuiUtils {

    /**
     * Render Text
     *
     * @param text  - Given Text (String)
     * @param posX  - Given Text Position X
     * @param posY  - Given Text Position Y
     * @param color - Given Color
     */
    public static void renderText(String text, int posX, int posY, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawString(text, posX, posY, color);
    }

    /**
     * Render Centered Text
     *
     * @param text  - Given Text (String)
     * @param posX  - Given Text Position X
     * @param posY  - Given Text Position Y
     * @param color - Given Color
     */
    public static void renderCenteredText(String text, int posX, int posY, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        renderText(text, posX - mc.fontRenderer.getStringWidth(text) / 2, posY, color);
    }

    /**
     * Render Text Scaled
     *
     * @param text       - Given Text (String)
     * @param posX       - Given Position X
     * @param posY       - Given Position Y
     * @param color      - Given Color
     * @param givenScale - Given Scale
     */
    public static void renderTextScaled(String text, int posX, int posY, int color, double givenScale) {

        GL11.glPushMatrix();
        GL11.glTranslated(posX, posY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        renderText(text, 0, 0, color);
        GL11.glPopMatrix();

    }

    /**
     * Render Centered Text Scaled
     *
     * @param text       - Given Text (String)
     * @param posX       - Given Text Position X
     * @param posY       - Given Text Position Y
     * @param color      - Given Text Color
     * @param givenScale - Given Scale
     */
    public static void renderCenteredTextScaled(String text, int posX, int posY, int color, double givenScale) {

        GL11.glPushMatrix();
        GL11.glTranslated(posX, posY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        renderCenteredText(text, 0, 0, color);
        GL11.glPopMatrix();

    }

    /**
     * Render Text With an Outline
     *
     * @param text         - Given Text (String)
     * @param x            - Given Text Position X
     * @param y            - Given Text Position Y
     * @param color        - Given Text Color
     * @param outlineColor - Given Outline Color
     */
    public static void renderTextWithOutline(String text, int x, int y, int color, int outlineColor) {

        renderText(text, x - 1, y + 1, outlineColor);
        renderText(text, x, y + 1, outlineColor);
        renderText(text, x + 1, y + 1, outlineColor);
        renderText(text, x - 1, y, outlineColor);
        renderText(text, x + 1, y, outlineColor);
        renderText(text, x - 1, y - 1, outlineColor);
        renderText(text, x, y - 1, outlineColor);
        renderText(text, x + 1, y - 1, outlineColor);

        renderText(text, x, y, color);

    }

    /**
     * Render Text Scaled With an Outline
     *
     * @param text         - Given Text (String)
     * @param x            - Given Text Position X
     * @param y            - Given Text Position Y
     * @param color        - Given Text Color
     * @param outlineColor - Given Outline Color
     * @param givenScale   - Given Text Scale
     */
    public static void renderTextScaledWithOutline(String text, int x, int y, int color, int outlineColor, double givenScale) {

        renderTextScaled(text, x - 1, y + 1, outlineColor, givenScale);
        renderTextScaled(text, x, y + 1, outlineColor, givenScale);
        renderTextScaled(text, x + 1, y + 1, outlineColor, givenScale);
        renderTextScaled(text, x - 1, y, outlineColor, givenScale);
        renderTextScaled(text, x + 1, y, outlineColor, givenScale);
        renderTextScaled(text, x - 1, y - 1, outlineColor, givenScale);
        renderTextScaled(text, x, y - 1, outlineColor, givenScale);
        renderTextScaled(text, x + 1, y - 1, outlineColor, givenScale);

        renderTextScaled(text, x, y, color, givenScale);

    }

    /**
     * Render Centered Text With an Outline
     *
     * @param text         - Given Text (String)
     * @param x            - Given Text Position X
     * @param y            - Given Text Position Y
     * @param color        - Given Text Color
     * @param outlineColor - Given Outline Color
     */
    public static void renderCenteredTextWithOutline(String text, int x, int y, int color, int outlineColor) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = mc.fontRenderer;

        renderText(text, x - 1 - fr.getStringWidth(text) / 2, y + 1, outlineColor);
        renderText(text, x - fr.getStringWidth(text) / 2, y + 1, outlineColor);
        renderText(text, x + 1 - fr.getStringWidth(text) / 2, y + 1, outlineColor);
        renderText(text, x - 1 - fr.getStringWidth(text) / 2, y, outlineColor);
        renderText(text, x + 1 - fr.getStringWidth(text) / 2, y, outlineColor);
        renderText(text, x - 1 - fr.getStringWidth(text) / 2, y - 1, outlineColor);
        renderText(text, x - fr.getStringWidth(text) / 2, y - 1,outlineColor);
        renderText(text, x + 1 - fr.getStringWidth(text) / 2, y - 1, outlineColor);

        renderText(text, x - fr.getStringWidth(text) / 2, y, color);
    }

    /**
     * Render a Rectangle
     *
     * @param givenPosX   - Given Start Position X
     * @param givenPosY   - Given start Position Y
     * @param givenWidth  - Given Rectangle Width
     * @param givenHeight - Given Rectangle Height
     * @param givenColor  - Given Rectangle Color
     */
    public static void renderRect(int givenPosX, int givenPosY, int givenWidth, int givenHeight, int givenColor) {

        givenWidth = givenPosX + givenWidth;
        givenHeight = givenPosY + givenHeight;

        if (givenPosX < givenWidth) {
            int i = givenPosX;
            givenPosX = givenWidth;
            givenWidth = i;
        }

        if (givenPosY < givenHeight) {
            int j = givenPosY;
            givenPosY = givenHeight;
            givenHeight = j;
        }

        float f3 = (float) (givenColor >> 24 & 255) / 255.0F;
        float f = (float) (givenColor >> 16 & 255) / 255.0F;
        float f1 = (float) (givenColor >> 8 & 255) / 255.0F;
        float f2 = (float) (givenColor & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) givenPosX, (double) givenHeight, 0.0D).endVertex();
        bufferbuilder.pos((double) givenWidth, (double) givenHeight, 0.0D).endVertex();
        bufferbuilder.pos((double) givenWidth, (double) givenPosY, 0.0D).endVertex();
        bufferbuilder.pos((double) givenPosX, (double) givenPosY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }

    /**
     * Draw a Rectangle with an Outline
     *
     * @param givenPosX         - Given Start Position X
     * @param givenPosY         - Given start Position Y
     * @param givenWidth        - Given Rectangle Width
     * @param givenHeight       - Given Rectangle Height
     * @param givenColor        - Given Rectangle Color
     * @param givenOutlineColor - Given Rectangle Outline Color
     * @param outlineThickness  - Given Outline Thickness
     */
    public static void renderRectWithOutline(int givenPosX, int givenPosY, int givenWidth, int givenHeight, int givenColor, int givenOutlineColor, int outlineThickness) {

        renderRect(givenPosX - outlineThickness, givenPosY - outlineThickness, givenWidth + (outlineThickness * 2), givenHeight + (outlineThickness * 2), givenOutlineColor);
        renderRect(givenPosX, givenPosY, givenWidth, givenHeight, givenColor);

    }

    public static void renderRectWithGradient(int givenPosX, int givenPosY, int givenWidth, int givenHeight, int startColor, int endColor, double givenZLevel) {

        givenWidth = givenPosX + givenWidth;
        givenHeight = givenPosY + givenHeight;

        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) givenWidth, (double) givenPosY, givenZLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) givenPosX, (double) givenPosY, givenZLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) givenPosX, (double) givenHeight, givenZLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) givenWidth, (double) givenHeight, givenZLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();

    }

    public static void renderPositionedImageNoDepth(ResourceLocation par1, double par2, double par3, double par4, float par5, float width, float height) {

        GL11.glPushMatrix();

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        renderPositionedImage(par1, par2, par3, par4, par5, width, height);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glPopMatrix();

    }

    public static void renderPositionedImage(ResourceLocation par1, double par2, double par3, double par4, float par5, float width, float height) {

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        GL11.glPushMatrix();

        GL11.glTranslated(par2, par3, par4);
        GL11.glTranslated(-mc.getRenderManager().viewerPosX,-mc.getRenderManager().viewerPosY,-mc.getRenderManager().viewerPosZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);

        GL11.glRotatef(-player.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(player.rotationPitch, 1.0F, 0.0F, 0.0F);

        GL11.glScalef(-0.03f, -0.03f, 0.03f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderImage(-width / 2, -height / 2, par1, width, height);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }

    public static void renderPositionedTextScaled(String givenText, double par2, double par3, double par4, float par5, int givenColor) {

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        GL11.glPushMatrix();

        GL11.glTranslated(par2, par3, par4);
        GL11.glTranslated(-mc.getRenderManager().viewerPosX,-mc.getRenderManager().viewerPosY,-mc.getRenderManager().viewerPosZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);

        GL11.glRotatef(-player.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(player.rotationPitch, 1.0F, 0.0F, 0.0F);

        GL11.glScalef(-0.03f, -0.03f, 0.03f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderCenteredTextScaled(givenText, 0, 0, givenColor, par5);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }

    public static void renderImage(double x, double y, ResourceLocation image, double width, double height) {
        GL11.glColor3f(1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(image);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_FASTEST);

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        bufferbuilder.pos(x, y + height, 0.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex(0.0D, 0.0D).endVertex();

        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);

    }

}
