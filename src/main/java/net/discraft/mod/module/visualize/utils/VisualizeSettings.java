package net.discraft.mod.module.visualize.utils;

import net.discraft.mod.Discraft;

import java.io.*;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;

public class VisualizeSettings {

    public boolean enableMotionBlur = false;
    public boolean enableTabToggle = false;

    public double motionBlurAmount = 5;
    public boolean tabToggled = false;

    File configFile = new File("config/discraft_visualize.cfg");

    public void init() {

        try {

            if (!configFile.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(configFile);

                properties.setProperty("enableMotionBlur", enableMotionBlur ? "true" : "false");
                properties.setProperty("enableTabToggle", enableTabToggle ? "true" : "false");
                properties.setProperty("motionBlurAmount", String.valueOf(motionBlurAmount));

                properties.store(output, "Discraft - Official Visualize Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft Visualize Configuration File");
        }

        loadConfig();

    }

    public void loadConfig() {

        try {

            Properties properties = new Properties();
            InputStream input = new FileInputStream(configFile);

            properties.load(input);

            enableMotionBlur = parseBoolean(properties.getProperty("enableMotionBlur"));
            enableTabToggle = parseBoolean(properties.getProperty("enableTabToggle"));
            motionBlurAmount = parseDouble(properties.getProperty("motionBlurAmount"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(configFile);

            properties.setProperty("enableMotionBlur", enableMotionBlur ? "true" : "false");
            properties.setProperty("enableTabToggle", enableTabToggle ? "true" : "false");
            properties.setProperty("motionBlurAmount", String.valueOf(motionBlurAmount));

            properties.store(output, "Discraft - Official Visualize Configuration Settings");

            loadConfig();

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
