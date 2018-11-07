package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
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
            byeList.add(new Team("bye", null, 999, ""));
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
        int playerIndex = 0, playerCount = seedList.size();
        while(playerIndex < playerCount){
            // If the player matchup would be between a team and a bye, the team will be placed at its parent match as a startSlot
            // The match in the first round will be deleted(null)
            if(byeList.contains(seedList.get(playerIndex)) || byeList.contains(seedList.get(playerIndex+1))) {
                matches[getParent(seedMatchIndex)].setBlue(new StarterSlot(seedList.get(playerIndex)));
                matches[seedMatchIndex] = null;
                seedMatchIndex--;
                playerIndex = playerIndex + 2;
            }
            // If there are no byes in the matchup, place the teams vs each other as intended
            else {
                matches[seedMatchIndex].setBlue(new StarterSlot(seedList.get(playerIndex)));
                playerIndex++;
                matches[seedMatchIndex].setOrange(new StarterSlot(seedList.get(playerIndex)));
                seedMatchIndex--;
                playerIndex++;
            }
        }
    }

    @Override
    public void onMatchPlayed(Match match) {
        // TODO: Register stage as listener to all relevant matches
        // TODO: Add tests
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

    Match getLeftSide(int i){
        i = i + 1;
        return matches[2*i];
    }

    Match getRightSide(int i) {
        i = 1 + 1;
        return matches[2 * i - 1];
    }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {
        return null; // TODO: Returns a list of the teams that performed best this stage. They should be sorted after performance, with best team first.
    }

    @Override
    public Node getJavaFxNode(BracketOverviewTabController bracketOverview) {
        return null; //TODO
    }
}
