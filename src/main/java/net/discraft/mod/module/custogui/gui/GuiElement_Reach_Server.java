package net.discraft.mod.module.custogui.gui;

import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiElement_Reach_Server extends GuiElementText {

    public GuiElement_Reach_Server(int givenPosX, int givenPosY, GuiScreen givenGUI, Module_CustoGUI givenModule) {
        super(givenPosX, givenPosY, givenGUI, givenModule);
    }

    @Override
    public void onRender(Minecraft mc, float parTick) {
        this.textValue = "S-Reach: " + this.module.elementData.serverReach;

        super.onRender(mc, parTick);
    }

    @Override
    public void onUpdate(Minecraft mc) {
        super.onUpdate(mc);
    }

}
