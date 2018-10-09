package net.discraft.mod.module.discord;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftScreenManager;
import net.discraft.mod.customevents.Event_RenderIngameTitle;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.ModuleSettings;
import net.discraft.mod.module.discord.utils.DiscordSettings;
import net.discraft.mod.screens.DiscraftScreen;
import net.discraft.mod.utils.UsefulHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

import static net.discraft.mod.module.discord.gui.GuiDiscord.discordBrowser;

public class Module_Discord extends DiscraftModule {

    public DiscordSettings discordSettings;
    public DiscraftScreenManager discraftScreenManager = new DiscraftScreenManager();

    public KeyBinding keyOpenDiscordGUI = new KeyBinding("key.discraft.discord.opengui", Keyboard.KEY_G, "key.discraft.category.elementCPS");

    public Module_Discord(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        super(givenModuleID, givenModuleName, givenModuleDescription, givenModuleDescriptionLong, givenModuleAuthor, givenModuleLogo);

        discordSettings = new DiscordSettings(new File("config/discraft_discord.cfg"));

        keyBindings.add(keyOpenDiscordGUI);

        this.discraftScreenManager.init(); /* Discraft Screen Manager */

    }

    public String getCommandPrefix() {
        return "discord";
    }

    public void execute(MinecraftServer givenServer, ICommandSender sender, String[] args) {

        if (args.length > 1) {
            switch (args[1]) {
                /* DISCORD GUI */
                case "gui":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.discordSettings.enableDiscordGUI = true;
                                sender.sendMessage(new TextComponentString("Discord GUI has been " + (discordSettings.enableDiscordGUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.discordSettings.saveConfig();
                                return;
                            case "disable":
                                this.discordSettings.enableDiscordGUI = false;
                                sender.sendMessage(new TextComponentString("Discord GUI has been " + (discordSettings.enableDiscordGUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.discordSettings.saveConfig();
                                return;
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("Discord GUI is: " + (discordSettings.enableDiscordGUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
            }
        }

        sender.sendMessage(new TextComponentString(getCommandHelp("/discraft discord <gui> <enable/disable>")));
        return;

    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        /* If Discraft is Enabled */
        if (mc.player != null && mc.world != null) {

            /* If the Open Gui key is pressed, initiate the opening sequence */
            if (this.discordSettings.enableDiscordGUI && this.keyOpenDiscordGUI.isPressed()) {

                /* If player is not sneaking, display the UI */
                if (!mc.player.isSneaking()) {
                    mc.player.openGui(Discraft.getInstance(), 0, mc.player.world, (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ);
                    /* Else, if the Discord Browser is not equal to Null, spawn it in */
                } else if (discordBrowser != null) {

                    /* Fancy little algorithm that makes the screen spawn infront of the player */
                    double deltaX1 = (double) (-MathHelper.sin(mc.player.rotationYaw / 180.0F * (float) Math.PI));
                    double deltaZ1 = (double) (MathHelper.cos(mc.player.rotationYaw / 180.0F * (float) Math.PI));

                    /* Spawn the new Discraft Screen */
                    this.discraftScreenManager.add(new DiscraftScreen(mc.player.posX + deltaX1, mc.player.posY + mc.player.getEyeHeight() + 2, mc.player.posZ + deltaZ1, this));
                } else {
                    /* Inform the user that they must start Discraft before spawning in a Screen */
                    mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + I18n.format("discraft.openfirst")));
                }
            }

            /* Ingame GUI Title Event Check */
            if (mc.ingameGUI != null) {
                try {
                    Event_RenderIngameTitle.onRenderIngameTitle((String) UsefulHooks.fieldIngameTitle.get(mc.ingameGUI));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public void onWorldLoad(WorldEvent.Load event) {

        /* Clear all Discraft screens from World */
        Discraft.getInstance().getLogger().printLine("Discraft", "Clearing Discord Screens from World...");
        this.discraftScreenManager.discraftScreens.clear();

    }

    @Override
    public void loadConfigurations() {
        this.discordSettings.loadConfig();
    }

    @Override
    public void initializeConfigurations() {
        this.discordSettings.init();
    }

    @Override
    public void addShutdownHook() {

        /* Add new Shutdown Hook Thread */
        Runtime.getRuntime().addShutdownHook(

                new Thread(() -> {
                    Discraft.getInstance().getLogger().printLine("Discraft", "Shutting down Discraft...");

                    discordBrowser.close();
                    discordBrowser = null;

                })

        );

    }

    @Override
    public void onClientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        /* Change Client-side Current Server Value */
        Discraft.getInstance().discraftPresence.updatePresence();

        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(I18n.format("discraft.ingame.tip.open", ChatFormatting.GREEN + (GameSettings.getKeyDisplayString(keyOpenDiscordGUI.getKeyCode())) + ChatFormatting.RESET, Discraft.MOD_VERSION)));

    }

    @Override
    public ModuleSettings getSettings() {
        return this.discordSettings;
    }

    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        if(this.discordSettings.enableDiscordGUI) {
            for (DiscraftScreen screen : this.discraftScreenManager.discraftScreens) {
                screen.render();
            }
        } else {
            if(this.discraftScreenManager.discraftScreens.size() > 0){
                this.discraftScreenManager.discraftScreens.clear();
            }
        }

    }

}
