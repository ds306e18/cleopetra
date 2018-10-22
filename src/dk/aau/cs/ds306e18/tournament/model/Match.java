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
    private Slot blueSlot, orangeSlot;
    // Where do winner/loser go? (They can only go to one destination)
    private Match winnerDestination, loserDestination;

    /** Construct a Match where both Teams are known from the start. */
    public Match(Team blue, Team orange) {
        setBlue(new StarterSlot(blue));
        setOrange(new StarterSlot(orange));
    }

    /** Construct a Match, given the Slots defining the Teams */
    public Match(Slot blue, Slot orange) {
        if (blue == null || orange == null) throw new IllegalArgumentException("A Match cannot have null as a Slot.");
        setBlue(blue);
        setOrange(orange);
    }

    /** Set the blue Team Slot of this Match. */
    public void setBlue(Slot blue) {
        if (blue == null) throw new IllegalArgumentException("A Match cannot have null as a Slot.");
        unlinkSlot(blueSlot);
        blueSlot = blue;
        linkSlot(blueSlot, this);
    }

    /** Set the orange Team Slot of this Match. */
    public void setOrange(Slot orange) {
        if (orange == null) throw new IllegalArgumentException("A Match cannot have null as a Slot.");
        unlinkSlot(orangeSlot);
        orangeSlot = orange;
        linkSlot(orangeSlot, this);
    }

    /** Adds the correct link between two Matches using the Slot from the parent Match */
    private static void linkSlot(Slot slot, Match parentMatch) {
        if (slot instanceof WinnerOf) {
            slot.getRequiredMatch().winnerDestination = parentMatch;
        } else if (slot instanceof LoserOf) {
            slot.getRequiredMatch().loserDestination = parentMatch;
        }
    }

    /** Removes the link between two Matches describe in Slot. The Slot should be discarded afterwards */
    private static void unlinkSlot(Slot slot) {
        if (slot instanceof WinnerOf) {
            slot.getRequiredMatch().winnerDestination = null;
        } else if (slot instanceof LoserOf) {
            slot.getRequiredMatch().loserDestination = null;
        }
    }

    /** Returns true when both Teams are known and ready, even if the Match has already been played. */
    public boolean isReadyToPlay() {
        return blueSlot.isReady() && orangeSlot.isReady();
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
        ArrayList<Match> list = new ArrayList<>();
        queue.add(this);

        // Matches are polled from the queue until it is empty
        while (!queue.isEmpty()) {
            Match match = queue.poll();
            list.add(match);

            // Enqueue child matches, if any
            // Orange is added first - this means the final order will be the reverse of the logical
            // order of playing matches
            Match blueReqMatch = blueSlot.getRequiredMatch();
            if (blueReqMatch != null) queue.add(blueReqMatch);
            Match orangeReqMatch = orangeSlot.getRequiredMatch();
            if (orangeReqMatch != null) queue.add(orangeReqMatch);
        }

        return list;
    }

    /** Returns a list of all Matches that must be finished before this Match is playable, including itself.
     * The Matches will be ordered after inorder depth-first search approach. */
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
            Match blueReqMatch = blueSlot.getRequiredMatch();
            if (blueReqMatch != null) stack.push(blueReqMatch);
            Match orangeReqMatch = orangeSlot.getRequiredMatch();
            if (orangeReqMatch != null) stack.push(orangeReqMatch);
        }

        return matches;
    }

    /** Returns true if the other Match must be concluded before this match is playable. */
    public boolean dependsOn(Match otherMatch) {
        if (this == otherMatch) return false;

        // If this match depends on the other match, this match must be a parent (or parent of a parent of a
        // parent ... ect) of the other match.
        // A queue contain all unchecked parent matches.
        LinkedList<Match> queue = new LinkedList<>();
        queue.push(otherMatch);

        // Matches are polled from the queue until it is empty.
        // Depending on bracket structure some matches might be checked multiple times.
        while (!queue.isEmpty()) {
            Match match = queue.poll();
            if (this == match)
                return true;

            // Enqueue parent matches, if any
            if (match.winnerDestination != null) queue.push(match.winnerDestination);
            if (match.loserDestination != null) queue.push(match.loserDestination);
        }

        return false;
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

    /** Returns the blue team or null if blue is unknown. */
    public Team getBlueTeam() {
        return blueSlot.getTeam();
    }

    /** Returns the orange team or null if orange is unknown. */
    public Team getOrangeTeam() {
        return orangeSlot.getTeam();
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
