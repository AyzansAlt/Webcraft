package net.discraft.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class DiscraftSettings {

    public boolean enableDiscraft = true;
    public boolean enableOpticraft = true;

    File configFile = new File("config/discraft.cfg");

    public void init() {

        try {

            if (!configFile.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(configFile);

                properties.setProperty("enableDiscraft", enableDiscraft ? "true" : "false");
                properties.setProperty("enableOpticraft", enableOpticraft ? "true" : "false");

                properties.store(output, "Discraft - Official Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft Configuration File");
        }

        loadConfig();

    }

    public void loadConfig() {

        Properties properties = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(configFile);

            properties.load(input);

            enableDiscraft = parseBoolean(properties.getProperty("enableDiscraft"));
            enableOpticraft = parseBoolean(properties.getProperty("enableOpticraft"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void saveConfig() {

        Properties properties = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream(configFile);

            properties.setProperty("enableDiscraft", enableDiscraft ? "true" : "false");
            properties.setProperty("enableOpticraft", enableOpticraft ? "true" : "false");

            properties.store(output, "Discraft - Official Configuration Settings");

            loadConfig();

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * Refresh Optimiser - Refreshes the Optimiser (To turn it on/off)
     */
    public void refreshOptimiser(){



    }

}
