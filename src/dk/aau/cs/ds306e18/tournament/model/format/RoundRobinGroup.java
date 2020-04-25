package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Series;

import java.util.ArrayList;
import java.util.List;


public class RoundRobinGroup {

    private ArrayList<Team> teams;
    private ArrayList<ArrayList<Series>> rounds;

    public RoundRobinGroup(List<Team> teams) {
        this.teams = new ArrayList<>(teams);
    }

    public void setRounds(ArrayList<ArrayList<Series>> round){
        this.rounds = round;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Series> getMatches() {

        ArrayList<Series> series = new ArrayList<>();

        for (ArrayList<Series> round : rounds)
            series.addAll(round);

        return series;
    }

    public ArrayList<ArrayList<Series>> getRounds() {
        return rounds;
    }
}
