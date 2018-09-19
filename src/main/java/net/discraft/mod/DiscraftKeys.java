package net.discraft.mod;

import net.discraft.mod.module.DiscraftModule;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class DiscraftKeys {

    public KeyBinding keyOpen = new KeyBinding("key.discraft.open", Keyboard.KEY_G, "key.discraft.category.main");

    /**
     * Initialize the registration of all Discraft-related Keys
     */
    public void init() {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            for (KeyBinding keyBinding : module.keyBindings) {
                ClientRegistry.registerKeyBinding(keyBinding);
            }
        }

        ClientRegistry.registerKeyBinding(keyOpen);
    }

}
