package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.menu.GuiDiscraftMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

/**
 * @author ScottehBoeh
 */
public class GuiDiscraftScrollerSlotText extends GuiScrollerSlot {

    public static final char COLOR_CHAR = '\u00A7';
    private final String displayText;
    private boolean renderSmall = false;
    private String linkURL;

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

    public GuiDiscraftScrollerSlotText setUrl(String givenURL) {
        this.linkURL = givenURL;
        return this;
    }

    public GuiDiscraftScrollerSlotText renderSmall() {
        this.renderSmall = true;
        return this;
    }

    @Override
    public boolean canSelect() {
        return true;
    }

    @Override
    public void doRender(int mouseX, int mouseY, float parTick) {

        String text = translateAlternateColorCodes(this.displayText);

        text = text.replace("{VERSION}", Discraft.MOD_VERSION);
        text = text.replace("{NAME}", Minecraft.getMinecraft().getSession().getUsername());

        GuiUtils.renderTextScaled(
                ((isSelected() || (isHovered(mouseX, mouseY) && this.linkURL != null)) ? ChatFormatting.WHITE : "")
                        + text,
                posX + 2,
                posY + 2,
                0xFFFFFF,
                this.renderSmall ? .5f : 1
        );
    }

    @Override
    public int height() {

        return this.renderSmall ? 6 : 11;
    }

    @Override
    public void clicked(int mouseX, int mouseY) {
        super.clicked(mouseX, mouseY);
        String[] words = this.displayText.split(" ");

        if (this.linkURL != null) {
            if (this.linkURL.startsWith("http://") || this.linkURL.startsWith("https://")) {
                GuiDiscraftMainMenu.openURL(this.linkURL);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return;
            }
        } else {
            for (String word : words) {
                if (word.length() > 2 && word.startsWith("&")) {
                    word = word.substring(2);
                }

                if (word.startsWith("http://") || word.startsWith("https://") && isHovered(mouseX, mouseY)) {
                    GuiDiscraftMainMenu.openURL(word);
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return;
                }
            }
        }
    }

}