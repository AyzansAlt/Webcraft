package net.discraft.mod.gui.api;

import net.discraft.mod.gui.GuiUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiScrollerSlot {

    public final List<GuiButton> buttons = new ArrayList<>();
    public GuiDiscraftScroller scroller;
    public int posX;
    public int posY;
    int index;

    final void init(GuiDiscraftScroller scroller, int index, int posX, int posY) {
        this.scroller = scroller;
        this.index = index;
        this.posX = posX;
        this.posY = posY;

        init();
    }

    protected void init() {
        buttons.clear();
    }

    public GuiButton addButton(GuiButton button) {
        buttons.add(button);
        return button;
    }

    public abstract boolean canSelect();

    protected void actionPerformed(GuiButton button) {
    }

    public void clicked(int mouseX, int mouseY) {
        Buttons.click(buttons, mouseX, mouseY, this::actionPerformed);
    }

    public final boolean isSelected() {
        return scroller.isSelected(this);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return GuiUtils.isInBox(posX, posY, scroller.width, height(), mouseX, mouseY);
    }

    protected void onClose() {
    }

    public void preRenderCallback(int mouseX, int mouseY) {
    }

    public void doRender(int mouseX, int mouseY, float partialTicks) {
        drawBackground(mouseX, mouseY);
        Buttons.draw(buttons, mouseX, mouseY, partialTicks);
    }

    public void renderNoClip(int x, int y, int mouseX, int mouseY) {
    }

    protected void drawBackground(int mouseX, int mouseY) {
        int color;
        if (isDisabled()) {
            color = 0xFF440000;
        } else {
            if (isSelected()) {
                color = 0x1A00FF00;
            } else if (isHovered(mouseX, mouseY)) {
                color = 0x21FFFFFF;
            } else {
                color = 0x6f000000;
            }
        }

        Gui.drawRect(posX + paddingLeft(), posY + paddingTop(), posX + scroller.width - paddingRight(), posY + height() - paddingBottom(), color);

    }

    protected int paddingLeft() {
        return 2;
    }

    protected int paddingRight() {
        return 20;
    }

    protected int paddingTop() {
        return 2;
    }

    protected int paddingBottom() {
        return 2;
    }

    public boolean isDisabled() {
        return false;
    }

    protected abstract int height();

}
