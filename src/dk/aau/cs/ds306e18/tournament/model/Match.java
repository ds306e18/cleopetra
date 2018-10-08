package dk.aau.cs.ds306e18.tournament.model;

import java.util.*;

/** <p>A Match consists of two Slots, which holds the Teams participating in the Match, and each Team's
 * score. A Slot might contain a Team, which is temporarily unknown (e.g. when the
 * Team is the winner of another Match). The method {@code isReadyToPlay()} returns true, when both Slots'
 * Team is known and ready.</p>
 * <p>When the Match get marked as has been played, and it is possible to retrieve
 * the winner and the loser of the match.</p> */
public class Match {

    private int blueScore = 0;
    private int orangeScore = 0;
    private boolean played = false;
    private Slot blueSlot;
    private Slot orangeSlot;

    /** <p>A Match consists of two Slots, which holds the Teams participating in the Match, and each Team's
     * score. A Slot might contain a Team, which is temporarily unknown (e.g. when the
     * Team is the winner of another Match). The method {@code isReadyToPlay()} returns true, when both Slots'
     * Team is known and ready.</p>
     * <p>When the Match get marked as has been played, and it is possible to retrieve
     * the winner and the loser of the match.</p> */
    public Match(Slot blueSlot, Slot orangeSlot) {
        this.blueSlot = blueSlot;
        this.orangeSlot = orangeSlot;
    }

    /** Returns true when both Slots' Team is known and ready, even if the Match has already been played. */
    public boolean isReadyToPlay() {
        return (blueSlot.isReady() && orangeSlot.isReady());
    }

    public Team getWinner() {
        if (!played) throw new IllegalStateException("Match has not been played.");
        if (blueScore > orangeScore)
            return blueSlot.getTeam();
        return orangeSlot.getTeam();
    }

    public Team getLoser() {
        if (!played) throw new IllegalStateException("Match has not been played.");
        if (blueScore > orangeScore)
            return orangeSlot.getTeam();
        return blueSlot.getTeam();
    }

    public MatchStatus getStatus() {
        if (!isReadyToPlay()) {
            return MatchStatus.NOT_PLAYABLE;
        } else if (!played) {
            return MatchStatus.READY_TO_BE_PLAYED;
        } else if (blueScore == orangeScore) {
            return MatchStatus.DRAW;
        } else if (blueScore > orangeScore) {
            return MatchStatus.BLUE_WINS;
        } else {
            return MatchStatus.ORANGE_WINS;
        }
    }

    /** Returns a list of all Matches that must be finished before this Match is playable. The matches will be ordered
     * after breadth-first-search approach. */
    public ArrayList<Match> getChildMatchesBFS() {
        // Breadth-first-search can be performed using a queue
        LinkedList<Match> queue = new LinkedList<>();
        ArrayList<Match> matches = new ArrayList<>();
        queue.add(this);

        // Matches are polled from the queue until it is empty
        while (!queue.isEmpty()) {
            Match match = queue.poll();
            matches.add(match);

            // Enqueue child matches, if any
            Match blueMatch = blueSlot.getRequiredMatch();
            if (blueMatch != null) queue.add(blueMatch);
            Match orangeMatch = orangeSlot.getRequiredMatch();
            if (orangeMatch != null) queue.add(orangeMatch);
        }

        return matches;
    }

    public boolean hasBeenPlayed() {
        return played;
    }

    public void setHasBeenPlayed(boolean played) {
        this.played = played;
    }

    public Team getBlueTeam() {
        return blueSlot.getTeam();
    }

    public Team getOrangeTeam() {
        return orangeSlot.getTeam();
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

    public int getBlueScore() {
        return blueScore;
    }

    public void setBlueScore(int blueScore) {
        this.blueScore = blueScore;
    }

    public int getOrangeScore() {
        return orangeScore;
    }

    public void setOrangeScore(int orangeScore) {
        this.orangeScore = orangeScore;
    }

    public void setScores(int blueScore, int orangeScore) {
        this.blueScore = blueScore;
        this.orangeScore = orangeScore;
    }
}
