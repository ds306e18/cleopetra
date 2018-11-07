package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

import java.util.ArrayList;


public class RoundRobinGroup {

    private ArrayList<Team> teams;
        private ArrayList<Match> matches;

    RoundRobinGroup(ArrayList<Team> seededTeams) {

        this.teams = new ArrayList<>(seededTeams);
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Team> getTeamsWithoutDummy() {
        ArrayList<Team> teams = new ArrayList<>();
        for (Team team: this.teams) {
            if (team.equals(RoundRobinFormat.getDummyTeam())) {
                continue;
            } else teams.add(team);
        }
        return teams;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }
}
