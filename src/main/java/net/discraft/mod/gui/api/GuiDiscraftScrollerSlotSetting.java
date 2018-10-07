package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.DiscraftSounds;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.menu.GuiDiscraftManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;

public class GuiDiscraftScrollerSlotSetting extends GuiScrollerSlot {

    public GuiDiscraftManager moduleManager;
    public String property;

    public GuiDiscraftScrollerSlotSetting(GuiDiscraftManager givenModuleManager, String givenProperty) {
        this.moduleManager = givenModuleManager;
        this.property = givenProperty;
    }

    public void doRender(int mouseX, int mouseY, float partialTicks) {
        drawBackground(mouseX, mouseY);

        String propertyValue = this.moduleManager.properties.getProperty(this.property);
        int propertyWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(propertyValue);

        GuiUtils.renderText(this.property, this.posX + 4, this.posY + 4, 0xFFFFFF);
        GuiUtils.renderText(getPropertyFormatted(propertyValue), (this.posX + this.scroller.width) - propertyWidth - 22, this.posY + 4, 0xFFFFFF);

        Buttons.draw(buttons, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean canSelect() {
        return false;
    }

    @Override
    public int height() {
        return 15;
    }

    @Override
    public void clicked(int mouseX, int mouseY) {

        String propertyValue = this.moduleManager.properties.getProperty(this.property);

        if (isHovered(mouseX, mouseY)) {

            if (Mouse.isButtonDown(0)) {
                if (propertyValue.equalsIgnoreCase("true")) {
                    propertyValue = "false";
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(DiscraftSounds.SETTING_CHANGE, .9F));
                } else if (propertyValue.equalsIgnoreCase("false")) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(DiscraftSounds.SETTING_CHANGE, 1F));
                    propertyValue = "true";
                } else {
                    if(isNumeric(propertyValue)) {
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1F));
                        Minecraft.getMinecraft().displayGuiScreen(new GuiDiscraftTextPromptPropertyString(this.scroller.parentGUI, this).isNumerical(10));
                    } else {
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1F));
                        Minecraft.getMinecraft().displayGuiScreen(new GuiDiscraftTextPromptPropertyString(this.scroller.parentGUI, this));
                    }
                }

                setProperty(propertyValue);

            }

        }
    }

    public void setProperty(String givenValue){
        this.moduleManager.properties.setProperty(this.property, givenValue);
        this.moduleManager.module.getSettings().loadConfig(this.moduleManager.properties);
        this.moduleManager.module.getSettings().saveConfig();
    }

    public String getPropertyFormatted(String givenProperty) {

        switch (givenProperty) {
            case "true":
                return ChatFormatting.GREEN + givenProperty;
            case "false":
                return ChatFormatting.RED + givenProperty;
            default:
                return ChatFormatting.DARK_GRAY + givenProperty;
        }

    }

    public enum EnumPropertyType {

        INTEGER,
        BOOLEAN,
        STRING

    }

    public static boolean isNumeric(String str) {
        try {
            int number = Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
