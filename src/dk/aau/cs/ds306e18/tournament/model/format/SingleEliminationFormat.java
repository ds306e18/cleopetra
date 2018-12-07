package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.SingleEliminationNode;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchStatus;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import javafx.scene.Node;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class SingleEliminationFormat implements Format, MatchPlayedListener {

    private StageStatus status = StageStatus.PENDING;
    private ArrayList<Team> seededTeams;
    private Match finalMatch;
    private Match[] bracket;
    private int rounds;

    @Override
    public void start(List<Team> seededTeams) {
        this.seededTeams = new ArrayList<>(seededTeams);
        rounds = (int) Math.ceil(Math.log(seededTeams.size()) / Math.log(2));
        generateBracket();
        seedBracket(seededTeams);
        status = StageStatus.RUNNING;
        finalMatch.registerMatchPlayedListener(this);
    }

    /** Generates a single-elimination bracket structure. Matches are referenced by setting winner of from earlier matches.
     * Matches are accessed through finalMatch (the root) or the array upperBracketMatchesArray.
     */
    private void generateBracket() {
        int matchesInFstRound = (int) Math.pow(2, rounds - 1);
        int amountOfMatches = (int) Math.pow(2,rounds)-1;
        bracket = new Match[amountOfMatches];
        for(int i = amountOfMatches - 1; i >= 0; i--) {
            //Creates empty matches for first round
            if(i >= amountOfMatches - matchesInFstRound) {
                bracket[i] = new Match();
            }
            //Creates the remaining matches which contains winners from their left- and right child-indexes.
            else {
                bracket[i] = new Match()
                        .setBlueToWinnerOf(bracket[getLeftIndex(i)])
                        .setOrangeToWinnerOf(bracket[getRightIndex(i)]);
            }
        }
        finalMatch = bracket[0];
    }

    /** Seeds the single-elimination bracket with teams to give better placements.
     * If there are an insufficient amount of teams, the team(s) with the best seeding(s) will be placed in the next round instead.
     * @param seededTeams a list containing the teams in the tournament */
    private void seedBracket(List<Team> seededTeams) {
        ArrayList<Team> seedList = new ArrayList<>(seededTeams);

        //Create needed amount of byes to match with the empty matches
        ArrayList<Team> byeList = addByes(seededTeams.size());
        seedList.addAll(byeList);

        //Reorders list with fair seeding method
        fairSeeding(seedList);

        //Places the teams in the bracket, and removes unnecessary matches
        placeTeamsInBracket(seedList, byeList);
    }

    /** Places the teams in the bracket and removes unnecessary matches
     * @param seedList a list of seeded teams in a fair seeding order
     * @param byeList a list of dummy teams */
    private void placeTeamsInBracket(ArrayList<Team> seedList, ArrayList<Team> byeList) {
        int seedMatchIndex = finalMatch.getTreeAsListBFS().size() - 1;
        int  amountOfTeams = seedList.size();
        for (int teamIndex = 0; teamIndex < amountOfTeams; teamIndex = teamIndex + 2) {
            // If the matchup would be between a team and a bye, the team will be placed at its parent match
            // The match in the first round will be deleted(null)
            if (byeList.contains(seedList.get(teamIndex)) || byeList.contains(seedList.get(teamIndex + 1))) {
                int matchCheckIndex = getParentIndex(seedMatchIndex);
                if (getLeftIndex(matchCheckIndex) == seedMatchIndex) {
                    bracket[getParentIndex(seedMatchIndex)].setBlue(seedList.get(teamIndex));
                }
                else {
                    bracket[getParentIndex(seedMatchIndex)].setOrange(seedList.get(teamIndex));
                }
                bracket[seedMatchIndex] = null;
                seedMatchIndex--;
            }
            // If there are no byes in the match up, place the teams vs each other as intended
            else {
                bracket[seedMatchIndex].setBlue(seedList.get(teamIndex));
                bracket[seedMatchIndex].setOrange(seedList.get(teamIndex+1));
                seedMatchIndex--;
            }
        }
    }

    /** Reorders seedList to a fair list order for seeding
     * @param seedList a list of seeded teams in ascending order */
    private void fairSeeding(ArrayList<Team> seedList) {
        //Variables used for seeding
        int slice = 1;
        int interactions = seedList.size() / 2;

        //Order the teams in a seeded list
        while (slice < interactions) {
            ArrayList<Team> temp = new ArrayList<>(seedList);
            seedList.clear();

            while (temp.size() > 0) {
                int lastIndex = temp.size();
                seedList.addAll(temp.subList(0, slice));
                seedList.addAll(temp.subList(lastIndex - slice, lastIndex));
                temp.removeAll(temp.subList(lastIndex - slice, lastIndex));
                temp.removeAll(temp.subList(0, slice));
            }

            slice *= 2;
        }
    }

    /** Add the needed amount of byes
     * @param amountOfTeams the amount of teams in the stage
     * @return byeList, an arrayList containing dummy teams */
    private ArrayList<Team> addByes(int amountOfTeams){
        int amountOfByes = (int) Math.pow(2, rounds) - amountOfTeams;
        ArrayList<Team> byeList = new ArrayList<>();
        while (byeList.size() < amountOfByes) {
            byeList.add(new Team("bye" + byeList.size(), new ArrayList<>(), 999, ""));
        }
        return byeList;
    }

    int getParentIndex(int i) {
        i = i + 1;
        if (i == 1) {
            return -1;
        } else {
            return Math.floorDiv(i,2)-1;
        }
    }

    int getLeftIndex(int i){
        i = i + 1;
        return 2 * i;
    }

    int getRightIndex(int i) {
        i = i + 1;
        return 2 * i - 1;
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
        return finalMatch.getTreeAsListBFS().stream().filter(c -> c.getStatus().equals(MatchStatus.READY_TO_BE_PLAYED) && !c.hasBeenPlayed()).collect(Collectors.toList());
    }

    @Override
    public List<Match> getPendingMatches() {
        return finalMatch.getTreeAsListBFS().stream().filter(c -> c.getStatus().equals(MatchStatus.NOT_PLAYABLE)).collect(Collectors.toList());
    }

    @Override
    public List<Match> getCompletedMatches() {
        return finalMatch.getTreeAsListBFS().stream().filter(c -> c.hasBeenPlayed()).collect(Collectors.toList());
    }

    public Match[] getMatchesAsArray() {
        return this.bracket;
    }

    @Override
    public void onMatchPlayed(Match match) {
        if (finalMatch.hasBeenPlayed()) {
            status = StageStatus.CONCLUDED;
        } else {
            status = StageStatus.RUNNING;
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
        int roundUpperBoundIndex = 1, currentMatchIndex = 0;

        if(count > seededTeams.size()){ count = seededTeams.size();}

        //Will run until team size fits the count
        while (topTeams.size() < count) {
            //places the losers and winners of the round into two different temporary lists
            while (currentMatchIndex < roundUpperBoundIndex) {
                if (!topTeams.contains(finalMatch.getTreeAsListBFS().get(currentMatchIndex).getWinner())) {
                    tempWinnerTeams.add(finalMatch.getTreeAsListBFS().get(currentMatchIndex).getWinner());
                }
                if (!topTeams.contains(finalMatch.getTreeAsListBFS().get(currentMatchIndex).getLoser())) {
                    tempLoserTeams.add(finalMatch.getTreeAsListBFS().get(currentMatchIndex).getLoser());
                }
                currentMatchIndex++;
            }

            //Sorts the teams accordingly to the tiebreaker
            if (tempWinnerTeams.size() > 1) {
                tempWinnerTeams = tieBreaker.compareAll(tempWinnerTeams, tempWinnerTeams.size());
            }
            if (tempLoserTeams.size() > 1) {
                tempLoserTeams = tieBreaker.compareAll(tempLoserTeams, tempLoserTeams.size());
            }

            //Winners will be placed before the losers, and the lists will be cleared
            topTeams.addAll(tempWinnerTeams);
            tempWinnerTeams.clear();
            topTeams.addAll(tempLoserTeams);
            tempLoserTeams.clear();

            //New round for the loop to iterate
            roundUpperBoundIndex = getRightIndex(roundUpperBoundIndex);
        }

        //If there are too many teams, remove teams
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
    public void repair() {
        // set final match to root of Match-tree
        this.finalMatch = this.bracket[0];
        // register listener for each finalMatch
        this.finalMatch.registerMatchPlayedListener(this);
        // recursively call postDeserializationRepair
        this.finalMatch.postDeserializationRepair();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleEliminationFormat that = (SingleEliminationFormat) o;
        boolean equals;
        equals = rounds == that.rounds &&
                getStatus() == that.getStatus() &&
                Objects.equals(seededTeams, that.seededTeams);

        equals = Arrays.equals(bracket, that.bracket);
        return equals;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getStatus(), seededTeams, finalMatch, rounds);
        result = 31 * result + Arrays.hashCode(bracket);
        return result;
    }
}
