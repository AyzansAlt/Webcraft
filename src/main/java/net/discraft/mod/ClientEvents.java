package net.discraft.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.customevents.Event_RenderIngameTitle;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.override.GuiDiscraftIngameMenu;
import net.discraft.mod.notification.ClientNotification;
import net.discraft.mod.render.EntityRenderInViewHelper;
import net.discraft.mod.screens.DiscraftScreen;
import net.discraft.mod.utils.hypixel.HypixelProfile;
import net.discraft.mod.utils.hypixel.HypixelProfileManager;
import net.discraft.mod.utils.UsefulHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static net.discraft.mod.gui.GuiDiscraftMain.discordBrowser;

public class ClientEvents {

    /**
     * On Client Tick - Called when the Client Ticks
     *
     * @param event - Given Event (TickEvent.ClientTickEvent)
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (event.phase.equals(TickEvent.Phase.START)) {

            Minecraft mc = Minecraft.getMinecraft();

            /* Add 1 to Swing Animation (Used for animations, etc) */
            Discraft.getInstance().discraftVariables.swing++;

            /* Notification for when network connection is lost/gained */
            if (Discraft.getInstance().clientNetworkConnection.client.isConnected() != Discraft.getInstance().discraftVariables.lastConnected) {

                if (Discraft.getInstance().clientNetworkConnection.client.isConnected()) {
                    ClientNotification.createNotification(ChatFormatting.GREEN + "Connected!", "Connection established!");
                } else {
                    ClientNotification.createNotification(ChatFormatting.RED + "Disconnected!", "Connection lost!", true);
                }
                Discraft.getInstance().discraftVariables.lastConnected = Discraft.getInstance().clientNetworkConnection.client.isConnected();
            }

            /* Render the Current Client Notifications */
            if (Discraft.getInstance().discraftVariables.currentClientNotification == null) {
                if (Discraft.getInstance().discraftVariables.clientNotificationList.size() > 0) {
                    Discraft.getInstance().discraftVariables.currentClientNotification = Discraft.getInstance().discraftVariables.clientNotificationList.get(0);
                    Discraft.getInstance().discraftVariables.clientNotificationList.remove(0);
                }
            } else {
                Discraft.getInstance().discraftVariables.currentClientNotification.onUpdate();

                if (Discraft.getInstance().discraftVariables.currentClientNotification.displayTime <= 0) {
                    Discraft.getInstance().discraftVariables.currentClientNotification = null;
                }
            }

            if (Discraft.getInstance().discraftSettings.enableDiscraft) {

                /* Run the Reconnection Timer */
                Discraft.getInstance().discraftVariables.reconnectionTimer--;

                /* Run Hypixel API Timeout Cooldown */
                Discraft.getInstance().hypixelVariables.timeOut--;

                /* If Reconnection timer is at 0 and there is no connection, attempt reconnection */
                if (Discraft.getInstance().discraftVariables.reconnectionTimer <= 0) {

                    Discraft.getInstance().discraftVariables.reconnectionTimer = Discraft.getInstance().discraftVariables.initialReconnectionTimer;
                    if (Discraft.getInstance().discraftSettings.enableDiscraft && !Discraft.getInstance().clientNetworkConnection.client.isConnected()) {
                        new Thread() {

                            public void run() {
                                Discraft.getInstance().getLogger().printLine("Discraft", "Attempting reconnection to Network...");
                                try {
                                    Discraft.getInstance().clientNetworkConnection.client.start();
                                    Discraft.getInstance().clientNetworkConnection.client.reconnect();
                                    Discraft.getInstance().getLogger().printLine("Discraft", "Successfully reconnected to Network!");
                                } catch (IOException e) {
                                    Discraft.getInstance().getLogger().printError("Discraft", "Failed to Connect to Network!");
                                }
                            }

                        }.start();
                    }
                }

                /* If Discraft is Enabled */
                if (mc.player != null && mc.world != null) {

                    /* Calculate CPS */
                    if (Discraft.getInstance().discraftVariables.swing % 10 == 0) {

                        /* Escalate the Refresh Rate of the CPS refresher */
                        Discraft.getInstance().discraftVariables.clicksPerSecondRefreshRate--;

                        Discraft.getInstance().discraftVariables.clicksPerSecondList.add(Discraft.getInstance().discraftVariables.clicksPerSecond);
                        Discraft.getInstance().discraftVariables.clicksPerSecond = 0;

                        int total = 0;

                        for (int value : Discraft.getInstance().discraftVariables.clicksPerSecondList) {
                            total += value;
                        }

                        Discraft.getInstance().discraftVariables.clicksPerSecondAverage = (total / Discraft.getInstance().discraftVariables.clicksPerSecondList.size());

                        /* If the CPS refresher reaches 0, reset the CPS list */
                        if (Discraft.getInstance().discraftVariables.clicksPerSecondRefreshRate <= 0) {
                            Discraft.getInstance().discraftVariables.clicksPerSecondList.clear();
                        }

                    }

                    /* If the Open Gui key is pressed, initiate the opening sequence */
                    if (Discraft.getInstance().getKeyRegistry().keyOpen.isPressed()) {
                        if (mc.player != null) {

                            /* If player is not sneaking, display the UI */
                            if (!mc.player.isSneaking()) {
                                mc.player.openGui(Discraft.getInstance(), 0, mc.player.world, (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ);
                                /* Else, if the Discord Browser is not equal to Null, spawn it in */
                            } else if (discordBrowser != null) {

                                /* Fancy little algorithm that makes the screen spawn infront of the player */
                                double deltaX1 = (double) (-MathHelper.sin(mc.player.rotationYaw / 180.0F * (float) Math.PI));
                                double deltaZ1 = (double) (MathHelper.cos(mc.player.rotationYaw / 180.0F * (float) Math.PI));

                                /* Spawn the new Discraft Screen */
                                Discraft.getInstance().discraftScreenManager.add(new DiscraftScreen(mc.player.posX + deltaX1, mc.player.posY + mc.player.getEyeHeight() + 2, mc.player.posZ + deltaZ1));
                            } else {
                                /* Inform the user that they must start Discraft before spawning in a Screen */
                                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + I18n.format("discraft.openfirst")));
                            }
                        }
                    }

                    /* PVP Rear Camera Feature */
                    if (Discraft.getInstance().pvpSettings.enableRearCam) {

                        if (Discraft.getInstance().discraftKeys.keyRearcam.isKeyDown()) {
                            Discraft.getInstance().discraftVariables.rearCamHasChecked = false;
                            mc.gameSettings.thirdPersonView = 2;
                        } else if (!Discraft.getInstance().discraftVariables.rearCamHasChecked) {
                            Discraft.getInstance().discraftVariables.rearCamHasChecked = true;
                            mc.gameSettings.thirdPersonView = 0;
                        }

                    }

                    Vec3d lastPosVec = new Vec3d(mc.player.prevPosX, mc.player.prevPosY, mc.player.prevPosZ);
                    Vec3d currentPosVec = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);

                    /* Euclidean distance between your last and current position */
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    Discraft.getInstance().discraftVariables.playerSpeed = Double.valueOf(decimalFormat.format((lastPosVec.distanceTo(currentPosVec) * 10) * 2));

                    /* Ingame GUI Title Event Check */
                    if (mc.world != null && mc.ingameGUI != null) {
                        try {
                            Event_RenderIngameTitle.onRenderIngameTitle((String) UsefulHooks.fieldIngameTitle.get(mc.ingameGUI));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

        }

    }

    /**
     * On Render Tick - Called when the Client Renderer Ticks
     *
     * @param event - Given Event (TickEvent.RenderTickEvent)
     */
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        /* If current screen is a Main Menu UI */
        if (mc.currentScreen instanceof GuiMainMenu) {
            /* Render the Discraft Version and Name */
            GuiUtils.renderTextWithShadow((Discraft.getInstance().discraftSettings.enableDiscraft ? ChatFormatting.GREEN : ChatFormatting.RED) + "Discraft " + ChatFormatting.RESET + Discraft.MOD_VERSION, 2, mc.currentScreen.height - 50, 0xFFFFFF);
        }

        /* Render Discraft Notifications */
        ClientNotification clietNotification = Discraft.getInstance().discraftVariables.currentClientNotification;
        if (clietNotification != null) {
            clietNotification.doRender(mc);
        }

        /* If Discraft is Enabled */
        if(Discraft.getInstance().discraftSettings.enableDiscraft){

            /* Calculate CPS (Clicks per Second) */
            if(Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()){
                if(!Discraft.getInstance().discraftVariables.clicksPerSecondHasPressed) {
                    Discraft.getInstance().discraftVariables.clicksPerSecondHasPressed = true;
                    Discraft.getInstance().discraftVariables.clicksPerSecond++;
                }
            } else {
                Discraft.getInstance().discraftVariables.clicksPerSecondHasPressed = false;
            }

        }

    }

    /**
     * On Chat Receive Event - Called when the Client receives a Chat Message
     * @param event - Given Event (ClientChatReceivedEvent)
     */
    @SubscribeEvent
    public void onChatReceiveEvent(ClientChatReceivedEvent event){

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        /* If the message is a new API from Hypixel, set it up with a variable */
        if(event.getMessage().getUnformattedText().toLowerCase().contains("your new api key is")){

            /* Get the UUID */
            String fullMessage = (event.getMessage().getUnformattedText().toLowerCase());
            String[] split = fullMessage.split(" ");
            String newAPIKey = split[split.length - 1];

            try{
                Discraft.getInstance().hypixelVariables.apiUUID = UUID.fromString(newAPIKey);
                Discraft.getInstance().hypixelAPI.setApiKey(Discraft.getInstance().hypixelVariables.apiUUID);
                ClientNotification.createNotification(I18n.format("discraft.hypixel.api.success"));
            } catch (IllegalArgumentException exception){
                ClientNotification.createNotification(ChatFormatting.RED + I18n.format("discraft.hypixel.api.fail"));
            }

            event.setCanceled(true);

        }

    }

    /**
     * On Gui Open - Called when a GUI is Opened
     *
     * @param event - Given Event (GuiOpenEvent)
     */
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getGui() instanceof GuiIngameMenu) {
            GuiDiscraftIngameMenu ingameMenu = new GuiDiscraftIngameMenu();
            event.setGui(ingameMenu);
        }

        if (event.getGui() instanceof GuiMainMenu) {
            if (!Discraft.getInstance().discraftVariables.firstStart) {
                ClientNotification.createNotification("Discraft - " + ChatFormatting.GREEN + Discraft.MOD_VERSION, "Successfully Initialized!");
                Discraft.getInstance().discraftVariables.firstStart = true;
            }
        }

    }

    /**
     * On Client Logged In - Called when the Client Logs into a Server
     *
     * @param event - Given Event (FMLNetworkEvent.ClientConnectedToServer)
     */
    @SubscribeEvent
    public void onClientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        /* Change Client-side Current Server Value */
        Discraft.getInstance().discraftPresence.updatePresence();

        /* If Discraft is Enabled */
        if (Discraft.getInstance().discraftSettings.enableDiscraft) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(I18n.format("discraft.ingame.tip.open", ChatFormatting.GREEN + (GameSettings.getKeyDisplayString(Discraft.getInstance().getKeyRegistry().keyOpen.getKeyCode())) + ChatFormatting.RESET, Discraft.MOD_VERSION)));
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(I18n.format("discraft.ingame.tip.open2",ChatFormatting.GREEN + "/discraft" + ChatFormatting.RESET)));

            /* If player is joining Hypixel for the first time, fetch a new API Key */
            if(Discraft.getInstance().discraftPresence.isHypixel() && !Discraft.getInstance().hypixelVariables.fetchedApi){

                Discraft.getInstance().hypixelVariables.fetchedApi = true;
                new Thread(){

                    public void run(){
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            if(mc.player != null) {
                                mc.player.sendChatMessage("/api new");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }.start();
            }

        }

    }

    /**
     * On Client Logged Out - Called when the Client Logs out of a Server
     *
     * @param event - Given Event (FMLNetworkEvent.ClientDisconnectionFromServerEvent)
     */
    @SubscribeEvent
    public void onClientLoggedIn(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {

        /* Reset the Client-side Current Server Value */
        Discraft.getInstance().discraftPresence.resetPresence();

    }

    /**
     * On World Load - Called when the Client-side World is Loaded/Initialized
     *
     * @param event - Given Event (WorldEvent.Load)
     */
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {

        Minecraft mc = Minecraft.getMinecraft();

        Discraft.getInstance().getLogger().printLine("Discraft", "Clearing Discord Screens from World...");

        /* Clear all Discraft screens from World */
        Discraft.getInstance().discraftScreenManager.discraftScreens.clear();

        /* Clear necessary Settings/Variables */
        Discraft.getInstance().hypixelVariables.hasAutoGG = false;
    }

    /**
     * On Render World Last - Called after the Client World Renderer has Ticked
     *
     * @param event - Given Event (RenderWorldLastEvent)
     */
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        /* If Discraft is Enabled */
        if (Discraft.getInstance().discraftSettings.enableDiscraft) {

            /* If the world exists */
            if (mc.world != null) {

                /* For each player that the client has loaded */
                for (EntityPlayer player : mc.world.playerEntities) {

                    /* If player is in a Hypixel-based Environment */
                    if(Discraft.getInstance().discraftPresence.isHypixel() && Discraft.getInstance().hypixelVariables.apiUUID != null){

                        HypixelProfile profile = HypixelProfileManager.getProfileFromUUID(player.getGameProfile().getId().toString());

                        if(profile == null){
                            GuiUtils.renderPositionedTextScaled(ChatFormatting.GOLD + "Loading Profile...",player.posX,player.posY + player.eyeHeight + 1 + (player.isSneaking() ? -.5 : 0),player.posZ,0.5f,0xFFFFFF);
                        } else if (profile.isBot){
                            if(!player.getDisplayName().getUnformattedText().contains("[NPC]") && player.getDisplayName().getUnformattedText().length() != 10) {
                                GuiUtils.renderPositionedTextScaled(I18n.format("discraft.hypixel.gui.profile.nicked"),player.posX,player.posY + player.eyeHeight + 0.95 + (player.isSneaking() ? -.5 : 0),player.posZ,.5f,0xFF2222);
                            } else {
                                GuiUtils.renderPositionedTextScaled(I18n.format("discraft.hypixel.gui.profile.bot"),player.posX,player.posY + player.eyeHeight + 0.95 + (player.isSneaking() ? -.5 : 0),player.posZ,.5f,0xFF2222);
                            }
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

                /* For each Discraft screen, render it */
                for (DiscraftScreen screen : Discraft.getInstance().discraftScreenManager.discraftScreens) {
                    screen.render();
                }

            }

        } else {
            /* Clear the Discraft Screens List */
            Discraft.getInstance().discraftScreenManager.discraftScreens.clear();
        }

    }

    /**
     * On Render Overlay - Called when the game is rendering the In-game Overlay
     *
     * @param event - Given Event (RenderGameOverlayEvent)
     */
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {

            if (Discraft.getInstance().discraftSettings.enableDiscraft) {

                if(Discraft.getInstance().discraftPresence.isHypixel()){
                    if(Discraft.getInstance().hypixelVariables.apiUUID != null){

                        if(mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {

                            int boxX = (width / 2) - (160 / 2);
                            int boxY = (height - 108);

                            EntityPlayer player = ((EntityPlayer)mc.objectMouseOver.entityHit);

                            GuiUtils.renderHypixelStatsBox(boxX,boxY,player,width,height);

                            GlStateManager.popMatrix();

                        }

                    }
                }

                if (Discraft.getInstance().guiSettings.enableUI) {

                    GuiUtils.renderTextWithOutline("FPS: ", 4, 4, 0xFFFFFF,0x000000);
                    GuiUtils.renderTextWithOutline("PING: ", 4, 14, 0xFFFFFF,0x000000);
                    GuiUtils.renderTextWithOutline("CPS: ", 4, 24, 0xFFFFFF,0x000000);
                    GuiUtils.renderTextWithOutline("SPEED: ", 4, 34, 0xFFFFFF,0x000000);
                    GuiUtils.renderTextWithOutline("REACH: ", 4, 44, 0xFFFFFF,0x000000);

                    GuiUtils.renderTextWithOutline("" + Minecraft.getDebugFPS() + (Discraft.getInstance().discraftSettings.enableOpticraft ? " - Optimised" : ""), 40, 4, 0x89FF30,0x000000);
                    GuiUtils.renderTextWithOutline((Minecraft.getMinecraft().getCurrentServerData() != null ? "" + Minecraft.getMinecraft().getCurrentServerData().pingToServer : "Unknown"), 40, 14, 0x89FF30,0x000000);
                    GuiUtils.renderTextWithOutline("" + Discraft.getInstance().discraftVariables.clicksPerSecondAverage, 40, 24, 0x89FF30,0x000000);
                    GuiUtils.renderTextWithOutline("" + (Discraft.getInstance().discraftVariables.playerSpeed < 0.01 ? "0" : Discraft.getInstance().discraftVariables.playerSpeed), 40, 34, 0x89FF30,0x000000);
                    GuiUtils.renderTextWithOutline("" + Discraft.getInstance().discraftVariables.lastReachD, 40, 44, 0x89FF30,0x000000);

                }

            }

        }

    }

    /**
     * On Entity Hurt Event - Called when an Entity is hurt on the Server
     *
     * @param event - Given Event (LivingHurtEvent)
     */
    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getEntity() instanceof EntityPlayer && event.getEntity() != null && event.getSource().getTrueSource() != null && event.getSource().getTrueSource().getEntityId() == mc.player.getEntityId()) {

            if (Discraft.getInstance().discraftSettings.enableDiscraft) {

                /* PVP - HIT BEEP */
                if (Discraft.getInstance().pvpSettings.enableHitBeep) {
                    float pitch = 2 - (((EntityPlayer) event.getEntity()).getHealth() - event.getAmount()) / 20;
                    Minecraft.getMinecraft().world.playSound(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, DiscraftSounds.HITBEEP, SoundCategory.AMBIENT, 1, pitch, false);
                }

            }

        }

    }

    /**
     * On Entity Attack Event - Called when an Entity is attacked on the Server
     *
     * @param event - Given Event (LivingHurtEvent)
     */
    @SubscribeEvent
    public void onEntityAttack(AttackEntityEvent event) {

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getTarget() instanceof EntityPlayer && event.getTarget() != null && event.getEntity() != null && event.getEntity().getEntityId() == mc.player.getEntityId()) {

            if (Discraft.getInstance().discraftSettings.enableDiscraft) {

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                Discraft.getInstance().discraftVariables.lastReachD = Double.valueOf(decimalFormat.format(event.getEntity().getDistance(event.getTarget())));

                /* PVP - HIT BEEP */
                if (Discraft.getInstance().pvpSettings.enableHitBeep) {
                    float pitch = 2 - (((EntityPlayer) event.getTarget()).getHealth()) / 20;
                    Minecraft.getMinecraft().world.playSound(event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, DiscraftSounds.HITBEEP_SINGLE, SoundCategory.AMBIENT, 1, pitch, false);
                }

            }

        }

    }

    @SubscribeEvent
    public void RenderPlayerEvent(net.minecraftforge.client.event.RenderLivingEvent.Pre event) {

        Minecraft mc = Minecraft.getMinecraft();

        if(Discraft.getInstance().discraftSettings.enableDiscraft
                && Discraft.getInstance().discraftSettings.enableOpticraft
                && !EntityRenderInViewHelper.canEntityBeSeenByPlayer(mc.player, event.getEntity())) {
                event.setCanceled(true);
                return;
        }
    }
}
