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

import java.util.*;
import java.util.stream.Collectors;

public class SingleEliminationFormat extends Elimination implements MatchPlayedListener {

    transient private List<MatchPlayedListener> matchPlayedListeners = new LinkedList<>();

    @Override
    public void start(List<Team> seededTeams) {
        this.seededTeams = new ArrayList<>(seededTeams);
        rounds = (int) Math.ceil(Math.log(seededTeams.size()) / Math.log(2));
        generateUpperBracket(rounds);
        seedUpperBracket(seededTeams, rounds);
        status = StageStatus.RUNNING;
        finalMatch.registerMatchPlayedListener(this);
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
        return this.upperBracketMatchesArray;
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
            roundUpperBoundIndex = getRightSide(roundUpperBoundIndex);
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

    /**
     * Repairs match-structure after deserialization
     */
    @Override
    public void repair() {
        // set final match to root of Match-tree
        this.finalMatch = this.upperBracketMatchesArray[0];
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

        equals = Arrays.equals(upperBracketMatchesArray, that.upperBracketMatchesArray);
        return equals;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getStatus(), seededTeams, finalMatch, rounds);
        result = 31 * result + Arrays.hashCode(upperBracketMatchesArray);
        return result;
    }
}
