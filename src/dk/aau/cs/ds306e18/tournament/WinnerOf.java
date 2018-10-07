package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Participant;

/** The WinnerOf is a Slot for a Match, where the Participant is unknown until another Match has been played.
 * The Participant in this Slot will be the winner of that Match. */
public class WinnerOf implements Slot {

    private Match match;

    /** The WinnerOf is a Slot for a Match, where the Participant is unknown until another Match has been played.
     * The Participant in this Slot will be the winner of that Match. */
    public WinnerOf(Match match) {
        this.match = match;
    }

    @Override
    public boolean isReady() {
        return match.hasBeenPlayed();
    }

    @Override
    public Participant getTeam() {
        return match.getWinner();
    }
}
