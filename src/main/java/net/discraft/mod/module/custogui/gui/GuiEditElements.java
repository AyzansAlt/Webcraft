package net.discraft.mod.module.custogui.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.api.GuiDiscraftButton;
import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.discraft.mod.module.custogui.utils.GuiElement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiEditElements extends GuiDiscraftScreen {

    public Module_CustoGUI module;

    public int BUTTON_ADDELEMENT = 4001;
    public int BUTTON_DELETEELEMENT = 4002;
    public int BUTTON_DELETEALLELEMENTS = 4003;

    public GuiButton buttonAddElement;
    public GuiButton buttonDeleteElement;
    public GuiButton buttonDeleteAllElement;

    public GuiEditElements(Module_CustoGUI givenModule) {
        module = givenModule;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        for (GuiElement element : this.module.guiElements) {
            element.onUpdateVisuals(mouseX,mouseY,mc);
        }

        GuiUtils.renderRect(0, 0, width, height, Discraft.getInstance().colorTheme);

        GuiUtils.renderCenteredTextScaledWithOutline("Edit In-game GUI", width / 2, 12, 0xFFFFFFFF, 0xFF000000, 1);
        GuiUtils.renderCenteredTextScaled(ChatFormatting.GRAY + "Click the 'add elements' button to begin,", width / 2, 25, 0xFFFFFFFF, .5);
        GuiUtils.renderCenteredTextScaled(ChatFormatting.GRAY + "or click and drag elements to reposition them.", width / 2, 30, 0xFFFFFFFF, .5);

        GuiUtils.renderCenteredTextScaled(ChatFormatting.WHITE + "Press ESC to close", width / 2, height - 15, 0xFFFFFFFF, .5);

        GuiUtils.renderRectWithGradient(0, 0, width, height, 0x00000000, 0xAA000000, 0);

        mc.ingameGUI.renderGameOverlay(partialTicks);

        for (GuiElement element : this.module.guiElements) {
            if (!element.isEditable) {
                element.isEditable = true;
            }
            element.onRender(mc, partialTicks);
        }

        this.updateScreen();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonAddElement = new GuiDiscraftButton(BUTTON_ADDELEMENT, (this.width / 2) - (70 / 2), 40, 70, 15, ChatFormatting.WHITE + I18n.format("discraft.button.module.custogui.addelement")).addToolTip(I18n.format("discraft.button.module.custogui.addelement.tip"));
        buttonDeleteElement = new GuiDiscraftButton(BUTTON_DELETEELEMENT, (this.width / 2) - (70 / 2), 60, 70, 15, ChatFormatting.RED + I18n.format("discraft.button.module.custogui.deleteelement")).addToolTip(I18n.format("discraft.button.module.custogui.deleteelement.tip"));
        buttonDeleteAllElement = new GuiDiscraftButton(BUTTON_DELETEALLELEMENTS, (this.width / 2) - (70 / 2), 80, 70, 15, ChatFormatting.RED + I18n.format("discraft.button.module.custogui.deleteallelements")).addToolTip(I18n.format("discraft.button.module.custogui.deleteallelements.tip"));

        this.buttonList.add(buttonAddElement);
        this.buttonList.add(buttonDeleteElement);
        this.buttonList.add(buttonDeleteAllElement);

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.id == BUTTON_ADDELEMENT) {
            mc.displayGuiScreen(new GuiDiscraftDropdownAddElement(this, button.x + button.width + 1, button.y, 75, 15, this.module));
            return;
        }

        if (button.id == BUTTON_DELETEELEMENT){

            for(GuiElement element : this.module.guiElements){
                if(element.isSelected){
                    this.module.removeGuiElement(element);
                }
            }

        }

        if (button.id == BUTTON_DELETEALLELEMENTS){

            for(GuiElement element : this.module.guiElements){
                this.module.removeGuiElement(element);
            }

        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        super.mouseClicked(mouseX,mouseY,mouseButton);

        for (GuiElement element : this.module.guiElements) {
            element.onClick(mouseX, mouseY, mouseButton);
        }

    }

}
