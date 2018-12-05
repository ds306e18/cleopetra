package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchStatus;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.ModelCoupledUI;
//import dk.aau.cs.ds306e18.tournament.ui.tabs.BracketOverview;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DoubleEliminationFormat extends Elimination implements MatchPlayedListener {
    Match[] lowerBracketMatchesArray;
    ArrayList<Match> upperBracketMatches = new ArrayList<>();
    ArrayList<Match> lowerBracketMatches = new ArrayList<>();

    @Override
    public void start(List<Team> seededTeams) {
        this.seededTeams = new ArrayList<>(seededTeams);
        rounds = (int) Math.ceil(Math.log(seededTeams.size()) / Math.log(2));
        generateUpperBracket(rounds);
        seedUpperBracket(seededTeams, rounds);
        generateLowerBracket(rounds);
        removeByes();
        status = StageStatus.RUNNING;
        finalMatch.registerMatchPlayedListener(this);
    }

    @Override
    public StageStatus getStatus() { return status; }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {
        List<Team> topTeamsList = new ArrayList<>(), tempWinnerList = new ArrayList<>(), tempLoserList = new ArrayList<>();
        topTeamsList.add(finalMatch.getWinner());
        if(count > 1) { topTeamsList.add(finalMatch.getLoser()); }

        int matchIndex = lowerBracketMatches.size()-1;
        int upperBound = lowerBracketMatches.size()-2;
        int upperBoundReduction = 1, doubleUpperBound = 1;
        while(topTeamsList.size() < count){
            while(matchIndex > upperBound){
                if(!topTeamsList.contains(lowerBracketMatches.get(matchIndex).getWinner())){
                    tempWinnerList.add(lowerBracketMatches.get(matchIndex).getWinner());
                }
                if(!topTeamsList.contains(lowerBracketMatches.get(matchIndex).getLoser())){
                    tempLoserList.add(lowerBracketMatches.get(matchIndex).getLoser());
                }
                matchIndex--;
            }

            upperBound = upperBound - upperBoundReduction;
            if(upperBound == 1){ upperBound += 1; }
            if(doubleUpperBound % 2 == 0){ upperBoundReduction = upperBoundReduction * 2; }
            doubleUpperBound++;

            if(tempWinnerList.size() > 1){ tempWinnerList = tieBreaker.compareAll(tempWinnerList, tempWinnerList.size()); }
            if(tempLoserList.size() > 1){ tempLoserList = tieBreaker.compareAll(tempLoserList, tempLoserList.size()); }
            topTeamsList.addAll(tempWinnerList);
            tempWinnerList.clear();
            topTeamsList.addAll(tempLoserList);
            tempLoserList.clear();
        }

        while(topTeamsList.size() > count){ topTeamsList.remove(topTeamsList.size()-1); }

        return topTeamsList;
    }

    @Override
    public List<Match> getAllMatches() {
        ArrayList<Match> allMatches = new ArrayList<>(upperBracketMatches);
        allMatches.addAll(lowerBracketMatches);
        allMatches.add(finalMatch);
        return allMatches;
    }

    @Override
    public List<Match> getUpcomingMatches() {
        return getAllMatches().stream().filter(c -> c.getStatus().equals(MatchStatus.READY_TO_BE_PLAYED) && !c.hasBeenPlayed()).collect(Collectors.toList());
    }

    @Override
    public List<Match> getPendingMatches() {
        return getAllMatches().stream().filter(c -> c.getStatus().equals(MatchStatus.NOT_PLAYABLE)).collect(Collectors.toList());
    }

    @Override
    public List<Match> getCompletedMatches() {
        return getAllMatches().stream().filter(c -> c.hasBeenPlayed()).collect(Collectors.toList());
    }

    @Override
    public <T extends Node & ModelCoupledUI> T getBracketFXNode(BracketOverviewTabController bracketOverview) {
        return null;
    }

    @Override
    public Node getSettingsFXNode() {
        return null;
    }

    @Override
    public void repair() {

    }

    public List<Match> getLowerBracketMatches() {
        return lowerBracketMatches;
    }

    public List<Match> getUpperBracketMatches(){
        return upperBracketMatches;
    }

    @Override
    public void onMatchPlayed(Match match) {
        // TODO: Register stage as listener to all relevant matches
        if (finalMatch.hasBeenPlayed()) {
            status = StageStatus.CONCLUDED;
        } else {
            status = StageStatus.RUNNING;
        }
    }

    public int getNumberOfTeams(){
        return seededTeams.size();
    }

    private void generateLowerBracket(int roundsInUpperBracket) {
        int matchesInCurrentRound = (int) Math.pow(2, roundsInUpperBracket - 2), matchNumberInRound, winnerOfMatchIndex = 0;
        upperBracketMatches = finalMatch.getTreeAsListBFS();
        lowerBracketMatchesArray = new Match[upperBracketMatchesArray.length - 1];
        int loserOfMatchIndex = upperBracketMatchesArray.length - 1;
        int roundCount = 1, byeNumber = 0;
        int rounds = roundsInUpperBracket;
        rounds = rounds + rounds - 2;

        // Generate the bracket from the first round to the final. All upperBracketMatches except those in
        // the first uses the winners of previous upperBracketMatches.
        for (int roundsLeft = rounds; roundsLeft > 0; roundsLeft--) {

            if (roundsLeft == rounds) {
                // First round, fill the round with loserOfs matches from the upperBrackets first round matches
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {

                    if (upperBracketMatchesArray[loserOfMatchIndex] == null && upperBracketMatchesArray[loserOfMatchIndex - 1] == null) {
                        lowerBracketMatches.add(new Match().setBlue(new Team("bye" + byeNumber, null, 999, ""))
                                .setOrange(new Team("bye" + byeNumber + 1, null, 999, "")));
                        byeNumber = byeNumber + 2;
                    } else if (upperBracketMatchesArray[loserOfMatchIndex] == null) {
                        lowerBracketMatches.add(new Match().setBlue(new Team("bye" + byeNumber, null, 999, "")).
                                setOrangeToLoserOf(upperBracketMatchesArray[loserOfMatchIndex - 1]));
                        byeNumber++;
                    } else if (upperBracketMatchesArray[loserOfMatchIndex - 1] == null) {
                        lowerBracketMatches.add(new Match().setBlueToLoserOf(upperBracketMatchesArray[loserOfMatchIndex]).
                                setOrange(new Team("bye" + byeNumber, null, 999, "")));
                        byeNumber++;
                    } else {
                        lowerBracketMatches.add(new Match().setBlueToLoserOf(upperBracketMatchesArray[loserOfMatchIndex]).
                                setOrangeToLoserOf(upperBracketMatchesArray[loserOfMatchIndex - 1]));
                    }
                    loserOfMatchIndex = loserOfMatchIndex - 2;
                }
            } else {
                // Fills all the remaining matches with winners from lowerbracket, and losers from upperbracket.
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    //The lowerbracket teams should play each other with no upperbracketteams being knocked to lowerbracket.
                    if (roundCount % 2 == 1) {
                        lowerBracketMatches.add(new Match().setBlueToWinnerOf(lowerBracketMatches.get(winnerOfMatchIndex)).
                                setOrangeToWinnerOf(lowerBracketMatches.get(winnerOfMatchIndex + 1)));
                        winnerOfMatchIndex = winnerOfMatchIndex + 2;
                    }
                    //the upperbracket teams knocked down to lowerbracket, will be play in the lowerbracket against other contestors.
                    if (roundCount % 2 == 0) {
                        lowerBracketMatches.add(new Match().setBlueToWinnerOf(lowerBracketMatches.get(winnerOfMatchIndex)).
                                setOrangeToLoserOf(upperBracketMatchesArray[loserOfMatchIndex]));
                        winnerOfMatchIndex++;
                        loserOfMatchIndex--;
                    }
                }
            }

            //Divides only when there are no upperbracket teams being knocked down to lowerbracket.
            if (roundCount % 2 == 0) {
                matchesInCurrentRound /= 2;
            }
            roundCount++;
        }

        // The final is the last match in the list
        finalMatch = new Match().setBlueToWinnerOf(finalMatch).setOrangeToWinnerOf(lowerBracketMatches.get(lowerBracketMatches.size() - 1));

        //Creates an array of matches to itarate trough when generating the lower bracket.
        lowerBracketMatchesArray = new Match[lowerBracketMatches.size()];
        lowerBracketMatchesArray = lowerBracketMatches.toArray(lowerBracketMatchesArray);
    }

    //The loop will remove the matches that would normally contain bye's.
    private void removeByes() {

        //Iterates for each match in the matches array
        for (Match match : lowerBracketMatchesArray) {

            int byes = 0;
            //Checks if one or both teams are a bye
            if (match.getBlueTeam() != null && match.getBlueTeam().getBots() == null) {
                byes++;
            }
            if (match.getOrangeTeam() != null && match.getOrangeTeam().getBots() == null) {
                byes++;
            }
            //If only one team is a bye, sets blue in winner destination to be the loser of the previous match
            //then removes the match from teh lowerBracketMatches list
            if (byes == 1) {

                match.getWinnerDestination().setBlueToLoserOf(match.getOrangeFromMatch());
                lowerBracketMatches.remove(match);
            }

            // IF both teams are bye's pass loser from the previous match to the either the blue or orange position
            //in the winner's winner's destination then delete both match and its winner destination
            if (byes == 2) {
                if (match.getWinnerDestination().getWinnerDestination().equals(match.getWinnerDestination())) {
                    match.getWinnerDestination().getWinnerDestination().setBlueToLoserOf(match.getWinnerDestination().getOrangeFromMatch());
                }

                if (match.getWinnerDestination().getWinnerDestination().equals(match.getWinnerDestination())) {
                    match.getWinnerDestination().getWinnerDestination().setOrangeToLoserOf(match.getWinnerDestination().getOrangeFromMatch());
                }
                lowerBracketMatches.remove(match.getWinnerDestination());
                lowerBracketMatches.remove(match);
            }
        }
    }


}

