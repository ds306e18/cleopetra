package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Participant;

/** <p>A Match consists of two Slots, which holds the Participants participating in the Match, and a Result,
 * which is initially null. A Slot might contain a Participant, which is temporarily unknown (e.g. when the
 * Participant is the winner of another Match). The method {@code isReadyToPlay()} returns true, when both Slots'
 * Participant is known and ready.</p>
 * <p>When the Result is set to a non-null value, {@code hasBeenPlayed()} returns true, and it is possible to retrieve
 * the winner and the loser of the match.</p> */
public class Match {

    private MatchResult result;
    private Slot blueSlot;
    private Slot orangeSlot;

    /** <p>A Match consists of two Slots, which holds the Participants participating in the Match, and a Result,
     * which is initially null. A Slot might contain a Participant, which is temporarily unknown (e.g. when the
     * Participant is the winner of another Match). The method {@code isReadyToPlay()} returns true, when both Slots'
     * Participant is known and ready.</p>
     * <p>When the Result is set to a non-null value, {@code hasBeenPlayed()} returns true, and it is possible to retrieve
     * the winner and the loser of the match.</p> */
    public Match(Slot blueSlot, Slot orangeSlot) {
        this.blueSlot = blueSlot;
        this.orangeSlot = orangeSlot;
    }

    /** Returns true when both Slots' Participant is known and ready, even if the Match has already been played. */
    public boolean isReadyToPlay() {
        return (blueSlot.isReady() && orangeSlot.isReady());
    }

    /** Returns true if the result of the Match is known. */
    public boolean hasBeenPlayed() {
        return result != null;
    }

    /** Returns the winner of the Match. Throws a NullPointerException if the Match Result is unknown. */
    public Participant getWinner() {
        if (result.getConclusion() == Conclusion.BLUE_WINS)
            return blueSlot.getTeam();
        return orangeSlot.getTeam();
    }

    /** Returns the loser of the Match. Throws a NullPointerException if the Match Result is unknown. */
    public Participant getLoser() {
        if (result.getConclusion() == Conclusion.BLUE_WINS)
            return orangeSlot.getTeam();
        return blueSlot.getTeam();
    }

    public Participant getBlueTeam() {
        return blueSlot.getTeam();
    }

    public Participant getOrangeTeam() {
        return orangeSlot.getTeam();
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public Slot getBlueSlot() {
        return blueSlot;
    }

    public void setBlueSlot(Slot blueSlot) {
        this.blueSlot = blueSlot;
    }

    public Slot getOrangeSlot() {
        return orangeSlot;
    }

    public void setOrangeSlot(Slot orangeSlot) {
        this.orangeSlot = orangeSlot;
    }
}
