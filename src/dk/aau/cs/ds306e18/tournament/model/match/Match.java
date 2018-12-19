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
    transient private Match blueFromMatch, orangeFromMatch;
    private boolean blueWasWinnerInPreviousMatch, orangeWasWinnerInPreviousMatch;
    transient private Match winnerDestination, loserDestination;
    private boolean winnerGoesToBlue, loserGoesToBlue;

    transient private List<MatchPlayedListener> playedListeners = new LinkedList<>();
    transient private List<MatchChangeListener> changeListeners = new LinkedList<>();

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
        notifyMatchChangeListeners();
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
        notifyMatchChangeListeners();
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
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
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
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
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
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
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
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
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
        if (blueScore == orangeScore) throw new IllegalStateException("Match ended in draw.");
        if (blueScore > orangeScore)
            return getBlueTeam();
        return getOrangeTeam();
    }

    public Team getLoser() {
        if (!played) throw new IllegalStateException("Match has not been played.");
        if (blueScore == orangeScore) throw new IllegalStateException("Match ended in draw.");
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

    public void setHasBeenPlayed(boolean hasBeenPlayed) {
        setScores(blueScore, orangeScore, hasBeenPlayed, false);
    }

    public void setBlueScore(int blueScore) {
        setScores(blueScore, orangeScore, played, false);
    }

    public void setOrangeScore(int orangeScore) {
        setScores(blueScore, orangeScore, played, false);
    }

    public void setScores(int blueScore, int orangeScore) {
        setScores(blueScore, orangeScore, played, false);
    }

    public void setScores(int blueScore, int orangeScore, boolean hasBeenPlayed) {
        setScores(blueScore, orangeScore, hasBeenPlayed, false);
    }

    public void setScores(int blueScore, int orangeScore, boolean hasBeenPlayed, boolean forceResetSubsequentMatches) {

        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");

        if (willOutcomeChange(blueScore, orangeScore, hasBeenPlayed)) {
            // Are there any subsequent matches that has been played?
            if ((winnerDestination != null && winnerDestination.hasBeenPlayed())
                    || (loserDestination != null && loserDestination.hasBeenPlayed())) {
                // A subsequent match has been played. Should it be reset?
                if (forceResetSubsequentMatches) {
                    if (winnerDestination != null) winnerDestination.forceReset();
                    if (loserDestination != null) loserDestination.forceReset();
                } else {
                    throw new MatchResultDependencyException();
                }


            }
        }

        if (played) {
            retractWinnerAndLoser();
        }

        // Apply changes
        played = hasBeenPlayed;
        _setBlueScore(blueScore);
        _setOrangeScore(orangeScore);

        if (played) {
            transferWinnerAndLoser();
        }

        notifyMatchChangeListeners();
        notifyMatchPlayedListeners();
    }

    /** Changes a match's result to be 0-0 and not played. If any matches depends on this match, those will also be reset. */
    public void forceReset() {
        setScores(0, 0, false, true);
    }

    /** Returns true if the outcome and thus the state of this match change, if it had the given score instead. */
    public boolean willOutcomeChange(int altBlueScore, int altOrangeScore, boolean altHasBeenPlayed) {
        if (this.played != altHasBeenPlayed) {
            return true;
        }

        // Was not played before, and is not played now. In this case we don't care about score. Nothing changes
        if (!altHasBeenPlayed) {
            return false;
        }

        // Check if both old and new score is draw
        if (blueScore == orangeScore && altBlueScore == altOrangeScore) {
            return false;
        }

        // Check if winner stays the same
        if ((blueScore > orangeScore) == (altBlueScore > altOrangeScore)) {
            return false;
        } else {
            return true;
        }
    }

    private void _setBlueScore(int blueScore) {

        //Update both teams goalsScored and goalsConceded
        blueTeam.addGoalsScored(-this.blueScore);
        blueTeam.addGoalsScored(blueScore);
        orangeTeam.addGoalsConceded(-this.blueScore);
        orangeTeam.addGoalsConceded(blueScore);

        this.blueScore = blueScore;
    }

    public void _setOrangeScore(int orangeScore) {

        //Update both teams goalsScored and goalsConceded
        orangeTeam.addGoalsScored(-this.orangeScore);
        orangeTeam.addGoalsScored(orangeScore);
        blueTeam.addGoalsConceded(-this.orangeScore);
        blueTeam.addGoalsConceded(orangeScore);

        this.orangeScore = orangeScore;
    }

    /**
     * Let the following matches know, that the winner or loser now found.
     */
    private void transferWinnerAndLoser() {
        if (getStatus() != MatchStatus.DRAW) {
            if (winnerDestination != null) {
                if (winnerDestination.isReadyToPlay()) winnerDestination.setScores(0, 0); // There can be leftover scores
                if (winnerGoesToBlue) winnerDestination.blueTeam = getWinner();
                else winnerDestination.orangeTeam = getWinner();
                winnerDestination.notifyMatchChangeListeners();
            }
            if (loserDestination != null) {
                if (loserDestination.isReadyToPlay()) loserDestination.setScores(0, 0); // There can be leftover scores
                if (loserGoesToBlue) loserDestination.blueTeam = getLoser();
                else loserDestination.orangeTeam = getLoser();
                loserDestination.notifyMatchChangeListeners();
            }
        }
    }

    /**
     * Let the following matches know, that the winner or loser is no longer defined.
     */
    private void retractWinnerAndLoser() {
        if (winnerDestination != null) {
            if (winnerDestination.isReadyToPlay()) winnerDestination.setScores(0, 0);  // There can be leftover scores
            if (winnerGoesToBlue) winnerDestination.blueTeam = null;
            else winnerDestination.orangeTeam = null;
            winnerDestination.notifyMatchChangeListeners();
        }
        if (loserDestination != null) {
            if (loserDestination.isReadyToPlay()) loserDestination.setScores(0, 0);  // There can be leftover scores
            if (loserGoesToBlue) loserDestination.blueTeam = null;
            else loserDestination.orangeTeam = null;
            loserDestination.notifyMatchChangeListeners();
        }
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getOrangeScore() {
        return orangeScore;
    }

    public boolean hasBeenPlayed() {
        return played;
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

    public Match getBlueFromMatch() {
        return blueFromMatch;
    }

    public Match getOrangeFromMatch() {
        return orangeFromMatch;
    }

    public Match getWinnerDestination() {
        return winnerDestination;
    }

    /** Listeners registered here will be notified when the match is played or reset. */
    public void registerMatchPlayedListener(MatchPlayedListener listener) {
        playedListeners.add(listener);
    }

    public void unregisterMatchPlayedListener(MatchPlayedListener listener) {
        playedListeners.remove(listener);
    }

    public void notifyMatchPlayedListeners() {
        for (MatchPlayedListener listener : playedListeners) {
            listener.onMatchPlayed(this);
        }
    }

    /** Listeners registered here will be notified when any value in the match changes. */
    public void registerMatchChangeListener(MatchChangeListener listener) {
        changeListeners.add(listener);
    }

    public void unregisterMatchChangeListener(MatchChangeListener listener) {
        changeListeners.remove(listener);
    }

    public void notifyMatchChangeListeners() {
        for (MatchChangeListener listener : changeListeners) {
            listener.onMatchChanged(this);
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

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("UID: ").append(hashCode()).append(", ").append("Status: ").append(getStatus().toString()).append("\n");

        if(blueTeam != null)
            sb.append("B ").append(getBlueTeam().getTeamName()).append(", Score: ").append(getBlueScore()).append("\n");
        else {
            sb.append("B ").append("TBD").append("\n");
        }
        if(orangeTeam != null)
            sb.append("O ").append(getOrangeTeam().getTeamName()).append(", Score: ").append(getOrangeScore());
        else
            sb.append("O ").append("TBD").append("\n");

        return sb.toString();
    }
}
