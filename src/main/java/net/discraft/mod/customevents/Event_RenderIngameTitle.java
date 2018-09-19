package net.discraft.mod.customevents;

import net.discraft.mod.Discraft;
import net.discraft.mod.module.DiscraftModule;

public class Event_RenderIngameTitle {

    /**
     * On Render Ingame Title - Rendering the In-game Title
     *
     * @param givenTitle - Given Title String to Render
     */
    public static void onRenderIngameTitle(String givenTitle) {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            module.onRenderIngameTitle(givenTitle);
        }

    }

}
