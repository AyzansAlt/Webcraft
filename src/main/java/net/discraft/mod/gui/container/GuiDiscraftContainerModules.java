package net.discraft.mod.gui.container;

import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftSounds;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.api.*;
import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.discraft.mod.module.DiscraftModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiDiscraftContainerModules extends GuiDiscraftContainer {

    private static final ResourceLocation MODULES_TEXTURE = new ResourceLocation(Discraft.MOD_ID + ":textures/gui/modulesbutton.png");

    public static boolean pulledOut = false;
    public static int extraX = 0;
    public int maxPullout = 100;

    public int BUTTON_PULLOUT = 3001;

    public GuiDiscraftScroller moduleScroller;

    public GuiDiscraftContainerModules(int givenID, int givenPosX, int givenPosY, int givenWidth, int givenHeight, GuiDiscraftScreen givenParentGUI) {
        super(givenID, givenPosX, givenPosY, givenWidth, givenHeight, givenParentGUI);

    }

    @Override
    public void initGui() {
        super.initGui();

        GuiDiscraftButton pulloutButton = new GuiDiscraftButton(BUTTON_PULLOUT, this.posX - 21 + extraX, this.posY + 4, 20, 20, "", MODULES_TEXTURE);
        this.addButton(pulloutButton);

        this.moduleScroller = new GuiDiscraftScroller(0, this.posX + 4 + extraX, this.posY + 5, 90, this.height - 10, this.parentGUI);
        this.moduleScroller.initGui();

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            this.moduleScroller.slots.add(new GuiDiscraftScrollerSlotModule(module, this.moduleScroller.width - 20, 45));
        }
        this.moduleScroller.onSlotHeightChanged();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GuiUtils.renderRectWithOutline(this.posX + extraX, this.posY, this.width + -extraX, this.height, 0x77000000, 0x77000000, 1);

        this.moduleScroller.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (pulledOut) {

            if (extraX > -maxPullout) {
                extraX -= 5;
                this.initGui();
            } else if (extraX < -maxPullout) {
                extraX = -maxPullout;
            }

        } else {

            if (extraX < 0) {
                extraX += 5;
                this.initGui();
            } else if (extraX > 0) {
                extraX = 0;
            }

        }

        this.moduleScroller.updateScreen();

    }

    @Override
    public void actionPerformed(GuiButton givenButton) {
        super.actionPerformed(givenButton);

        if (givenButton.id == BUTTON_PULLOUT) {
            pulledOut = !pulledOut;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(DiscraftSounds.NOTIFICATION, pulledOut ? 1.2F : 0.8F));
        }

        this.moduleScroller.actionPerformed(givenButton);

    }

    @Override
    public void handleScroll(int mouseX, int mouseY, int dWheel) {
        super.handleScroll(mouseX, mouseY, dWheel);
        this.moduleScroller.handleScroll(mouseX, mouseY, dWheel);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiScrollerSlot moduleSlot : this.moduleScroller.slots) {
            moduleSlot.clicked(mouseX, mouseY);
        }
    }

}
