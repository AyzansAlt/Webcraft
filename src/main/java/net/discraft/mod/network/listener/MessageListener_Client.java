package net.discraft.mod.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.discraft.mod.network.messages.Response_FromServer_Broadcast;
import net.discraft.mod.notification.ClientNotification;

/**
 * Created by Scott on 9/23/2017.
 */
public class MessageListener_Client extends Listener {

    @Override
    public void received(Connection connection, Object object) {

        connection.isIdle();

        if(object instanceof Response_FromServer_Broadcast){
            Response_FromServer_Broadcast response = ((Response_FromServer_Broadcast)object);

            ClientNotification.createNotification(response.givenBroadcastMessage,"Network Broadcast");

        }

    }

}