package net.discraft.mod.module.hypixel.utils;

import net.discraft.mod.Discraft;

import java.io.*;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class HypixelSettings {

    public boolean enableAutoGG = false;
    public boolean enableAutoFriend = false;
    public boolean enableProfileGUI = false;

    File configFile = new File("config/discraft_hypixel.cfg");

    public void init() {

        try {

            if (!configFile.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(configFile);

                properties.setProperty("enableAutoGG", enableAutoGG ? "true" : "false");
                properties.setProperty("enableAutoFriend", enableAutoFriend ? "true" : "false");

                properties.store(output, "Discraft - Official Hypixel Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft Hypixel Configuration File");
        }

        loadConfig();

    }

    public void loadConfig() {

        try {

            Properties properties = new Properties();
            InputStream input = new FileInputStream(configFile);

            properties.load(input);

            enableAutoGG = parseBoolean(properties.getProperty("enableAutoGG"));
            enableAutoFriend = parseBoolean(properties.getProperty("enableAutoFriend"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(configFile);

            properties.setProperty("enableAutoGG", enableAutoGG ? "true" : "false");
            properties.setProperty("enableAutoFriend", enableAutoFriend ? "true" : "false");

            properties.store(output, "Discraft - Official Hypixel Configuration Settings");

            loadConfig();

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}