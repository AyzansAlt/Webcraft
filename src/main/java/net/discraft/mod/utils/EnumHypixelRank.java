package net.discraft.mod.utils;

import com.mojang.realmsclient.gui.ChatFormatting;

public enum EnumHypixelRank {

    DEFAULT("Default",ChatFormatting.GRAY + "Default"),
    HELPER("Helper",ChatFormatting.BLUE + "Helper"),
    MOD("Moderator",ChatFormatting.GREEN + "Moderator"),
    YOUTUBER("YouTuber",ChatFormatting.RED + "YouTuber"), //GOT
    ADMIN("Admin",ChatFormatting.RED + "Admin"),
    BUILDER("Builder",ChatFormatting.BLUE + "Builder"),
    OWNER("Owner",ChatFormatting.RED + "Owner");

    String inEnglish = "Default";
    String inEnglishFormatted = ChatFormatting.GRAY + "Default";

    EnumHypixelRank(String givenEnglish, String givenEnglishFormatted){
        this.inEnglish = givenEnglish;
        this.inEnglishFormatted = givenEnglishFormatted;
    }

    public static EnumHypixelRank getRankFromKey(String givenKey){

        switch(givenKey){
            case "YOUTUBER":
                return YOUTUBER;
            case "HELPER":
                return HELPER;
            case "MOD":
                return MOD;
            case "ADMIN":
                return ADMIN;
            case "BUILDER":
                return BUILDER;
            case "OWNER":
                return OWNER;
            default:
                return DEFAULT;
        }

    }

    public String toEnglish() {
        return this.inEnglish;
    }

    public String toEnglishFormatted() {
        return this.inEnglishFormatted;
    }
}
