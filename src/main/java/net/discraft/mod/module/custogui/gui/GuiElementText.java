package net.discraft.mod.module.custogui.gui;

import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.discraft.mod.module.custogui.utils.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiElementText extends GuiElement {

    String textValue = "";

    public GuiElementText(int givenPosX, int givenPosY, GuiScreen givenGUI, Module_CustoGUI givenModule) {
        super(givenPosX, givenPosY, givenGUI, givenModule);
    }

    @Override
    public void onRender(Minecraft mc, float parTick) {
        super.onRender(mc, parTick);

        this.height = 9;
        this.width = mc.fontRenderer.getStringWidth(textValue) + 1;

        GuiUtils.renderTextWithOutline(textValue, this.posX + 1, this.posY + 1, 0xFFFFFF00, 0xFF000000);

    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {

        Minecraft mc = Minecraft.getMinecraft();

        if (isHovered(mouseX, mouseY) && mouseButton == 0) {

            if (this.isSelected) {
                mc.displayGuiScreen(new GuiElementText_Edit(this, this.parentGUI));
            }

        }

        super.onClick(mouseX, mouseY, mouseButton);

    }

}
