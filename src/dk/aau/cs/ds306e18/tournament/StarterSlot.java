package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Participant;

/** The StarterSlot is a Slot for a Match. The Participant in this slot is known at the start of the Stage. */
public class StarterSlot implements Slot {

    private Participant team;

    /** The StarterSlot is a Slot for a Match. The Participant in this slot is known at the start of the Stage. */
    public StarterSlot(Participant team) {
        this.team = team;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public Participant getTeam() {
        return team;
    }
}
