package net.discraft.mod.gui.api;

import com.google.common.collect.Iterators;
import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GuiDiscraftScroller extends GuiDiscraftContainer {

    public static final int SCROLLBAR_HEIGHT = 20;
    public static final int SCROLLBAR_WIDTH = 6;
    public static final int SCROLLBAR_Y_PAD = 2;
    public ArrayList<GuiScrollerSlot> slots;

    public int scrollbarY;
    public int scrollbarMinY, scrollbarMaxY;
    public int scrollbarX;
    public boolean shouldDrawBackground = true;
    private int smoothScrollTargetY = -1;
    private int selectedSlot;
    private int totalHeight;
    private boolean scrollerClicked;
    private int scrollerClickMouseOffset;

    public GuiDiscraftScroller(int containerID, int posX, int posY, int width, int height, GuiDiscraftScreen parentGUI) {
        super(containerID, posX, posY, width, height, parentGUI);
        this.slots = new ArrayList<>();
        this.scrollerClicked = false;
        this.selectedSlot = -1;
    }

    public void initGui() {
        super.initGui();

        scrollbarMinY = posY + SCROLLBAR_Y_PAD * 2;
        scrollbarMaxY = scrollbarMinY;
        scrollbarY = scrollbarMinY;
        scrollbarX = posX + width - 12;

        onSlotHeightChanged();
    }

    public void onSlotHeightChanged() {
        int currentHeight = 0;
        for (int i = 0, n = slots.size(); i < n; i++) {
            GuiScrollerSlot slot = slots.get(i);

            slot.init(this, i, posX, posY + currentHeight);
            currentHeight += slot.height();
        }
        totalHeight = currentHeight;

        if (totalHeight > height) {
            scrollbarMaxY = posY + height - SCROLLBAR_Y_PAD * 2 - SCROLLBAR_HEIGHT;
        } else {
            scrollbarMaxY = scrollbarMinY;
        }
    }

    public void scrollTo(int y) {
        int maxTranslate = totalHeight - height;
        float translate = Math.min(maxTranslate, y - posY);

        smoothScrollTargetY = clampToScrollbar(scrollbarMinY + (int) ((translate / maxTranslate) * (scrollbarMaxY - scrollbarMinY)));
    }

    private void updateScrollPosition(int newPos) {
        scrollbarY = clampToScrollbar(newPos);
    }

    private int clampToScrollbar(int newPos) {
        return MathHelper.clamp(newPos, scrollbarMinY, scrollbarMaxY);
    }

    private int getActualScrollbarPos() {
        return smoothScrollTargetY == -1 ? scrollbarY : smoothScrollTargetY;
    }

    @Override
    public void handleScroll(int mouseX, int mouseY, int dWheel) {
        super.handleScroll(mouseX, mouseY, dWheel);
        if (!scrollerClicked && !slots.isEmpty() && GuiUtils.isInBox(posX, posY, width, height, mouseX, mouseY)) {
            smoothScrollTargetY = clampToScrollbar((int) (getActualScrollbarPos() - ((float) dWheel / totalHeight) * 30f));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int translate = getSlotYTranslation();

        for (GuiScrollerSlot slot : slots) {
            if (isWithin(mouseX, mouseY, slot)) {
                slot.clicked(mouseX, mouseY + translate);
            }
        }

        if (GuiUtils.isInBox(scrollbarX, scrollbarY, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT, mouseX, mouseY)) {
            smoothScrollTargetY = -1;
            scrollerClickMouseOffset = mouseY - scrollbarY;
            scrollerClicked = true;
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
        this.scrollerClicked = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.scrollerClicked) {
            updateScrollPosition(mouseY - scrollerClickMouseOffset);
        } else if (smoothScrollTargetY != -1) {
            double amnt = (smoothScrollTargetY - scrollbarY) / 6d;
            updateScrollPosition((int) (scrollbarY + (amnt <= 0 ? Math.floor(amnt) : Math.ceil(amnt))));
            if (scrollbarY == smoothScrollTargetY) {
                smoothScrollTargetY = -1;
            }
        }

        for (GuiScrollerSlot slot : slots) {
            slot.preRenderCallback(mouseX, mouseY);
        }

        drawBackground();

        renderScrollbar();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int factor = sr.getScaleFactor();

        glPushMatrix();

        int translation = getSlotYTranslation();
        glTranslatef(0, -translation, 0);

        glEnable(GL_SCISSOR_TEST);
        glScissor(posX * factor, (parentGUI.height - (posY + height - 4)) * factor - 8, width * factor - 30, (height - 8) * factor + 15);

        try {
            for (GuiScrollerSlot slot : slots) {
                slot.doRender(mouseX, mouseY + translation, partialTicks);
            }
        } catch (ConcurrentModificationException e) {

        }

        glDisable(GL_SCISSOR_TEST);

        for (GuiScrollerSlot slot : slots) {
            slot.renderNoClip(slot.posX, slot.posY, mouseX, mouseY);
        }

        glPopMatrix();

    }

    public int getSlotYTranslation() {
        int maxTranslate = totalHeight - height;
        return (int) ((float) (scrollbarY - scrollbarMinY) / (scrollbarMaxY - scrollbarMinY) * maxTranslate);
    }

    private void renderScrollbar() {

        boolean needsScroller = (totalHeight > this.height);

        GuiUtils.renderRect(scrollbarX - SCROLLBAR_Y_PAD, posY + SCROLLBAR_Y_PAD, SCROLLBAR_WIDTH + SCROLLBAR_Y_PAD * 2, height - 2 * SCROLLBAR_Y_PAD, needsScroller ? 0x775C9B34 : 0x773F443C);

        GuiUtils.renderRect(scrollbarX, scrollbarY, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT, needsScroller ? 0xFF5C9B34 : 0xFF3F443C);

    }

    public void setTextList(List<String> list) {

        this.slots.clear();

        for (String text : list) {

            if (!text.startsWith("//")) {

                if (text.startsWith("<image>")) {

                    String[] split = text.replace("<image>", "").split("<>");

                    String imageUrl = split[0];
                    int imageWidth = Integer.parseInt(split[1]);
                    int imageHeight = Integer.parseInt(split[2]);

                    GuiDiscraftScrollerSlotImage imageSlot = new GuiDiscraftScrollerSlotImage(imageUrl, imageWidth, imageHeight);
                    imageSlot.scroller = this;
                    this.slots.add(imageSlot);
                    this.onSlotHeightChanged();

                } else {

                    boolean renderSmall = false;

                    if (text.startsWith("<s>")) {
                        renderSmall = true;
                        text = text.replace("<s>", "");
                    }

                    GuiDiscraftScrollerSlotText newSlot = new GuiDiscraftScrollerSlotText(text);
                    if (renderSmall) {
                        newSlot.renderSmall();
                    }
                    newSlot.scroller = this;
                    this.slots.add(newSlot);
                    this.onSlotHeightChanged();

                }

            }

        }

        this.onSlotHeightChanged();

    }

    boolean isSelected(GuiScrollerSlot slot) {
        return slot.index == selectedSlot;
    }

    public void setSlotList(Iterator<? extends GuiScrollerSlot> newSlots) {
        this.slots.clear();
        try {
            Iterators.addAll(slots, newSlots);
            onSlotHeightChanged();
        } catch (ConcurrentModificationException e) {
            throw e;
        }
    }

    @Override
    public void onClose() {
        slots.forEach(GuiScrollerSlot::onClose);
    }

    @Override
    public void drawBackground() {
        if (shouldDrawBackground) {
            GuiUtils.renderRectWithOutline(this.posX, this.posY, this.width, this.height, Discraft.getInstance().colorTheme - 0x22000000, Discraft.getInstance().colorTheme - 0x22000000, 1);
        }
    }

    public boolean isWithin(int mouseX, int mouseY, GuiScrollerSlot slot) {

        int translate = getSlotYTranslation();
        return (GuiUtils.isInBox(slot.posX, slot.posY - translate, width, slot.height(), mouseX, mouseY) && GuiUtils.isInBox(this.posX, this.posY, this.width, this.height, mouseX, mouseY));
    }

}
