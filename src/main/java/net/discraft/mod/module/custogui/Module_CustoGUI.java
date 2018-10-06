package net.discraft.mod.module.custogui;

import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.custogui.gui.GuiEditElements;
import net.discraft.mod.module.custogui.utils.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Module_CustoGUI extends DiscraftModule {

    public static ArrayList<GuiElement> guiElements = new ArrayList<>();

    public KeyBinding keyEditGui = new KeyBinding("key.discraft.custogui.editgui", Keyboard.KEY_K, "key.discraft.category.elementCPS");

    public Module_CustoGUI(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        super(givenModuleID, givenModuleName, givenModuleDescription, givenModuleDescriptionLong, givenModuleAuthor, givenModuleLogo);

        this.keyBindings.add(keyEditGui);

    }

    @Override
    public void onRenderOverlay(RenderGameOverlayEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
            for (GuiElement element : guiElements) {
                if (!(mc.currentScreen instanceof GuiEditElements) && element.isEditable) {
                    element.isEditable = false;
                }
                element.onRender(mc, event.getPartialTicks());
            }
        }

    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        for (GuiElement element : guiElements) {
            element.onUpdate(mc);
        }

        if (keyEditGui.isPressed() && mc.player != null && mc.world != null) {
            mc.displayGuiScreen(new GuiEditElements(this));
        }

    }

    /**
     * Add GUI Element - Add a new GUI Element to the In-game GUI
     *
     * @param givenElement - Given Element to Add
     */
    public void addGuiElement(GuiElement givenElement) {

        if (!guiElements.contains(givenElement)) {
            guiElements.add(givenElement);
        }

    }

    /**
     * Remove GUI Element - Remove a new GUI Element from the In-game GUI
     *
     * @param givenElementUUID - Given Element UUID
     */
    public void removeGuiElement(GuiElement givenElementUUID) {

        ArrayList<GuiElement> newList = new ArrayList<>();

        for (GuiElement element : this.guiElements) {
            if (!element.equals(givenElementUUID)) {
                newList.add(element);
            }
        }

        this.guiElements = newList;

    }


}
