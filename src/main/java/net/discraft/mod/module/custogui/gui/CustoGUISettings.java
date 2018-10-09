package net.discraft.mod.module.custogui.gui;

import net.discraft.mod.Discraft;
import net.discraft.mod.module.ModuleSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class CustoGUISettings extends ModuleSettings {

    public boolean enableCustomMainMenu = false;
    public String defaultBackgroundImageURL = "https://www.mcdecimation.net/upload/image/discraftbackgrounddefault.jpg";
    public String backgroundImageURL = defaultBackgroundImageURL;
    public boolean dynamicBackground = false;
    public boolean backgroundZoom = false;
    public int zoomSpeed = 1;
    public int zoomAmount = 25;
    public boolean dynamicBackgroundInvertMouse = false;

    public CustoGUISettings(File givenFile) {
        super(givenFile);
    }

    public void init() {

        try {

            if (!moduleConfig.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(moduleConfig);

                properties.setProperty("enableCustomMainMenu", enableCustomMainMenu ? "true" : "false");
                properties.setProperty("backgroundImageURL", backgroundImageURL);
                properties.setProperty("dynamicBackground", dynamicBackground ? "true" : "false");
                properties.setProperty("backgroundZoom", backgroundZoom ? "true" : "false");
                properties.setProperty("zoomSpeed", String.valueOf(zoomSpeed));
                properties.setProperty("zoomAmount", String.valueOf(zoomAmount));
                properties.setProperty("dynamicBackgroundInvertMouse", dynamicBackgroundInvertMouse ? "true" : "false");

                properties.store(output, "Discraft - Official CustoGUI Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft CustoGUI Configuration File");
        }

        loadConfig();

    }

    @Override
    public void loadConfig(Properties givenProperties) {

        enableCustomMainMenu = parseBoolean(givenProperties.getProperty("enableCustomMainMenu"));
        backgroundImageURL = givenProperties.getProperty("backgroundImageURL");
        dynamicBackground = parseBoolean(givenProperties.getProperty("dynamicBackground"));
        backgroundZoom = parseBoolean(givenProperties.getProperty("backgroundZoom"));
        zoomSpeed = parseInt(givenProperties.getProperty("zoomSpeed"));
        zoomAmount = parseInt(givenProperties.getProperty("zoomAmount"));
        dynamicBackgroundInvertMouse = parseBoolean(givenProperties.getProperty("dynamicBackgroundInvertMouse"));

    }

    @Override
    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(this.getModuleConfig());

            properties.setProperty("enableCustomMainMenu", enableCustomMainMenu ? "true" : "false");
            properties.setProperty("backgroundImageURL", backgroundImageURL);
            properties.setProperty("dynamicBackground", dynamicBackground ? "true" : "false");
            properties.setProperty("backgroundZoom", backgroundZoom ? "true" : "false");
            properties.setProperty("zoomSpeed", String.valueOf(zoomSpeed));
            properties.setProperty("zoomAmount", String.valueOf(zoomAmount));
            properties.setProperty("dynamicBackgroundInvertMouse", dynamicBackgroundInvertMouse ? "true" : "false");

            properties.store(output, "Discraft - Official CustoGUI Configuration Settings");

            loadConfig(properties);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
