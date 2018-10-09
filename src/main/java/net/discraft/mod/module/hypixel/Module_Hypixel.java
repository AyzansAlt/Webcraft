package net.discraft.mod.module.hypixel;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftPresence;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.ModuleSettings;
import net.discraft.mod.module.hypixel.api.HypixelAPI;
import net.discraft.mod.module.hypixel.utils.HypixelGuiUtils;
import net.discraft.mod.module.hypixel.utils.HypixelProfileManager;
import net.discraft.mod.module.hypixel.utils.HypixelSettings;
import net.discraft.mod.module.hypixel.utils.HypixelVariables;
import net.discraft.mod.module.hypixel.utils.profileobjects.HypixelProfile;
import net.discraft.mod.notification.ClientNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Module_Hypixel extends DiscraftModule {

    public HypixelVariables hypixelVariables = new HypixelVariables();
    public HypixelAPI hypixelAPI = HypixelAPI.getInstance();
    public HypixelSettings hypixelSettings = new HypixelSettings(new File("config/discraft_hypixel.cfg"));

    public HypixelGuiUtils hypixelGuiUtils;
    public HypixelProfileManager hypixelProfileManager;
    public String focusedUsername = "ScottehBoeh";
    private Pattern friendRequestPattern = Pattern.compile(
            "\u00A7m----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*");
    private Pattern friendExpiredPattern = Pattern.compile(
            "\u00A7m-----------------------------------------------------The friend request from (?<name>.+) has expired.");
    private Pattern friendRemovePattern = Pattern.compile(
            "-----------------------------------------------------\n(?<name>.+) removed you from their friends list!.");

    public Module_Hypixel(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        super(givenModuleID, givenModuleName, givenModuleDescription, givenModuleDescriptionLong, givenModuleAuthor, givenModuleLogo);
        this.hypixelProfileManager = new HypixelProfileManager(this);
        this.hypixelGuiUtils = new HypixelGuiUtils(this);
    }

    public String getCommandPrefix() {
        return "hypixel";
    }

    public void execute(MinecraftServer givenServer, ICommandSender sender, String[] args) {

        if (args.length > 1) {
            switch (args[1]) {
                /* AUTO GG */
                case "autogg":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.hypixelSettings.enableAutoGG = true;
                                sender.sendMessage(new TextComponentString("Hypixel Auto-GG has been " + (this.hypixelSettings.enableAutoGG ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.hypixelSettings.saveConfig();
                                return;
                            case "disable":
                                this.hypixelSettings.enableAutoGG = false;
                                sender.sendMessage(new TextComponentString("Hypixel Auto-GG has been " + (this.hypixelSettings.enableAutoGG ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.hypixelSettings.saveConfig();
                                return;
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("Hypixel Auto-GG is: " + (this.hypixelSettings.enableAutoGG ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
                    /* PROFILE GUI */
                case "profilegui":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.hypixelSettings.enableProfileGUI = true;
                                sender.sendMessage(new TextComponentString("Hypixel Profile GUI has been " + (this.hypixelSettings.enableProfileGUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.hypixelSettings.saveConfig();
                                return;
                            case "disable":
                                this.hypixelSettings.enableProfileGUI = false;
                                sender.sendMessage(new TextComponentString("Hypixel Profile GUI has been " + (this.hypixelSettings.enableProfileGUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.hypixelSettings.saveConfig();
                                return;
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("Hypixel Profile GUI is: " + (this.hypixelSettings.enableProfileGUI ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
                    /* AUTO FRIEND */
                case "autofriend":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.hypixelSettings.enableAutoFriend = true;
                                sender.sendMessage(new TextComponentString("Hypixel Auto-Friend has been " + (this.hypixelSettings.enableAutoFriend ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.hypixelSettings.saveConfig();
                                return;
                            case "disable":
                                this.hypixelSettings.enableAutoFriend = false;
                                sender.sendMessage(new TextComponentString("Hypixel Auto-Friend has been " + (this.hypixelSettings.enableAutoFriend ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                this.hypixelSettings.saveConfig();
                                return;
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("Hypixel Auto-Friend is: " + (this.hypixelSettings.enableAutoFriend ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
                    /* Fetch new Hypixel API Key */
                case "fetchapi":
                    sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "Discraft is fetching new Hypixel API Key..."));
                    Minecraft.getMinecraft().player.sendChatMessage("/api new");
                    return;
                /* Hypixel API Key */
                case "viewapikey":
                    if (this.hypixelVariables.apiUUID != null) {
                        sender.sendMessage(new TextComponentString("Current Hypixel API Key: " + ChatFormatting.GREEN + this.hypixelVariables.apiUUID.toString()));
                    } else {
                        sender.sendMessage(new TextComponentString(ChatFormatting.RED + "You need a new Hypixel API Key! Type " + ChatFormatting.GRAY + "/api new" + ChatFormatting.RED + " on Hypixel!"));
                    }
                    return;
                /* Clears all Cached profiles */
                case "clear":
                    sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "Clearing all Cached Hypixel Profiles..."));
                    this.hypixelProfileManager.clearCache();
                    sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "Hypixel Profiles Cache has been Successfully cleared!"));
                    return;
            }
        }

        sender.sendMessage(new TextComponentString(getCommandHelp("/discraft hypixel <autogg/profilegui.../fetchapi/viewapikey> <enable/disable>")));
        return;

    }

    public void initializeConfigurations() {
        this.hypixelSettings.init();
    }

    public void loadConfigurations() {
        this.hypixelSettings.loadConfig();
    }

    public void onRenderWorldLast(RenderWorldLastEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        /* If the world exists */
        if (mc.world != null) {

            /* For each player that the client has loaded */
            for (EntityPlayer player : mc.world.playerEntities) {

                /* If player is in a Hypixel-based Environment */
                if (Discraft.getInstance().discraftPresence.isHypixel() && this.hypixelVariables.apiUUID != null) {

                    HypixelProfile profile = this.hypixelProfileManager.getProfileFromUUID(player.getGameProfile().getId().toString());

                    if (profile == null) {
                        GuiUtils.renderPositionedTextScaled(ChatFormatting.GOLD + "Loading Profile...", player.posX, player.posY + player.eyeHeight + 1 + (player.isSneaking() ? -.5 : 0), player.posZ, 0.5f, 0xFFFFFF);
                    }

                }

                /* Iterate through each Developer UUID in the Developer Array List */
                for (String developerUUID : Discraft.developerList) {
                    if (player.getEntityId() != mc.player.getEntityId() && developerUUID.equals(player.getGameProfile().getId().toString()) && !player.isSneaking()) {
                        GuiUtils.renderPositionedTextScaled(I18n.format("discraft.nametag.developer"), player.posX, player.posY + player.getEyeHeight() + 0.9f, player.posZ, .5f, 0xFFFFFF);
                        GuiUtils.renderPositionedImage(new ResourceLocation(Discraft.MOD_ID, "textures/gui/logo.png"), player.posX, player.posY + player.getEyeHeight() + 1.05f, player.posZ, 1, 16, 12);
                    }
                }

            }

        }
    }

    public void onClientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        /* If player is joining Hypixel for the first time, fetch a new API Key */
        if (Discraft.getInstance().discraftPresence.isHypixel() && !this.hypixelVariables.fetchedApi) {

            this.hypixelVariables.fetchedApi = true;
            new Thread() {

                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(4);
                        if (mc.player != null) {
                            mc.player.sendChatMessage("/api new");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }.start();
        }

    }

    public void onChatReceiveEvent(ClientChatReceivedEvent event) {

        String msg = event.getMessage().getUnformattedText();

        /* If the message is a new API from Hypixel, set it up with a variable */
        if (msg.toLowerCase().contains("your new api key is")) {

            /* Get the UUID */
            String fullMessage = (event.getMessage().getUnformattedText().toLowerCase());
            String[] split = fullMessage.split(" ");
            String newAPIKey = split[split.length - 1];

            try {
                this.hypixelVariables.apiUUID = UUID.fromString(newAPIKey);
                this.hypixelAPI.setApiKey(this.hypixelVariables.apiUUID);
                ClientNotification.createNotification(I18n.format("discraft.hypixel.api.success"));
            } catch (IllegalArgumentException exception) {
                ClientNotification.createNotification(ChatFormatting.RED + I18n.format("discraft.hypixel.api.fail"));
            }

            event.setCanceled(true);

        }

        msg = msg.replace("\n", "");

        /* Auto-friend Feature */
        if (this.hypixelSettings.enableAutoFriend) {
            Matcher friendMatcher = friendRequestPattern.matcher(msg);

            if (friendMatcher.matches()) {
                String name = friendMatcher.group("name");
                if (name.startsWith("[")) {
                    name = name.substring(name.indexOf("] ") + 2);
                }
                Minecraft.getMinecraft().player.sendChatMessage("/friend accept " + name);
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Discraft has Auto-friended " + ChatFormatting.GREEN + name + ChatFormatting.RESET + "!"));
            }
        }


    }

    @Override
    public void onServerChatEvent(ServerChatEvent event, String givenFormatted, String givenUnformatted) {

        System.out.println("Server chat!!!");

        if (friendRemovePattern.matcher(givenUnformatted).matches()) {
            String username = friendRemovePattern.matcher(givenUnformatted).group("player");
            System.out.println("Player " + username + " tried to REMOVE YOU!");
        }

        if (friendRequestPattern.matcher(givenUnformatted).matches()) {
            String username = friendRequestPattern.matcher(givenUnformatted).group("player");
            System.out.println("Player " + username + " tried to ADD YOU!");
        }

    }

    public void onClientTick(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        if (event.phase.equals(TickEvent.Phase.START)) {
            /* Run Hypixel API Timeout Cooldown */
            this.hypixelVariables.timeOut--;

            if (this.hypixelSettings.enableProfileGUI) {
                if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {

                    EntityPlayer player = ((EntityPlayer) mc.objectMouseOver.entityHit);
                    String playerName = player.getGameProfile().getName();

                    if (mc.player != null && mc.player.isSneaking() && Mouse.isButtonDown(1) && !this.focusedUsername.equalsIgnoreCase(playerName)) {
                        this.focusedUsername = playerName;
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F));
                        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.WHITE + I18n.format("module.hypixel.profilestats", ChatFormatting.GREEN + this.focusedUsername + ChatFormatting.RESET)));
                    }

                }
            }

        }

    }

    public void onRenderIngameTitle(String givenTitle) {

        if (givenTitle != null) {

            /* FEATURE: HYPIXEL AUTO-GG */
            if (Discraft.getInstance().discraftPresence.currentServer.equals(DiscraftPresence.EnumServerType.HYPIXEL)) {
                if (this.hypixelSettings.enableAutoGG) {
                    if (givenTitle.toLowerCase().contains("victory".toLowerCase())
                            || givenTitle.toLowerCase().contains("draw".toLowerCase())
                            || givenTitle.toLowerCase().contains("defeat".toLowerCase())
                            || givenTitle.toLowerCase().contains("game end".toLowerCase())
                            || givenTitle.toLowerCase().contains("wins".toLowerCase())
                            || givenTitle.toLowerCase().contains("loses".toLowerCase())
                            || givenTitle.toLowerCase().contains("game over".toLowerCase())) {
                        this.hypixelVariables.autoGGTimer--;
                        if (this.hypixelVariables.autoGGTimer <= 0) {
                            this.hypixelVariables.autoGGTimer = this.hypixelVariables.initialAutoGGTimer;
                            if (!this.hypixelVariables.hasAutoGG) {
                                this.hypixelVariables.hasAutoGG = true;
                                double d = Math.random();
                                Minecraft.getMinecraft().player.sendChatMessage(d < 0.5 ? "gg" : "GG");
                            }
                        }
                    } else {
                        this.hypixelVariables.hasAutoGG = false;
                        this.hypixelVariables.autoGGTimer = this.hypixelVariables.initialAutoGGTimer;
                    }
                }
            }
        }
    }

    public void onRenderOverlay(RenderGameOverlayEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {

            if (Discraft.getInstance().discraftPresence.isHypixel()) {
                if (this.hypixelVariables.apiUUID != null && this.hypixelSettings.enableProfileGUI) {

                    if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {

                        GlStateManager.pushMatrix();
                        int boxX = (width / 2) - (160 / 2);
                        int boxY = (height - 108);

                        EntityPlayer player = ((EntityPlayer) mc.objectMouseOver.entityHit);
                        String playerUsername = player.getGameProfile().getName();

                        if (player.getGameProfile().getName().equalsIgnoreCase(focusedUsername)) {
                            GlStateManager.pushMatrix();
                            this.hypixelGuiUtils.renderHypixelStatsBox(boxX, boxY, player, width, height);
                            GlStateManager.popMatrix();
                        } else {
                            double hitDistance = 0;
                            hitDistance = GuiUtils.getDistanceToClientCamera(player.posX, player.posY, player.posZ);
                            double scale = (2 - (hitDistance / 3)) * .75;
                            GuiUtils.renderCenteredTextScaled(ChatFormatting.WHITE + I18n.format("module.hypixel.inspect", ChatFormatting.GREEN + playerUsername + ChatFormatting.RESET), width / 2, height / 2 + 15, 0xFFFFFF, scale);
                        }
                        GlStateManager.popMatrix();

                    }

                }
            }
        }
    }

    @Override
    public ModuleSettings getSettings() {
        return this.hypixelSettings;
    }

}
