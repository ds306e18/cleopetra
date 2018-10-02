package dk.aau.cs.ds306e18.tournament.participants;

import java.nio.file.Path;
import java.util.ArrayList;

public class Player implements Participant {

    private String name;
    private int wins;
    private int losses;
    private int matchesPlayed;
    private double ranking;
    private Path configPath;

    public Player(String name, Path configPath) {
        this.name = name;
        this.configPath = configPath;
        //defaults
        this.wins = 0;
        this.losses = 0;
        this.matchesPlayed = 0;
        //choosing 1500 ELO as default, can be overridden
        this.ranking = 1500;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWins() {
        return wins;
    }

    @Override
    public int getLosses() {
        return losses;
    }

    @Override
    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    @Override
    public double getRanking() {
        return ranking;
    }

    @Override
    public ArrayList<Path> getConfigPath() {
        ArrayList<Path> paths = new ArrayList<>();
        paths.add(this.configPath);
        return paths;
    }
}
