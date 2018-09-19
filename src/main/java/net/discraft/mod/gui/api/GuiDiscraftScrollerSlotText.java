package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.gui.GuiUtils;

/**
 * @author ScottehBoeh
 */
public class GuiDiscraftScrollerSlotText extends GuiScrollerSlot {

    public static final char COLOR_CHAR = '\u00A7';
    private final String displayText;

    public GuiDiscraftScrollerSlotText(String text) {
        displayText = text;
    }

    public static String translateAlternateColorCodes(String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    @Override
    public boolean canSelect() {
        return false;
    }

    @Override
    public void doRender(int mouseX, int mouseY, float partialTicks) {
        GuiUtils.renderTextWithShadow((isSelected() ? ChatFormatting.WHITE : "") + translateAlternateColorCodes(this.displayText), posX + 2, posY + 2, 0xFFFFFF);
    }

    @Override
    protected int height() {
        return 11;
    }
}
