package dk.aau.cs.ds306e18.tournament.model.match;

import com.google.common.primitives.Ints;
import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.*;
import java.util.function.Consumer;

/**
 * <p>A Series of matches between two teams. Teams might be temporarily unknown as they are winner or loser of
 * other Series. The method {@code isReadyToPlay()} returns true, when both teams are is known and ready.</p>
 * <p>When the Series get marked as has been played, and it is possible to retrieve
 * the winner and the loser.</p>
 */
public final class Series {

    public enum Status {
        NOT_PLAYABLE, READY_TO_BE_PLAYED, HAS_BEEN_PLAYED
    }

    public enum Outcome {
        UNKNOWN, TEAM_ONE_WINS, TEAM_TWO_WINS, DRAW
    }

    private static int nextId = 0;

    private final int id;
    private int identifier = 0;
    /** For longer series, e.g. best of 3 */
    private int length = 1;
    private List<Integer> teamOneScores = new ArrayList<>(Arrays.asList(0));
    private List<Integer> teamTwoScores = new ArrayList<>(Arrays.asList(0));
    private boolean played = false;
    private Team teamOne, teamTwo;
    private boolean teamOneIsBlue = true;
    transient private Series teamOneFromSeries, teamTwoFromSeries;
    private boolean teamOneWasWinnerInPreviousMatch, teamTwoWasWinnerInPreviousMatch;
    transient private Series winnerDestination, loserDestination;
    private boolean winnerGoesToTeamOne, loserGoesToTeamOne;

    transient private List<MatchPlayedListener> playedListeners = new LinkedList<>();
    transient private List<MatchChangeListener> changeListeners = new LinkedList<>();

    /**
     * Construct an empty Match.
     */
    public Series() {
        id = nextId++;
    }

    /**
     * Construct a Match where both Teams are known from the start.
     */
    public Series(Team teamOne, Team teamTwo) {
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
    public Series setTeamOne(Team one) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (teamOneFromSeries != null) {
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
    public Series setTeamTwo(Team two) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (teamTwoFromSeries != null) {
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
    public Series setTeamOneToWinnerOf(Series series) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (series == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamOneFromSeries != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamOneWasWinnerInPreviousMatch) teamOneFromSeries.winnerDestination = null;
            else teamOneFromSeries.loserDestination = null;
        }

        if (series == null) {
            // Remove any connection
            teamOneFromSeries = null;
            teamOne = null;

        } else {
            // Assumption: winners can only go to one match. So we check if the winner currently goes somewhere else
            // and removes that connection
            if (series.winnerDestination != null) {
                if (series.winnerGoesToTeamOne) series.winnerDestination.setTeamOneToWinnerOf(null);
                else series.winnerDestination.setTeamTwoToWinnerOf(null);
            }

            // Add new connection
            teamOneFromSeries = series;
            teamOneWasWinnerInPreviousMatch = true;
            series.winnerDestination = this;
            series.winnerGoesToTeamOne = true;
            if (series.hasBeenPlayed()) teamOne = series.getWinner();
            else teamOne = null;
            series.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team one of this Match to use the loser of another Match. Any previous connection or definition of
     * the team one will be removed. Returns the Match itself, which allows chaining.
     */
    public Series setTeamOneToLoserOf(Series series) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (series == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamOneFromSeries != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamOneWasWinnerInPreviousMatch) teamOneFromSeries.winnerDestination = null;
            else teamOneFromSeries.loserDestination = null;
        }

        if (series == null) {
            // Remove any connection
            teamOneFromSeries = null;
            teamOne = null;

        } else {
            // Assumption: losers can only go to one match. So we check if the loser currently goes somewhere else
            // and removes that connection
            if (series.loserDestination != null) {
                if (series.loserGoesToTeamOne) series.loserDestination.setTeamOneToLoserOf(null);
                else series.loserDestination.setTeamTwoToLoserOf(null);
            }

            // Add new connection
            teamOneFromSeries = series;
            teamOneWasWinnerInPreviousMatch = false;
            series.loserDestination = this;
            series.loserGoesToTeamOne = true;
            if (series.hasBeenPlayed()) teamOne = series.getLoser();
            else teamOne = null;
            series.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team two of this Match to use the winner of another Match. Any previous connection or definition of
     * team two will be removed. Returns the Match itself, which allows chaining.
     */
    public Series setTeamTwoToWinnerOf(Series series) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (series == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamTwoFromSeries != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamTwoWasWinnerInPreviousMatch) teamTwoFromSeries.winnerDestination = null;
            else teamTwoFromSeries.loserDestination = null;
        }

        if (series == null) {
            // Remove any connection
            teamTwoFromSeries = null;
            teamTwo = null;

        } else {
            // Assumption: winners can only go to one match. So we check if the winner currently goes somewhere else
            // and removes that connection
            if (series.winnerDestination != null) {
                if (series.winnerGoesToTeamOne) series.winnerDestination.setTeamOneToWinnerOf(null);
                else series.winnerDestination.setTeamTwoToWinnerOf(null);
            }

            // Add new connection
            teamTwoFromSeries = series;
            teamTwoWasWinnerInPreviousMatch = true;
            series.winnerDestination = this;
            series.winnerGoesToTeamOne = false;
            if (series.hasBeenPlayed()) teamTwo = series.getWinner();
            else teamTwo = null;
            series.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team two of this Match to use the loser of another Match. Any previous connection or definition of
     * team two will be removed. Returns the Match itself, which allows chaining.
     */
    public Series setTeamTwoToLoserOf(Series series) {
        if (played) throw new IllegalStateException("Match has already been played.");
        if (series == this) throw new IllegalArgumentException("A match can not have a connection to itself.");

        if (teamTwoFromSeries != null) {
            // This match no longer wants anything from the previous fromMatch
            if (teamTwoWasWinnerInPreviousMatch) teamTwoFromSeries.winnerDestination = null;
            else teamTwoFromSeries.loserDestination = null;
        }

        if (series == null) {
            // Remove any connection
            teamTwoFromSeries = null;
            teamTwo = null;

        } else {
            // Assumption: losers can only go to one match. So we check if the loser currently goes somewhere else
            // and removes that connection
            if (series.loserDestination != null) {
                if (series.loserGoesToTeamOne) series.loserDestination.setTeamOneToLoserOf(null);
                else series.loserDestination.setTeamTwoToLoserOf(null);
            }

            // Add new connection
            teamTwoFromSeries = series;
            teamTwoWasWinnerInPreviousMatch = false;
            series.loserDestination = this;
            series.loserGoesToTeamOne = false;
            if (series.hasBeenPlayed()) teamTwo = series.getLoser();
            else teamTwo = null;
            series.notifyMatchChangeListeners();
        }

        notifyMatchChangeListeners();
        return this;
    }

    /**
     * Set team one of this Match to use the winner of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamOneToWinnerOf(Series other) {
        teamOneFromSeries = other;
        teamOneWasWinnerInPreviousMatch = true;
        other.winnerDestination = this;
        other.winnerGoesToTeamOne = true;
    }

    /**
     * Set team one of this Match to use the loser of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamOneToLoserOf(Series other) {
        teamOneFromSeries = other;
        teamOneWasWinnerInPreviousMatch = false;
        other.loserDestination = this;
        other.loserGoesToTeamOne = true;
    }

    /**
     * Set team two of this Match to use the winner of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamTwoToWinnerOf(Series other) {
        teamTwoFromSeries = other;
        teamTwoWasWinnerInPreviousMatch = true;
        other.winnerDestination = this;
        other.winnerGoesToTeamOne = false;
    }

    /**
     * Set team two of this Match to use the loser of another Match. This ignores any previous connections
     * and states. Use with caution. TODO: Serialize connections in a way that does not require a call to this function.
     */
    public void reconnectTeamTwoToLoserOf(Series other) {
        teamTwoFromSeries = other;
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
        switch (getOutcome()) {
            case UNKNOWN: throw new IllegalStateException("Match has not been played.");
            case DRAW: throw new IllegalStateException("Match ended in draw.");
            case TEAM_ONE_WINS: return getTeamTwo();
            case TEAM_TWO_WINS: return getTeamOne();
        }
        return null;
    }

    public Team getLoser() {
        switch (getOutcome()) {
            case UNKNOWN: throw new IllegalStateException("Match has not been played.");
            case DRAW: throw new IllegalStateException("Match ended in draw.");
            case TEAM_ONE_WINS: return getTeamOne();
            case TEAM_TWO_WINS: return getTeamTwo();
        }
        return null;
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
        if (getStatus() != Status.HAS_BEEN_PLAYED) return Outcome.UNKNOWN;
        else return winnerIfScores(teamOneScores, teamTwoScores);
    }

    /**
     * Returns a list of all Matches that must be finished before this Match is playable, including itself.
     * The matches will be ordered after breadth-first search approach. If the order is reversed, the order will be
     * the logical order of playing the Matches, with the root as the last Match.
     */
    public ArrayList<Series> getTreeAsListBFS() {
        // Breadth-first search can be performed using a queue
        LinkedList<Series> queue = new LinkedList<>();
        ArrayList<Series> list = new ArrayList<>();
        Set<Series> marked = new HashSet<>();
        queue.add(this);
        marked.add(this);

        Consumer<Series> addFunction = (m) -> {
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
            Series series = queue.poll();
            list.add(series);

            // Enqueue child matches, if any
            // Team two is added first - this means the final order will be the reverse of the logical
            // order of playing matches
            addFunction.accept(series.teamTwoFromSeries);
            addFunction.accept(series.teamOneFromSeries);
        }

        return list;
    }

    /**
     * Returns a list of all Matches that must be finished before this Match is playable, including itself.
     * The Matches will be ordered after pre-order depth-first search approach.
     */
    public ArrayList<Series> getTreeAsListDFS() {
        // Depth-first search can be performed using a stack
        LinkedList<Series> stack = new LinkedList<>();
        ArrayList<Series> series = new ArrayList<>();
        stack.push(this);

        // Matches are popped from the stack until it is empty
        while (!stack.isEmpty()) {
            Series serie = stack.pop();
            series.add(serie);

            // Push child matches, if any
            if (serie.teamOneFromSeries != null) stack.push(serie.teamOneFromSeries);
            if (serie.teamTwoFromSeries != null) stack.push(serie.teamTwoFromSeries);
        }

        return series;
    }

    /**
     * Returns true if the other Match must be concluded before this match is playable.
     */
    public boolean dependsOn(Series otherSeries) {
        if (this == otherSeries) return false;

        // If this match depends on the other match, this match must be a parent (or parent of a parent of a
        // parent ... ect) of the other match.
        // A queue contain all unchecked parent matches.
        LinkedList<Series> queue = new LinkedList<>();
        queue.push(otherSeries);

        // Matches are polled from the queue until it is empty.
        // Depending on bracket structure some matches might be checked multiple times.
        while (!queue.isEmpty()) {
            Series series = queue.poll();
            if (this == series)
                return true;

            // Enqueue parent matches, if any
            if (series.winnerDestination != null) queue.push(series.winnerDestination);
            if (series.loserDestination != null) queue.push(series.loserDestination);
        }

        return false;
    }

    /**
     * Change the Series length. Any current scores are kept. The Series length must be a positive odd number.
     * This can change the the outcome of the match so a force reset of subsequent series might be needed.
     */
    public void setSeriesLength(int length, boolean forceResetSubsequentSeries) {
        if (this.length > length) {
            // Series has been shortened
            setScores(length, teamOneScores.subList(0, length), teamTwoScores.subList(0, length), played, forceResetSubsequentSeries);
        } else {
            // Series has been extended
            List<Integer> newTeamOneScores = new ArrayList<>(teamOneScores);
            List<Integer> newTeamTwoScores = new ArrayList<>(teamTwoScores);
            int newMatchesCount = length - this.length;
            for (int i = 0; i < newMatchesCount; i++) {
                newTeamOneScores.add(0);
                newTeamTwoScores.add(0);
            }
            setScores(length, newTeamOneScores, newTeamTwoScores, false, forceResetSubsequentSeries);
        }
    }

    public void setHasBeenPlayed(boolean hasBeenPlayed) {
        setScores(length, teamOneScores, teamTwoScores, hasBeenPlayed, false);
    }

    public void setTeamOneScores(List<Integer> teamOneScores) {
        setScores(length, teamOneScores, teamTwoScores, played, false);
    }

    public void setTeamTwoScores(List<Integer> teamTwoScores) {
        setScores(length, teamOneScores, teamTwoScores, played, false);
    }

    public void setScores(int teamOneScore, int teamTwoScore, int match) {
        if (match >= length) throw new IllegalArgumentException("Series only has " + length + " matches");

        List<Integer> newTeamOneScores = new ArrayList<>(teamOneScores);
        List<Integer> newTeamTwoScores = new ArrayList<>(teamTwoScores);
        newTeamOneScores.set(match, teamOneScore);
        newTeamTwoScores.set(match, teamTwoScore);

        setScores(newTeamOneScores, newTeamTwoScores);
    }

    public void setScores(List<Integer> teamOneScores, List<Integer> teamTwoScores) {
        setScores(length, teamOneScores, teamTwoScores, played, false);
    }

    public void setScores(List<Integer> teamOneScores, List<Integer> teamTwoScores, boolean hasBeenPlayed) {
        setScores(length, teamOneScores, teamTwoScores, hasBeenPlayed, false);
    }

    public void setScores(int seriesLength, List<Integer> teamOneScores, List<Integer> teamTwoScores, boolean hasBeenPlayed, boolean forceResetSubsequentSeries) {

        if (!isReadyToPlay()) throw new IllegalStateException("Match is not playable");

        if (seriesLength <= 0) throw new IllegalArgumentException("Series length must be at least one.");
        if (seriesLength % 2 == 0) throw new IllegalArgumentException("Series must have an odd number of matches.");
        if (seriesLength == teamOneScores.size()) throw new IllegalArgumentException("Wrong number of team one scores given for a series of length " + seriesLength);
        if (seriesLength == teamTwoScores.size()) throw new IllegalArgumentException("Wrong number of team two scores given for a series of length " + seriesLength);

        boolean outcomeChanging = willOutcomeChange(teamOneScores, teamTwoScores, hasBeenPlayed);

        if (outcomeChanging) {
            // Are there any subsequent matches that has been played?
            if ((winnerDestination != null && winnerDestination.hasAnyNonZeroScore())
                    || loserDestination != null && loserDestination.hasAnyNonZeroScore()) {

                // A subsequent match has scores that are not 0. We can only proceed with force
                if (forceResetSubsequentSeries) {
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
        length = seriesLength;
        played = hasBeenPlayed;
        this.teamOneScores = teamOneScores;
        this.teamTwoScores = teamTwoScores;

        // Transfer because there might be a new winner/loser
        if (played && outcomeChanging) {
            transferWinnerAndLoser();
        }

        notifyMatchChangeListeners();
        notifyMatchPlayedListeners();
    }

    /**
     * @return true if any of the matches scores are not 0
     */
    public boolean hasAnyNonZeroScore() {
        for (int i = 0; i < length; i++) {
            if (teamOneScores.get(i) != 0) return true;
            if (teamTwoScores.get(i) != 0) return true;
        }
        return false;
    }

    /**
     * Change all matches' result to be 0-0 and the series to be not played. If any series that depends on this
     * series, those will also be reset.
     */
    public void forceReset() {
        setScores(length, Ints.asList(new int[length]), Ints.asList(new int[length]), false, true);
    }

    /** Returns true if the outcome and thus the state of this match change, if it had the given score instead. */
    public boolean willOutcomeChange(List<Integer> altTeamOneScore, List<Integer> altTeamTwoScore, boolean altHasBeenPlayed) {
        if (this.played != altHasBeenPlayed) {
            return true;
        }

        // Was not played before, and is not played now. In this case we don't care about score. Nothing changes
        if (!altHasBeenPlayed) {
            return false;
        }

        // Check if winner stays the same
        Outcome currentOutcome = winnerIfScores(teamOneScores, teamTwoScores);
        Outcome altOutcome = winnerIfScores(altTeamOneScore, altTeamTwoScore);
        return currentOutcome != altOutcome;
    }

    /**
     * Returns the outcome of the series if the given scores were the scores of the series and assuming the series
     * has been played an is over. Draws are possible.
     */
    static private Outcome winnerIfScores(List<Integer> teamOneScores, List<Integer> teamTwoScores) {
        if (teamOneScores.size() != teamTwoScores.size()) throw new IllegalArgumentException("Not the same amout of scores");

        // Count wins
        int oneWins = 0;
        int twoWins = 0;
        for (int i = 0; i < teamOneScores.size(); i++) {
            if (teamOneScores.get(i) > teamTwoScores.get(i)) oneWins++;
            else if (teamOneScores.get(i) < teamTwoScores.get(i)) twoWins++;
        }

        if (oneWins > twoWins) return Outcome.TEAM_ONE_WINS;
        else if (oneWins < twoWins) return Outcome.TEAM_TWO_WINS;
        else return Outcome.DRAW;
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

    public int getTeamOneScores() {
        return teamOneScores;
    }

    public int getTeamTwoScores() {
        return teamTwoScores;
    }

    public int getBlueScore() {
        return teamOneIsBlue ? getTeamOneScores() : getTeamTwoScores();
    }

    public int getOrangeScore() {
        return teamOneIsBlue ? getTeamTwoScores() : getTeamOneScores();
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
            if (teamOneFromSeries != null) {
                return (teamOneWasWinnerInPreviousMatch ? "Winner of " : "Loser of ") + teamOneFromSeries.getIdentifier();
            }
            return "TBD";
        }
        return teamOne.getTeamName();
    }

    /** Returns team two's name. If team two is null, then "Winner/Loser of .." or "TBD" is returned. */
    public String getTeamTwoAsString() {
        if (teamTwo == null) {
            if (teamTwoFromSeries != null) {
                return (teamTwoWasWinnerInPreviousMatch ? "Winner of " : "Loser of ") + teamTwoFromSeries.getIdentifier();
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

    public Series getTeamOneFromSeries() {
        return teamOneFromSeries;
    }

    public Series getTeamTwoFromSeries() {
        return teamTwoFromSeries;
    }

    public Series getWinnerDestination() {
        return winnerDestination;
    }

    public Series getLoserDestination() {
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
        Series series = (Series) o;
        return id == series.id &&
                getTeamOneScores() == series.getTeamOneScores() &&
                getTeamTwoScores() == series.getTeamTwoScores() &&
                played == series.played &&
                teamOneIsBlue == series.teamOneIsBlue &&
                teamOneWasWinnerInPreviousMatch == series.teamOneWasWinnerInPreviousMatch &&
                teamTwoWasWinnerInPreviousMatch == series.teamTwoWasWinnerInPreviousMatch &&
                winnerGoesToTeamOne == series.winnerGoesToTeamOne &&
                loserGoesToTeamOne == series.loserGoesToTeamOne &&
                Objects.equals(getTeamOne(), series.getTeamOne()) &&
                Objects.equals(getTeamTwo(), series.getTeamTwo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getTeamOneScores(), getTeamTwoScores(), played, teamOneIsBlue, getTeamOne(), getTeamTwo(), teamOneWasWinnerInPreviousMatch, teamTwoWasWinnerInPreviousMatch, winnerGoesToTeamOne, loserGoesToTeamOne);
    }

    @Override
    public String toString() {
        return "Match:{" + getTeamOneAsString() + " (" + teamOneScores + ") vs ("
                + teamTwoScores + ") " + getTeamTwoAsString() + ", status: " + getStatus() + "}";
    }
}
