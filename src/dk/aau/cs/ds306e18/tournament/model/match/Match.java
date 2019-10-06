package dk.aau.cs.ds306e18.tournament.model.match;

import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.*;
import java.util.function.Consumer;

/**
 * <p>A Match consists of two Slots, which holds the Teams participating in the Match, and each Team's
 * score. A Slot might contain a Team, which is temporarily unknown (e.g. when the
 * Team is the winner of another Match). The method {@code isReadyToPlay()} returns true, when both Slots'
 * Team is known and ready.</p>
 * <p>When the Match get marked as has been played, and it is possible to retrieve
 * the winner and the loser of the match.</p>
 */
public final class Match {

    public enum Status {
        NOT_PLAYABLE, READY_TO_BE_PLAYED, HAS_BEEN_PLAYED
    }

    public enum Outcome {
        UNKNOWN, TEAM_ONE_WINS, TEAM_TWO_WINS, DRAW
    }

    public enum OutcomeColored {
        UNKNOWN, BLUE_WINS, ORANGE_WINS, DRAW
    }

    private static int nextId = 0;

    private final int id;
    private int identifier = 0;
    private int teamOneScore = 0;
    private int teamTwoScore = 0;
    private boolean played = false;
    private Team teamOne, teamTwo;
    private boolean teamOneIsBlue = true;
    transient private Match teamOneFromMatch, teamTwoFromMatch;
    private boolean teamOneWasWinnerInPreviousMatch, teamTwoWasWinnerInPreviousMatch;
    transient private Match winnerDestination, loserDestination;
    private boolean winnerGoesToTeamOne, loserGoesToTeamOne;

    transient private List<MatchPlayedListener> playedListeners = new LinkedList<>();
    transient private List<MatchChangeListener> changeListeners = new LinkedList<>();

    /**
     * Construct an empty Match.
     */
    public Match() {
        id = nextId++;
    }

    /**
     * Construct a Match where both Teams are known from the start.
     */
    public Match(Team teamOne, Team teamTwo) {
        id = nextId++;
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    /** Set the identifier. The match is referenced to by that identifier. E.g. "Winner of 5", where 5 is the identifier. */
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
        notifyMatchChangeListeners();
    }

    /** Get the identifier. The match is referenced to by that identifier. E.g. "Winner of 5", where 5 is the identifier. */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * Set team one of this Match. Returns the Match itself, which allows chaining.
     */
    public Match setTeamOne(Team one) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (teamOneFromMatch != null) {
            // Remove connection to the fromMatch
            if (teamOneWasWinnerInPreviousMatch) setTeamOneToWinnerOf(null);
            else setTeamOneToLoserOf(null);
        }
        teamOne = one;
        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team two of this Match. Returns the Match itself, which allows chaining.
     */
    public Match setTeamTwo(Team two) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (teamTwoFromMatch != null) {
            // Remove connection to the fromMatch
            if (teamTwoWasWinnerInPreviousMatch) setTeamTwoToWinnerOf(null);
            else setTeamTwoToLoserOf(null);
        }
        teamTwo = two;
        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team one of this Match to use the winner of another Match. Any previous connection or definition of
     * team one will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setTeamOneToWinnerOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamOneFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamOneWasWinnerInPreviousMatch) teamOneFromMatch.winnerDestination = null;
            else teamOneFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            teamOneFromMatch = null;
            teamOne = null;

        } else {
            // Assumption: winners can only go to one match. So we check if the winner currently goes somewhere else
            // and removes that connection
            if (match.winnerDestination != null) {
                if (match.winnerGoesToTeamOne) match.winnerDestination.setTeamOneToWinnerOf(null);
                else match.winnerDestination.setTeamTwoToWinnerOf(null);
            }

            // Add new connection
            teamOneFromMatch = match;
            teamOneWasWinnerInPreviousMatch = true;
            match.winnerDestination = this;
            match.winnerGoesToTeamOne = true;
            if (match.hasBeenPlayed()) teamOne = match.getWinner();
            else teamOne = null;
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team one of this Match to use the loser of another Match. Any previous connection or definition of
     * the team one will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setTeamOneToLoserOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamOneFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamOneWasWinnerInPreviousMatch) teamOneFromMatch.winnerDestination = null;
            else teamOneFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            teamOneFromMatch = null;
            teamOne = null;

        } else {
            // Assumption: losers can only go to one match. So we check if the loser currently goes somewhere else
            // and removes that connection
            if (match.loserDestination != null) {
                if (match.loserGoesToTeamOne) match.loserDestination.setTeamOneToLoserOf(null);
                else match.loserDestination.setTeamTwoToLoserOf(null);
            }

            // Add new connection
            teamOneFromMatch = match;
            teamOneWasWinnerInPreviousMatch = false;
            match.loserDestination = this;
            match.loserGoesToTeamOne = true;
            if (match.hasBeenPlayed()) teamOne = match.getLoser();
            else teamOne = null;
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team two of this Match to use the winner of another Match. Any previous connection or definition of
     * team two will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setTeamTwoToWinnerOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamTwoFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamTwoWasWinnerInPreviousMatch) teamTwoFromMatch.winnerDestination = null;
            else teamTwoFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            teamTwoFromMatch = null;
            teamTwo = null;

        } else {
            // Assumption: winners can only go to one match. So we check if the winner currently goes somewhere else
            // and removes that connection
            if (match.winnerDestination != null) {
                if (match.winnerGoesToTeamOne) match.winnerDestination.setTeamOneToWinnerOf(null);
                else match.winnerDestination.setTeamTwoToWinnerOf(null);
            }

            // Add new connection
            teamTwoFromMatch = match;
            teamTwoWasWinnerInPreviousMatch = true;
            match.winnerDestination = this;
            match.winnerGoesToTeamOne = false;
            if (match.hasBeenPlayed()) teamTwo = match.getWinner();
            else teamTwo = null;
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team two of this Match to use the loser of another Match. Any previous connection or definition of
     * team two will be removed. Returns the Match itself, which allows chaining.
     */
    public Match setTeamTwoToLoserOf(Match match) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (match == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamTwoFromMatch != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamTwoWasWinnerInPreviousMatch) teamTwoFromMatch.winnerDestination = null;
            else teamTwoFromMatch.loserDestination = null;
        }

        if (match == null) {
            // Remove any connection
            teamTwoFromMatch = null;
            teamTwo = null;

        } else {
            // Assumption: losers can only go to one match. So we check if the loser currently goes somewhere else
            // and removes that connection
            if (match.loserDestination != null) {
                if (match.loserGoesToTeamOne) match.loserDestination.setTeamOneToLoserOf(null);
                else match.loserDestination.setTeamTwoToLoserOf(null);
            }

            // Add new connection
            teamTwoFromMatch = match;
            teamTwoWasWinnerInPreviousMatch = false;
            match.loserDestination = this;
            match.loserGoesToTeamOne = false;
            if (match.hasBeenPlayed()) teamTwo = match.getLoser();
            else teamTwo = null;
            match.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team one of this Match to use the winner of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamOneToWinnerOf(Match other) {
        teamOneFromMatch = other;
        teamOneWasWinnerInPreviousMatch = true;
        other.winnerDestination = this;
        other.winnerGoesToTeamOne = true;
    }

    /**
     * Set team one of this Match to use the loser of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamOneToLoserOf(Match other) {
        teamOneFromMatch = other;
        teamOneWasWinnerInPreviousMatch = false;
        other.loserDestination = this;
        other.loserGoesToTeamOne = true;
    }

    /**
     * Set team two of this Match to use the winner of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamTwoToWinnerOf(Match other) {
        teamTwoFromMatch = other;
        teamTwoWasWinnerInPreviousMatch = true;
        other.winnerDestination = this;
        other.winnerGoesToTeamOne = false;
    }

    /**
     * Set team two of this Match to use the loser of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamTwoToLoserOf(Match other) {
        teamTwoFromMatch = other;
        teamTwoWasWinnerInPreviousMatch = false;
        other.loserDestination = this;
        other.loserGoesToTeamOne = false;
    }

    /**
     * Returns true when both Teams are known and ready, even if the Match has already been played.
     */
    public boolean isReadyToPlay() {
        return teamOne != null && teamTwo != null;
    }

    public Team getWinner() {
        if (!played) throw new IllegalStateException("Match has not been played.");
        if (teamOneScore == teamTwoScore) throw new IllegalStateException("Match ended in draw.");
        if (teamOneScore > teamTwoScore)
            return getTeamOne();
        return getTeamTwo();
    }

    public Team getLoser() {
        if (!played) throw new IllegalStateException("Match has not been played.");
        if (teamOneScore == teamTwoScore) throw new IllegalStateException("Match ended in draw.");
        if (teamOneScore > teamTwoScore)
            return getTeamTwo();
        return getTeamOne();
    }

    /** Returns the status of the match; NOT_PLAYABLE, READY_TO_BE_PLAYED, or HAS_BEEN_PLAYED. See getOutcome for the outcome. */
    public Status getStatus() {
        if (!isReadyToPlay()) {
            return Status.NOT_PLAYABLE;
        } else if (!played) {
            return Status.READY_TO_BE_PLAYED;
        } else {
            return Status.HAS_BEEN_PLAYED;
        }
    }

    /** Returns the outcome of the match; UNKNOWN, TEAM_ONE_WINS, or TEAM_TWO_WINS. See getStatus for the status. */
    public Outcome getOutcome() {
        if (getStatus() != Status.HAS_BEEN_PLAYED) {
            return Outcome.UNKNOWN;
        } else if (teamOneScore == teamTwoScore) {
            // Should never happen. Rocket League cannot end in a draw under normal circumstance
            return Outcome.DRAW;
        } else if (teamOneScore > teamTwoScore) {
            return Outcome.TEAM_ONE_WINS;
        } else {
            return Outcome.TEAM_TWO_WINS;
        }
    }

    /** Returns the outcome of the match; UNKNOWN, BLUE_WINS, or ORANGE_WINS. See getStatus for the status. */
    public OutcomeColored getOutcomeColored() {
        if (getStatus() != Status.HAS_BEEN_PLAYED) {
            return OutcomeColored.UNKNOWN;
        } else if (teamOneScore == teamTwoScore) {
            // Should never happen. Rocket League cannot end in a draw under normal circumstance
            return OutcomeColored.DRAW;
        } else if (teamOneScore > teamTwoScore) {
            return teamOneIsBlue ? OutcomeColored.BLUE_WINS : OutcomeColored.ORANGE_WINS;
        } else {
            return teamOneIsBlue ? OutcomeColored.ORANGE_WINS : OutcomeColored.BLUE_WINS;
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
        Set<Match> marked = new HashSet<>();
        queue.add(this);
        marked.add(this);

        Consumer<Match> addFunction = (m) -> {
            if (m != null && !marked.contains(m)) {
                // We check if both parent matches are marked (or null) to be sure we create the correct order
                if ((m.winnerDestination == null || marked.contains(m.winnerDestination))
                        && (m.loserDestination == null || marked.contains(m.loserDestination))) {
                    queue.add(m);
                    marked.add(m);
                }
            }
        };

        // Matches are polled from the queue until it is empty
        while (!queue.isEmpty()) {
            Match match = queue.poll();
            list.add(match);

            // Enqueue child matches, if any
            // Team two is added first - this means the final order will be the reverse of the logical
            // order of playing matches
            addFunction.accept(match.teamTwoFromMatch);
            addFunction.accept(match.teamOneFromMatch);
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
            if (match.teamOneFromMatch != null) stack.push(match.teamOneFromMatch);
            if (match.teamTwoFromMatch != null) stack.push(match.teamTwoFromMatch);
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
        setScores(teamOneScore, teamTwoScore, hasBeenPlayed, false);
    }

    public void setTeamOneScore(int teamOneScore) {
        setScores(teamOneScore, teamTwoScore, played, false);
    }

    public void setTeamTwoScore(int teamTwoScore) {
        setScores(teamOneScore, teamTwoScore, played, false);
    }

    public void setScores(int teamOneScore, int teamTwoScore) {
        setScores(teamOneScore, teamTwoScore, played, false);
    }

    public void setScores(int teamOneScore, int teamTwoScore, boolean hasBeenPlayed) {
        setScores(teamOneScore, teamTwoScore, hasBeenPlayed, false);
    }

    public void setScores(int teamOneScore, int teamTwoScore, boolean hasBeenPlayed, boolean forceResetSubsequentMatches) {

        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");

        boolean outcomeChanging = willOutcomeChange(teamOneScore, teamTwoScore, hasBeenPlayed);

        if (outcomeChanging) {
            // Are there any subsequent matches that has been played?
            if ((winnerDestination != null && (winnerDestination.teamOneScore != 0 || winnerDestination.teamTwoScore != 0))
                    || (loserDestination != null && (loserDestination.teamOneScore != 0 || loserDestination.teamTwoScore != 0))) {

                // A subsequent match has scores that are not 0. We can only proceed with force
                if (forceResetSubsequentMatches) {
                    if (winnerDestination != null) winnerDestination.forceReset();
                    if (loserDestination != null) loserDestination.forceReset();
                } else {
                    throw new MatchResultDependencyException();
                }
            }
        }

        // Retract if there might be a new winner/loser
        if (played && outcomeChanging) {
            retractWinnerAndLoser();
        }

        // Apply changes
        played = hasBeenPlayed;
        this.teamOneScore = teamOneScore;
        this.teamTwoScore = teamTwoScore;

        // Transfer because there might be a new winner/loser
        if (played && outcomeChanging) {
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
    public boolean willOutcomeChange(int altTeamOneScore, int altTeamTwoScore, boolean altHasBeenPlayed) {
        if (this.played != altHasBeenPlayed) {
            return true;
        }

        // Was not played before, and is not played now. In this case we don't care about score. Nothing changes
        if (!altHasBeenPlayed) {
            return false;
        }

        // Check if both old and new score is draw
        if (teamOneScore == teamTwoScore && altTeamOneScore == altTeamTwoScore) {
            return false;
        }

        // Check if winner stays the same
        if ((teamOneScore > teamTwoScore) == (altTeamOneScore > altTeamTwoScore)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Let the following matches know, that the winner or loser now found.
     */
    private void transferWinnerAndLoser() {
        if (winnerDestination != null) {
            if (winnerDestination.isReadyToPlay()) winnerDestination.setScores(0, 0); // There can be leftover scores
            if (winnerGoesToTeamOne) winnerDestination.teamOne = getWinner();
            else winnerDestination.teamTwo = getWinner();
            winnerDestination.notifyMatchChangeListeners();
        }
        if (loserDestination != null) {
            if (loserDestination.isReadyToPlay()) loserDestination.setScores(0, 0); // There can be leftover scores
            if (loserGoesToTeamOne) loserDestination.teamOne = getLoser();
            else loserDestination.teamTwo = getLoser();
            loserDestination.notifyMatchChangeListeners();
        }
    }

    /**
     * Let the following matches know, that the winner or loser is no longer defined.
     */
    private void retractWinnerAndLoser() {
        if (winnerDestination != null) {
            if (winnerDestination.isReadyToPlay()) winnerDestination.setScores(0, 0);  // There can be leftover scores
            if (winnerGoesToTeamOne) winnerDestination.teamOne = null;
            else winnerDestination.teamTwo = null;
            winnerDestination.notifyMatchChangeListeners();
        }
        if (loserDestination != null) {
            if (loserDestination.isReadyToPlay()) loserDestination.setScores(0, 0);  // There can be leftover scores
            if (loserGoesToTeamOne) loserDestination.teamOne = null;
            else loserDestination.teamTwo = null;
            loserDestination.notifyMatchChangeListeners();
        }
    }

    /**
     * Returns true if the match is currently playable and both teams consists of one bot.
     */
    public boolean isOneVsOne() {
        return (isReadyToPlay() && teamOne.size() == 1 && teamTwo.size() == 1);
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public int getBlueScore() {
        return teamOneIsBlue ? getTeamOneScore() : getTeamTwoScore();
    }

    public int getOrangeScore() {
        return teamOneIsBlue ? getTeamTwoScore() : getTeamOneScore();
    }

    public boolean hasBeenPlayed() {
        return played;
    }

    public boolean isTeamOneBlue() {
        return teamOneIsBlue;
    }

    public void setTeamOneToBlue(boolean teamOneIsBlue) {
        this.teamOneIsBlue = teamOneIsBlue;
        notifyMatchChangeListeners();
    }

    /**
     * Returns team one or null if team one is unknown.
     */
    public Team getTeamOne() {
        return teamOne;
    }

    /**
     * Returns team two or null if team two is unknown.
     */
    public Team getTeamTwo() {
        return teamTwo;
    }

    /**
     * Returns blue team or null if blue team is unknown.
     */
    public Team getBlueTeam() {
        return teamOneIsBlue ? getTeamOne() : getTeamTwo();
    }

    /**
     * Returns orange team or null if orange team is unknown.
     */
    public Team getOrangeTeam() {
        return teamOneIsBlue ? getTeamTwo() : getTeamOne();
    }

    /** Returns team one's name. If team one is null, then "Winner/Loser of .." or "TBD" is returned. */
    public String getTeamOneAsString() {
        if (teamOne == null) {
            if (teamOneFromMatch != null) {
                return (teamOneWasWinnerInPreviousMatch ? "Winner of " : "Loser of ") + teamOneFromMatch.getIdentifier();
            }
            return "TBD";
        }
        return teamOne.getTeamName();
    }

    /** Returns team two's name. If team two is null, then "Winner/Loser of .." or "TBD" is returned. */
    public String getTeamTwoAsString() {
        if (teamTwo == null) {
            if (teamTwoFromMatch != null) {
                return (teamTwoWasWinnerInPreviousMatch ? "Winner of " : "Loser of ") + teamTwoFromMatch.getIdentifier();
            }
            return "TBD";
        }
        return teamTwo.getTeamName();
    }

    /** Returns the blue team's name. If blue team is null, then "Winner/Loser of .." or "TBD" is returned. */
    public String getBlueTeamAsString() {
        return teamOneIsBlue ? getTeamOneAsString() : getTeamTwoAsString();
    }

    /** Returns the orange team's name. If orange team is null, then "Winner/Loser of .." or "TBD" is returned. */
    public String getOrangeTeamAsString() {
        return teamOneIsBlue ? getTeamTwoAsString() : getTeamOneAsString();
    }

    public Match getTeamOneFromMatch() {
        return teamOneFromMatch;
    }

    public Match getTeamTwoFromMatch() {
        return teamTwoFromMatch;
    }

    public Match getWinnerDestination() {
        return winnerDestination;
    }

    public Match getLoserDestination() {
        return loserDestination;
    }

    public boolean wasTeamOneWinnerInPreviousMatch() {
        return teamOneWasWinnerInPreviousMatch;
    }

    public boolean wasTeamTwoWinnerInPreviousMatch() {
        return teamTwoWasWinnerInPreviousMatch;
    }

    public boolean doesWinnerGoToTeamOne() {
        return winnerGoesToTeamOne;
    }

    public boolean doesLoserGoToTeamOne() {
        return loserGoesToTeamOne;
    }

    /** Listeners registered here will be notified when the match is played or reset. */
    public void registerMatchPlayedListener(MatchPlayedListener listener) {
        playedListeners.add(listener);
    }

    public void unregisterMatchPlayedListener(MatchPlayedListener listener) {
        playedListeners.remove(listener);
    }

    protected void notifyMatchPlayedListeners() {
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

    protected void notifyMatchChangeListeners() {
        for (MatchChangeListener listener : changeListeners) {
            listener.onMatchChanged(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id == match.id &&
                getTeamOneScore() == match.getTeamOneScore() &&
                getTeamTwoScore() == match.getTeamTwoScore() &&
                played == match.played &&
                teamOneIsBlue == match.teamOneIsBlue &&
                teamOneWasWinnerInPreviousMatch == match.teamOneWasWinnerInPreviousMatch &&
                teamTwoWasWinnerInPreviousMatch == match.teamTwoWasWinnerInPreviousMatch &&
                winnerGoesToTeamOne == match.winnerGoesToTeamOne &&
                loserGoesToTeamOne == match.loserGoesToTeamOne &&
                Objects.equals(getTeamOne(), match.getTeamOne()) &&
                Objects.equals(getTeamTwo(), match.getTeamTwo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getTeamOneScore(), getTeamTwoScore(), played, teamOneIsBlue, getTeamOne(), getTeamTwo(), teamOneWasWinnerInPreviousMatch, teamTwoWasWinnerInPreviousMatch, winnerGoesToTeamOne, loserGoesToTeamOne);
    }

    @Override
    public String toString() {
        return "Match:{" + getTeamOneAsString() + " (" + teamOneScore + ") vs ("
                + teamTwoScore + ") " + getTeamTwoAsString() + ", status: " + getStatus() + "}";
    }
}
