package net.discraft.mod.gui.api;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static net.minecraft.client.Minecraft.getMinecraft;

/**
 * @author ScottehBoeh
 */
public class GuiDiscraftScrollerSlotImage extends GuiScrollerSlot {

    public String imageURL;
    public ResourceLocation imageResourceLocation;

    public int width;
    public int height;

    public GuiDiscraftScrollerSlotImage(String imageURL, int imageWidth, int imageHeight) {

        this.imageURL = imageURL;
        this.width = imageWidth;
        this.height = imageHeight;

    }

    public static BufferedImage downloadBanner(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.out.println("Errors reading online image: '" + url + "'");
        }
        return null;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean canSelect() {
        return false;
    }

    @Override
    public void doRender(int mouseX, int mouseY, float parTicks) {

        if (imageResourceLocation != null) {
            GuiUtils.renderImageTransparent(this.posX + 5, this.posY, this.imageResourceLocation, this.width, this.height, 1);
        } else {
            try {
                imageResourceLocation = getMinecraft().getTextureManager().getDynamicTextureLocation(this.imageURL, new DynamicTexture(downloadBanner(this.imageURL)));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void clicked(int mouseX, int mouseY) {
        super.clicked(mouseX, mouseY);

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
    }

    @Override
    protected int height() {
        return this.height;
    }

}
