package net.discraft.mod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class DiscraftKeys {

    public KeyBinding keyOpen = new KeyBinding("key.discraft.open", Keyboard.KEY_G, "key.discraft.category");

    /**
     * Initialize the registration of all Discraft-related Keys
     */
    public void init() {
        ClientRegistry.registerKeyBinding(keyOpen);
    }

}
