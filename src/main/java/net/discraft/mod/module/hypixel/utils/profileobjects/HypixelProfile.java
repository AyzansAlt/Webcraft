package net.discraft.mod.module.hypixel.utils.profileobjects;

import net.discraft.mod.utils.EnumHypixelRank;

public class HypixelProfile {

    public HypixelProfileSettings hypixelProfileSettings = new HypixelProfileSettings();

    public EnumHypixelRank hypixelRank = EnumHypixelRank.DEFAULT;

    public String hypixelProfileID = "Unknown";
    public String hypixelUsername = "Unknown";
    public String hypixelDisplayName = "Unknown";

    public double networkEXP = 0;

    public long lastLogin = 0;
    public long firstLogin = 0;

    public boolean isBot = false;

    public long karma = 0;

    public ArcadeStats arcadeStats = new ArcadeStats();
    public BedwarsStats bedwarsStats = new BedwarsStats();
    public BuildBattleStats buildBattleStats = new BuildBattleStats();
    public CopsAndCrimsStats copsAndCrimsStats = new CopsAndCrimsStats();
    public CrazyWallsStats crazyWallsStats = new CrazyWallsStats();
    public DuelsStats duelsStats = new DuelsStats();
    public HungerGamesStats hungerGamesStats = new HungerGamesStats();
    public WallsStats wallsStats = new WallsStats();
}
