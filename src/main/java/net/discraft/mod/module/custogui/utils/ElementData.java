package net.discraft.mod.module.custogui.utils;

import net.discraft.mod.module.custogui.utils.cps.CpsNode;

import java.util.ArrayList;

public class ElementData {

    public ArrayList<CpsNode> CPSNodes = new ArrayList<>();

    public float serverReach = 0;
    public float clientReach = 0;

    public int killCounter = 0;
    public int deathCounter = 0;

    public void update() {

        ArrayList<CpsNode> newList = new ArrayList<>();

        for (CpsNode node : CPSNodes) {
            if (!node.mustDie()) {
                newList.add(node);
            }
        }

        CPSNodes = newList;

    }

}
