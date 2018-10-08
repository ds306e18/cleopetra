package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.participants.Team;

/** The StarterSlot is a Slot for a Match. The Team in this slot is known at the start of the Stage. */
public class StarterSlot implements Slot {

    private Team team;

    /** The StarterSlot is a Slot for a Match. The Participant in this slot is known at the start of the Stage. */
    public StarterSlot(Team team) {
        this.team = team;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public Team getTeam() {
        return team;
    }
}
