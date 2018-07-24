package net.discraft.mod;

import static net.discraft.mod.gui.GuiDiscraftMain.discordBrowser;

public class DiscraftShutdownHook {

    /**
     * Initialize the Shutdown Hook
     */
    public void init() {

        /* Add new Shutdown Hook Thread */
        Runtime.getRuntime().addShutdownHook(

                new Thread(() -> {
                    Discraft.getInstance().getLogger().printLine("Discraft", "Shutting down Discraft...");

                    discordBrowser.close();
                    discordBrowser = null;
                })

        );

    }

}
