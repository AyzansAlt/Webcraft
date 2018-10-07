package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.menu.GuiDiscraftScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public abstract class GuiDiscraftTextPrompt extends GuiDiscraftScreen {

    public GuiTextField promptInputText;
    public String[] promptInformation;
    public String promptTip = "";
    /**
     * Max amount of characters allowed in the prompt for an answer
     */
    public int maxCharacters = 32;
    public int promptWidth = 200;
    public int promptHeight = 20;
    /**
     * Text to be placed in the prompt input
     */
    public String promptedTextInput = "";
    public boolean restrictedUsernameInput = false;
    public boolean mustBeNumber = false;
    public int maxNumber = 0;
    protected GuiScreen parentGui;
    protected int promptX;
    protected int promptY;
    protected String errorMessage = "";
    private boolean isButtonClicked = false;
    private boolean isOpened = false;
    private GuiDiscraftButton enterButton;
    private GuiDiscraftButton exitButton;
    /**
     * Small ticker to check if the backspace button is held
     */
    private int backspaceTicker = 0;

    /**
     * Create a new drop down menu. Parent Gui, x, y, width, height
     */
    public GuiDiscraftTextPrompt(GuiScreen par1) {
        this.parentGui = par1;
    }

    /**
     * Info displayed above the input field, max 3 lines
     */
    public GuiDiscraftTextPrompt addInformation(String... par1) {
        promptInformation = par1;
        return this;
    }

    public GuiDiscraftTextPrompt addTip(String par1) {
        promptTip = par1;
        return this;
    }

    public GuiDiscraftTextPrompt setUsernameInput() {
        this.restrictedUsernameInput = true;
        return this;
    }

    @Override
    public void initGui() {

        this.promptX = this.width / 2 - (this.promptWidth / 2);
        this.promptY = this.height / 2 - (this.promptHeight / 2);

        this.promptInputText = new GuiTextField(0,this.fontRenderer, this.promptX, this.promptY, this.promptWidth, this.promptHeight);
        this.promptInputText.setMaxStringLength(this.maxCharacters);
        this.promptInputText.setText(this.promptedTextInput);

        this.buttonList.add(new GuiDiscraftButton(10, this.promptX, this.promptY + 25, 80, 20, "Close"));
        this.buttonList.add(new GuiDiscraftButton(11, this.promptX + this.promptWidth - 80, this.promptY + 25, 80, 20, "Confirm"));

        if (this.isOpened == true) {
            this.mc.displayGuiScreen(this.parentGui);
        }

        this.isOpened = true;
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {

        if (guibutton.id == 11) {

            this.isButtonClicked = true;

            String text = this.promptInputText.getText();

            if (text.isEmpty()) {
                this.errorMessage = "Empty Field";
                return;
            }

            if (this.restrictedUsernameInput && text.contains(Minecraft.getMinecraft().getSession().getUsername())) {
                    this.errorMessage = "Cannot add own Username";
            }

            if (this.restrictedUsernameInput) {

                if (text.contains(" ")) {
                    this.errorMessage = "Spaces are Invalid!";
                    return;
                }

                for (int i = 0; i < text.length(); i++) {

                    char char1 = text.charAt(i);

                    if (Character.isAlphabetic(char1) == false && Character.isDigit(char1) == false && char1 != '_') {
                        this.errorMessage = "Invalid Character Found";
                        return;
                    }
                }
            }

            if(this.mustBeNumber && (!isNumeric(text) || text.contains(" "))){
                this.errorMessage = "Given value must be numerical!";
                return;
            }

            if(this.mustBeNumber){

                text = text.replace("0x","");
                int number = Integer.decode(text);

                if(this.maxNumber > 0 && number > this.maxNumber){
                    this.errorMessage = "Given value is too high!";
                    return;
                }

            }

            this.onPromptEntered();
        }

        if (guibutton.id == 10) {
            if (this.mc.currentScreen == this) {
                this.mc.displayGuiScreen(this.parentGui);
            }
        }
    }

    public void updateScreen() {
        super.updateScreen();
        this.promptInputText.updateCursorCounter();

        if (Keyboard.isKeyDown(14)) {

            if (this.backspaceTicker >= 15) {

                this.promptInputText.deleteFromCursor(-1);

            } else {

                this.backspaceTicker++;
            }
        } else {

            this.backspaceTicker = 0;
        }
    }

    protected void keyTyped(char par1, int par2) throws IOException {
        super.keyTyped(par1, par2);
        this.promptInputText.textboxKeyTyped(par1, par2);
    }

    public abstract void onPromptEntered();

    public String getTextField() {

        return this.promptInputText.getText();
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.promptInputText.mouseClicked(par1, par2, par3);

        Rectangle rect = new Rectangle(this.promptX, this.promptY, this.promptWidth, this.promptHeight);

        if (rect.contains(par1, par2)) {
            return;
        }

        if (this.isButtonClicked) {
            this.isButtonClicked = false;
            return;
        }

        if (this.mc.currentScreen == this) {
            this.mc.displayGuiScreen(this.parentGui);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.parentGui.drawScreen(mouseX, mouseY, partialTicks);

        GuiUtils.renderRectWithGradient(0,0,width,height / 2,0x00000000,0x99000000,0);
        GuiUtils.renderRectWithGradient(0,height / 2,width,height,0x99000000,0x00000000,0);

        int margin = 60;

        GuiUtils.renderRectWithOutline(this.promptX - 4, this.promptY - margin, this.promptWidth + 8, this.promptHeight + margin + 30, 0xFF509026, 0x77509026, 1);
        GuiUtils.renderRectWithGradient(this.promptX - 4, this.promptY - margin, this.promptWidth + 8, this.promptHeight + margin + 30, 0x00000000, 0x33000000, 0);

        for (int k = 0; k < this.buttonList.size(); ++k) {

            GuiButton guibutton = this.buttonList.get(k);
            guibutton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        if (this.promptInformation != null) {

            for (int i1 = 0; i1 < this.promptInformation.length; i1++) {

                String var1 = this.promptInformation[i1];
                GuiUtils.renderCenteredText(var1, this.promptX + this.promptWidth / 2, this.promptY - margin + 5 + (i1 * 11),0xFFFFFF);
            }
        }

        GuiUtils.renderCenteredText(ChatFormatting.RED + "" + this.errorMessage, this.promptX + this.promptWidth / 2, this.promptY - margin + 48,0xFFFFFF);

        this.promptInputText.drawTextBox();

        if(this.getTextField().length() <= 0) {
            GuiUtils.renderText(this.promptTip, promptInputText.x + 4, promptInputText.y + 7, 0xFFFFFF);
        }

    }

    public static boolean isNumeric(String str) {
        try {
            int number = Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
