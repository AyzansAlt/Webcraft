package net.discraft.mod;

import net.discraft.mod.module.DiscraftModule;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class DiscraftKeys {

    /**
     * Initialize the registration of all Discraft-related Keys
     */
    public void init() {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            for (KeyBinding keyBinding : module.keyBindings) {
                ClientRegistry.registerKeyBinding(keyBinding);
            }
        }

    }

}
