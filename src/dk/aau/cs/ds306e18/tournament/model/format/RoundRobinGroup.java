package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

import java.util.ArrayList;


public class RoundRobinGroup {

    private ArrayList<Team> teams;
    private static final Team DUMMY_TEAM = new Team("Dummy", new ArrayList<Bot>(), 0, "");
    private ArrayList<Match> matches;

    RoundRobinGroup(ArrayList<Team> seededTeams) {

        this.teams = new ArrayList<>(seededTeams);

        //if there is an uneven amount of teams, add a dummy team and later remove matches that include the dummy team
        if (teams.size() % 2 != 0)
            teams.add(DUMMY_TEAM);
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }
}
