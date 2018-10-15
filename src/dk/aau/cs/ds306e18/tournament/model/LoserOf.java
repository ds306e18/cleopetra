package dk.aau.cs.ds306e18.tournament.model;


/** The LoserOf is a Slot for a Match, where the Team is unknown until another Match has been played.
 * The Team in this Slot will be the loser of that Match. */
public class LoserOf implements Slot {

    private Match match;

    /** The LoserOf is a Slot for a Match, where the Team is unknown until another Match has been played.
     * The Team in this Slot will be the loser of that Match. */
    public LoserOf(Match match) {
        this.match = match;
    }

    @Override
    public boolean isReady() {
        return match.hasBeenPlayed();
    }

    @Override
    public Team getTeam() {
        return match.getLoser();
    }

    @Override
    public Match getRequiredMatch() {
        return match;
    }
}

