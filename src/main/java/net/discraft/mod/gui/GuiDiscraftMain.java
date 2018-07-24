package net.discraft.mod.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftCards;
import net.discraft.mod.gui.api.GuiDCButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static net.discraft.mod.Discraft.discordUrl;

public class GuiDiscraftMain extends GuiScreen {

    public static IBrowser discordBrowser;
    public int ySpacing = 20;

    @Override
    public void initGui() {

        DiscraftCards.generateRandom();

        GuiDCButton refreshButton = new GuiDCButton(10, this.width - 40, 3, I18n.format("discraft.ingame.button.reload"))
                .setColor(0xFF4A5A8F)
                .setToolTip("discraft.ingame.button.tooltip.reload")
                .flip()
                .renderOutline(0x304738);

        refreshButton.width = 30;
        refreshButton.height = 15;

        this.buttonList.add(refreshButton);

        if (discordBrowser == null) {

            API api = MCEFApi.getAPI();
            if (api == null) {
                return;
            }

            discordBrowser = api.createBrowser(discordUrl, false);
        } else {
            if (!discordBrowser.getURL().startsWith("https://discordapp.com/")) {
                discordBrowser.loadURL(discordUrl);
            }
        }

        if (discordBrowser != null) {
            discordBrowser.resize(mc.displayWidth, mc.displayHeight - scaleY(ySpacing));
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        GL11.glPushMatrix();

        GuiUtils.renderRect(0, 0, this.width, this.ySpacing, 0xCC509026);
        GuiUtils.renderRect(0, 20, this.width, this.height, 0xBB36393E);

        String loading = I18n.format("discraft.ingame.loading");
        String toolTip_Close = I18n.format("discraft.ingame.tip.close");

        String discraftVersion = ChatFormatting.BOLD + "Discraft " + ChatFormatting.RESET + "v" + Discraft.MOD_VERSION;
        String discraftCredit = "Created by " + ChatFormatting.UNDERLINE + "@ScottehBoeh";

        String loadingCard = ChatFormatting.ITALIC + "*" + DiscraftCards.currentCard + "*";

        if (discordBrowser != null && !discordBrowser.isPageLoading()) {
            GlStateManager.enableAlpha();
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            discordBrowser.draw(0, height, width, ySpacing);
            GlStateManager.enableDepth();
        } else if (discordBrowser.isPageLoading()) {
            GuiUtils.renderCenteredText(loading, (width / 2), height / 2 - 15, 0x66FFFFFF);
            GuiUtils.renderCenteredTextScaled(loadingCard, (width / 2), height / 2 - 5, 0x66FFFFFF, .75f);
        }

        GL11.glPushMatrix();
        GuiUtils.renderImage(0, -2, new ResourceLocation(Discraft.MOD_ID, "textures/gui/logo.png"), 32, 24);
        GL11.glPopMatrix();

        GuiUtils.renderTextWithOutline(toolTip_Close, (width / 2) - (mc.fontRenderer.getStringWidth(toolTip_Close) / 2), 6, 0xFFFFFF, 0x304738);

        GuiUtils.renderTextScaled(discraftVersion, 33, 6, 0xFFFFFF, .5);
        GuiUtils.renderTextScaled(discraftCredit, 33, 11, 0xFFFFFF, .5);

        GuiUtils.renderRectWithGradient(0, this.ySpacing, width, 5, 0x66000000, 0x00000000, this.zLevel);

        for (GuiButton button : this.buttonList) {
            button.drawButton(mc, mouseX, mouseY, partialTicks);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void onGuiClosed() {

    }

    @Override
    public void handleInput() {

        while (Keyboard.next()) {

            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                mc.displayGuiScreen(null);
                return;
            }

            boolean pressed = Keyboard.getEventKeyState();

            char key = Keyboard.getEventCharacter();
            int num = Keyboard.getEventKey();

            int modifier = 0;

            if (Keyboard.isKeyDown(42)) {
                modifier = 1;
            } else if (Keyboard.isKeyDown(29)) {
                modifier = 2;
            } else if (Keyboard.isKeyDown(56)) {
                modifier = 3;
            }

            if (discordBrowser != null) { //Inject events into browser. TODO: Handle keyboard mods.
                if (key != '.' && key != ';' && key != ',') { //Workaround
                    if (pressed) {
                        discordBrowser.injectKeyPressed(key, modifier);
                    } else {
                        discordBrowser.injectKeyReleased(key, modifier);
                    }
                }

                if (key != Keyboard.CHAR_NONE) {
                    discordBrowser.injectKeyTyped(key, modifier);
                }
            }

        }

        while (Mouse.next()) {

            int btn = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            int sx = Mouse.getEventX();
            int sy = Mouse.getEventY();
            int wheel = Mouse.getEventDWheel();

            if (discordBrowser != null) {

                int modifier = 0;

                if (Keyboard.isKeyDown(42)) {
                    modifier = 1;
                } else if (Keyboard.isKeyDown(29)) {
                    modifier = 2;
                } else if (Keyboard.isKeyDown(56)) {
                    modifier = 3;
                }

                int y = mc.displayHeight - sy - scaleY(this.ySpacing); //Don't forget to flip Y axis.

                /* Fix small issue with right/mouse-wheel clicking */
                if (btn == 1) {
                    btn = 2;
                } else if (btn == 2) {
                    btn = 1;
                }

                if (wheel != 0) {
                    discordBrowser.injectMouseWheel(sx, y, modifier, 1, wheel);
                } else if (btn == -1) {
                    discordBrowser.injectMouseMove(sx, y, modifier, y < 0);
                } else {
                    discordBrowser.injectMouseButton(sx, y, modifier, btn + 1, pressed, 1);
                }
            }

            if (pressed) { //Forward events to GUI.
                int x = sx * width / mc.displayWidth;
                int y = height - (sy * height / mc.displayHeight) - 1;

                try {
                    mouseClicked(x, y, btn);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        }
    }

    public int scaleY(int y) {
        double sy = ((double) y) / ((double) height) * ((double) mc.displayHeight);
        return (int) sy;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {

            case 10:

                Discraft.getInstance().getLogger().printLine("Discraft Browser", "Reloading Discraft browser...");
                DiscraftCards.generateRandom();
                discordBrowser.loadURL(Discraft.discordUrl);

                break;

        }

    }

}
