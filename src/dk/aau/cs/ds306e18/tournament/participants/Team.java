package dk.aau.cs.ds306e18.tournament.participants;

import java.nio.file.Path;
import java.util.ArrayList;

public class Team implements Participant {

    private String teamName;
    private int wins;
    private int losses;
    private int matchesPlayed;
    private double ranking;
    private ArrayList<Player> members;

    Team(String teamName, int wins, int losses, int matchesPlayed, double ranking, ArrayList<Player> members) {
        this.teamName = teamName;
        this.wins = wins;
        this.losses = losses;
        this.matchesPlayed = matchesPlayed;
        this.ranking = ranking;
        this.members = members;
    }

    @Override
    public String getName() {
        return this.teamName;
    }

    @Override
    public int getWins() {
        return this.wins;
    }

    @Override
    public int getLosses() {
        return this.losses;
    }

    @Override
    public int getMatchesPlayed() {
        return this.matchesPlayed;
    }

    @Override
    public double getRanking() {
        return this.ranking;
    }

    @Override
    public ArrayList<Path> getConfigPath() {
        ArrayList<Path> configPaths = new ArrayList<>();

        for(Player player : members) {
            configPaths.add(player.getConfigPath().get(0));
        }

        return configPaths;
    }
}