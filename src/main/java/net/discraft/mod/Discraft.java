package net.discraft.mod;

import net.discraft.mod.configs.GuiSettings;
import net.discraft.mod.configs.HypixelSettings;
import net.discraft.mod.configs.HypixelVariables;
import net.discraft.mod.configs.PvpSettings;
import net.discraft.mod.gui.GuiHandler;
import net.discraft.mod.network.ClientNetworkConnection;
import net.discraft.mod.network.listener.MessageListener_Client;
import net.discraft.mod.render.entity.EntityItemRenderFactoryDiscraft;
import net.discraft.mod.render.layer.LayerDiscraftCape;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.IOException;

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
    public static final String MOD_VERSION = "1.0a";
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
     * Mod Variables
     */
    public DiscraftVariables discraftVariables = new DiscraftVariables();
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
     * Necessary Settings
     */
    public HypixelSettings hypixelSettings = new HypixelSettings();
    public PvpSettings pvpSettings = new PvpSettings();
    public GuiSettings guiSettings = new GuiSettings();

    /**
     * Necessary Server-based Variables
     */
    public HypixelVariables hypixelVariables = new HypixelVariables();

    /**
     * Discraft Network Connection
     */
    public ClientNetworkConnection clientNetworkConnection;

    public String clientNetworkConnection_ip = "network.mcdecimation.net";
    public int clientNetworkConnection_tcp = 54666;
    public int clientNetworkConnection_udp = 54888;

    public HypixelAPI hypixelAPI = HypixelAPI.getInstance();

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

        initializeConfigurations(); /* Initialize the Discraft Configuration Files */

        /* Register Event Handlers */
        MinecraftForge.EVENT_BUS.register(new ClientEvents());

        /* Register GUI Handlers */
        NetworkRegistry.INSTANCE.registerGuiHandler(getInstance(), new GuiHandler());

        /* Register Client-Side Commands */
        ClientCommandHandler.instance.registerCommand(new DiscraftCommands());

        RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, new EntityItemRenderFactoryDiscraft());

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

        Discraft.getInstance().getLogger().printLine("Discraft", "Created new Client-side Network Connection. Starting...");
        clientNetworkConnection = new ClientNetworkConnection();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            clientNetworkConnection.client.addListener(new MessageListener_Client());
        }
        try {
            clientNetworkConnection.initializeClientConnection(clientNetworkConnection_ip, clientNetworkConnection_tcp, clientNetworkConnection_udp);
        } catch (IOException e) {
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Client Connection Instance!");
            e.printStackTrace();
        }

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

    public void initializeConfigurations(){

        getInstance().hypixelSettings.init(); /* Hypixel Settings */
        getInstance().pvpSettings.init(); /* PvP Settings */
        getInstance().guiSettings.init(); /* GUI Settings */

    }

    /**
     * Load Configurations - Loads all Necessary Configuration Files
     */
    public void loadConfigurations() {

        getInstance().hypixelSettings.loadConfig(); /* Hypixel Settings */
        getInstance().pvpSettings.loadConfig(); /* PvP Settings */
        getInstance().guiSettings.loadConfig(); /* GUI Settings */

    }

}
