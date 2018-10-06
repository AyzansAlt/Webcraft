package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
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
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, .9F));
                } else if (propertyValue.equalsIgnoreCase("false")) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, 1F));
                    propertyValue = "true";
                } else {
                    System.out.println("Meh. Idk");
                }

                this.moduleManager.properties.setProperty(this.property, propertyValue);
                this.moduleManager.module.getSettings().loadConfig(this.moduleManager.properties);
                this.moduleManager.module.getSettings().saveConfig();

            }

        }
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

}
