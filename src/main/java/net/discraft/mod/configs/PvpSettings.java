package net.discraft.mod.configs;

import net.discraft.mod.Discraft;

import java.io.*;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class PvpSettings {

    public boolean enableHitBeep = false;
    public boolean enableRearCam = false;

    File configFile = new File("config/discraft_pvp.cfg");

    public void init() {

        try {

            if (!configFile.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(configFile);

                properties.setProperty("enableHitBeep", enableHitBeep ? "true" : "false");
                properties.setProperty("enableRearCam", enableRearCam ? "true" : "false");

                properties.store(output, "Discraft - Official GUI Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft GUI Configuration File");
        }

        loadConfig();

    }

    public void loadConfig() {

        try {

            Properties properties = new Properties();
            InputStream input = new FileInputStream(configFile);

            properties.load(input);

            enableHitBeep = parseBoolean(properties.getProperty("enableHitBeep"));
            enableRearCam = parseBoolean(properties.getProperty("enableRearCam"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(configFile);

            properties.setProperty("enableHitBeep", enableHitBeep ? "true" : "false");
            properties.setProperty("enableRearCam", enableRearCam ? "true" : "false");

            properties.store(output, "Discraft - Official PVP Configuration Settings");

            loadConfig();

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
