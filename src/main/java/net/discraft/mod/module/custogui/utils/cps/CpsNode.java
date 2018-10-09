package net.discraft.mod.module.custogui.utils.cps;

public class CpsNode {

    public int liveTime = 20;

    public boolean mustDie(){
        liveTime--;
        if(liveTime <= 0){
            return true;
        }
        return false;
    }

}
