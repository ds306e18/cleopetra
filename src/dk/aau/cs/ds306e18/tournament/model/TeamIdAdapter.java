package dk.aau.cs.ds306e18.tournament.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class TeamIdAdapter extends TypeAdapter<Team> {

    @Override
    public void write(JsonWriter out, Team team) throws IOException {
        // Determine the index of the team. Use -1 if team is null
        int id = -1;
        if (team != null) {
            id = Tournament.get().getTeams().indexOf(team);
        }
        // Store index
        out.beginObject();
        out.name("team");
        out.value(id);
        out.endObject();
    }

    @Override
    public Team read(JsonReader in) throws IOException {
        // Read index
        in.beginObject();
        in.nextName();
        int id = in.nextInt();
        in.endObject();
        // Get the team from index
        if (id == -1) {
            return null;
        } else {
            return TeamDeserializationCatcher.teams.get(id);
        }
    }
}
