package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
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
    private ArrayList<Team> seededTeams;
    private Match finalMatch;
    private Match[] bracket;
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
        finalMatch.registerMatchPlayedListener(this);
        setupStatsTracking();
    }

    /** Generates a single-elimination bracket structure. Matches are referenced by setting winner of from earlier matches.
     * Matches are accessed through finalMatch (the root) or the array upperBracketMatchesArray.
     */
    private void generateBracket() {
        int matchesInFirstRound = pow2(rounds - 1);
        int numberOfMatches = pow2(rounds) - 1;
        bracket = new Match[numberOfMatches];
        for(int i = numberOfMatches - 1; i >= 0; i--) {
            // Creates empty matches for first round
            if(i >= numberOfMatches - matchesInFirstRound) {
                bracket[i] = new Match();
            }
            // Creates the remaining matches which contains winners from their left- and right child-indexes.
            else {
                bracket[i] = new Match()
                        .setTeamOneToWinnerOf(bracket[getLeftIndex(i)])
                        .setTeamTwoToWinnerOf(bracket[getRightIndex(i)]);
            }
        }
        finalMatch = bracket[0];
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
        int seedMatchIndex = finalMatch.getTreeAsListBFS().size() - 1;
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
        List<Match> treeAsListBFS = finalMatch.getTreeAsListBFS();
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
        List<Match> allMatches = getAllMatches();
        for (Team team : seededTeams) {
            team.getStatsManager().trackMatches(this, allMatches);
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
    public List<Match> getAllMatches() {
        return finalMatch.getTreeAsListBFS();
    }

    @Override
    public List<Match> getUpcomingMatches() {
        return finalMatch.getTreeAsListBFS().stream().filter(c -> c.getStatus().equals(Match.Status.READY_TO_BE_PLAYED) && !c.hasBeenPlayed()).collect(Collectors.toList());
    }

    @Override
    public List<Match> getPendingMatches() {
        return finalMatch.getTreeAsListBFS().stream().filter(c -> c.getStatus().equals(Match.Status.NOT_PLAYABLE)).collect(Collectors.toList());
    }

    @Override
    public List<Match> getCompletedMatches() {
        return finalMatch.getTreeAsListBFS().stream().filter(Match::hasBeenPlayed).collect(Collectors.toList());
    }

    public Match[] getMatchesAsArray() {
        return this.bracket;
    }

    @Override
    public void onMatchPlayed(Match match) {
        // Was it last match?
        StageStatus oldStatus = status;
        if (finalMatch.hasBeenPlayed()) {
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
        List<Match> matchesBFS = finalMatch.getTreeAsListBFS();
        int numberOfMatches = matchesBFS.size();
        int roundUpperBoundIndex = 1, currentMatchIndex = 0;

        if(count > seededTeams.size()){ count = seededTeams.size();}

        // Will run until enough teams has been found
        while (topTeams.size() < count) {
            // places the losers and winners of the round into two different temporary lists
            while (currentMatchIndex < roundUpperBoundIndex && currentMatchIndex < numberOfMatches) {
                Match current = matchesBFS.get(currentMatchIndex);
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
                tempWinnerTeams = tieBreaker.compareAll(tempWinnerTeams, tempWinnerTeams.size());
            }
            if (tempLoserTeams.size() > 1) {
                tempLoserTeams = tieBreaker.compareAll(tempLoserTeams, tempLoserTeams.size());
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
        this.finalMatch = this.bracket[0];
        finalMatch.registerMatchPlayedListener(this);

        // Reconnect all matches
        for (int i = 0; i < bracket.length; i++) {
            if (bracket[i] != null) {
                // Blue is winner from left match, if such a match exists
                int leftIndex = getLeftIndex(i);
                if (leftIndex < bracket.length) {
                    Match leftMatch = bracket[leftIndex];
                    if (leftMatch != null) {
                        bracket[i].reconnectTeamOneToWinnerOf(leftMatch);
                    }
                }
                // Orange is winner from right match, if such a match exists
                int rightIndex = getRightIndex(i);
                if (rightIndex < bracket.length) {
                    Match rightMatch = bracket[rightIndex];
                    if (rightMatch != null) {
                        bracket[i].reconnectTeamTwoToWinnerOf(rightMatch);
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
                Objects.equals(finalMatch, that.finalMatch) &&
                Arrays.equals(bracket, that.bracket);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(status, seededTeams, finalMatch, rounds);
        result = 31 * result + Arrays.hashCode(bracket);
        return result;
    }
}
