package net.discraft.mod;

import net.discraft.mod.gui.GuiHandler;
import net.discraft.mod.render.layer.LayerDiscraftCape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(
        modid = Discraft.MOD_ID,
        name = Discraft.MOD_NAME,
        version = Discraft.MOD_VERSION,
        acceptableRemoteVersions = "@ALLOWEDVERSIONS@",
        dependencies = "after:mcef"
)
public class Discraft {

    /**
     * Mod Attributes
     */
    public static final String MOD_ID = "dc";
    public static final String MOD_VERSION = "0.7a";
    public static final String MOD_NAME = "Discraft";
    public static final String[] developerList = new String[]{"dbc4ea0e-cb87-4ffc-b431-d9a8eec1428c"};
    /**
     * Discord Page URL
     */
    public static String discordUrl = "http://discordapp.com/channels/@me";
    /**
     * Mod Instance
     */
    @Mod.Instance(MOD_ID)
    public static Discraft discraftInstance = new Discraft();
    /**
     * Necessary Instange Utilities
     */
    public DiscraftLogger discraftLogger = new DiscraftLogger();
    public DiscraftKeys discraftKeys = new DiscraftKeys();
    public DiscraftShutdownHook discraftShutdownHook = new DiscraftShutdownHook();
    public DiscraftPresence discraftPresence = new DiscraftPresence();
    public DiscraftSettings discraftSettings = new DiscraftSettings();
    public DiscraftScreenManager discraftScreenManager = new DiscraftScreenManager();

    /**
     * Get Instance - Get the initial Discraft Instance
     *
     * @return - Given Instance (Discraft)
     */
    public static Discraft getInstance() {
        return discraftInstance;
    }

    /**
     * Forge Pre-Initialization Stage
     *
     * @param event - Given Event (FMLPreInitializationEvent)
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Discraft.getInstance().getLogger().printLine("Discraft", "You are running Discraft v" + MOD_VERSION + " developed by @ScottehBoeh");

        Discraft.getInstance().getLogger().printLine("Discraft", "Pre-initializing Discraft...");

        /* Initialize Handlers/Managers */
        getInstance().discraftSettings.init(); /* Discraft Settings */
        getInstance().discraftKeys.init(); /* Discraft Keys */
        getInstance().discraftShutdownHook.init(); /* Discraft Shutdown Hook */
        getInstance().discraftPresence.init(); /* Discraft Rich Presence Hook */
        getInstance().discraftScreenManager.init(); /* Discraft Screen Manager */

        /* Register Event Handlers */
        MinecraftForge.EVENT_BUS.register(new ClientEvents());

        /* Register GUI Handlers */
        NetworkRegistry.INSTANCE.registerGuiHandler(getInstance(), new GuiHandler());

    }

    /**
     * Forge Initialization Stage
     *
     * @param event - Given Event (FMLInitializationEvent)
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Discraft.getInstance().getLogger().printLine("Discraft", "Initializing Discraft...");
    }

    /**
     * Forge Post-Initialization Stage
     *
     * @param event - Given Event (FMLPostInitializationEvent)
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        Discraft.getInstance().getLogger().printLine("Discraft", "Post-initializing Discraft...");

        /* Register the Discraft Cape Render Layer */
        for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            renderPlayer.addLayer(new LayerDiscraftCape(renderPlayer));
        }

        Discraft.getInstance().getLogger().printLine("Discraft", "Initialized and Registered Cape Layer!");

    }

    /**
     * Get Logger - Get the Discraft Logger Utility
     *
     * @return - Given Logger Utility (DiscraftLogger)
     */
    public DiscraftLogger getLogger() {
        return getInstance().discraftLogger;
    }

    /**
     * Get Key Registry - Get the Key Registry Utility
     *
     * @return - Given Key Utility (DiscraftKeys)
     */
    public DiscraftKeys getKeyRegistry() {
        return getInstance().discraftKeys;
    }
}
