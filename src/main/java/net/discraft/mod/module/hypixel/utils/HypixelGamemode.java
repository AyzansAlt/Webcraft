package net.discraft.mod.module.hypixel.utils;

public enum HypixelGamemode {

    NONE("None", "NONE"),
    IN_LOBBY_OR_HUB("Lobby or Hub", "HYPIXEL"),
    SKYWARS("SkyWars", "SKYWARS"),
    SKYCLASH("SkyClash", "SKYCLASH"),
    COPS_AND_CRIMS("Cops & Crims", "COPS AND CRIMS"),
    PROTOTYPE("Prototype", "PROTOTYPE"),
    ARCADE_GAMES("Arcade Games", "ARCADE GAMES"),
    MEGA_WALLS("Mega Walls", "MEGA WALLS"),
    CRAZY_WALLS("Crazy Walls", "CRAZY WALLS"),
    BED_WARS("BedWars", "BED WARS"),
    THE_TNT_GAMES("The TNT Games", "THE TNT GAMES"),
    BLITZ_SURVIVAL_GAMES("Blitz SurvivalGames", "BLITZ SG"),
    WARLORDS("WarLords", "WARLORDS"),
    MURDER_MYSTERY("Murder Mystery", "MURDER MYSTERY"),
    PLAYER_HOUSING("Player Housing", "HOUSING"),
    UHC_CHAMPIONS("UHC Champions", "UHC CHAMPIONS"),
    SPEED_UHC("Speed UHC", "SPEED UHC"),
    CLASSIC_GAMES("Classic Games", "CLASSIC GAMES"),
    BUILD_BATTLE("Build Battle", "BUILD BATTLE"),
    MINI_WALLS("Mini Walls", "MINI WALLS"),
    SMASH_HEROES("Smash Heroes", "SMASH HEROES"),
    THE_PIT("The Pit", "THE HYPIXEL PIT"),
    DUELS("Duels", "DUELS");

    public String inEnglish = "";
    public String scoreboardString = "";

    HypixelGamemode(String givenEnglish, String givenScoreboardString) {
        this.inEnglish = givenEnglish;
        this.scoreboardString = givenScoreboardString;
    }

    public String getInEnglish() {
        return this.inEnglish;
    }

}
