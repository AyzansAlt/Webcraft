package net.discraft.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.notification.ClientNotification;
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
        return Arrays.asList("?");
    }

    @Override
    public void execute(MinecraftServer givenServer, ICommandSender sender, String[] args) throws CommandException {

        Minecraft mc = Minecraft.getMinecraft();

        if (args.length > 0) {

            for (DiscraftModule module : Discraft.getInstance().discraftModules) {
                if (module.getCommandPrefix().equals(args[0])) {
                    if (module.isEnabled) {
                        module.execute(givenServer, sender, args);
                    } else {
                        sender.sendMessage(new TextComponentString("The elementCPS " + module.moduleName + " is " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + "!"));
                    }
                    return;
                }
            }

        }

        if (args.length > 0) {

            switch (args[0]) {

                case "help":

                    if (Desktop.isDesktopSupported()) {
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
