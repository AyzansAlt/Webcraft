package net.discraft.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.gui.override.GuiDiscraftIngameMenu;
import net.discraft.mod.screens.DiscraftScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import static net.discraft.mod.gui.GuiDiscraftMain.discordBrowser;

public class ClientEvents {

    /**
     * On Client Tick - Called when the Client Ticks
     *
     * @param event - Given Event (TickEvent.ClientTickEvent)
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        /* If Discraft is Enabled */
        if (Discraft.getInstance().discraftSettings.enableDiscraft) {

            /* Add 1 to Swing Animation (Used for animations, etc) */
            DiscraftVariables.swing++;

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
                        mc.ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString(ChatFormatting.GREEN + "[Discraft] " + ChatFormatting.RESET + I18n.format("discraft.openfirst")));
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
            GuiUtils.renderText((Discraft.getInstance().discraftSettings.enableDiscraft ? ChatFormatting.GREEN : ChatFormatting.RED) + "Discraft " + ChatFormatting.RESET + Discraft.MOD_VERSION, 2, mc.currentScreen.height - 50, 0xFFFFFF);
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

        /* If Discraft is Enabled */
        if (Discraft.getInstance().discraftSettings.enableDiscraft) {
            mc.ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString(I18n.format("discraft.ingame.tip.open", ChatFormatting.GREEN + Discraft.getInstance().getKeyRegistry().keyOpen.getDisplayName() + ChatFormatting.RESET, Discraft.MOD_VERSION)));
        }

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

    }

}
