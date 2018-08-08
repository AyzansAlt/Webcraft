package net.discraft.mod.gui.override;

import net.discraft.mod.gui.api.GuiButtonDiscraftOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;

import java.io.IOException;

public class GuiDiscraftIngameMenu extends GuiIngameMenu {

    public void initGui() {
        super.initGui();
        int j = this.height / 4 + 48;
        this.buttonList.add(new GuiButtonDiscraftOptions(30, this.width / 2 - 124, j + 20 + 12));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 30:
                this.mc.displayGuiScreen(new GuiDiscraftOptions(this));
                break;
        }
    }
}
