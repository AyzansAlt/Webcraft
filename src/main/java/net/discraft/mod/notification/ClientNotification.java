package net.discraft.mod.notification;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.DiscraftSounds;
import net.discraft.mod.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ClientNotification {

    /** Regular Notification Textures */
    public final ResourceLocation notificationTexture = new ResourceLocation(Discraft.MOD_ID + ":textures/gui/notification.png");
    public final ResourceLocation notificationTextureOverlay = new ResourceLocation(Discraft.MOD_ID + ":textures/gui/notification_overlay.png");

    /** Warning Notification Textures */
    public final ResourceLocation notificationWarningTexture = new ResourceLocation(Discraft.MOD_ID + ":textures/gui/notificationwarning.png");
    public final ResourceLocation notificationWarningTextureOverlay = new ResourceLocation(Discraft.MOD_ID + ":textures/gui/notificationwarning_overlay.png");

    /**
     * Message in the notification
     */
    private String[] message;
    /**
     * Sub message of the notification (Countercraft, Ferullo Gaming etc)
     */
    private String subMessage;

    /**
     * Time the message is on the screen
     */
    public double displayTime = 5 * 20;

    private double posY = -35;
    private double motionY = 3D;

    private double alpha = 0;

    private boolean hasPlayedSound = false;

    private boolean isWarning = false;

    private int type = 0;

    public ClientNotification(String par1) {
        this.message = this.trimString(par1);
        this.subMessage = "Discraft";
    }

    public ClientNotification(String par1, String par2) {
        this.message = new String[2];
        this.message[0] = par1;
        this.message[1] = par2;
        this.subMessage = "Discraft";
    }

    /**
     * Create and add a new notification to the client
     */
    public static void createNotification(String givenMessage) {
        ClientNotification not = new ClientNotification(givenMessage);
        Discraft.getInstance().discraftVariables.clientNotificationList.add(not);
    }

    public static void createNotification(String givenMessage, String givenSubmessage) {
        ClientNotification not = new ClientNotification(givenMessage, givenSubmessage);
        Discraft.getInstance().discraftVariables.clientNotificationList.add(not);
    }

    public static void createNotification(String givenMessage, String givenSubmessage, boolean isWarning) {
        ClientNotification not = new ClientNotification(givenMessage, givenSubmessage).setWarning();
        Discraft.getInstance().discraftVariables.clientNotificationList.add(not);
    }

    public static void clearNotifications() {
        Discraft.getInstance().discraftVariables.clientNotificationList.clear();
    }

    public ClientNotification setWarning() {
        this.isWarning = true;
        return this;
    }

    public ClientNotification setSubMessage(String par1) {
        this.subMessage = par1;
        return this;
    }

    public ClientNotification setType(int par1) {
        this.type = par1;
        return this;
    }

    public void onUpdate() {

        if (!this.hasPlayedSound) {
            this.hasPlayedSound = true;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(DiscraftSounds.NOTIFICATION, 1.0F));
        }

        if (this.posY < 0) {
            this.posY += this.motionY;
        }

        if (this.alpha < 0.8) {
            this.alpha += 0.040;
        }

        if (this.posY > 0) {
            this.posY = 0;
        }

        if (this.motionY > 0) {
            this.motionY -= 0.13d;
        } else {
            this.motionY = 0;
        }

        if (this.displayTime > 0) {
            this.displayTime--;
        }
    }

    public void doRender() {
        float val = (float) (Math.sin(Discraft.getInstance().discraftVariables.smoothSwing / 8) + 1);
        GuiUtils.renderImageTransparent(3, (int) this.posY + 3, isWarning ? notificationWarningTexture : notificationTexture, 160, 32, alpha);
        GuiUtils.renderImageTransparent(3, (int) this.posY + 3, isWarning ? notificationWarningTextureOverlay : notificationTextureOverlay, 160, 32, val);
        GuiUtils.renderText(ChatFormatting.WHITE + this.message[0].trim(), 7, (int) this.posY + 7, 0xFFFFFF);
        GuiUtils.renderText(ChatFormatting.WHITE + this.message[1].trim(), 7, (int) this.posY + 17, 0xFFFFFF);
        GuiUtils.renderTextScaled((type == 0 ? ChatFormatting.GREEN : ChatFormatting.RESET) + this.subMessage, 7, (int) this.posY + 27, 0xFFFFFF, 0.65D);
        GL11.glColor4f(1, 1, 1, 1);
    }

    private String[] trimString(String par1) {
        int var1 = Minecraft.getMinecraft().fontRenderer.getStringWidth(par1);
        String[] returnMessage = new String[2];
        returnMessage[0] = "";
        returnMessage[1] = "";
        int var3 = 0;
        int max = 160;

        if (var1 > max) {
            String[] var2 = par1.split(" ");

            for (int i = 0; i < var2.length; i++) {
                String var4 = returnMessage[var3] + " " + var2[i];

                if (Minecraft.getMinecraft().fontRenderer.getStringWidth(var4) < max) {
                    returnMessage[var3] += " " + var2[i];
                } else {
                    if (var3 != 1) {
                        var3 = 1;
                        returnMessage[var3] += " " + var2[i];
                    }
                }
            }
        } else {
            returnMessage[0] = par1;
        }

        return returnMessage;
    }
}
