package net.discraft.mod.servers;

public interface Server {

    void tick();

    void disconnect();

    void connect();

    void onVictory();

    void onDefeat();

}
