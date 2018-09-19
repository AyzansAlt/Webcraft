package net.discraft.mod.module.custogui;

import net.discraft.mod.module.DiscraftModule;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class Module_CustoGUI extends DiscraftModule {

    public KeyBinding keyEditGui = new KeyBinding("key.discraft.custogui.editgui", Keyboard.KEY_K, "key.discraft.category.module");

    public Module_CustoGUI(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        super(givenModuleID, givenModuleName, givenModuleDescription, givenModuleDescriptionLong, givenModuleAuthor, givenModuleLogo);

        this.keyBindings.add(keyEditGui);

    }
}
