package dk.aau.cs.ds306e18.tournament.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamIdListAdapter extends TypeAdapter<ArrayList<Team>> {

    @Override
    public void write(JsonWriter out, ArrayList<Team> value) throws IOException {
        List<Team> allTeams = Tournament.get().getTeams();
        // Build json array
        out.beginArray();
        for (Team team : value) {
            if (team != null) {
                // Use index of each team instead of the actual team
                int id = allTeams.indexOf(team);
                out.value(id);
            }
        }
        out.endArray();
    }

    @Override
    public ArrayList<Team> read(JsonReader in) throws IOException {

        ArrayList<Team> teams = new ArrayList<>();
        List<Team> allTeams = Tournament.get().getTeams();

        // Read array to re-construct list
        in.beginArray();
        while (!in.peek().equals(JsonToken.END_ARRAY)) {
            // Find team from index
            int id = in.nextInt();
            teams.add(allTeams.get(id));
        }
        in.endArray();

        return teams;
    }
}
