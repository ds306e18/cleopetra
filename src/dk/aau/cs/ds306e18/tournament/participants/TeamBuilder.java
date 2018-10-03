package dk.aau.cs.ds306e18.tournament.participants;

import java.util.ArrayList;

class TeamBuilder {

    private String teamName;
    private int wins = 0;
    private int losses = 0;
    private int matchesPlayed = 0;
    //choosing 1500 ELO as default
    private double ranking = 1500;
    private ArrayList<Player> members = new ArrayList<>();

    public void addPlayer(Player player) {
        members.add(player);
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public void setRanking(double ranking) {
        this.ranking = ranking;
    }

    public Team build() {
        return new Team(this.teamName, this.wins, this.losses, this.matchesPlayed, this.ranking, this.members);
    }
}
