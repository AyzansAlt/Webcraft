package net.discraft.mod.module.hypixel.api.adapters;

import com.google.gson.*;
import net.discraft.mod.module.hypixel.api.util.APIUtil;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

/**
 * Our dates are always saved as a timestamp
 * if we diverge from that path we can adapt
 * it in here as well by just using some more
 * parsing.
 */
public class DateTimeTypeAdapter implements JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {

    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toInstant().toEpochMilli());
    }

    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return APIUtil.getDateTime(Long.parseLong(json.getAsString()));
    }

}
