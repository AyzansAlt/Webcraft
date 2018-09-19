package net.discraft.mod.module.pvpessentials;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.pvpessentials.utils.PvpSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class Module_PvpEssentials extends DiscraftModule {

    public PvpSettings pvpSettings = new PvpSettings();

    public KeyBinding keyRearcam = new KeyBinding("key.discraft.pvp.rearcam", Keyboard.KEY_X, "key.discraft.category.module");

    public Module_PvpEssentials(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        super(givenModuleID, givenModuleName, givenModuleDescription, givenModuleDescriptionLong, givenModuleAuthor, givenModuleLogo);

        keyBindings.add(keyRearcam);

    }

    @Override
    public String getCommandPrefix() {
        return "pvp";
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        if (event.phase.equals(TickEvent.Phase.START)) {

            if (mc.player != null && mc.world != null) {
                /* PVP Rear Camera Feature */
                if (pvpSettings.enableRearCam) {

                    if (this.keyRearcam.isKeyDown()) {
                        Discraft.getInstance().discraftVariables.rearCamHasChecked = false;
                        mc.gameSettings.thirdPersonView = 2;
                    } else if (!Discraft.getInstance().discraftVariables.rearCamHasChecked) {
                        Discraft.getInstance().discraftVariables.rearCamHasChecked = true;
                        mc.gameSettings.thirdPersonView = 0;
                    }

                }
            }

        }

    }

    @Override
    public void execute(MinecraftServer givenServer, ICommandSender sender, String[] args) {

        if (args.length > 1) {
            switch (args[1]) {
                /* REARCAM */
                case "rearcam":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.pvpSettings.enableRearCam = true;
                                sender.sendMessage(new TextComponentString("PVP Rear-cam has been " + (this.pvpSettings.enableRearCam ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.pvpSettings.saveConfig();
                                return;
                            case "disable":
                                this.pvpSettings.enableRearCam = false;
                                sender.sendMessage(new TextComponentString("PVP Rear-cam has been " + (this.pvpSettings.enableRearCam ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.pvpSettings.saveConfig();
                                return;
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("PVP Rear-cam is: " + (this.pvpSettings.enableRearCam ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
            }
        }

        sender.sendMessage(new TextComponentString(getCommandHelp("/discraft pvp <rearcam> <enable/disable>")));
        return;

    }

    @Override
    public void loadConfigurations() {
        this.pvpSettings.init();
    }

    @Override
    public void initializeConfigurations() {
        this.pvpSettings.loadConfig();
    }

}
