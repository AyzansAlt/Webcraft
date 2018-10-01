package net.discraft.mod.gui.api;

import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public abstract class GuiDiscraftDropdown extends GuiDiscraftScreen {

    protected GuiScreen parentGui;
    protected ArrayList<String> optionsList = new ArrayList<String>();
    protected ArrayList<String> optionsListTooltip = new ArrayList<String>();
    protected ArrayList<Boolean> optionsListEnabled = new ArrayList<Boolean>();
    protected int buttonX;
    protected int buttonY;
    protected int buttonWidth = 85;
    protected int buttonHeight = 20;
    private boolean isButtonClicked = false;
    private boolean isOpened = false;

    /**
     * Create a new drop down menu. Parent Gui, x, y, width, height
     */
    public GuiDiscraftDropdown(GuiScreen par1, int par2, int par3, int par4, int par5) {
        this.parentGui = par1;
        this.buttonX = par2;
        this.buttonY = par3;
        this.buttonWidth = par4;
        this.buttonHeight = par5;
        this.optionsList.clear();
    }

    public GuiDiscraftDropdown addOption(String par1) {
        this.optionsList.add(par1);
        this.optionsListTooltip.add("");
        this.optionsListEnabled.add(true);
        return this;
    }

    public GuiDiscraftDropdown addOption(String par1, String par2) {
        this.optionsList.add(par1);
        this.optionsListTooltip.add(par2);
        this.optionsListEnabled.add(true);
        return this;
    }

    public GuiDiscraftDropdown addOption(String par1, boolean par2) {
        this.optionsList.add(par1);
        this.optionsListEnabled.add(par2);
        return this;
    }

    @Override
    public void initGui() {

        if (this.optionsList.size() <= 0) {
            this.mc.displayGuiScreen(this.parentGui);
        }

        for (int i = 0; i < this.optionsList.size(); i++) {
            int y = i * (this.buttonHeight + 1);

            GuiDiscraftButton button;

            if(this.optionsListTooltip.get(i).length() > 0){
                button = new GuiDiscraftButton(i + 1, this.buttonX, this.buttonY + y, this.buttonWidth, this.buttonHeight, this.optionsList.get(i)).addToolTip(this.optionsListTooltip.get(i));
            } else {
                button = new GuiDiscraftButton(i + 1, this.buttonX, this.buttonY + y, this.buttonWidth, this.buttonHeight, this.optionsList.get(i));
            }

            boolean enabled = this.optionsListEnabled.get(i);
            button.enabled = enabled;
            this.buttonList.add(button);
        }

        if (this.isOpened == true) {
            this.mc.displayGuiScreen(this.parentGui);
        }

        this.isOpened = true;
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        this.onOptionClicked(guibutton.id);
    }

    public abstract void onOptionClicked(int par1);

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);

        if (this.isButtonClicked == false && this.mc.currentScreen == this) {
            this.mc.displayGuiScreen(this.parentGui);
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        if (mc.world != null) {
            this.parentGui.drawScreen(i, j, f);
        }

        for (int k = 0; k < this.buttonList.size(); ++k) {

            GuiButton guibutton = (GuiButton) this.buttonList.get(k);
            guibutton.drawButton(this.mc, i, j, f);
        }
    }
}

