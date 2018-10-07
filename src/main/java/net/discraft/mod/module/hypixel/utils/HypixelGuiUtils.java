package net.discraft.mod.module.hypixel.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.ScissorState;
import net.discraft.mod.module.hypixel.Module_Hypixel;
import net.discraft.mod.module.hypixel.utils.profileobjects.HypixelProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HypixelGuiUtils {

    public Module_Hypixel hypixelModule;

    public HypixelGuiUtils(Module_Hypixel givenModule) {
        this.hypixelModule = givenModule;
    }

    public void renderHypixelStatsBox(int givenX, int givenY, EntityPlayer givenPlayer, int givenWidth, int givenHeight) {

        GlStateManager.pushMatrix();

        HypixelProfile profile = this.hypixelModule.hypixelProfileManager.getProfileFromUUID(givenPlayer.getGameProfile().getId().toString());

        GuiUtils.renderImageCenteredTransparent(givenWidth / 2, givenHeight - 73, new ResourceLocation(Discraft.MOD_ID, "textures/gui/hypixelstats.png"), 160, 70, .85);
        GuiUtils.renderText(givenPlayer.getDisplayName().getFormattedText(), givenX + 5, givenY + 5, 0xFFFFFF);

        ScissorState.scissor(givenX + 1, givenY + 1, 158, 68, true);

        GL11.glPushMatrix();
        GuiUtils.renderOtherPlayer((AbstractClientPlayer) givenPlayer, givenX + 45, givenY - 5);
        GL11.glPopMatrix();

        if (profile != null) {

            Date firstLoginDate = new Date(profile.firstLogin);
            Date lastLoginDate = new Date(profile.lastLogin);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat dateFormatLoggedIn = new SimpleDateFormat("HH:mm:ss");

            if (!profile.isBot) {

                GuiUtils.renderText(givenPlayer.getDisplayName().getFormattedText(), givenX + 5, givenY + 5, 0xFFFFFF);

                GuiUtils.renderText("Rank: " + profile.hypixelRank.toEnglishFormatted(), givenX + 5, givenY + 15, 0xFFFFFF);
                GuiUtils.renderText("EXP: " + ChatFormatting.YELLOW + (long) profile.networkEXP, givenX + 5, givenY + 24, 0xFFFFFF);

                if (profile.hypixelProfileSettings != null) {
                    if (!profile.hypixelProfileSettings.allowFriendRequests) {

                        GuiUtils.renderCenteredTextScaledWithOutline("Has Friend" + (!profile.hypixelProfileSettings.allowGuildRequests ? " and Guild" : "") + " Requests Disabled", givenWidth / 2, givenHeight - 46, 0xFF0000, 0x000000, 0.5f);

                    } else if (!profile.hypixelProfileSettings.allowFriendRequests) {

                        GuiUtils.renderCenteredTextScaledWithOutline(!profile.hypixelProfileSettings.allowFriendRequests ? "Has Guild Requests Disabled" : "", givenWidth / 2, givenHeight - 46, 0xFF0000, 0x000000, 0.5f);

                    }
                }

                switch (Discraft.getInstance().discraftPresence.getCurrentGamemode()) {
                    default:
                        GuiUtils.renderText("Joined: " + ChatFormatting.GRAY + dateFormat.format(firstLoginDate), givenX + 5, givenY + 33, 0xFFFFFF);
                        GuiUtils.renderText("Login: " + ChatFormatting.GRAY + dateFormatLoggedIn.format(lastLoginDate), givenX + 5, givenY + 42, 0xFFFFFF);
                        GuiUtils.renderText("Karma: " + ChatFormatting.LIGHT_PURPLE + profile.karma, givenX + 5, givenY + 51, 0xFFFFFF);
                        break;
                }

            } else {
                GuiUtils.renderText(givenPlayer.getDisplayName().getFormattedText(), givenX + 5, givenY + 5, 0xFFFFFF);
                GuiUtils.renderText(ChatFormatting.RED + I18n.format("discraft.hypixel.gui.profile.unknown"), givenX + 5, givenY + 15, 0xFFFFFF);


            }

        } else {
            GuiUtils.renderText(ChatFormatting.GRAY + "Fetching details...", givenX + 5, givenY + 15, 0xFFFFFF);
        }

        ScissorState.endScissor();

        GlStateManager.popMatrix();

    }

}
