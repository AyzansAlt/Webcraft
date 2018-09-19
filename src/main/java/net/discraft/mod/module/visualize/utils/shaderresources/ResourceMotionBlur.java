package net.discraft.mod.module.visualize.utils.shaderresources;

import net.discraft.mod.module.visualize.Module_Visualize;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Locale;

public class ResourceMotionBlur implements IResource {

    public Module_Visualize module;

    public String jsonString = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";

    public ResourceMotionBlur(Module_Visualize givenModule) {
        this.module = givenModule;
    }

    public InputStream getInputStream() {
        double amount = 0.7D + this.module.visualizeSettings.motionBlurAmount / 100.0D * 3.0D - 0.01D;
        Double[] doubleList = new Double[]{amount, amount, amount};
        return IOUtils.toInputStream(String.format(Locale.ENGLISH, jsonString, doubleList));
    }

    public boolean hasMetadata() {
        return false;
    }

    public IMetadataSection getMetadata(String metadata) {
        return null;
    }

    public ResourceLocation getResourceLocation() {
        return null;
    }

    public String getResourcePackName() {
        return null;
    }

    @Override
    public void close() {
    }
}
