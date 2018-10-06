package net.discraft.mod.module.pvpessentials.utils;

import net.discraft.mod.Discraft;
import net.discraft.mod.module.ModuleSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class PvpSettings extends ModuleSettings {

    public boolean enableRearCam = false;

    public PvpSettings(File givenFile) {
        super(givenFile);
    }

    public void init() {

        try {

            if (!moduleConfig.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(moduleConfig);

                properties.setProperty("enableRearCam", enableRearCam ? "true" : "false");

                properties.store(output, "Discraft - Official GUI Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft GUI Configuration File");
        }

        loadConfig();

    }

    @Override
    public void loadConfig(Properties givenProperties) {
        enableRearCam = parseBoolean(givenProperties.getProperty("enableRearCam"));
    }

    @Override
    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(getModuleConfig());

            properties.setProperty("enableRearCam", enableRearCam ? "true" : "false");

            properties.store(output, "Discraft - Official PVP Configuration Settings");

            loadConfig(properties);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
