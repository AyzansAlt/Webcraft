package net.discraft.mod;

import net.montoyo.mcef.MCEF;

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

                    MCEF.onMinecraftShutdown();

                    Discraft.getInstance().getLogger().printLine("Discraft", "Shutting down Java with Exit Code...");
                    System.exit(-1);

                })

        );

    }

}
