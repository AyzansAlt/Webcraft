package net.discraft.mod.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.discraft.mod.Discraft;
import net.discraft.mod.network.messages.Response_FromServer_Broadcast;
import net.discraft.mod.network.messages.Response_FromServer_PlayerCount;
import net.discraft.mod.notification.ClientNotification;

/**
 * Created by Scott on 9/23/2017.
 */
public class MessageListener_Client extends Listener {

    @Override
    public void received(Connection connection, Object object) {

        connection.isIdle();

        if (object instanceof Response_FromServer_Broadcast) {
            Response_FromServer_Broadcast response = ((Response_FromServer_Broadcast) object);

            ClientNotification.createNotification(response.givenBroadcastMessage, "Network Broadcast");

        } else if (object instanceof Response_FromServer_PlayerCount) {
            Response_FromServer_PlayerCount response = ((Response_FromServer_PlayerCount) object);

            Discraft.getInstance().discraftVariables.playerCount = response.playCount;

        }

    }

}