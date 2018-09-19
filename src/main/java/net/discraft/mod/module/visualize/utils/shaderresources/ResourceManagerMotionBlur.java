package net.discraft.mod.module.visualize.utils.shaderresources;

import net.discraft.mod.module.visualize.Module_Visualize;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Set;

public class ResourceManagerMotionBlur implements IResourceManager {

    public Module_Visualize module;

    public ResourceManagerMotionBlur(Module_Visualize givenModule) {
        this.module = givenModule;
    }

    public Set<String> getResourceDomains() {
        return null;
    }

    public IResource getResource(ResourceLocation location) {
        return new ResourceMotionBlur(this.module);
    }

    public List<IResource> getAllResources(ResourceLocation location) {
        return null;
    }

}
