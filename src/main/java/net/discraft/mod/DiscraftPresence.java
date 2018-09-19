package net.discraft.mod;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.hypixel.utils.HypixelGamemode;
import net.minecraft.client.Minecraft;

import static net.discraft.mod.Discraft.MOD_VERSION;

/**
 * Discraft Precense
 * <p>
 * Used to display a Discord-application Rich Presence alternation that fits the Discraft Theme
 */
public class DiscraftPresence {

    public final String discordAppID = "461671131770781718";
    public final String steamID = "";

    public EnumServerType currentServer = EnumServerType.UNKNOWN;

    /**
     * Initialize Rich Presencew
     */
    public void init() {

        Discraft.getInstance().getLogger().printLine("Discraft-RPD", "Initializing Discord RP...");

        try {
            /* Get the Discord RPC Instance */
            DiscordRPC lib = DiscordRPC.INSTANCE;

            /* Create a new Discord RPC Event Handler Instance */
            DiscordEventHandlers handlers = new DiscordEventHandlers();

            /* Print out that the RPD has been initialized */
            handlers.ready = () -> Discraft.getInstance().getLogger().printLine("Discraft-RPD", "Discraft RP has Initialized");
            lib.Discord_Initialize(discordAppID, handlers, true, steamID);

            /* Create a new Discord rich presence Instance */
            DiscordRichPresence presence = new DiscordRichPresence();

            /* Set the necessary parameters and attributes of the presence instance */
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            presence.details = "Minecraft - Discraft";
            presence.state = "Minecraft/Discord Utility";
            presence.smallImageKey = "minecraft";
            presence.largeImageKey = "logo";
            presence.largeImageText = "Discraft v" + MOD_VERSION + " (MC v" + Minecraft.getMinecraft().getVersion() + ")";

            /* Update the Rich Presence instance */
            lib.Discord_UpdatePresence(presence);

            /* Create new looping thread for rich presence */
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler").start();

        } catch (UnsatisfiedLinkError e) {
            Discraft.getInstance().getLogger().printError("Discraft-RPD", "Failed to Initialize Discord RP!");
        }

    }

    /**
     * Get Server Type - Get the Type of Server (Presence Name)
     *
     * @return - Returns the Server Type (Server Branding Name)
     */
    public EnumServerType getServerType() {

        return this.currentServer;

    }

    /**
     * Update Presence - Used to update the Server Precense
     */
    public void updatePresence() {

        if (Minecraft.getMinecraft().getCurrentServerData() != null) {
            String address = Minecraft.getMinecraft().getCurrentServerData().serverIP;

            if (address == null || address.length() <= 0) {
                currentServer = EnumServerType.NONE;
            } else if (address.contains("hypixel")) {
                currentServer = EnumServerType.HYPIXEL;
            } else if (address.contains("mineplex")) {
                currentServer = EnumServerType.MINEPLEX;
            } else if (address.contains("hivemc")) {
                currentServer = EnumServerType.HIVEMC;
            } else if (address.contains("veltpvp")) {
                currentServer = EnumServerType.VELTPVP;
            } else {
                currentServer = EnumServerType.UNKNOWN;
            }
        } else {
            currentServer = EnumServerType.NONE;
        }

    }

    /**
     * Reset Presence - Used to reset the Server Presence to NONE
     */
    public void resetPresence() {
        currentServer = EnumServerType.NONE;
    }

    public boolean isHypixel() {
        return currentServer.equals(EnumServerType.HYPIXEL);
    }

    public boolean isMineplex() {
        return currentServer.equals(EnumServerType.MINEPLEX);
    }

    public boolean isVeltPVP() {
        return currentServer.equals(EnumServerType.VELTPVP);
    }

    public boolean isHiveMC() {
        return currentServer.equals(EnumServerType.HIVEMC);
    }

    public boolean isUnknown() {
        return currentServer.equals(EnumServerType.UNKNOWN);
    }

    public boolean isNone() {
        return currentServer.equals(EnumServerType.NONE);
    }

    /**
     * Get Current Gamemode - Get the Current Gamemode that your Player is Playing
     *
     * @return - Given Gamemode (HypixelGamemode)
     */
    public HypixelGamemode getCurrentGamemode() {

        Minecraft mc = Minecraft.getMinecraft();
        String title = GuiUtils.getScoreboardTitle(mc);

        if (title != null) {

            for (HypixelGamemode gamemode : HypixelGamemode.values()) {
                if (gamemode.scoreboardString.equalsIgnoreCase(title)) {
                    return gamemode;
                }
            }

        }

        return HypixelGamemode.NONE;

    }

    /**
     * EnumServerType - Used to Distinguish Servers Brands
     */
    public enum EnumServerType {
        NONE,
        HYPIXEL,
        MINEPLEX,
        VELTPVP,
        HIVEMC,
        UNKNOWN
    }

}
