package net.discraft.mod.gui.menu;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.api.GuiDiscraftContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class GuiDiscraftScreen extends GuiScreen {

    public static final int colorTheme = 0x77000000;
    protected List<GuiDiscraftContainer> containerList = Lists.newArrayList();

    public static void openURL(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
        }
    }

    public static void openURL(String par1) {

        try {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(par1));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.containerList.clear();
        this.initButtons();
    }

    public void initButtons() {

    }

    public void addContainer(GuiDiscraftContainer container) {
        container.initGui();
        this.containerList.add(container);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateContainers();
    }

    public void updateContainers() {
        for (GuiDiscraftContainer gui : this.containerList) {
            gui.updateScreen();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        actionPerformedContainer(button);
    }

    public void actionPerformedContainer(GuiButton guiButton) {
        for (GuiDiscraftContainer gui : this.containerList) {
            gui.parentActionPerformed(guiButton);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (GuiDiscraftContainer gui : containerList) {
            gui.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawContainers(mouseX, mouseY, partialTicks);

        GuiUtils.renderTextScaled(ChatFormatting.BLACK + "\u00a9 Copyright - Discraft", 3, height - 7, 0xFFFFFFFF, 0.5);
        GuiUtils.renderTextScaled(ChatFormatting.BLACK + "www.mcdecimation.net", width - 50, height - 7, 0xFFFFFFFF, 0.5);

    }

    public void drawContainers(int mouseX, int mouseY, float partialTicks) {
        for (GuiDiscraftContainer gui : containerList) {
            gui.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    public GuiDiscraftContainer getContainer(int containerID) {
        for (GuiDiscraftContainer cont : containerList) {
            if (cont.containerID == containerID) {
                return cont;
            }
        }
        return null;
    }

    public void onGuiClosed() {
        containerList.forEach(GuiDiscraftContainer::onClose);
    }

    @Override
    public void handleMouseInput() {
        try {
            super.handleMouseInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            for (GuiDiscraftContainer container : containerList) {
                container.handleScroll(mouseX, mouseY, dWheel);
            }
        }
    }

    @Override
    protected void mouseClickMove(int par1, int par2, int par3, long timeSinceLastClick) {
        super.mouseClickMove(par1, par2, par3, timeSinceLastClick);
        for (GuiDiscraftContainer container : containerList) {
            container.mouseReleased(par1, par2);
        }
    }

}

