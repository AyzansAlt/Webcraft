package net.discraft.mod.module.visualize.utils;

import net.discraft.mod.module.ModuleSettings;

import java.io.File;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

public class BlockHighlightSettings extends ModuleSettings {

    public float DEFAULTblockHighlightThickness = 1;
    public int DEFAULTblockHighlightColor = 0xFFFFFFFF;
    public int DEFAULTblockChromaSpeed = 1;
    public float DEFAULTblockAlpha = 1;

    public int blockHighlightColor = 0xFFFFFFFF;
    public float blockHighlightThickness = 1;
    public boolean blockIsChroma = false;
    public int blockChromaSpeed = 1;
    public float blockAlpha = 1;
    public int blockDistance = 6;
    public boolean enableFill = false;

    public BlockHighlightSettings(File givenFile) {
        super(givenFile);
    }

    public void init(Properties givenProperties) {
        givenProperties.setProperty("blockHighlightColor", String.valueOf(this.blockHighlightColor));
        givenProperties.setProperty("blockHighlightThickness", String.valueOf(this.blockHighlightThickness));
        givenProperties.setProperty("blockIsChroma", this.blockIsChroma ? "true" : "false");
        givenProperties.setProperty("blockChromaSpeed", String.valueOf(this.blockChromaSpeed));
        givenProperties.setProperty("blockAlpha", String.valueOf(this.blockAlpha));
        givenProperties.setProperty("blockDistance", String.valueOf(this.blockDistance));
        givenProperties.setProperty("enableFill", this.enableFill ? "true" : "false");
        return;
    }

    public void loadConfig(Properties givenProperties) {
        this.blockHighlightColor = (int) Long.parseLong(givenProperties.getProperty("blockHighlightColor").replace("0x", ""), 16);
        this.blockHighlightThickness = Float.parseFloat(givenProperties.getProperty("blockHighlightThickness"));
        this.blockIsChroma = parseBoolean(givenProperties.getProperty("blockIsChroma"));
        this.blockChromaSpeed = Integer.parseInt(givenProperties.getProperty("blockChromaSpeed"));
        this.blockAlpha = Float.parseFloat(givenProperties.getProperty("blockAlpha"));
        this.blockDistance = Integer.parseInt(givenProperties.getProperty("blockDistance"));
        this.enableFill = parseBoolean(givenProperties.getProperty("enableFill"));
        return;
    }

    public void saveConfig(Properties givenProperties) {
        givenProperties.setProperty("blockHighlightColor", String.valueOf(Integer.toHexString(this.blockHighlightColor)));
        givenProperties.setProperty("blockHighlightThickness", String.valueOf(this.blockHighlightThickness));
        givenProperties.setProperty("blockIsChroma", this.blockIsChroma ? "true" : "false");
        givenProperties.setProperty("blockChromaSpeed", String.valueOf(this.blockChromaSpeed));
        givenProperties.setProperty("blockAlpha", String.valueOf(this.blockAlpha));
        givenProperties.setProperty("blockDistance", String.valueOf(this.blockDistance));
        givenProperties.setProperty("enableFill", this.enableFill ? "true" : "false");
        return;
    }

}
