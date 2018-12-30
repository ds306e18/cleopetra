package dk.aau.cs.ds306e18.tournament.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TeamIdToPointMapAdapter extends TypeAdapter<HashMap<Team, Integer>> {

    @Override
    public void write(JsonWriter out, HashMap<Team, Integer> value) throws IOException {

        List<Team> allTeams = Tournament.get().getTeams();

        // Build array of objects containing index and points. E.g.: { "team": 0, "points": 2 }
        out.beginArray();
        for (Team team : value.keySet()) {

            int id = allTeams.indexOf(team);
            int points = value.get(team);

            out.beginObject();
            out.name("team").value(id);
            out.name("points").value(points);
            out.endObject();
        }
        out.endArray();
    }

    @Override
    public HashMap<Team, Integer> read(JsonReader in) throws IOException {

        HashMap<Team, Integer> map = new HashMap<>();
        List<Team> allTeams = Tournament.get().getTeams();

        // Re-construct map from array
        in.beginArray();
        while (!in.peek().equals(JsonToken.END_ARRAY)) {

            in.beginObject();
            in.nextName();
            int id = in.nextInt();
            in.nextName();
            int points = in.nextInt();
            in.endObject();

            // Put team in map
            Team team = allTeams.get(id);
            map.put(team, points);
        }
        in.endArray();

        return map;
    }
}
