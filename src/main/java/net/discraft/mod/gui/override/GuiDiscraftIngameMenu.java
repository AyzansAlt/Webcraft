package net.discraft.mod.gui.override;

import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.api.GuiDiscraftTextPrompt;
import net.discraft.mod.gui.container.GuiDiscraftContainerModules;
import net.discraft.mod.gui.menu.GuiDiscraftMainMenu;
import net.discraft.mod.gui.menu.GuiDiscraftManager;
import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDiscraftIngameMenu extends GuiDiscraftScreen {

    public GuiDiscraftContainerModules containerModules;

    private int saveStep;
    private int visibleTime;

    private static float moduleFade = 0;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        super.initGui();

        this.saveStep = 0;
        this.buttonList.clear();
        int i = -16;
        int j = 98;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + -16, I18n.format("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning()) {
            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(12, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("fml.menu.modoptions")));

        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats")));

        GuiButton guibutton = this.addButton(new GuiButton(7, this.width / 2 - 100, this.height / 4 + 72 + -16, 200, 20, I18n.format("menu.shareToLan")));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
        //this.buttonList.add(new GuiButton(8, this.width / 2 + 2, this.height / 4 + 72 + -16, 98, 20, I18n.format("discraft.ingame.button.opensettings")));

        containerModules = new GuiDiscraftContainerModules(0, width + 1, 0, 50, height, this);
        if (!this.containerList.contains(containerModules)) {
            addContainer(containerModules);
        }

    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                boolean flag = this.mc.isIntegratedServerRunning();
                boolean flag1 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.world.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);

                if (flag) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                } else if (flag1) {
                    RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new GuiMainMenu());
                } else {
                    this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                }

            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            case 5:
                if (this.mc.player != null)
                    this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.getAdvancementManager()));
                break;
            case 6:
                if (this.mc.player != null)
                    this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 12:
                net.minecraftforge.fml.client.FMLClientHandler.instance().showInGameModOptions(new GuiIngameMenu());
                break;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        ++this.visibleTime;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        GuiUtils.renderImageCentered(width / 2, 23, GuiDiscraftMainMenu.MENU_LOGO, 148, 40);

        if (mc.currentScreen instanceof GuiDiscraftManager
                || mc.currentScreen instanceof GuiDiscraftTextPrompt) {
            if (this.moduleFade < 0.8) {
                this.moduleFade += 0.1f;
            }
        } else {
            if (this.moduleFade > 0) {
                this.moduleFade -= 0.1f;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        GuiUtils.renderRect(0, 0, width, height, 0xBB000000, this.moduleFade);

    }
}