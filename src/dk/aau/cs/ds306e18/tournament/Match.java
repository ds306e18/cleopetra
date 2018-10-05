package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Participant;

public class Match {

    private Participant blueTeam;
    private Participant orangeTeam;
    private Match bluePriorMatch;
    private Match orangePriorMatch;

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
