package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftSounds;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.StringListHelperDiscraft;
import net.discraft.mod.gui.menu.GuiDiscraftManager;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.notification.ClientNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;

public class GuiDiscraftScrollerSlotModule extends GuiScrollerSlot {

    public DiscraftModule module;

    public boolean playedSound = false;

    public int width;
    public int height;

    public float descriptionFade = 0;
    public float descriptionMaxFade = .5f;

    public int slotIndex;
    public int maxIndex;

    public GuiDiscraftScrollerSlotModule(DiscraftModule givenModule, int givenWidth, int givenHeight, int givenIndex, int givenMaxIndex) {

        this.module = givenModule;

        this.height = givenHeight;
        this.width = givenWidth;

        this.slotIndex = givenIndex;
        this.maxIndex = givenMaxIndex;

    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean canSelect() {
        return true;
    }

    @Override
    public void doRender(int mouseX, int mouseY, float partialTicks) {

        if (isHovered(mouseX, mouseY)) {
            GuiUtils.renderRect(this.posX + 2, this.posY + 2, width + 1, height - 8, 0xFF5E9D34);

            if (!playedSound) {
                playedSound = true;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 2.0F));
            }

        } else {
            playedSound = false;
        }

        GuiUtils.renderImageTransparent(this.posX + 3, this.posY + 3, this.module.moduleLogo, width - 1, height - 10, 1);
        if (!this.module.isEnabled) {
            GuiUtils.renderRect(this.posX + 3, this.posY + 3, width - 1, height - 10, 0xAA000000);
        }
        GuiUtils.renderTextScaled(this.module.moduleName + ((!this.module.isEnabled) ? ChatFormatting.RED + " Disabled" : ""), this.posX + 3, this.posY + this.height - 5, this.module.isEnabled ? 0xFFFFFF : 0x777777, .5);

        glDisable(GL_SCISSOR_TEST);

        if (isHovered(mouseX, mouseY)) {

            GlStateManager.pushMatrix();

            if (descriptionFade < descriptionMaxFade) {
                descriptionFade += 0.1f;
            }

            int descWidth = 150;
            int descHeight = 70;
            int descX = this.posX - descWidth - 10;
            int descY = this.posY;

            GuiUtils.renderRectWithOutline(descX + descWidth + 1, descY + 3, 4, 35, Discraft.getInstance().colorTheme, Discraft.getInstance().colorTheme, 1);

            GuiUtils.renderRectWithOutline(descX, descY, descWidth, descHeight, Discraft.getInstance().colorTheme, Discraft.getInstance().colorTheme, 1);
            GuiUtils.renderRectWithGradientWithAlpha(descX, descY + (descHeight / 2), descWidth, descHeight / 2, 0x00000000, 0xFF5E9D34, 0, 0, descriptionFade);

            GuiUtils.renderText(this.module.moduleName, descX + 2, descY + 3, 0xFFFFFF);
            GuiUtils.renderTextScaled(this.module.moduleDescription, descX + 2, descY + 12, 0xFFFFFF, .5);
            GuiUtils.renderTextScaled(I18n.format("discraft.module.creator", ChatFormatting.GREEN + this.module.moduleAuthor), descX + 2, descY + 65, 0xFFFFFF, .5);

            int hx = descX + 2;
            int hy = descY - 5;

            ArrayList<String> desc = StringListHelperDiscraft.getListLimitWidth(this.module.moduleDescriptionLong, descWidth * 2);
            desc.add("");
            desc.add(ChatFormatting.WHITE + I18n.format("discraft.module.leftclick", ChatFormatting.GREEN + I18n.format("discraft.module.leftclick.tip") + ChatFormatting.RESET));

            if (module.getConfigFile() != null) {
                desc.add(ChatFormatting.WHITE + I18n.format("discraft.module.rightclick", ChatFormatting.GREEN + I18n.format("discraft.module.rightclick.tip") + ChatFormatting.RESET));
            }

            for (int i = 0; i < desc.size(); i++) {
                String var1 = desc.get(i);
                GuiUtils.renderTextScaled(ChatFormatting.GRAY + var1, hx, hy + 25 + (i * 6), 0xFFFFFF, .5f);
            }

            GlStateManager.popMatrix();


        } else {

            descriptionFade = 0;

        }

    }

    @Override
    public void clicked(int mouseX, int mouseY) {

        if (Mouse.isButtonDown(0)) {
            this.module.isEnabled = !this.module.isEnabled;

            if (!this.module.isEnabled) {
                this.module.onModuleDisable();
            }

            Discraft.getInstance().discraftSettingsModules.saveConfig();

            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, this.module.isEnabled ? 1.0F : .8F));
            ClientNotification.clearNotifications();
            ClientNotification.createNotification(this.module.moduleName, "Module " + (this.module.isEnabled ? ChatFormatting.GREEN + "Enabled!" : ChatFormatting.RED + "Disabled!"));
        } else if (Mouse.isButtonDown(1)) {

            if (module.getConfigFile() != null) {
                try {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiDiscraftManager(this.scroller.parentGUI, module));
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1f));
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(DiscraftSounds.ZOOM_IN, 2f));
                } catch (IOException e) {
                    ClientNotification.createNotification("Error loading settings for module '" + module.moduleName + "'");
                }
            }

        }

        super.clicked(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public int width() {
        return this.width;
    }

}
