package net.discraft.mod.gui.api;

import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonDiscraftOptions extends GuiButton {
    public GuiButtonDiscraftOptions(int buttonID, int xPos, int yPos) {
        super(buttonID, xPos, yPos, 20, 20, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        String toolTipFormatted = I18n.format("discraft.ingame.menu.options.tooltip.reload");

        if (this.visible) {
            GuiUtils.renderImage(this.x, this.y + 2, new ResourceLocation(Discraft.MOD_ID, "textures/gui/logo.png"), this.width, this.height - 4);

            if (this.hovered) {
                GuiUtils.renderTextWithOutline(toolTipFormatted, mouseX, mouseY, 0xFFFFFF, 0x304738);
            }

        }
    }
}