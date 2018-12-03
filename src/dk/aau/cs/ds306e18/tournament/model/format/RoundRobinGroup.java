package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

import java.util.ArrayList;


public class RoundRobinGroup {

    private ArrayList<Team> teams;
    private ArrayList<ArrayList<Match>> rounds;

    public RoundRobinGroup(ArrayList<Team> seededTeams) {
        this.teams = new ArrayList<>(seededTeams);
    }

    public void setRounds(ArrayList<ArrayList<Match>> round){
        this.rounds = round;
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

    public ArrayList<Match> getMatches(){

        ArrayList<Match> matches = new ArrayList<>();

        for (ArrayList<Match> round : rounds)
            matches.addAll(round);

        return matches;
    }

    public ArrayList<ArrayList<Match>> getRounds() {
        return rounds;
    }
}
