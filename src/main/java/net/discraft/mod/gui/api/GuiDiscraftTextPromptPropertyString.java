package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;

public class GuiDiscraftTextPromptPropertyString extends GuiDiscraftTextPrompt {

    public GuiDiscraftScrollerSlotSetting settingsSlot;

    public GuiDiscraftTextPromptPropertyString(GuiScreen par1, GuiDiscraftScrollerSlotSetting givenSlot) {
        super(par1);

        this.settingsSlot = givenSlot;

        this.addInformation(
                ChatFormatting.WHITE + "Set Property Value",
                ChatFormatting.WHITE + "",
                ChatFormatting.WHITE + "Please enter a Value"
        );

        this.maxCharacters = 255;
        this.restrictedUsernameInput = false;

    }

    public GuiDiscraftTextPromptPropertyString isNumerical(int givenMax) {
        this.mustBeNumber = true;
        this.maxNumber = givenMax;

        this.addTip(ChatFormatting.GREEN + "Value must be numerical!" + (this.maxNumber > 0 ? " (Max " + this.maxNumber + ")" : ""));

        return this;
    }

    @Override
    public void onPromptEntered() {
        String var1 = this.getTextField();

        settingsSlot.setProperty(var1);

        this.mc.displayGuiScreen(this.parentGui);

    }
}
