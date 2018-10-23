package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tournament {

    private String name = "Unnamed Tournament";
    private ArrayList<Team> teams = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTeam(Team team) {
        teams.add(team);
        sortTeams();
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public void removeTeam(int index) {
        teams.remove(index);
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    public void sortTeams() {
        // Sort teams by comparator and not data structure, since seed value is not final
        teams.sort(Comparator.comparingInt(Team::getInitialSeedValue));
    }
}
