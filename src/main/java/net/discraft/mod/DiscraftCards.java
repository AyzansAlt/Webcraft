package net.discraft.mod;

import java.util.Random;

public class DiscraftCards {

    public static String currentCard = "Charging Creepers";

    public static String[] cards = {
            "Charging Creepers...",
            "Skinning Skeletons...",
            "Crafting Tables...",
            "Burning Furnaces...",
            "Cooking Pork...",
            "Fighting Zombies...",
            "Click-baiting Videos...",
            "Finding Entity303...",
            "Raging at Skywars...",
            "Building a Dirt House...",
            "Griefing Noobs...",
            "Looking for Herobrine...",
            "Applying for Staff...",
            "I'm from Planet Minecraft...",
            "Flowering Flowers...",
            "Equipping Armor...",
            "Spreading some Jif...",
            "Hacking on Hypixel..."
    };

    public static void generateRandom() {

        Random r = new Random();
        currentCard = cards[r.nextInt(cards.length)];

    }

}
