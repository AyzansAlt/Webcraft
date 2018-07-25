package net.discraft.mod.gui.override;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftSettings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDiscraftOptions extends GuiScreen {

    private final GuiScreen lastScreen;
    /**
     * Reference to the GameSettings object.
     */
    private final DiscraftSettings settings;
    protected String title = "Discraft Options";

    public GuiDiscraftOptions(GuiScreen p_i1046_1_, DiscraftSettings p_i1046_2_) {
        this.lastScreen = p_i1046_1_;
        this.settings = p_i1046_2_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.title = I18n.format("discraft.options.title");

        this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, I18n.format("discraft.options.reloadconfig", Discraft.MOD_VERSION)));
        this.buttonList.add(new GuiButton(201, this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, I18n.format("discraft.options.enablemod", this.settings.enableDiscraft)));

        this.buttonList.add(new GuiButton(300, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));

    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
                case 300:
                    mc.displayGuiScreen(this.lastScreen);
                    return;
                case 200:
                    this.settings.loadConfig();
                    mc.ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + I18n.format("discraft.config.reload")));
                    break;
                case 201:
                    this.settings.enableDiscraft = !this.settings.enableDiscraft;
                    mc.ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + I18n.format("discraft.config.enable", this.settings.enableDiscraft ? I18n.format("discraft.enabled") : I18n.format("discraft.disabled"))));
                    break;
            }
        }
        this.settings.saveConfig();
        mc.displayGuiScreen(new GuiDiscraftOptions(this.lastScreen, this.settings));
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}