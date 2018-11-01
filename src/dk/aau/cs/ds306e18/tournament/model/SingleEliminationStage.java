package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.UI.Tabs.BracketOverview;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SingleEliminationStage implements Format, MatchListener {

    private StageStatus status = StageStatus.PENDING;
    private ArrayList<Team> seededTeams;
    private Match finalMatch;
    private Match[] matches;
    private int rounds;

    @Override
    public void start(List<Team> seededTeams) {
        this.seededTeams = new ArrayList<>(seededTeams);
        rounds = (int) Math.ceil(Math.log(seededTeams.size()) / Math.log(2));
        generateBracket(rounds);
        seedBracket(seededTeams, rounds);
        status = StageStatus.RUNNING;
        finalMatch.registerListener(this);
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
        return this.matches;
    }

    /** Generates a single-elimination bracket structure. References between the empty matches are made by winnerOf and starterSlots.
     * Matches are accessed through finalMatch (the root).
     * @param rounds the amount of rounds in the bracket*/
    private void generateBracket(int rounds) {
        int matchesInCurrentRound, matchNumberInRound, matchIndex = 0;
        ArrayList<Match> bracketList = new ArrayList<>();

        // Generate the bracket from the first round to the final. All matches except those in
        // the first uses the winners of previous matches.
        for (int roundsLeft = rounds; roundsLeft > 0; roundsLeft--) {
            matchesInCurrentRound = (int) Math.pow(2, roundsLeft-1);

            if (roundsLeft == rounds) {
                // First round, all matches are empty
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    bracketList.add(new Match(new StarterSlot(null), new StarterSlot(null)));
                }
            } else {
                // Fills all the remaining matches with winners from earlier rounds.
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    Match match = new Match(new WinnerOf(bracketList.get(matchIndex)), new WinnerOf(bracketList.get(matchIndex + 1)));
                    bracketList.add(match);
                    matchIndex = matchIndex + 2;
                }
            }
        }

        // The final is the last match in the list
        finalMatch = bracketList.get(bracketList.size() - 1);
        matches = new Match[bracketList.size()];
        matches = finalMatch.getTreeAsListBFS().toArray(matches);
    }

    /** Seeds the single-elimination bracket with teams to give better placements.
     * If there are an insufficient amount of teams, the team(s) with the best seeding(s) will be placed in the next round instead.
     * @param seededTeams a list containing the teams in the tournament
     * @param rounds the amount of rounds in the bracket
     */
    private void seedBracket(List<Team> seededTeams, int rounds) {

        ArrayList<Team> seedList = new ArrayList<>(seededTeams);

        //Create needed amount of byes to match with the slots
        ArrayList<Team> byeList = new ArrayList<>();
        int amountOfByes = (int) Math.pow(2, rounds) - seedList.size();
        while(byeList.size() < amountOfByes) {
            byeList.add(new Team("bye" + byeList.size(), new ArrayList<>(), 999, ""));
        }
        seedList.addAll(byeList);

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

        // Using the seeded list to place the teams into the correct matches
        // If there are byes, the best seeded teams will be placed in their slots parents
        int seedMatchIndex = finalMatch.getTreeAsListBFS().size()-1;
        int teamIndex = 0, teamCount = seedList.size();
        while(teamIndex < teamCount){
            // If the matchup would be between a team and a bye, the team will be placed at its parent match as a starterSlot
            // The match in the first round will be deleted(null)
            if(byeList.contains(seedList.get(teamIndex)) || byeList.contains(seedList.get(teamIndex+1))) {
                int matchCheckIndex = getParent(seedMatchIndex);
                if(getRightSide(matchCheckIndex) == seedMatchIndex) {
                    matches[getParent(seedMatchIndex)].setBlue(new StarterSlot(seedList.get(teamIndex)));
                }
                else matches[getParent(seedMatchIndex)].setOrange(new StarterSlot(seedList.get(teamIndex)));
                matches[seedMatchIndex] = null;
                seedMatchIndex--;
                teamIndex = teamIndex + 2;
            }
            // If there are no byes in the matchup, place the teams vs each other as intended
            else {
                matches[seedMatchIndex].setBlue(new StarterSlot(seedList.get(teamIndex)));
                teamIndex++;
                matches[seedMatchIndex].setOrange(new StarterSlot(seedList.get(teamIndex)));
                seedMatchIndex--;
                teamIndex++;
            }
        }
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

    int getParent(int i) {
        i = i + 1;
        if (i == 1) {
            return -1;
        } else {
            return Math.floorDiv(i,2)-1;
        }
    }

    int getLeftSide(int i){
        i = i + 1;
        return 2 * i;
    }

    int getRightSide(int i) {
        i = i + 1;
        return 2 * i - 1;
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

        //Will run until team size fits the count
        while(topTeams.size() < count) {
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
        while(topTeams.size() > count){
            topTeams.remove(topTeams.size()-1);
        }

        return topTeams;
    }

    @Override
    public Node getJavaFxNode(BracketOverview bracketOverview) {
        return null; //TODO
    }
}
