package net.discraft.mod.module.custogui.gui;

import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.discraft.mod.module.custogui.utils.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiElement_CPS extends GuiElement {

    public String cpsOutput = "Unknown";

    public boolean enableBackground = false;

    public GuiElement_CPS(int givenPosX, int givenPosY, GuiScreen givenGUI, Module_CustoGUI givenModule) {
        super(givenPosX, givenPosY, givenGUI, givenModule);

        this.width = 30;
        this.height = 7;

    }

    @Override
    public void onRender(Minecraft mc, float parTick) {
        super.onRender(mc, parTick);

        cpsOutput = "CPS: " + "34";

        GuiUtils.renderTextWithOutline(cpsOutput, this.posX, this.posY, 0xFFFFFF00, 0xFF000000);

    }

    @Override
    public void onUpdate(Minecraft mc) {
        super.onUpdate(mc);
        this.width = mc.fontRenderer.getStringWidth(cpsOutput);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {

        Minecraft mc = Minecraft.getMinecraft();

        if (isHovered(mouseX, mouseY) && mouseButton == 0) {

            if (this.isSelected) {
                mc.displayGuiScreen(new GuiElement_CPS_Edit(this, this.parentGUI));
            }

        }

        super.onClick(mouseX, mouseY, mouseButton);

    }

}
