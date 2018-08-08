package net.discraft.mod.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import net.discraft.mod.network.listener.MessageListener_Client;

import java.io.IOException;

/**
 * Created by Scott on 9/23/2017.
 */
public class ServerNetworkConnection {

    public static Server server = new Server(32768, 8192);
    public static Kryo kryoInstance;

    public static void initializeServer(int tcpPort, int udpPort) throws IOException {

        /** Register Objects */
        kryoInstance = server.getKryo();
        ObjectRegistry.registerObjects(kryoInstance);

        server.start();
        server.bind(tcpPort, udpPort);
        System.out.println("Created new Management Server Instance on ports TCP/UDP: " + tcpPort + "/" + udpPort);

        /** Start Server Listener */
        server.addListener(new MessageListener_Client());


    }

    public Kryo getKryoInstance() {
        return kryoInstance;
    }

}