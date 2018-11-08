package dk.aau.cs.ds306e18.tournament.model.match;

import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <p>A Match consists of two Slots, which holds the Teams participating in the Match, and each Team's
 * score. A Slot might contain a Team, which is temporarily unknown (e.g. when the
 * Team is the winner of another Match). The method {@code isReadyToPlay()} returns true, when both Slots'
 * Team is known and ready.</p>
 * <p>When the Match get marked as has been played, and it is possible to retrieve
 * the winner and the loser of the match.</p>
 */
public final class Match {
    private int blueScore = 0;
    private int orangeScore = 0;
    private boolean played = false;
    private Team blueTeam, orangeTeam;
    private Match blueFromMatch, orangeFromMatch;
    private boolean blueWasWinnerInPreviousMatch, orangeWasWinnerInPreviousMatch;
    transient private Match winnerDestination, loserDestination;
    private boolean winnerGoesToBlue, loserGoesToBlue;

    transient private List<MatchListener> listeners = new LinkedList<>();

    /**
     * Construct an empty Match.
     */
    public Match() {
    }

    /**
     * Construct a Match where both Teams are known from the start.
     */
    public Match(Team blue, Team orange) {
        blueTeam = blue;
        orangeTeam = orange;
    }

    /**
     * Set the blue Team of this Match. Returns the Match itself, which allows chaining.
     */
    public Match setBlue(Team blue) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (blueFromMatch != null) {
            // Remove connection to the fromMatch
            if (blueWasWinnerInPreviousMatch) setBlueToWinnerOf(null);
            else setBlueToLoserOf(null);
        }
        blueTeam = blue;
        return this;
    }

    /**
     * Set the orange Team of this Match. Returns the Match itself, which allows chaining.
     */
    public Match setOrange(Team orange) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (orangeFromMatch != null) {
            // Remove connection to the fromMatch
            if (orangeWasWinnerInPreviousMatch) setOrangeToWinnerOf(null);
            else setOrangeToLoserOf(null);
        }
        orangeTeam = orange;
        return this;
    }

    /**
     * Set the blue Team of this Match to use the winner of another Match. Any previous connection or definition of
     * the blue Team will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setBlueToWinnerOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (blueFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (blueWasWinnerInPreviousMatch) blueFromMatch.winnerDestination = null;
            else blueFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            blueFromMatch = null;
            blueTeam = null;

        } else {
            // Assumption: winners can only go to one match. So we check if the winner currently goes somewhere else
            // and removes that connection
            if (match.winnerDestination != null) {
                if (match.winnerGoesToBlue) match.winnerDestination.setBlueToWinnerOf(null);
                else match.winnerDestination.setOrangeToWinnerOf(null);
            }

            // Add new connection
            blueFromMatch = match;
            blueWasWinnerInPreviousMatch = true;
            match.winnerDestination = this;
            match.winnerGoesToBlue = true;
            if (match.hasBeenPlayed()) blueTeam = match.getWinner();
            else blueTeam = null;
        }
        return this;
    }

    /**
     * Set the blue Team of this Match to use the loser of another Match. Any previous connection or definition of
     * the blue Team will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setBlueToLoserOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (blueFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (blueWasWinnerInPreviousMatch) blueFromMatch.winnerDestination = null;
            else blueFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            blueFromMatch = null;
            blueTeam = null;

        } else {
            // Assumption: losers can only go to one match. So we check if the loser currently goes somewhere else
            // and removes that connection
            if (match.loserDestination != null) {
                if (match.loserGoesToBlue) match.loserDestination.setBlueToLoserOf(null);
                else match.loserDestination.setOrangeToLoserOf(null);
            }

            // Add new connection
            blueFromMatch = match;
            blueWasWinnerInPreviousMatch = false;
            match.loserDestination = this;
            match.loserGoesToBlue = true;
            if (match.hasBeenPlayed()) blueTeam = match.getLoser();
            else blueTeam = null;
        }
        return this;
    }

    /**
     * Set the orange Team of this Match to use the winner of another Match. Any previous connection or definition of
     * the orange Team will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setOrangeToWinnerOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (orangeFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (orangeWasWinnerInPreviousMatch) orangeFromMatch.winnerDestination = null;
            else orangeFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            orangeFromMatch = null;
            orangeTeam = null;

        } else {
            // Assumption: winners can only go to one match. So we check if the winner currently goes somewhere else
            // and removes that connection
            if (match.winnerDestination != null) {
                if (match.winnerGoesToBlue) match.winnerDestination.setBlueToWinnerOf(null);
                else match.winnerDestination.setOrangeToWinnerOf(null);
            }

            // Add new connection
            orangeFromMatch = match;
            orangeWasWinnerInPreviousMatch = true;
            match.winnerDestination = this;
            match.winnerGoesToBlue = false;
            if (match.hasBeenPlayed()) orangeTeam = match.getWinner();
            else orangeTeam = null;
        }
        return this;
    }

    /**
     * Set the orange Team of this Match to use the loser of another Match. Any previous connection or definition of
     * the orange Team will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setOrangeToLoserOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (orangeFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (orangeWasWinnerInPreviousMatch) orangeFromMatch.winnerDestination = null;
            else orangeFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            orangeFromMatch = null;
            orangeTeam = null;

        } else {
            // Assumption: losers can only go to one match. So we check if the loser currently goes somewhere else
            // and removes that connection
            if (match.loserDestination != null) {
                if (match.loserGoesToBlue) match.loserDestination.setBlueToLoserOf(null);
                else match.loserDestination.setOrangeToLoserOf(null);
            }

            // Add new connection
            orangeFromMatch = match;
            orangeWasWinnerInPreviousMatch = false;
            match.loserDestination = this;
            match.loserGoesToBlue = false;
            if (match.hasBeenPlayed()) orangeTeam = match.getLoser();
            else orangeTeam = null;
        }
        return this;
    }

    /**
     * Returns true when both Teams are known and ready, even if the Match has already been played.
     */
    public boolean isReadyToPlay() {
        return blueTeam != null && orangeTeam != null;
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

    /**
     * Returns a list of all Matches that must be finished before this Match is playable, including itself.
     * The matches will be ordered after breadth-first search approach. If the order is reversed, the order will be
     * the logical order of playing the Matches, with the root as the last Match.
     */
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
            if (match.orangeFromMatch != null) queue.add(match.orangeFromMatch);
            if (match.blueFromMatch != null) queue.add(match.blueFromMatch);
        }

        return list;
    }

    /**
     * Returns a list of all Matches that must be finished before this Match is playable, including itself.
     * The Matches will be ordered after pre-order depth-first search approach.
     */
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
            if (match.blueFromMatch != null) stack.push(match.blueFromMatch);
            if (match.orangeFromMatch != null) stack.push(match.orangeFromMatch);
        }

        return matches;
    }

    /**
     * Returns true if the other Match must be concluded before this match is playable.
     */
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

    public int getBlueScore() {
        return blueScore;
    }

    public void setBlueScore(int blueScore) {
        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");

        //Update both teams goalsScored and goalsConceded
        blueTeam.addGoalsScored(-this.blueScore);
        blueTeam.addGoalsScored(blueScore);
        orangeTeam.addGoalsConceded(-this.blueScore);
        orangeTeam.addGoalsConceded(blueScore);

        this.blueScore = blueScore;
    }

    public int getOrangeScore() {
        return orangeScore;
    }

    public void setOrangeScore(int orangeScore) {
        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");

        //Update both teams goalsScored and goalsConceded
        orangeTeam.addGoalsScored(-this.orangeScore);
        orangeTeam.addGoalsScored(orangeScore);
        blueTeam.addGoalsConceded(-this.orangeScore);
        blueTeam.addGoalsConceded(orangeScore);

        this.orangeScore = orangeScore;
    }

    public void setScores(int blueScore, int orangeScore) {
        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");
        setBlueScore(blueScore);
        setOrangeScore(orangeScore);
    }

    public void setScores(int blueScore, int orangeScore, boolean hasBeenPlayed) {
        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");
        setScores(blueScore, orangeScore);
        setHasBeenPlayed(hasBeenPlayed);
    }

    public boolean hasBeenPlayed() {
        return played;
    }

    public void setHasBeenPlayed(boolean played) {
        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");
        if (this.played != played) {
            if (played) {
                this.played = true;
                transferWinnerAndLoser();
                notifyListeners();
            } else {
                if ((winnerDestination != null && winnerDestination.hasBeenPlayed()) || (loserDestination != null && loserDestination.hasBeenPlayed()))
                    // TODO Does not check if a following stage has started
                    throw new IllegalStateException("A following match has already been played.");
                this.played = false;
                retractWinnerAndLoser();
                notifyListeners();
            }
        }
    }

    /**
     * Let the following matches know, that the winner or loser now found.
     * Helper method to {@code setHasBeenPlayed(boolean)}
     */
    private void transferWinnerAndLoser() {
        if (winnerDestination != null) {
            if (winnerGoesToBlue) winnerDestination.blueTeam = getWinner();
            else winnerDestination.orangeTeam = getWinner();
        }
        if (loserDestination != null) {
            if (loserGoesToBlue) loserDestination.blueTeam = getLoser();
            else loserDestination.orangeTeam = getLoser();
        }
    }

    /**
     * Let the following matches know, that the winner or loser is no longer defined.
     * Helper method to {@code setHasBeenPlayed(boolean)}
     */
    private void retractWinnerAndLoser() {
        if (winnerDestination != null) {
            if (winnerGoesToBlue) winnerDestination.blueTeam = null;
            else winnerDestination.orangeTeam = null;
        }
        if (loserDestination != null) {
            if (loserGoesToBlue) loserDestination.blueTeam = null;
            else loserDestination.orangeTeam = null;
        }
    }

    /**
     * Sets both team scores to 0 and marks match as not played yet.
     */
    public void reset() {
        setScores(0, 0, false);
    }

    /**
     * Returns the blue team or null if blue is unknown.
     */
    public Team getBlueTeam() {
        return blueTeam;
    }

    /**
     * Returns the orange team or null if orange is unknown.
     */
    public Team getOrangeTeam() {
        return orangeTeam;
    }

    public void registerListener(MatchListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(MatchListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (MatchListener listener : listeners) {
            listener.onMatchPlayed(this);
        }
    }

    /**
     * Repairs references to parents in Match-tree for deserialized objects
     */
    public void postDeserializationRepair() {
        if (this.blueFromMatch != null) {
            if (this.blueWasWinnerInPreviousMatch) {
                this.blueFromMatch.winnerDestination = this;
                this.blueFromMatch.winnerGoesToBlue = true;
            } else {
                this.blueFromMatch.loserDestination = this;
                this.blueFromMatch.loserGoesToBlue = true;
            }
            blueFromMatch.postDeserializationRepair();
        }

        if (this.orangeFromMatch != null) {
            if (this.orangeWasWinnerInPreviousMatch) {
                this.orangeFromMatch.winnerDestination = this;
                this.orangeFromMatch.winnerGoesToBlue = false;
            } else {
                this.orangeFromMatch.loserDestination = this;
                this.orangeFromMatch.winnerGoesToBlue = false;
            }
            orangeFromMatch.postDeserializationRepair();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return getBlueScore() == match.getBlueScore() &&
                getOrangeScore() == match.getOrangeScore() &&
                played == match.played &&
                blueWasWinnerInPreviousMatch == match.blueWasWinnerInPreviousMatch &&
                orangeWasWinnerInPreviousMatch == match.orangeWasWinnerInPreviousMatch &&
                winnerGoesToBlue == match.winnerGoesToBlue &&
                loserGoesToBlue == match.loserGoesToBlue &&
                Objects.equals(getBlueTeam(), match.getBlueTeam()) &&
                Objects.equals(getOrangeTeam(), match.getOrangeTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBlueScore(), getOrangeScore(), played, getBlueTeam(), getOrangeTeam(), blueWasWinnerInPreviousMatch, orangeWasWinnerInPreviousMatch, winnerGoesToBlue, loserGoesToBlue);
    }
}
