package dk.aau.cs.ds306e18.tournament.model;

import java.util.*;

/** <p>A Match consists of two Teams and a result. A Team can be a winner or loser from another Match and thus
 * temporarily unknown. If team is a winner or loser of another Match use the {@code useWinnerFrom()} and
 * {@code useLoserFrom} methods.</p>
 * <p>The method {@code isReadyToPlay()} returns true, when both Teams are known and ready.</p>
 * <p>When the Match get marked as has been played, it is possible to retrieve
 * the winner and the loser of the match.</p> */
public class Match {

    private int blueScore = 0;
    private int orangeScore = 0;
    private boolean played = false;

    // teams are only stored, when they start in this match, and is not the winner/loser of a previous match
    private Team predefinedBlue, predefinedOrange;

    // Where do winner/loser go? (They can only go to one destination)
    private Match winnerDestination, loserDestination;

    // Where did blue and orange come from?
    private Match blueFrom, orangeFrom;
    private boolean blueIsPrevWinner, orangeIsPrevWinner;

    /** <p>A Match consists of two Teams and a result. A Team can be a winner or loser from another Match and thus
     * temporarily unknown. If team is a winner or loser of another Match use the {@code useWinnerFrom()} and
     * {@code useLoserFrom} methods.</p>
     * <p>The method {@code isReadyToPlay()} returns true, when both Teams are known and ready.</p>
     * <p>When the Match get marked as has been played, it is possible to retrieve
     * the winner and the loser of the match.</p> */
    public Match() { }

    /** <p>A Match consists of two Teams and a result. A Team can be a winner or loser from another Match and thus
     * temporarily unknown. If team is a winner or loser of another Match use the {@code useWinnerFrom()} and
     * {@code useLoserFrom} methods.</p>
     * <p>The method {@code isReadyToPlay()} returns true, when both Teams are known and ready.</p>
     * <p>When the Match get marked as has been played, it is possible to retrieve
     * the winner and the loser of the match.</p>
     * @param blue the blue Team. Can be null or set later.
     * @param orange the orange Team. Can be null or set later. */
    public Match(Team blue, Team orange) {
        predefinedBlue = blue;
        predefinedOrange = orange;
    }

    /** Set the blue Team of this Match. If the blue was previously marked as being the winner or loser of another
     * match that connection is automatically removed. */
    public void setBlue(Team blue) {
        predefinedBlue = blue;
        if (blueFrom != null) {
            if (blueIsPrevWinner) blueFrom.winnerDestination = null;
            else blueFrom.loserDestination = null;
            blueFrom = null;
        }
    }

    /** Set the orange Team of this Match. If the orange was previously marked as being the winner or loser of another
     * match that connection is automatically removed. */
    public void setOrange(Team orange) {
        predefinedOrange = orange;
        if (orangeFrom != null) {
            if (orangeIsPrevWinner) orangeFrom.winnerDestination = null;
            else orangeFrom.loserDestination = null;
            orangeFrom = null;
        }
    }

    /** Set a winner of another Match to participate in this Match.
     * @param match the Match where the winner is taken from.
     * @param winnerBecomesBlue if true, the winner will be blue in this Match, otherwise orange. */
    public void useWinnerFrom(Match match, boolean winnerBecomesBlue) {
        if (winnerBecomesBlue) {
            blueFrom = match;
            blueIsPrevWinner = true;
            predefinedBlue = null;
        } else {
            orangeFrom = match;
            orangeIsPrevWinner = true;
            predefinedOrange = null;
        }
        match.winnerDestination = this;
    }

    /** Set a loser of another Match to participate in this Match.
     * @param match the Match where the winner is taken from.
     * @param loserBecomesOrange if true, the loser will be orange in this Match, otherwise blue. */
    public void setLoserDestination(Match match, boolean loserBecomesOrange) {
        if (loserBecomesOrange) {
            orangeFrom = match;
            orangeIsPrevWinner = false;
            predefinedOrange = null;
        } else {
            blueFrom = match;
            blueIsPrevWinner = false;
            predefinedBlue = null;
        }
        match.loserDestination = this;
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
        ArrayList<Match> list = new ArrayList<>();
        queue.add(this);

        // Matches are polled from the queue until it is empty
        while (!queue.isEmpty()) {
            Match match = queue.poll();
            list.add(match);

            // Enqueue child matches, if any
            // Orange is added first - this means the final order will be the reverse of the logical
            // order of playing matches
            if (match.orangeFrom != null) queue.add(match.orangeFrom);
            if (match.blueFrom != null) queue.add(match.blueFrom);
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
            if (match.blueFrom != null) stack.push(match.blueFrom);
            if (match.orangeFrom != null) stack.push(match.orangeFrom);
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

    /** Returns the blue team or null if blue is unknown or unset. */
    public Team getBlueTeam() {
        if (predefinedBlue != null) return predefinedBlue;
        if (blueFrom == null) return null; // Blue is completely unknown if this is true
        if (!blueFrom.hasBeenPlayed()) return null;
        if (blueIsPrevWinner) return blueFrom.getWinner();
        else return blueFrom.getLoser();
    }

    /** Returns the orange team or null if orange is unknown or unset. */
    public Team getOrangeTeam() {
        if (predefinedOrange != null) return predefinedOrange;
        if (orangeFrom == null) return null;  // Orange is completely unknown if this is true
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
