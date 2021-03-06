package net.discraft.mod.module.custogui.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.gui.api.GuiDiscraftButton;
import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiElementText_Edit extends GuiDiscraftScreen {

    public GuiElementText elementCPS;

    public GuiScreen parentGUI;

    public int BUTTON_FINISH = 5000;
    public int BUTTON_ENABLEBACKGROUND = 5001;

    public GuiButton buttonFinish;
    public GuiButton buttonEnableBackground;

    public GuiElementText_Edit(GuiElementText givenElement, GuiScreen givenParentGUI) {
        this.elementCPS = givenElement;
        this.parentGUI = givenParentGUI;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        super.initGui();

        int buttonWidth = 200;

        this.buttonEnableBackground = new GuiDiscraftButton(BUTTON_ENABLEBACKGROUND, (this.width / 2) - (buttonWidth / 2), 60, buttonWidth, 20, ChatFormatting.WHITE + "Enable Background (" + (this.elementCPS.shouldDrawBackground ? "True" : "False") + ")");

        this.buttonFinish = new GuiDiscraftButton(BUTTON_FINISH, (this.width / 2) - (buttonWidth / 2), height - 60, buttonWidth, 20, "Finish");

        this.buttonList.add(buttonEnableBackground);
        this.buttonList.add(buttonFinish);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.parentGUI.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.id == BUTTON_ENABLEBACKGROUND) {
            this.elementCPS.shouldDrawBackground = !this.elementCPS.shouldDrawBackground;
            this.initGui();
            return;
        }

        if (button.id == BUTTON_FINISH) {
            mc.displayGuiScreen(parentGUI);
        }

    }

}
