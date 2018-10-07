package net.discraft.mod.module.hypixel.utils;

import net.discraft.mod.Discraft;
import net.discraft.mod.module.ModuleSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class HypixelSettings extends ModuleSettings {

    public boolean enableAutoGG = false;
    public boolean enableAutoFriend = false;
    public boolean enableProfileGUI = false;

    public HypixelSettings(File givenFile) {
        super(givenFile);
    }

    public void init() {

        try {

            if (!moduleConfig.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(moduleConfig);

                properties.setProperty("enableAutoGG", enableAutoGG ? "true" : "false");
                properties.setProperty("enableAutoFriend", enableAutoFriend ? "true" : "false");
                properties.setProperty("enableProfileGUI", enableProfileGUI ? "true" : "false");

                properties.store(output, "Discraft - Official Hypixel Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft Hypixel Configuration File");
        }

        loadConfig();

    }

    @Override
    public void loadConfig(Properties givenProperties) {

        enableAutoGG = parseBoolean(givenProperties.getProperty("enableAutoGG"));
        enableAutoFriend = parseBoolean(givenProperties.getProperty("enableAutoFriend"));
        enableProfileGUI = parseBoolean(givenProperties.getProperty("enableProfileGUI"));

    }

    @Override
    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(this.getModuleConfig());

            properties.setProperty("enableAutoGG", enableAutoGG ? "true" : "false");
            properties.setProperty("enableAutoFriend", enableAutoFriend ? "true" : "false");
            properties.setProperty("enableProfileGUI", enableProfileGUI ? "true" : "false");

            properties.store(output, "Discraft - Official Hypixel Configuration Settings");

            loadConfig(properties);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
