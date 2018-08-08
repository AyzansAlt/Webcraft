package net.discraft.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.notification.ClientNotification;
import net.discraft.mod.utils.hypixel.HypixelProfile;
import net.discraft.mod.utils.hypixel.HypixelProfileManager;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class DiscraftCommands implements ICommand {
    @Override
    public String getName() {
        return "discraft";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "View the full list of Commands by typing " + ChatFormatting.GREEN + "/discraft help" + ChatFormatting.RESET;
    }

    public String getCommandHelp(String givenHelp) {
        return "Invalid Use! Try: " + ChatFormatting.GREEN + givenHelp + ChatFormatting.RESET;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.<String>asList(new String[]{"?"});
    }

    @Override
    public void execute(MinecraftServer givenServer, ICommandSender sender, String[] args) throws CommandException {

        Minecraft mc = Minecraft.getMinecraft();

        if (args.length > 0) {

            switch (args[0]) {

                case "help":

                    if(Desktop.isDesktopSupported()){
                        try {
                            Desktop.getDesktop().browse(new URI(DiscraftRef.URL_COMMANDS));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    return;

                case "config":

                    if (args.length > 1) {

                        switch (args[1]) {
                            case "reload":
                                Discraft.getInstance().loadConfigurations();
                                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + I18n.format("discraft.config.reload")));
                                ClientNotification.createNotification(I18n.format("Discraft"), I18n.format("discraft.config.reload"));
                                return;
                        }

                    }

                    sender.sendMessage(new TextComponentString(getCommandHelp("/discraft config <reload>")));
                    return;

                case "optimise":

                    if (args.length > 1) {

                        switch (args[1]) {
                            case "enable":
                                Discraft.getInstance().discraftSettings.enableOpticraft = true;
                                sender.sendMessage(new TextComponentString("Discraft Opticraft has been " + (Discraft.getInstance().discraftSettings.enableOpticraft ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                Discraft.getInstance().discraftSettings.saveConfig();
                                return;
                            case "disable":
                                Discraft.getInstance().discraftSettings.enableOpticraft = false;
                                sender.sendMessage(new TextComponentString("Discraft Opticraft has been " + (Discraft.getInstance().discraftSettings.enableOpticraft ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                Discraft.getInstance().discraftSettings.saveConfig();
                                return;
                        }

                    }

                    sender.sendMessage(new TextComponentString(getCommandHelp("/discraft optimise <enable/disable>")));
                    return;

                case "hypixel":

                    if (args.length > 1) {
                        switch (args[1]) {
                            /* AUTO GG */
                            case "autogg":
                                if (args.length > 2) {
                                    switch (args[2]) {
                                        case "enable":
                                            Discraft.getInstance().hypixelSettings.enableAutoGG = true;
                                            sender.sendMessage(new TextComponentString("Hypixel Auto-GG has been " + (Discraft.getInstance().hypixelSettings.enableAutoGG ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().hypixelSettings.saveConfig();
                                            return;
                                        case "disable":
                                            Discraft.getInstance().hypixelSettings.enableAutoGG = false;
                                            sender.sendMessage(new TextComponentString("Hypixel Auto-GG has been " + (Discraft.getInstance().hypixelSettings.enableAutoGG ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().hypixelSettings.saveConfig();
                                            return;
                                    }
                                } else {
                                    sender.sendMessage(new TextComponentString("Hypixel Auto-GG is: " + (Discraft.getInstance().hypixelSettings.enableAutoGG ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                    return;
                                }
                                /* Fetch new Hypixel API Key */
                            case "fetchapi":
                                sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "Discraft is fetching new Hypixel API Key..."));
                                Minecraft.getMinecraft().player.sendChatMessage("/api new");
                                return;
                                /* Hypixel API Key */
                            case "viewapikey":
                                if(Discraft.getInstance().hypixelVariables.apiUUID != null){
                                    sender.sendMessage(new TextComponentString("Current Hypixel API Key: " + ChatFormatting.GREEN + Discraft.getInstance().hypixelVariables.apiUUID.toString()));
                                } else {
                                    sender.sendMessage(new TextComponentString(ChatFormatting.RED + "You need a new Hypixel API Key! Type " + ChatFormatting.GRAY + "/api new" + ChatFormatting.RED + " on Hypixel!"));
                                }
                                return;
                                /* Clears all Cached profiles */
                            case "clear":
                                sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "Clearing all Cached Hypixel Profiles..."));
                                HypixelProfileManager.clearCache();
                                sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "Hypixel Profiles Cache has been Successfully cleared!"));
                                return;
                        }
                    }

                    sender.sendMessage(new TextComponentString(getCommandHelp("/discraft hypixel <autogg.../fetchapi/viewapikey> <enable/disable>")));
                    return;

                case "gui":

                    if (args.length > 1) {
                        switch (args[1]) {
                            /* DISCRAFT GUI */
                            case "all":
                                if (args.length > 2) {
                                    switch (args[2]) {
                                        case "enable":
                                            Discraft.getInstance().guiSettings.enableUI = true;
                                            sender.sendMessage(new TextComponentString("Discraft GUI has been " + (Discraft.getInstance().guiSettings.enableUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().guiSettings.saveConfig();
                                            return;
                                        case "disable":
                                            Discraft.getInstance().guiSettings.enableUI = false;
                                            sender.sendMessage(new TextComponentString("Discraft GUI has been " + (Discraft.getInstance().guiSettings.enableUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().guiSettings.saveConfig();
                                            return;
                                    }
                                } else {
                                    sender.sendMessage(new TextComponentString("Discraft GUI is: " + (Discraft.getInstance().guiSettings.enableUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                    return;
                                }
                        }
                    }

                    sender.sendMessage(new TextComponentString(getCommandHelp("/discraft gui all <enable/disable>")));
                    return;

                case "pvp":

                    if (args.length > 1) {
                        switch (args[1]) {
                            /* HITBEEP */
                            case "hitbeep":
                                if (args.length > 2) {
                                    switch (args[2]) {
                                        case "enable":
                                            Discraft.getInstance().pvpSettings.enableHitBeep = true;
                                            sender.sendMessage(new TextComponentString("PVP Hitbeep has been " + (Discraft.getInstance().pvpSettings.enableHitBeep ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().pvpSettings.saveConfig();
                                            return;
                                        case "disable":
                                            Discraft.getInstance().pvpSettings.enableHitBeep = false;
                                            sender.sendMessage(new TextComponentString("PVP Hitbeep has been " + (Discraft.getInstance().pvpSettings.enableHitBeep ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().pvpSettings.saveConfig();
                                            return;
                                    }
                                } else {
                                    sender.sendMessage(new TextComponentString("PVP Hitbeep is: " + (Discraft.getInstance().pvpSettings.enableHitBeep ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                    return;
                                }

                                /* REARCAM */
                            case "rearcam":
                                if (args.length > 2) {
                                    switch (args[2]) {
                                        case "enable":
                                            Discraft.getInstance().pvpSettings.enableRearCam = true;
                                            sender.sendMessage(new TextComponentString("PVP Rear-cam has been " + (Discraft.getInstance().pvpSettings.enableRearCam ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().pvpSettings.saveConfig();
                                            return;
                                        case "disable":
                                            Discraft.getInstance().pvpSettings.enableRearCam = false;
                                            sender.sendMessage(new TextComponentString("PVP Rear-cam has been " + (Discraft.getInstance().pvpSettings.enableRearCam ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            Discraft.getInstance().pvpSettings.saveConfig();
                                            return;
                                    }
                                } else {
                                    sender.sendMessage(new TextComponentString("PVP Rear-cam is: " + (Discraft.getInstance().pvpSettings.enableRearCam ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                    return;
                                }
                        }
                    }

                    sender.sendMessage(new TextComponentString(getCommandHelp("/discraft pvp <hitbeep/rearcam> <enable/disable>")));
                    return;
            }
        }

        sender.sendMessage(new TextComponentString(getUsage(sender)));

    }

    @Override
    public boolean checkPermission(MinecraftServer givenServer, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer givenServer, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
