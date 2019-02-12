package net.discraft.mod;

import net.discraft.mod.gui.GuiHandler;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.custogui.Module_CustoGUI;
import net.discraft.mod.module.discord.Module_Discord;
import net.discraft.mod.module.hypixel.Module_Hypixel;
import net.discraft.mod.module.protection.Module_Protection;
import net.discraft.mod.module.pvpessentials.Module_PvpEssentials;
import net.discraft.mod.module.visualize.Module_Visualize;
import net.discraft.mod.network.ClientNetworkConnection;
import net.discraft.mod.network.listener.MessageListener_Client;
import net.discraft.mod.render.layer.LayerDiscraftCape;
import net.discraft.mod.utils.DiscraftCrashChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.IOException;
import java.util.ArrayList;

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
    public static final String MOD_VERSION = "1.2a";
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
    public ArrayList<DiscraftModule> discraftModules = new ArrayList<>();
    /**
     * Mod Variables
     */
    public DiscraftVariables discraftVariables = new DiscraftVariables();
    /**
     * Necessary Instance Utilities
     */
    public DiscraftLogger discraftLogger = new DiscraftLogger();
    public DiscraftKeys discraftKeys = new DiscraftKeys();
    public DiscraftShutdownHook discraftShutdownHook = new DiscraftShutdownHook();
    public DiscraftPresence discraftPresence = new DiscraftPresence();
    public DiscraftSettings discraftSettings = new DiscraftSettings();
    public DiscraftSettingsModules discraftSettingsModules = new DiscraftSettingsModules();
    public DiscraftCrashChecker discraftChecker = new DiscraftCrashChecker();

    /**
     * Discraft Network Connection
     */
    public ClientNetworkConnection clientNetworkConnection;

    public String clientNetworkConnection_ip = "network.mcdecimation.net";
    public int clientNetworkConnection_tcp = 54666;
    public int clientNetworkConnection_udp = 54888;

    public int colorThemeDefault = 0x55000000;
    public int colorTheme = colorThemeDefault;

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

        /* Change Icon and Title of Window */
        DiscraftJavaWindow.init();

        /* Initialize default Modules */
        DiscraftModule moduleHypixel = new Module_Hypixel(
                "module_hypixel",
                "Hypixel API",
                "Discraft Hypixel API Fetcher",
                "The Discraft Hypixel API is a utility that hooks into the Hypixel API and caches the player data of all players in your game. Great for quickly viewing player hypixel stats in-game.",
                "ScottehBoeh",
                new ResourceLocation(Discraft.MOD_ID,
                        "textures/modules/hypixel.png"));

        DiscraftModule modulePvpEssentials = new Module_PvpEssentials(
                "module_pvpessentials", "PVP Essentials",
                "A set of essential PVP features",
                "PVP Essentials consists of various tools and utilities that you can find useful for in-game combat.",
                "ScottehBoeh",
                new ResourceLocation(Discraft.MOD_ID,
                        "textures/modules/pvpessentials.png"));

        DiscraftModule moduleCustoGUI = new Module_CustoGUI(
                "module_custogui", "Custo-GUI",
                "Customise your In-game GUI",
                "Custo-GUI gives you the power to display various different GUI widgets on your in-game GUI. Great for streamers and professional players",
                "ScottehBoeh",
                new ResourceLocation(Discraft.MOD_ID,
                        "textures/modules/custogui.png"));

        DiscraftModule moduleVisualize = new Module_Visualize(
                "module_visualize", "Visualize",
                "Edit your game visuals",
                "Visualize lets you edit how various aspects of your game are rendered.",
                "ScottehBoeh",
                new ResourceLocation(Discraft.MOD_ID,
                        "textures/modules/visualize.png"));

        DiscraftModule moduleDiscord = new Module_Discord(
                "module_discord", "Discord",
                "Use Discord in Minecraft",
                "Discord Module allows you to use and configure your Discord session from in-game. This includes the ability to share files, watch videos, and message other users via in-game",
                "ScottehBoeh",
                new ResourceLocation(Discraft.MOD_ID,
                        "textures/modules/discord.png"));

        DiscraftModule moduleProtection = new Module_Protection(
                "module_protection", "Protection",
                "Discraft Client Protector",
                "This modules functionality is to increase the safety of your Client. This includes fixing any possible exploits that may be present when using Discraft",
                "ScottehBoeh",
                new ResourceLocation(Discraft.MOD_ID,
                        "textures/modules/protection.png"));


        this.discraftModules.add(moduleHypixel);
        this.discraftModules.add(modulePvpEssentials);
        this.discraftModules.add(moduleCustoGUI);
        this.discraftModules.add(moduleVisualize);
        this.discraftModules.add(moduleDiscord);
        this.discraftModules.add(moduleProtection);

        /* Initialize Handlers/Managers */
        getInstance().discraftSettings.init(); /* Discraft Settings */
        getInstance().discraftKeys.init(); /* Discraft Keys */
        getInstance().discraftShutdownHook.init(); /* Discraft Shutdown Hook */
        getInstance().discraftPresence.init(); /* Discraft Rich Presence Hook */

        initializeConfigurations(); /* Initialize the Discraft Configuration Files */

        /* Initialize Discraft Checker Utility */
        this.discraftChecker.init();
        /* Register Event Handlers */
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        /* Register GUI Handlers */
        NetworkRegistry.INSTANCE.registerGuiHandler(getInstance(), new GuiHandler());
        /* Register Client-Side Commands */
        ClientCommandHandler.instance.registerCommand(new DiscraftCommands());

        /**
         * Smooth Swing Ticker - Used to smoothen out Swing Value for Animations
         */
        new Thread("SmoothSwingTicker") {

            public void run() {

                long lastTime = System.nanoTime();
                double amountOfTicks = 60.0;
                double ns = 1000000000 / amountOfTicks;
                double delta = 0;

                while (true) {
                    long now = System.nanoTime();
                    delta += (now - lastTime) / ns;
                    lastTime = now;
                    while (delta >= 1) {
                        Discraft.getInstance().discraftVariables.smoothSwing++;
                        delta--;
                    }
                }

            }

        }.start();

        /* Pre-Initialize Modules */
        for (DiscraftModule module : discraftModules) {
            module.preInit(event);
        }

    }

    /**
     * Forge Initialization Stage
     *
     * @param event - Given Event (FMLInitializationEvent)
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        Discraft.getInstance().getLogger().printLine("Discraft", "Initializing Discraft...");

        /* Initialize Modules */
        for (DiscraftModule module : discraftModules) {
            module.init(event);
        }

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
            ClientNetworkConnection.client.addListener(new MessageListener_Client());
        }

        /* Attempt connection to Management Server */
        try {
            ClientNetworkConnection.initializeClientConnection(clientNetworkConnection_ip, clientNetworkConnection_tcp, clientNetworkConnection_udp);
        } catch (IOException e) {
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Client Connection Instance!");
            e.printStackTrace();
        }

        /* Post-Initialize Modules */
        for (DiscraftModule module : discraftModules) {
            module.postInit(event);
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

    public void initializeConfigurations() {

        getInstance().discraftSettingsModules.init(); /* GUI Settings */

        for (DiscraftModule module : discraftModules) {
            module.initializeConfigurations();
        }

    }

    /**
     * Load Configurations - Loads all Necessary Configuration Files
     */
    public void loadConfigurations() {

        /* For each module, load configuration */
        for (DiscraftModule module : discraftModules) {
            module.loadConfigurations();
        }

    }

    /**
     * Does Module Exist - Check if module exists using a specified Module ID
     * @param givenModuleID - Given Module ID
     * @return - Yes/No - Does exist/Doesn't exist
     */
    public boolean doesModuleExist(String givenModuleID) {

        return (getModuleFromID(givenModuleID) != null);

    }

    /**
     * Get Module from ID - Get a specific Module using a Module ID reference
     * @param givenModuleID - Given Module ID
     * @return - Returns the Discraft Module (if exists)
     */
    public DiscraftModule getModuleFromID(String givenModuleID) {

        for (DiscraftModule module : discraftModules) {
            if (module.moduleID.equals(givenModuleID)) {
                return module;
            }
        }

        return null;

    }

}
