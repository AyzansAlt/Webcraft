package net.discraft.mod.module.discord.utils;

import net.discraft.mod.Discraft;

import java.io.*;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class DiscordSettings {

    public boolean enableDiscordGUI;

    File configFile = new File("config/discraft_discord.cfg");

    public void init() {

        try {

            if (!configFile.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(configFile);

                properties.setProperty("enableDiscordGUI", enableDiscordGUI ? "true" : "false");

                properties.store(output, "Discraft - Official Discord Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft Discord Configuration File");
        }

        loadConfig();

    }

    public void loadConfig() {

        try {

            Properties properties = new Properties();
            InputStream input = new FileInputStream(configFile);

            properties.load(input);

            enableDiscordGUI = parseBoolean(properties.getProperty("enableDiscordGUI"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(configFile);

            properties.setProperty("enableDiscordGUI", enableDiscordGUI ? "true" : "false");

            properties.store(output, "Discraft - Official Discord Configuration Settings");

            loadConfig();

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
