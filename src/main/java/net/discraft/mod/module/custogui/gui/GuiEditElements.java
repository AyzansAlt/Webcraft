package net.discraft.mod.module.custogui.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.api.GuiDiscraftButton;
import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.discraft.mod.module.custogui.utils.GuiElement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiEditElements extends GuiDiscraftScreen {

    public Module_CustoGUI module;

    public int BUTTON_ADDELEMENT = 4001;

    public GuiButton buttonAddElement;

    public GuiEditElements(Module_CustoGUI givenModule){
        module = givenModule;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {

        GuiUtils.renderRect(0,0,width,height,0x77000000);

        GuiUtils.renderCenteredTextScaledWithOutline("Edit In-game GUI",width / 2,12,0xFFFFFFFF,0xFF000000,1);
        GuiUtils.renderCenteredTextScaled(ChatFormatting.GRAY + "Click the 'add elements' button to begin,",width / 2,25,0xFFFFFFFF,.5);
        GuiUtils.renderCenteredTextScaled(ChatFormatting.GRAY + "or click and drag elements to reposition them.",width / 2,30,0xFFFFFFFF,.5);

        GuiUtils.renderCenteredTextScaled(ChatFormatting.WHITE + "Press ESC to close",width / 2,height - 15,0xFFFFFFFF,.5);

        GuiUtils.renderRectWithGradient(0,0,width,height,0x00000000,0xAA000000,1);

        mc.ingameGUI.renderGameOverlay(partialTicks);

        for(GuiElement element : this.module.guiElements){
            if(!element.isEditable){
                element.isEditable = true;
            }
            element.onRender(mc,partialTicks);
        }

        super.drawScreen(mouseX,mouseY,partialTicks);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        buttonAddElement = new GuiDiscraftButton(BUTTON_ADDELEMENT,(this.width / 2) - (70 / 2),40,70,15,ChatFormatting.WHITE + I18n.format("discraft.button.module.custogui.addelement")).addToolTip(I18n.format("discraft.button.module.custogui.addelement.tip"));
        this.buttonList.add(buttonAddElement);

    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        for(GuiElement element : this.module.guiElements){
            element.onUpdate(mc);
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);

        if(button.id == BUTTON_ADDELEMENT){
            mc.displayGuiScreen(new GuiDiscraftDropdownAddElement(this,button.x + button.width + 1,button.y,75,15, this.module));
            return;
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX,mouseY,mouseButton);

        for(GuiElement element : this.module.guiElements){
            element.onClick(mouseX,mouseY,mouseButton);
        }

    }

}
