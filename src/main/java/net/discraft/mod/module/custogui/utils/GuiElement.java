package net.discraft.mod.module.custogui.utils;

import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;

import java.util.UUID;

public class GuiElement {

    public UUID guiElementID = UUID.randomUUID();

    public Module_CustoGUI module;

    public GuiScreen parentGUI;

    public int height = 1;
    public int width = 1;

    public boolean isEditable = false;
    public boolean isSelected = false;

    public int posX;
    public int posY;

    public int backgroundColor = 0x22000000;
    public int textColor = 0xFFFFFF;
    public int textValueColor = 0xFFFF00;

    public boolean shouldDrawBackground = false;

    public GuiElement(int givenPosX, int givenPosY, GuiScreen givenGUI, Module_CustoGUI givenModule) {
        this.posX = givenPosX;
        this.posY = givenPosY;
        this.module = givenModule;
        this.parentGUI = givenGUI;
    }

    public void onRender(Minecraft mc, float parTick) {

        if (this.isSelected) {
            GuiUtils.renderTextScaled("Selected", this.posX, this.posY + this.height + 3, 0xFFFFFF, .5);
        }

        if (this.shouldDrawBackground) {
            GuiUtils.renderRectWithOutline(this.posX, this.posY, this.width, this.height, backgroundColor, backgroundColor, 1);
        }

    }

    /**
     * On Update Visuals - Update Elements Visually
     *
     * @param mouseX - Given Mouse X
     * @param mouseY - Given Mouse Y
     * @param mc     - Given Minecraft Instance
     */
    public void onUpdateVisuals(int mouseX, int mouseY, Minecraft mc) {

        if (this.isSelected) {
            if (Mouse.isButtonDown(1)) {
                this.posX = mouseX - (this.width / 2);
                this.posY = mouseY - (this.height / 2);
            }
        }

    }

    /**
     * On Update - Update element Attributes
     */
    public void onUpdate(Minecraft mc) {
        if (!this.isEditable) {
            this.isSelected = false;
        }
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {

        if (isHovered(mouseX, mouseY) && mouseButton == 0) {

            for (GuiElement element : Module_CustoGUI.guiElements) {
                element.isSelected = false;
            }

            if (this.isEditable && !this.isSelected) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 2.0F));
                this.isSelected = true;
                return;
            }

        } else {
            if (this.isSelected && mouseButton == 0) {
                this.isSelected = false;
            }
        }

    }

    public boolean isHovered(int mouseX, int mouseY) {
        return GuiUtils.isInBox(posX, posY, getWidth(), getHeight(), mouseX, mouseY);
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

}
