package net.discraft.mod;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
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

    /**
     * Initialize Rich Presence
     */
    public void init() {

        /* Get the Discord RPC Instance */
        DiscordRPC lib = DiscordRPC.INSTANCE;

        /* Create a new Discord RPC Event Handler Instance */
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        /* Print out that the RPD has been initialized */
        handlers.ready = () -> Discraft.getInstance().getLogger().printLine("Discraft-RPD", "Discraft RPD has Initialized");
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

    }

}
