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
    private Team predefinedBlue, predefinedOrange; // teams are only stored, when they start in this match, and is not the winner/loser of a previous match
    private Match winnerDestination, loserDestination, blueFrom, orangeFrom;
    private boolean blueIsPrevWinner, orangeIsPrevWinner;

    public Match() { }

    public Match(Team predefinedBlue, Team predefinedOrange) {
        this.predefinedBlue = predefinedBlue;
        this.predefinedOrange = predefinedOrange;
    }

    public void setWinnerDestination(Match match, boolean blueInWinner) {
        // TODO
    }

    public void setLoserDestination(Match match, boolean blueInWinner) {
        // TODO
    }

    /** Returns true when both Teams are known and ready, even if the Match has already been played. */
    public boolean isReadyToPlay() {
        return getBlueTeam() != null && getOrangeTeam() != null;
    }

    public Team getWinner() {
        if (!played) throw new IllegalStateException("Match has not been played.");
        if (blueScore > orangeScore)
            return getBlueTeam();
        return getOrangeTeam();
    }

    public Team getLoser() {
        if (!played) throw new IllegalStateException("Match has not been played.");
        if (blueScore > orangeScore)
            return getOrangeTeam();
        return getBlueTeam();
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

    /** Returns a list of all Matches that must be finished before this Match is playable, including itself.
     * The matches will be ordered after breadth-first search approach. If the order is reversed, the order will be
     * the logical order of playing the Matches, with the root as the last Match. */
    public ArrayList<Match> getTreeAsListBFS() {
        // Breadth-first search can be performed using a queue
        LinkedList<Match> queue = new LinkedList<>();
        ArrayList<Match> matches = new ArrayList<>();
        queue.add(this);

        // Matches are polled from the queue until it is empty
        while (!queue.isEmpty()) {
            Match match = queue.poll();
            matches.add(match);

            // Enqueue child matches, if any
            // Orange is added first - this means the final order will be the reverse of the logical
            // order of playing matches
            if (orangeFrom != null) queue.add(orangeFrom);
            if (blueFrom != null) queue.add(blueFrom);
        }

        return matches;
    }

    /** Returns a list of all Matches that must be finished before this Match is playable, including itself.
     * The Matches will be ordered after depth-first search approach. */
    public ArrayList<Match> getTreeAsListDFS() {
        // Depth-first search can be performed using a stack
        LinkedList<Match> stack = new LinkedList<>();
        ArrayList<Match> matches = new ArrayList<>();
        stack.push(this);

        // Matches are popped from the stack until it is empty
        while (!stack.isEmpty()) {
            Match match = stack.pop();
            matches.add(match);

            // Push child matches, if any
            if (blueFrom != null) stack.push(blueFrom);
            if (orangeFrom != null) stack.push(orangeFrom);
        }

        return matches;
    }

    /** Returns true if the other Match must be concluded before this match is playable. */
    public boolean dependsOn(Match otherMatch) {
        if (this == otherMatch) return false;
        return getTreeAsListBFS().contains(otherMatch);
    }

    /** Sets both team scores to 0 and marks match as not played yet. */
    public void reset() {
        setScores(0, 0, false);
    }

    public boolean hasBeenPlayed() {
        return played;
    }

    public void setHasBeenPlayed(boolean played) {
        this.played = played;
    }

    public boolean blueIsPredefined() {
        return predefinedBlue != null;
    }

    public boolean orangeIsPredefined() {
        return predefinedOrange != null;
    }

    public Team getBlueTeam() {
        if (predefinedBlue != null) return predefinedBlue;
        if (blueFrom == null) return null;
        if (!blueFrom.hasBeenPlayed()) return null;
        if (blueIsPrevWinner) return blueFrom.getWinner();
        else return blueFrom.getLoser();
    }

    public Team getOrangeTeam() {
        if (predefinedOrange != null) return predefinedOrange;
        if (orangeFrom == null) return null;
        if (!orangeFrom.hasBeenPlayed()) return null;
        if (orangeIsPrevWinner) return orangeFrom.getWinner();
        else return orangeFrom.getLoser();
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

    public void setScores(int blueScore, int orangeScore, boolean hasBeenPlayed) {
        this.blueScore = blueScore;
        this.orangeScore = orangeScore;
        this.played = hasBeenPlayed;
    }
}
