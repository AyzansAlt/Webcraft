package net.discraft.mod.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

/**
 * Created by Scott on 9/23/2017.
 */
public class ClientNetworkConnection {

    public static Client client = new Client(32768, 8192);
    public static Kryo kryoInstance;

    public static void initializeClientConnection(String port, int tcpPort, int udpPort) throws IOException {

        /** Register Objects */
        kryoInstance = client.getKryo();
        ObjectRegistry.registerObjects(kryoInstance);

        client.start();
        client.connect(5000, port, tcpPort, udpPort);
        System.out.println("Connecting to " + port + " on ports TCP/UDP: " + tcpPort + "/" + udpPort);

    }

    public static Kryo getKryoInstance() {
        return kryoInstance;
    }

}