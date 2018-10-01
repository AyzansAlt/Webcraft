package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.StringListHelperDiscraft;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.notification.ClientNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

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

    public GuiDiscraftScrollerSlotModule(DiscraftModule givenModule, int givenWidth, int givenHeight) {

        this.module = givenModule;

        this.height = givenHeight;
        this.width = givenWidth;

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

            GuiUtils.renderRectWithOutline(descX + descWidth + 1, descY + 3, 4, 35, 0x77000000, 0x77000000, 1);

            GuiUtils.renderRectWithOutline(descX, descY, descWidth, descHeight, 0x77000000, 0x77000000, 1);
            GuiUtils.renderRectWithGradientWithAlpha(descX, descY + (descHeight / 2), descWidth, descHeight / 2, 0x00000000, 0xFF5E9D34, 1, 0, descriptionFade);

            GuiUtils.renderText(this.module.moduleName, descX + 2, descY + 3, 0xFFFFFF);
            GuiUtils.renderTextScaled(this.module.moduleDescription, descX + 2, descY + 12, 0xFFFFFF, .5);
            GuiUtils.renderTextScaled("Created by " + ChatFormatting.GREEN + this.module.moduleAuthor, descX + 2, descY + 65, 0xFFFFFF, .5);

            int hx = descX + 2;
            int hy = descY - 5;

            ArrayList<String> desc = StringListHelperDiscraft.getListLimitWidth(this.module.moduleDescriptionLong, descWidth * 2);
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

        if (isHovered(mouseX,mouseY)) {
            this.module.isEnabled = !this.module.isEnabled;

            if (!this.module.isEnabled) {
                this.module.onModuleDisable();
            }

            Discraft.getInstance().discraftSettingsModules.saveConfig();

            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, this.module.isEnabled ? 1.0F : .8F));
            ClientNotification.clearNotifications();
            ClientNotification.createNotification(this.module.moduleName, "Module " + (this.module.isEnabled ? ChatFormatting.GREEN + "Enabled!" : ChatFormatting.RED + "Disabled!"));

        }

        super.clicked(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
    }

    @Override
    protected int height() {
        return this.height;
    }

}
