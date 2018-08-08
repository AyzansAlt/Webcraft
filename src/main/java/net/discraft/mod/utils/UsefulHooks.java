package net.discraft.mod.utils;

import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.remapFieldNames;

public class UsefulHooks {

    public static final Field fieldIngameTitle = ReflectionHelper.findField(GuiIngame.class, remapFieldNames(GuiIngame.class.getName(), "field_175201_x"));

}
