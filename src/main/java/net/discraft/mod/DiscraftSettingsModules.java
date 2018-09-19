package net.discraft.mod;

import net.discraft.mod.module.DiscraftModule;

import java.io.*;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class DiscraftSettingsModules {

    File configFile = new File("config/discraft_modules.cfg");

    public void init() {

        try {

            if (!configFile.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(configFile);

                for(DiscraftModule module : Discraft.getInstance().discraftModules){
                    properties.setProperty(module.moduleID, module.isEnabled ? "true" : "false");
                }

                properties.store(output, "Discraft - Module Configuration Settings");

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

            for(DiscraftModule module : Discraft.getInstance().discraftModules){
                    module.isEnabled = parseBoolean(properties.getProperty(module.moduleID));
            }

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

            for(DiscraftModule module : Discraft.getInstance().discraftModules){
                    properties.setProperty(module.moduleID, module.isEnabled ? "true" : "false");
            }

            properties.store(output, "Discraft - Module Configuration Settings");

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

}
