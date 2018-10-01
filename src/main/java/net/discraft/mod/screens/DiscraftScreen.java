package net.discraft.mod.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.discord.Module_Discord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import static net.discraft.mod.module.discord.gui.GuiDiscord.discordBrowser;

public class DiscraftScreen {

    public double posX;
    public double posY;
    public double posZ;

    public Module_Discord discordModule;

    public DiscraftScreen(double givenX, double givenY, double givenZ, Module_Discord givenModule) {
        this.posX = givenX;
        this.posY = givenY;
        this.posZ = givenZ;
        this.discordModule = givenModule;
    }

    public void render() {

        GL11.glPushMatrix();

        float val = (float) (Math.sin(Discraft.getInstance().discraftVariables.swing / 20) / 25);
        float val2 = (float) (Math.sin(Discraft.getInstance().discraftVariables.swing / 50));

        GL11.glRotatef(val2, 0, 1, 0);
        GL11.glTranslated(0, val, 0);

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        float scale = 0.1f;

        GL11.glPushMatrix();

        float width = 1280 * scale;
        float height = 720 * scale;

        GL11.glTranslated(this.posX, this.posY, this.posZ);
        GL11.glTranslated(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);

        GL11.glRotatef(-player.rotationYaw, 0.0F, 1.0F, 0.0F);

        GL11.glScalef(-0.03f, -0.03f, 0.03f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (discordBrowser != null) {

            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated(-(width / 2), 0, 0);
            discordBrowser.draw(0, height, width, 0);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(0, -6.5, 0);
            GuiUtils.renderCenteredTextScaled(ChatFormatting.WHITE + I18n.format("discraft.screen.tip", (GameSettings.getKeyDisplayString(this.discordModule.keyOpenDiscordGUI.getKeyCode()))), 0, 0, 0, .5);
            GL11.glPopMatrix();

        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glPopMatrix();

    }

    public void setPosition(double givenX, double givenY, double givenZ) {
        this.posX = givenX;
        this.posY = givenY;
        this.posZ = givenZ;
    }

}
