package net.discraft.mod.gui.api;

import net.discraft.mod.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiDCButton extends GuiButton {

    public int buttonColor = 0xFFFFFFFF;

    public boolean playedSound = false;

    public String toolTip;

    public boolean flip = false;
    public boolean renderOutline = false;
    public int outlineColor = 0x00000000;

    public GuiDCButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public GuiDCButton setColor(int givenColor) {
        this.buttonColor = givenColor;
        return this;
    }

    public GuiDCButton setToolTip(String givenToolTip) {
        this.toolTip = givenToolTip;
        return this;
    }

    public GuiDCButton flip() {
        this.flip = true;
        return this;
    }

    public GuiDCButton renderOutline(int givenColor) {
        this.renderOutline = true;
        this.outlineColor = givenColor;
        return this;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (this.visible) {

            FontRenderer fontrenderer = mc.fontRenderer;

            int color = this.hovered ? 0x61A137 : 0xFFFFFF;

            if (renderOutline) {
                GuiUtils.renderCenteredTextWithOutline(this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, color, this.outlineColor);
            } else {
                GuiUtils.renderCenteredText(this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
            }
            if (this.hovered) {

                if (!playedSound) {
                    playedSound = true;
                    mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 2.0F));
                }

                if (this.toolTip != null) {
                    String toolTipFormatted = I18n.format(this.toolTip);
                    GuiUtils.renderTextWithOutline(toolTipFormatted, mouseX - (this.flip ? fontrenderer.getStringWidth(toolTipFormatted) : 0), mouseY, 0xFFFFFF, 0x304738);
                }
            } else {
                playedSound = false;
            }

        }
    }

}
