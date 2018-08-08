package net.discraft.mod.network;

import com.esotericsoftware.kryo.Kryo;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import net.discraft.mod.network.messages.Response_FromServer_Broadcast;

import java.util.*;

/**
 * Created by Scott on 9/23/2017.
 */
public class ObjectRegistry {

    /**
     * Register Objects - Register all Objects/Requests/Responses used by the Kryonet Instance
     *
     * @param givenKryo - Given Kryo Instance
     */
    public static void registerObjects(Kryo givenKryo) {

        /** Useful Variables */
        givenKryo.register(String.class);
        givenKryo.register(int.class);
        givenKryo.register(long.class);
        givenKryo.register(ArrayList.class);
        givenKryo.register(UUID.class);
        givenKryo.register(Iterator.class);
        givenKryo.register(JsonDeserializationContext.class);
        givenKryo.register(Map.Entry.class);
        givenKryo.register(Map.class);
        givenKryo.register(JsonElement.class);
        givenKryo.register(Date.class);
        givenKryo.register(boolean.class);
        givenKryo.register(Boolean.class);

        /** Requests */

        /** Responses */
        givenKryo.register(Response_FromServer_Broadcast.class);

        /** Normal Objects */

    }

}
