package net.discraft.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * <p>Class used to change the Window Icon and Title of the Minecraft
 * frame/instance. Once initialised, the frame sets the title of the
 * game (including the game version)</p>
 *
 * @author ScottehBoeh
 */
public class DiscraftJavaWindow {

    /**
     * Initialise function to change Icon and Title of game frame
     */
    public static void init() {

        /**
         * Change Text Element of Window
         */

        Display.setTitle("Minecraft - Discraft v" + Discraft.MOD_VERSION);
        /**
         * Set the Icon Image of the Window (NON MAC)
         */
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try {
                Display.setIcon(new ByteBuffer[]
                        {
                                readImage(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Discraft.MOD_ID, "textures/gui/icon_16x16.png")).getInputStream()),
                                readImage(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Discraft.MOD_ID, "textures/gui/icon_32x32.png")).getInputStream())
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Used to init Input Stream image (for displaying window icon
     */
    public static ByteBuffer readImage(InputStream par1File) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(par1File);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint[j];
            bytebuffer.putInt(k << 8 | k >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }

}
