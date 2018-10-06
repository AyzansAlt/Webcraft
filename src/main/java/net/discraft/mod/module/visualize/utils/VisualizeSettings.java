package net.discraft.mod.module.visualize.utils;

import net.discraft.mod.Discraft;
import net.discraft.mod.module.ModuleSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;

public class VisualizeSettings extends ModuleSettings {

    public double DEFAULTmotionBlurAmount = 5;

    public boolean enableMotionBlur = false;
    public boolean enableTabToggle = false;
    public boolean enableCustomBlockHighlight = false;

    public double motionBlurAmount = 5;
    public boolean tabToggled = false;

    public BlockHighlightSettings blockHighlightSettings;

    public VisualizeSettings(File givenFile) {
        super(givenFile);
        blockHighlightSettings = new BlockHighlightSettings(givenFile);
    }

    public void init() {

        try {

            if (!moduleConfig.exists()) {

                Properties properties = new Properties();

                OutputStream output = new FileOutputStream(moduleConfig);

                properties.setProperty("enableMotionBlur", enableMotionBlur ? "true" : "false");
                properties.setProperty("enableTabToggle", enableTabToggle ? "true" : "false");
                properties.setProperty("enableCustomBlockHighlight", enableCustomBlockHighlight ? "true" : "false");
                properties.setProperty("motionBlurAmount", String.valueOf(motionBlurAmount));

                this.blockHighlightSettings.init(properties);

                properties.store(output, "Discraft - Official Visualize Configuration Settings");

            }

        } catch (IOException io) {
            io.printStackTrace();
            Discraft.getInstance().getLogger().printError("Discraft", "Failed to Initialize Discraft Visualize Configuration File");
        }

        loadConfig();

    }

    @Override
    public void loadConfig(Properties givenProperties) {

        this.enableMotionBlur = parseBoolean(givenProperties.getProperty("enableMotionBlur"));
        this.enableTabToggle = parseBoolean(givenProperties.getProperty("enableTabToggle"));
        this.enableCustomBlockHighlight = parseBoolean(givenProperties.getProperty("enableCustomBlockHighlight"));
        this.motionBlurAmount = parseDouble(givenProperties.getProperty("motionBlurAmount"));

        this.blockHighlightSettings.loadConfig(givenProperties);

    }

    @Override
    public void saveConfig() {

        try {

            Properties properties = new Properties();
            OutputStream output = new FileOutputStream(getModuleConfig());

            properties.setProperty("enableMotionBlur", enableMotionBlur ? "true" : "false");
            properties.setProperty("enableTabToggle", enableTabToggle ? "true" : "false");
            properties.setProperty("enableCustomBlockHighlight", enableCustomBlockHighlight ? "true" : "false");
            properties.setProperty("motionBlurAmount", String.valueOf(motionBlurAmount));

            this.blockHighlightSettings.saveConfig(properties);

            properties.store(output, "Discraft - Official Visualize Configuration Settings");

            loadConfig(properties);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
