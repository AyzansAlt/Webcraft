package net.discraft.mod;

import net.discraft.mod.module.DiscraftModule;

public class DiscraftShutdownHook {

    /**
     * Initialize the Shutdown Hook
     */
    public void init() {

        /* Add all necessary shutdown hooks from each discraft elementCPS */
        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            if (module.isEnabled) {
                module.addShutdownHook();
            }
        }

    }

}
