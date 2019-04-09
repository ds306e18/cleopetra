package dk.aau.cs.ds306e18.tournament.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This adapter is meant to override the behaviour of the TeamIdAdapter. The list of teams in the Tournament class
 * uses this adapter when serialized, which should be the only use of this. This adapter stores the actual teams in
 * a list, so the TeamIdAdapter can retrieve them again on deserialization.
 */
public class TrueTeamListAdapter extends TypeAdapter<ArrayList<Team>> {

    // Clean gson for teams (without turning them into id's)
    private final Gson teamSpecificGson = new GsonBuilder()
            .registerTypeAdapter(Bot.class, new BotAdapter())
            .create();

    @Override
    public void write(JsonWriter out, ArrayList<Team> value) throws IOException {

        // Construct array of actual teams
        out.beginArray();
        for (Team team : value) {
            teamSpecificGson.toJson(team, Team.class, out);
        }
        out.endArray();
    }

    @Override
    public ArrayList<Team> read(JsonReader in) throws IOException {

        ArrayList<Team> teams = new ArrayList<>();

        // Re-construct ArrayList of teams
        in.beginArray();
        while (!in.peek().equals(JsonToken.END_ARRAY)) {
            teams.add(teamSpecificGson.fromJson(in, Team.class));
        }
        in.endArray();

        return teams;
    }
}
