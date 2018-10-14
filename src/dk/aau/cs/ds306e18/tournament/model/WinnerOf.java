package dk.aau.cs.ds306e18.tournament.model;

/** The WinnerOf is a Slot for a Match, where the Team is unknown until another Match has been played.
 * The Team in this Slot will be the winner of that Match. */
public class WinnerOf implements Slot {

    private Match match;

    /** The WinnerOf is a Slot for a Match, where the Team is unknown until another Match has been played.
     * The Team in this Slot will be the winner of that Match. */
    public WinnerOf(Match match) {
        this.match = match;
    }

    @Override
    public boolean isReady() {
        return match.hasBeenPlayed();
    }

    @Override
    public Team getTeam() {
        return match.getWinner();
    }

    @Override
    public Match getRequiredMatch() {
        return match;
    }
}

