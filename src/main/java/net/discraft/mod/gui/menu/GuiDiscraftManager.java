package net.discraft.mod.gui.menu;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.DiscraftSounds;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.api.GuiDiscraftButton;
import net.discraft.mod.gui.api.GuiDiscraftScroller;
import net.discraft.mod.gui.api.GuiDiscraftScrollerSlotSetting;
import net.discraft.mod.module.DiscraftModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GuiDiscraftManager extends GuiDiscraftScreen {

    public GuiScreen parentGui;

    public DiscraftModule module;
    public Properties properties = new Properties();

    public GuiDiscraftScroller moduleSettingsScroller;

    public int ySpacing = 20;

    public int containerX = (this.width / 2);
    public int containerY = (this.height / 2);

    int containerWidth = 250;
    int containerHeight = 100;

    public GuiDiscraftManager(GuiScreen givenScreen, DiscraftModule givenModule) throws IOException {
        this.parentGui = givenScreen;
        this.module = givenModule;

        this.moduleSettingsScroller = new GuiDiscraftScroller(0, 0, 0, 0, 0, this);

        if (givenModule.getConfigFile() != null) {
            InputStream input = new FileInputStream(givenModule.getConfigFile());
            this.properties.load(input);
        }

    }

    @Override
    public void initGui() {

        super.initGui();

        containerWidth = 240;
        containerHeight = 100;

        containerX = (this.width / 2) - (containerWidth / 2);
        containerY = (this.height / 2) - (containerHeight / 2);

        moduleSettingsScroller = new GuiDiscraftScroller(0, containerX + 6, containerY + 20, containerWidth - 12, containerHeight - 26, this);
        moduleSettingsScroller.shouldDrawBackground = false;

        GuiDiscraftButton exitButton = new GuiDiscraftButton(0, containerX + containerWidth - 13, containerY + 5, 8, 8, ChatFormatting.BOLD + "X");
        exitButton.buttonColor = 0xFF509026;

        this.buttonList.add(exitButton);
        this.containerList.add(moduleSettingsScroller);

        for (String property : this.properties.stringPropertyNames()) {
            this.moduleSettingsScroller.slots.add(new GuiDiscraftScrollerSlotSetting(this, property));
            this.moduleSettingsScroller.onSlotHeightChanged();
        }

        this.moduleSettingsScroller.onSlotHeightChanged();
        this.moduleSettingsScroller.initGui();

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.parentGui.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();

        GuiUtils.renderRectWithOutline(containerX, containerY, containerWidth, containerHeight, 0xFF509026, 0x77509026, 1);
        GuiUtils.renderRectWithGradient(containerX, containerY, containerWidth, containerHeight, 0x00000000, 0x33000000, 1);

        GuiUtils.renderRectWithOutline(containerX + 6, containerY + 20, containerWidth - 12, containerHeight - 26, 0x77213812, 0x77213812, 1);
        GuiUtils.renderRectWithGradient(containerX + 6, containerY + 20, containerWidth - 12, containerHeight - 26, 0x00000000, 0x33000000, 1);

        GuiUtils.renderText(module.moduleName + " (" + (module.isEnabled ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.RED + "Disabled") + ChatFormatting.RESET + ")", containerX + 3, containerY + 3, 0xFFFFFF);
        GuiUtils.renderTextScaled(I18n.format("discraft.module.creator", ChatFormatting.GREEN + this.module.moduleAuthor), containerX + 3, containerY + 13, 0xFFFFFF, .5);

        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(DiscraftSounds.NOTIFICATION, 1f));
                mc.currentScreen = this.parentGui;
                break;
        }
    }

}
