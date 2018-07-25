package net.discraft.mod;

import java.io.*;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class DiscraftSettings {

    public boolean enableDiscraft = true;

    File configFile = new File("config/discraft.cfg");

    public void init() {

        Properties properties = new Properties();
        OutputStream output = null;

        try {

            if (!configFile.exists()) {
                output = new FileOutputStream(configFile);

                properties.setProperty("enableDiscraft", enableDiscraft ? "true" : "false");

                properties.store(output, "Discraft - Official Configuration Settings");
            }

            loadConfig();

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft","Failed to Initialize Discraft Configuration File");
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

    public void loadConfig() {

        Properties properties = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(configFile);

            properties.load(input);

            enableDiscraft = parseBoolean(properties.getProperty("enableDiscraft"));

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

}
