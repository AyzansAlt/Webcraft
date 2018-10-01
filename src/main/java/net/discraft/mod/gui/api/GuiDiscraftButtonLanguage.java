package net.discraft.mod.gui.api;

import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDiscraftButtonLanguage extends GuiDiscraftButton
{
    public GuiDiscraftButtonLanguage(int buttonID, int xPos, int yPos)
    {
        super(buttonID, xPos, yPos, 20, 20, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        super.drawButton(mc,mouseX,mouseY,partialTicks);

        if(this.visible){
            GuiUtils.renderImageTransparent(this.x + 2,this.y + 2,new ResourceLocation(Discraft.MOD_ID,"textures/gui/language.png"),this.width - 4,this.height - 4,1);
        }

    }
}