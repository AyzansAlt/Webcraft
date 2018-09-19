package net.discraft.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.customevents.Event_RenderIngameTitle;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.menu.GuiDiscraftMainMenu;
import net.discraft.mod.gui.override.GuiDiscraftIngameMenu;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.network.ClientNetworkConnection;
import net.discraft.mod.notification.ClientNotification;
import net.discraft.mod.screens.DiscraftScreen;
import net.discraft.mod.utils.UsefulHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.IOException;

import static net.discraft.mod.module.discord.gui.GuiDiscord.discordBrowser;

public class ClientEvents {

    /**
     * On Client Tick - Called when the Client Ticks
     *
     * @param event - Given Event (TickEvent.ClientTickEvent)
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            if (module.isEnabled)
                module.onClientTick(event);
        }

        if (event.phase.equals(TickEvent.Phase.START)) {

            Minecraft mc = Minecraft.getMinecraft();

            /* Add 1 to Swing Animation (Used for animations, etc) */
            Discraft.getInstance().discraftVariables.swing++;

            /* Notification for when network connection is lost/gained */
            if (ClientNetworkConnection.client.isConnected() != Discraft.getInstance().discraftVariables.lastConnected) {

                if (ClientNetworkConnection.client.isConnected()) {
                    ClientNotification.createNotification(ChatFormatting.GREEN + "Connected!", "Connection established!");
                } else {
                    ClientNotification.createNotification(ChatFormatting.RED + "Disconnected!", "Connection lost!", true);
                }
                Discraft.getInstance().discraftVariables.lastConnected = ClientNetworkConnection.client.isConnected();
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

                /* If Reconnection timer is at 0 and there is no connection, attempt reconnection */
                if (Discraft.getInstance().discraftVariables.reconnectionTimer <= 0) {

                    Discraft.getInstance().discraftVariables.reconnectionTimer = Discraft.getInstance().discraftVariables.initialReconnectionTimer;
                    if (Discraft.getInstance().discraftSettings.enableDiscraft && !ClientNetworkConnection.client.isConnected()) {
                        new Thread() {

                            public void run() {
                                Discraft.getInstance().getLogger().printLine("Discraft", "Attempting reconnection to Network...");
                                try {
                                    ClientNetworkConnection.client.start();
                                    ClientNetworkConnection.client.reconnect();
                                    Discraft.getInstance().getLogger().printLine("Discraft", "Successfully reconnected to Network!");
                                } catch (IOException e) {
                                    Discraft.getInstance().getLogger().printError("Discraft", "Failed to Connect to Network!");
                                }
                            }

                        }.start();
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

    }

    /**
     * On Chat Receive Event - Called when the Client receives a Chat Message
     *
     * @param event - Given Event (ClientChatReceivedEvent)
     */
    @SubscribeEvent
    public void onChatReceiveEvent(ClientChatReceivedEvent event) {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            module.onChatReceiveEvent(event);
        }

    }

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {

        String formattedText = event.getComponent().getFormattedText();
        String unformattedText = ChatFormatting.stripFormatting(event.getComponent().getUnformattedText());

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            module.onServerChatEvent(event,formattedText,unformattedText);
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

        if (event.getGui() instanceof GuiMainMenu) {

            event.setGui(new GuiDiscraftMainMenu());

            if (!Discraft.getInstance().discraftVariables.firstStart) {
                ClientNotification.createNotification("Discraft - " + ChatFormatting.GREEN + Discraft.MOD_VERSION, I18n.format("discraft.initialized.modules","" + ChatFormatting.GREEN + Discraft.getInstance().discraftModules.size() + ChatFormatting.RESET));
                Discraft.getInstance().discraftVariables.firstStart = true;
            }
        }

        if (event.getGui() instanceof GuiIngameMenu) {
            event.setGui(new GuiDiscraftIngameMenu());
        }

    }

    /**
     * On Client Logged In - Called when the Client Logs into a Server
     *
     * @param event - Given Event (FMLNetworkEvent.ClientConnectedToServer)
     */
    @SubscribeEvent
    public void onClientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            if (module.isEnabled)
                module.onClientLoggedIn(event);
        }

        /* Get the Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        /* Change Client-side Current Server Value */
        Discraft.getInstance().discraftPresence.updatePresence();

        /* If Discraft is Enabled */
        if (Discraft.getInstance().discraftSettings.enableDiscraft) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(I18n.format("discraft.ingame.tip.open", ChatFormatting.GREEN + (GameSettings.getKeyDisplayString(Discraft.getInstance().getKeyRegistry().keyOpen.getKeyCode())) + ChatFormatting.RESET, Discraft.MOD_VERSION)));
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(I18n.format("discraft.ingame.tip.open2", ChatFormatting.GREEN + "/discraft" + ChatFormatting.RESET)));
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

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            if (module.isEnabled)
                module.onWorldLoad(event);
        }

    }

    /**
     * On Render World Last - Called after the Client World Renderer has Ticked
     *
     * @param event - Given Event (RenderWorldLastEvent)
     */
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            if (module.isEnabled)
                module.onRenderWorldLast(event);
        }

    }

    /**
     * On Render Overlay - Called when the game is rendering the In-game Overlay
     *
     * @param event - Given Event (RenderGameOverlayEvent)
     */
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {

        for (DiscraftModule module : Discraft.getInstance().discraftModules) {
            if (module.isEnabled)
                module.onRenderOverlay(event);
        }

        if (Discraft.getInstance().discraftSettings.enableDiscraft) {

        }

    }

    /**
     * On Entity Hurt Event - Called when an Entity is hurt on the Server
     *
     * @param event - Given Event (LivingHurtEvent)
     */
    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {

        if (Discraft.getInstance().discraftSettings.enableDiscraft) {

        }

    }

    /**
     * On Entity Attack Event - Called when an Entity is attacked on the Server
     *
     * @param event - Given Event (LivingHurtEvent)
     */
    @SubscribeEvent
    public void onEntityAttack(AttackEntityEvent event) {

        if (Discraft.getInstance().discraftSettings.enableDiscraft) {

        }

    }

    @SubscribeEvent
    public void RenderPlayerEvent(net.minecraftforge.client.event.RenderLivingEvent.Pre event) {

        if (Discraft.getInstance().discraftSettings.enableDiscraft) {

        }

    }
}
