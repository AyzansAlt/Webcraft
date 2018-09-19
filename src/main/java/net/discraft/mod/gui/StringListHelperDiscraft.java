package net.discraft.mod.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;

public class StringListHelperDiscraft {

    public static ArrayList<String> convertListToLimitedWidth(ArrayList<String> par1, int par2Width) {

        return getListLimitWidth(convertListToString(new ArrayList<String>(par1)), par2Width);
    }

    public static String convertListToString(ArrayList<String> par1) {

        String var1 = "";

        for (int i = 0; i < par1.size(); i++) {

            var1 = var1 + " " + par1.get(i).trim();
        }

        return var1.trim();
    }

    public static ArrayList<String> getListLimitWidth(String par1, int maxWidth) {
        ArrayList<String> list = new ArrayList<String>();
        String theText = colorizeString(par1);

        String[] split = theText.split(" ");
        String line = "";
        int maxLineWidth = maxWidth;
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

        for (int i = 0; i < split.length; i++) {
            String var1 = split[i];

            if (var1.equals("[br]")) {
                if (fr.getStringWidth(line) > 0) {
                    list.add(new String(line));
                }
                line = "";
                continue;
            }

            if (var1.equals("[nl]") || var1.equals("++")) {
                if (fr.getStringWidth(line) > 0) {
                    list.add(new String(line));
                }
                list.add("");
                line = "";
                continue;
            }

            if (fr.getStringWidth(line) + fr.getStringWidth(var1) <= maxLineWidth) {
                line += var1 + " ";
            } else {
                list.add(new String(line));
                line = var1 + " ";
            }
        }

        if (fr.getStringWidth(line) > 0) {
            list.add(line);
        }

        return list;
    }

    public static String colorizeString(String par1) {
        String var1 = "";
        boolean skipNext = false;

        for (int i = 0; i < par1.length(); i++) {
            char var2 = par1.charAt(i);

            if (skipNext) {
                skipNext = false;
                continue;
            }

            if (var2 == '&' && i != par1.length() - 1) {
                char var3 = par1.charAt(i + 1);
                boolean found = false;
                for (ChatFormatting color : ChatFormatting.values()) {
                    if (color.getChar() == var3) {
                        var1 = var1 + color;
                        skipNext = true;
                        found = true;
                        break;
                    }
                }

                if (found) {
                    continue;
                }
            }

            var1 = var1 + var2;
        }

        return var1;
    }
}
