package net.discraft.mod.module.discord.utils;

import net.discraft.mod.Discraft;
import net.discraft.mod.module.ModuleSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class DiscordSettings extends ModuleSettings {

    public boolean enableDiscordGUI = true;

    public DiscordSettings(File givenFile) {
        super(givenFile);
    }

    public void init() {

        try {

            if (!moduleConfig.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(moduleConfig);

                properties.setProperty("enableDiscordGUI", enableDiscordGUI ? "true" : "false");

                properties.store(output, "Discraft - Official Discord Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft Discord Configuration File");
        }

        loadConfig();

    }

    @Override
    public void loadConfig(Properties givenProperties) {

        enableDiscordGUI = parseBoolean(givenProperties.getProperty("enableDiscordGUI"));

    }

    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(moduleConfig);

            properties.setProperty("enableDiscordGUI", enableDiscordGUI ? "true" : "false");

            properties.store(output, "Discraft - Official Discord Configuration Settings");

            loadConfig(properties);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
