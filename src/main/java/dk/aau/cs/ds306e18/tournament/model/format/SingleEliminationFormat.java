package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.SingleEliminationNode;
import javafx.scene.Node;

import java.util.*;
import java.util.stream.Collectors;

import static dk.aau.cs.ds306e18.tournament.utility.PowMath.log2;
import static dk.aau.cs.ds306e18.tournament.utility.PowMath.pow2;

public class SingleEliminationFormat implements Format, MatchPlayedListener {

    private StageStatus status = StageStatus.PENDING;
    private int defaultSeriesLength = 1;
    private ArrayList<Team> seededTeams;
    private Series finalSeries;
    private Series[] bracket;
    private int rounds;

    transient private List<StageStatusChangeListener> statusChangeListeners = new LinkedList<>();

    @Override
    public void start(List<Team> seededTeams, boolean doSeeding) {
        this.seededTeams = new ArrayList<>(seededTeams);
        rounds = log2(seededTeams.size());
        generateBracket();
        seedBracket(seededTeams, doSeeding);
        giveMatchesIdentifiers();
        status = StageStatus.RUNNING;
        finalSeries.registerMatchPlayedListener(this);
        setupStatsTracking();
    }

    /** Generates a single-elimination bracket structure. Matches are referenced by setting winner of from earlier matches.
     * Matches are accessed through finalMatch (the root) or the array upperBracketMatchesArray.
     */
    private void generateBracket() {
        int matchesInFirstRound = pow2(rounds - 1);
        int numberOfMatches = pow2(rounds) - 1;
        bracket = new Series[numberOfMatches];
        for(int i = numberOfMatches - 1; i >= 0; i--) {
            // Creates empty matches for first round
            if(i >= numberOfMatches - matchesInFirstRound) {
                bracket[i] = new Series(defaultSeriesLength);
            }
            // Creates the remaining matches which contains winners from their left- and right child-indexes.
            else {
                bracket[i] = new Series(defaultSeriesLength)
                        .setTeamOneToWinnerOf(bracket[getLeftIndex(i)])
                        .setTeamTwoToWinnerOf(bracket[getRightIndex(i)]);
            }
        }
        finalSeries = bracket[0];
    }

    /** Seeds the single-elimination bracket with teams to give better placements.
     * If there are an insufficient amount of teams, the team(s) with the best seeding(s) will be placed in the next round instead.
     * @param seededTeams a list containing the teams in the tournament
     * @param doSeeding whether or not to use seeding. If false, the reordering of teams is skipped. */
    private void seedBracket(List<Team> seededTeams, boolean doSeeding) {
        List<Team> seedList = new ArrayList<>(seededTeams);

        //Create needed amount of byes to match with the empty matches
        ArrayList<Team> byeList = addByes(seededTeams.size());
        seedList.addAll(byeList);

        if (doSeeding) {
            //Reorders list with fair seeding method
            seedList = Seeding.fairSeedList(seedList);
        }

        //Places the teams in the bracket, and removes unnecessary matches
        placeTeamsInBracket(seedList, byeList);
    }

    /** Places the teams in the bracket and removes unnecessary matches
     * @param seedList a list of seeded teams in a fair seeding order
     * @param byeList a list of dummy teams */
    private void placeTeamsInBracket(List<Team> seedList, ArrayList<Team> byeList) {
        int seedMatchIndex = finalSeries.getTreeAsListBFS().size() - 1;
        int  numberOfTeams = seedList.size();
        for (int teamIndex = 0; teamIndex < numberOfTeams; teamIndex = teamIndex + 2) {
            // If the matchup would be between a team and a bye, the team will be placed at its parent match
            // The match in the first round will be deleted(null)
            if (byeList.contains(seedList.get(teamIndex)) || byeList.contains(seedList.get(teamIndex + 1))) {
                int matchCheckIndex = getParentIndex(seedMatchIndex);
                if (getLeftIndex(matchCheckIndex) == seedMatchIndex) {
                    bracket[getParentIndex(seedMatchIndex)].setTeamOne(seedList.get(teamIndex));
                }
                else {
                    bracket[getParentIndex(seedMatchIndex)].setTeamTwo(seedList.get(teamIndex));
                }
                bracket[seedMatchIndex] = null;
                seedMatchIndex--;
            }
            // If there are no byes in the match up, place the teams vs each other as intended
            else {
                bracket[seedMatchIndex].setTeamOne(seedList.get(teamIndex));
                bracket[seedMatchIndex].setTeamTwo(seedList.get(teamIndex+1));
                seedMatchIndex--;
            }
        }
    }

    /** Add the needed amount of byes
     * @param numberOfTeams the amount of teams in the stage
     * @return byeList, an arrayList containing dummy teams */
    private ArrayList<Team> addByes(int numberOfTeams){
        int numberOfByes = pow2(rounds) - numberOfTeams;
        ArrayList<Team> byeList = new ArrayList<>();
        while (byeList.size() < numberOfByes) {
            byeList.add(new Team("bye" + byeList.size(), new ArrayList<>(), 999, ""));
        }
        return byeList;
    }

    /** Gives the matches identifiers. */
    private void giveMatchesIdentifiers() {
        List<Series> treeAsListBFS = finalSeries.getTreeAsListBFS();
        int index = 1;
        for (int i = treeAsListBFS.size() - 1; i >= 0; i--) {
            treeAsListBFS.get(i).setIdentifier(index++);
        }
    }

    private int getParentIndex(int i) {
        if (i == 0) {
            return -1;
        } else {
            return Math.floorDiv(i + 1, 2) - 1;
        }
    }

    private int getLeftIndex(int i){
        return 2 * (i + 1);
    }

    private int getRightIndex(int i) {
        return 2 * (i + 1) - 1;
    }

    private void setupStatsTracking() {
        List<Series> allSeries = getAllSeries();
        for (Team team : seededTeams) {
            team.getStatsManager().trackAllSeries(this, allSeries);
        }
    }

    public int getRounds() {
        return rounds;
    }

    @Override
    public StageStatus getStatus() {
        return status;
    }

    @Override
    public List<Series> getAllSeries() {
        return finalSeries.getTreeAsListBFS();
    }

    @Override
    public List<Series> getUpcomingMatches() {
        return finalSeries.getTreeAsListBFS().stream().filter(c -> c.getStatus().equals(Series.Status.READY_TO_BE_PLAYED) && !c.hasBeenPlayed()).collect(Collectors.toList());
    }

    @Override
    public List<Series> getPendingMatches() {
        return finalSeries.getTreeAsListBFS().stream().filter(c -> c.getStatus().equals(Series.Status.NOT_PLAYABLE)).collect(Collectors.toList());
    }

    @Override
    public List<Series> getCompletedMatches() {
        return finalSeries.getTreeAsListBFS().stream().filter(Series::hasBeenPlayed).collect(Collectors.toList());
    }

    public Series[] getMatchesAsArray() {
        return this.bracket;
    }

    @Override
    public void onMatchPlayed(Series series) {
        // Was it last match?
        StageStatus oldStatus = status;
        if (finalSeries.hasBeenPlayed()) {
            status = StageStatus.CONCLUDED;
        } else {
            status = StageStatus.RUNNING;
        }

        // Notify listeners if status changed
        if (oldStatus != status) {
            notifyStatusListeners(oldStatus, status);
        }
    }

    @Override
    public void registerStatusChangedListener(StageStatusChangeListener listener) {
        statusChangeListeners.add(listener);
    }

    @Override
    public void unregisterStatusChangedListener(StageStatusChangeListener listener) {
        statusChangeListeners.remove(listener);
    }

    /** Let listeners know, that the status has changed */
    private void notifyStatusListeners(StageStatus oldStatus, StageStatus newStatus) {
        for (StageStatusChangeListener listener : statusChangeListeners) {
            listener.onStageStatusChanged(this, oldStatus, newStatus);
        }
    }

    /** Determines the teams with the best performances in the current stage
     * @param count amount of teams to return
     * @param tieBreaker a tiebreaker to decide the best teams if their are placed the same
     * @return a list containing the best placed teams in the tournament */
    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {
        List<Team> topTeams = new ArrayList<>();
        List<Team> tempWinnerTeams = new ArrayList<>();
        List<Team> tempLoserTeams = new ArrayList<>();
        List<Series> matchesBFS = finalSeries.getTreeAsListBFS();
        int numberOfMatches = matchesBFS.size();
        int roundUpperBoundIndex = 1, currentMatchIndex = 0;

        if(count > seededTeams.size()){ count = seededTeams.size();}

        // Will run until enough teams has been found
        while (topTeams.size() < count) {
            // places the losers and winners of the round into two different temporary lists
            while (currentMatchIndex < roundUpperBoundIndex && currentMatchIndex < numberOfMatches) {
                Series current = matchesBFS.get(currentMatchIndex);
                if (!topTeams.contains(current.getWinner())) {
                    tempWinnerTeams.add(current.getWinner());
                }
                if (!topTeams.contains(current.getLoser())) {
                    tempLoserTeams.add(current.getLoser());
                }
                currentMatchIndex++;
            }

            // Sorts the teams accordingly to the tiebreaker
            if (tempWinnerTeams.size() > 1) {
                tempWinnerTeams = tieBreaker.compareAll(tempWinnerTeams, this);
            }
            if (tempLoserTeams.size() > 1) {
                tempLoserTeams = tieBreaker.compareAll(tempLoserTeams, this);
            }

            // Winners will be placed before the losers, and the lists will be cleared
            topTeams.addAll(tempWinnerTeams);
            tempWinnerTeams.clear();
            topTeams.addAll(tempLoserTeams);
            tempLoserTeams.clear();

            // New round for the loop to iterate
            roundUpperBoundIndex = getRightIndex(roundUpperBoundIndex);
        }

        // If there are too many teams, remove teams
        while (topTeams.size() > count) {
            topTeams.remove(topTeams.size() - 1);
        }

        return topTeams;
    }

    @Override
    public void setDefaultSeriesLength(int seriesLength) {
        if (seriesLength <= 0) throw new IllegalArgumentException("Series length must be at least one.");
        if (seriesLength % 2 == 0) throw new IllegalArgumentException("Series must have an odd number of matches.");
        defaultSeriesLength = seriesLength;
    }

    @Override
    public int getDefaultSeriesLength() {
        return defaultSeriesLength;
    }

    @Override
    public SingleEliminationNode getBracketFXNode(BracketOverviewTabController boc) {
        return new SingleEliminationNode(this, boc);
    }

    @Override
    public Node getSettingsFXNode() {
        return null;
    }

    /** Repairs match-structure after deserialization */
    @Override
    public void postDeserializationRepair() {
        // Find final match
        this.finalSeries = this.bracket[0];
        finalSeries.registerMatchPlayedListener(this);

        // Reconnect all matches
        for (int i = 0; i < bracket.length; i++) {
            if (bracket[i] != null) {
                // Blue is winner from left match, if such a match exists
                int leftIndex = getLeftIndex(i);
                if (leftIndex < bracket.length) {
                    Series leftSeries = bracket[leftIndex];
                    if (leftSeries != null) {
                        bracket[i].reconnectTeamOneToWinnerOf(leftSeries);
                    }
                }
                // Orange is winner from right match, if such a match exists
                int rightIndex = getRightIndex(i);
                if (rightIndex < bracket.length) {
                    Series rightSeries = bracket[rightIndex];
                    if (rightSeries != null) {
                        bracket[i].reconnectTeamTwoToWinnerOf(rightSeries);
                    }
                }
            }
        }

        setupStatsTracking();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleEliminationFormat that = (SingleEliminationFormat) o;
        return rounds == that.rounds &&
                status == that.status &&
                Objects.equals(seededTeams, that.seededTeams) &&
                Objects.equals(finalSeries, that.finalSeries) &&
                Arrays.equals(bracket, that.bracket);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(status, seededTeams, finalSeries, rounds);
        result = 31 * result + Arrays.hashCode(bracket);
        return result;
    }
}
