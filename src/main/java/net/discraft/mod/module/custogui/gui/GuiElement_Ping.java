package net.discraft.mod.module.custogui.gui;

import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GuiElement_Ping extends GuiElementText {

    public GuiElement_Ping(int givenPosX, int givenPosY, GuiScreen givenGUI, Module_CustoGUI givenModule) {
        super(givenPosX, givenPosY, givenGUI, givenModule);
    }

    @Override
    public void onRender(Minecraft mc, float parTick) {

        int pingValue = 0;

        if(mc.player != null && mc.player.connection != null && mc.player.connection.getPlayerInfo(mc.player.getUniqueID()) != null){
            pingValue = mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
        }

        this.textValue = "Ping: " + pingValue;

        super.onRender(mc, parTick);
    }

    @Override
    public void onUpdate(Minecraft mc) {
        super.onUpdate(mc);
    }

}
