package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.utility.instanceCreators.TieBreakerInstanceCreator;

/**
 * SerializeManager handles all methods associated with serializing and deserializing a Tournament object
 */
public class SerializeManager {

    private final static String filename = "tournamentState.obj";

    /**
     * Takes a tournament and returns the serialized object as a String
     *
     * @param tournament is the object to be serialized
     * @return JSON-string representation of given tournament parameter
     */
    public static String serialize(Tournament tournament) {
        Gson gson = new GsonBuilder().registerTypeAdapter(TieBreaker.class, new TieBreakerInstanceCreator()).create();
        return gson.toJson(tournament);
    }

    /**
     * Takes a JSON-string representation of a Tournament object, deserializes it, and returns it
     *
     * @param json the JSON-string representation
     * @return the reserialized Tournament object
     */
    public static Tournament deserialise(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(TieBreaker.class, new TieBreakerInstanceCreator()).create();
        return gson.fromJson(json, Tournament.class);
    }
}
