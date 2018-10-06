package net.discraft.mod.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ModuleSettings {

    public File moduleConfig;

    public ModuleSettings(File givenFile) {
        this.moduleConfig = givenFile;
    }

    public File getModuleConfig() {
        return this.moduleConfig;
    }

    public void init() {

    }

    public void loadConfig(Properties givenProperties) {

    }

    public void loadConfig() {

        try {
            InputStream input = new FileInputStream(moduleConfig);
            Properties properties = new Properties();
            properties.load(input);

            loadConfig(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveConfig() {

    }

}
