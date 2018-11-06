package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.utility.instanceCreators.TieBreakerInstanceCreator;

/**
 * SerializeManager handles all methods associated with serializing and deserializing a Tournament object
 */
public class Serializer {

    // Initialize a GSON-object where the TieBreaker InstanceCreator has been registered
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(TieBreaker.class, new TieBreakerInstanceCreator())
            .registerTypeAdapter(Format.class, new FormatAdaptor()).create();

    /**
     * Takes a tournament and returns the serialized object as a String
     *
     * @param tournament is the object to be serialized
     * @return JSON-string representation of given tournament parameter
     */
    public static String serialize(Tournament tournament) {
        return gson.toJson(tournament);
    }

    /**
     * Takes a JSON-string representation of a Tournament object, deserializes it, and returns it
     *
     * @param json the JSON-string representation
     * @return the reserialized Tournament object
     */
    public static Tournament deserialise(String json) {
        return gson.fromJson(json, Tournament.class);
    }
}
