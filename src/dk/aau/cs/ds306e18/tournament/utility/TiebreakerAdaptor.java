package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.*;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;

import java.lang.reflect.Type;

public class TiebreakerAdaptor implements JsonSerializer<TieBreaker>, JsonDeserializer<TieBreaker> {

    @Override
    public JsonElement serialize(TieBreaker src, Type typeOfSrc, JsonSerializationContext context) {
        // Save class type and the instance's properties
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public TieBreaker deserialize(JsonElement json, Type redundantType, JsonDeserializationContext context) {
        // Load class type and instance's properties
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            // Deserialize based on the class type and the loaded properties
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException e) {
            // If class has not been found, print error to user
            System.out.println("ERROR: When deserializing, could not find: " + e.getMessage());
        }

        return null;
    }
}
