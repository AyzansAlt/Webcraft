package net.discraft.mod.module.custogui.utils;

import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.event.KeyEvent;
import java.util.UUID;

import static net.discraft.mod.module.custogui.Module_CustoGUI.*;

public class GuiElement {

    public UUID guiElementID = UUID.randomUUID();

    public Module_CustoGUI module;

    public GuiScreen parentGUI;

    public int height = 1;
    public int width = 1;

    public boolean isEditable = false;
    public boolean isSelected = false;

    public int posX;
    public int posY;

    public GuiElement(int givenPosX, int givenPosY, GuiScreen givenGUI, Module_CustoGUI givenModule){
        this.posX = givenPosX;
        this.posY = givenPosY;
        this.module = givenModule;
        this.parentGUI = givenGUI;
    }

    public void onRender(Minecraft mc, float parTick){

        if(this.isSelected){
            GuiUtils.renderRectWithOutline(this.posX - 1,this.posY - 1,this.width + 2,this.height + 2,0x77000000,0x55FFFF00,1);
            GuiUtils.renderTextScaled("Selected",this.posX, this.posY + this.height + 3,0xFFFFFF,.5);
        } else if (this.isEditable){
            GuiUtils.renderRectWithOutline(this.posX - 1,this.posY - 1,this.width + 2,this.height + 2,0xFF000000,0x55555555,1);
        }

    }

    public void onUpdate(Minecraft mc){

        int mouseX = Mouse.getEventX() * this.parentGUI.width / mc.displayWidth;
        int mouseY = this.parentGUI.height - Mouse.getEventY() * this.parentGUI.height / mc.displayHeight - 1;

        if(!this.isEditable){
            this.isSelected = false;
        }

        if(this.isSelected) {
            if (Mouse.isButtonDown(1) && this.isHovered(mouseX,mouseY)) {
                this.posX = mouseX - (this.width / 2);
                this.posY = mouseY - (this.height / 2);
            }
        }

    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {

        if(isHovered(mouseX,mouseY) && mouseButton == 0){

            for(GuiElement element : Module_CustoGUI.guiElements){
                element.isSelected = false;
            }

            if(this.isEditable && !this.isSelected) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 2.0F));
                this.isSelected = true;
                return;
            }

        } else {
            if(this.isSelected && mouseButton == 0){
                this.isSelected = false;
            }
        }

    }

    public boolean isHovered(int mouseX, int mouseY) {
        return GuiUtils.isInBox(posX, posY, getWidth(), getHeight(), mouseX, mouseY);
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

}
