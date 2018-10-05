package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Participant;

public class Match {

    private MatchResult result;
    private Participant blueTeam;
    private Participant orangeTeam;
    private Match bluePriorMatch;
    private Match orangePriorMatch;

    public boolean isReadyToPlay() {
        return (bluePriorMatch != null && bluePriorMatch.hasBeenPlayed()
                && orangePriorMatch != null && orangePriorMatch.hasBeenPlayed());
    }

    public boolean hasBeenPlayed() {
        return result != null;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public Participant getWinner() {
        switch (result.getConclusion()) {
            case BLUE_WINS: return blueTeam;
            case ORANGE_WINS: return orangeTeam;
            default: return blueTeam; // TODO Add TieBreaker
        }
    }

    public Participant getLoser() {
        switch (result.getConclusion()) {
            case BLUE_WINS: return orangeTeam;
            case ORANGE_WINS: return blueTeam;
            default: return orangeTeam; // TODO Add TieBreaker
        }
    }

    public Participant getBlueTeam() {
        return blueTeam;
    }

    public void setBlueTeam(Participant blueTeam) {
        this.blueTeam = blueTeam;
    }

    public Participant getOrangeTeam() {
        return orangeTeam;
    }

    public void setOrangeTeam(Participant orangeTeam) {
        this.orangeTeam = orangeTeam;
    }

    public Match getBluePriorMatch() {
        return bluePriorMatch;
    }

    public void setBluePriorMatch(Match bluePriorMatch) {
        this.bluePriorMatch = bluePriorMatch;
    }

    public Match getOrangePriorMatch() {
        return orangePriorMatch;
    }

    public void setOrangePriorMatch(Match orangePriorMatch) {
        this.orangePriorMatch = orangePriorMatch;
    }
}
