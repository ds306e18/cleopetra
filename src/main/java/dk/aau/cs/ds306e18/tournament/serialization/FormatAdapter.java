package dk.aau.cs.ds306e18.tournament.serialization;

import com.google.gson.*;
import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.format.Format;

import java.lang.reflect.Type;

public class FormatAdapter implements JsonSerializer<Format>, JsonDeserializer<Format> {

    @Override
    public JsonElement serialize(Format src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        // denote type of class when serializing
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        // append serialized context of class to json-object
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Format deserialize(JsonElement json, Type redundantType, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        // get SimpleName of class specified when serializing
        String type = jsonObject.get("type").getAsString();
        // get all specified properties, that being fields withing specified class,
        JsonElement element = jsonObject.get("properties");

        try {
            // specify location of classes, these are contained within root of the format package
            String thepackage = "dk.aau.cs.ds306e18.tournament.model.format.";
            // return the class found with given elements and class-type
            return context.deserialize(element, Class.forName(thepackage + type));
        } catch (ClassNotFoundException e) {
            // if class has not been found, print error to user
            Main.LOGGER.log(System.Logger.Level.ERROR, "Error when deserializing stage format. Could not find package.", e);
        }
        return null;
    }
}