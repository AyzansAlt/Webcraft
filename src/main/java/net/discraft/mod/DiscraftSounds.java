package net.discraft.mod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class DiscraftSounds {

    public static SoundEvent HITBEEP = new SoundEvent(new ResourceLocation(Discraft.MOD_ID, "hitbeep"));
    public static SoundEvent HITBEEP_SINGLE = new SoundEvent(new ResourceLocation(Discraft.MOD_ID, "hitbeep.single"));

    public static SoundEvent NOTIFICATION = new SoundEvent(new ResourceLocation(Discraft.MOD_ID, "notification"));

    public static SoundEvent ZOOM_IN = new SoundEvent(new ResourceLocation(Discraft.MOD_ID, "zoomin"));
    public static SoundEvent ZOOM_OUT = new SoundEvent(new ResourceLocation(Discraft.MOD_ID, "zoomout"));

    public static SoundEvent SETTING_CHANGE = new SoundEvent(new ResourceLocation(Discraft.MOD_ID, "settingchange"));

}
