package net.discraft.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.screens.DiscraftScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

public class DiscraftScreenManager {

    public ArrayList<DiscraftScreen> discraftScreens;

    /**
     * Add - Add a new Screen to the Discraft Screen List
     *
     * @param discraftScreen - Given Discraft Screen Object
     */
    public void add(DiscraftScreen discraftScreen) {

        Minecraft mc = Minecraft.getMinecraft();
        ArrayList<DiscraftScreen> newArray = new ArrayList<>();
        boolean hasRemovedClose = false;

        /* Iterate through each screen from the screens list */
        for (DiscraftScreen discraftScreens : discraftScreens) {
            /* Check if the player is close enough to a screen to have it removed, or create a new one */
            if (mc.player.getDistance(discraftScreens.posX, discraftScreens.posY, discraftScreens.posZ) > 5) {
                newArray.add(discraftScreens);
                /* Remove a screen from the list and inform the user of it */
            } else {
                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + "Removed Screen at " + (int) discraftScreens.posX + "/" + (int) discraftScreens.posY + "/" + (int) discraftScreens.posZ));
                hasRemovedClose = true;
            }
        }

        /* If no screens have been removed, add a new screen */
        if (!hasRemovedClose) {
            /* Add screen to new screens list */
            newArray.add(discraftScreen);
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + "Placed Screen at " + (int) mc.player.posX + "/" + (int) mc.player.posY + "/" + (int) mc.player.posZ));
            mc.world.playSound(mc.player.posX, mc.player.posY, mc.player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.AMBIENT, 1, 1, false);
            /* else, play the pop sound */
        } else {
            mc.world.playSound(mc.player.posX, mc.player.posY, mc.player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.AMBIENT, 1, 0, false);
        }

        /* Replace the current discraft screen list with the new, altered version */
        this.discraftScreens = newArray;

    }

    /**
     * Init - Initialize the Discraft Screen Manager
     */
    public void init() {
        /* Cast screens list as a new array list */
        discraftScreens = new ArrayList<>();
    }

}
