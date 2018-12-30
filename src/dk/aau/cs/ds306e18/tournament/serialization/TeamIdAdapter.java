package dk.aau.cs.ds306e18.tournament.serialization;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.Tournament;

import java.io.IOException;

/**
 * This adapter changes how teams are serialized. Instead of storing a copy of the team, this will instead store
 * the team's index based on the list of teams in the Tournament class. On deserialization the team in retrieved from
 * the same list. The list in the Tournament class overrides this behaviour with a TrueTeamListAdapter.
 */
public class TeamIdAdapter extends TypeAdapter<Team> {

    @Override
    public void write(JsonWriter out, Team team) throws IOException {

        // Determine the index of the team. Use -1 if team is null
        int id = -1;
        if (team != null) {
            id = Tournament.get().getTeams().indexOf(team);
        }

        // Store index
        out.value(id);
    }

    @Override
    public Team read(JsonReader in) throws IOException {

        // Read index
        int id = in.nextInt();

        // Get the team from index
        if (id == -1) {
            return null;
        } else {
            return Tournament.get().getTeams().get(id);
        }
    }
}
