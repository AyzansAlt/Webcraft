package net.discraft.mod.customevents;

import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftPresence;
import net.minecraft.client.Minecraft;

public class Event_RenderIngameTitle {

    /**
     * On Render Ingame Title - Rendering the In-game Title
     *
     * @param givenTitle - Given Title String to Render
     */
    public static void onRenderIngameTitle(String givenTitle) {

        if (givenTitle != null) {

            /* FEATURE: HYPIXEL AUTO-GG */
            if (Discraft.getInstance().discraftPresence.currentServer.equals(DiscraftPresence.EnumServerType.HYPIXEL)) {
                if (Discraft.getInstance().hypixelSettings.enableAutoGG) {
                    if (givenTitle.toLowerCase().contains("victory".toLowerCase())
                            || givenTitle.toLowerCase().contains("draw".toLowerCase())
                            || givenTitle.toLowerCase().contains("defeat".toLowerCase())
                            || givenTitle.toLowerCase().contains("game end".toLowerCase())
                            || givenTitle.toLowerCase().contains("wins".toLowerCase())
                            || givenTitle.toLowerCase().contains("loses".toLowerCase())
                            || givenTitle.toLowerCase().contains("game over".toLowerCase())) {
                        Discraft.getInstance().hypixelVariables.autoGGTimer--;
                        if (Discraft.getInstance().hypixelVariables.autoGGTimer <= 0) {
                            Discraft.getInstance().hypixelVariables.autoGGTimer = Discraft.getInstance().hypixelVariables.initialAutoGGTimer;
                            if (!Discraft.getInstance().hypixelVariables.hasAutoGG) {
                                Discraft.getInstance().hypixelVariables.hasAutoGG = true;
                                double d = Math.random();
                                Minecraft.getMinecraft().player.sendChatMessage(d < 0.5 ? "gg" : "GG");
                            }
                        }
                    } else {
                        Discraft.getInstance().hypixelVariables.hasAutoGG = false;
                        Discraft.getInstance().hypixelVariables.autoGGTimer = Discraft.getInstance().hypixelVariables.initialAutoGGTimer;
                    }
                }
            }
        }

    }

}
