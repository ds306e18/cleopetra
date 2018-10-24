package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.List;

public class SingleEliminationStage implements Stage {

    private String name = "Single Elimination";
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public StageStatus getStatus() {
        return status;
    }

    @Override
    public ArrayList<Match> getAllMatches() {
        return finalMatch.getTreeAsListBFS();
    }

    @Override
    public ArrayList<Match> getUpcomingMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getPendingMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getCompletedMatches() {
        return null;
    }

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

    private void seedBracket(List<Team> seededTeams, int rounds) {

        ArrayList<Team> seedList = new ArrayList<>(seededTeams);
        ArrayList<Team> byeList = new ArrayList<>();
        int amountOfByes = (int) Math.pow(2, rounds) - seedList.size();
        while(byeList.size() < amountOfByes) {
            byeList.add(new Team("bye", null, 999, ""));
        }
        seedList.addAll(byeList);

        int slice = 1;
        int interactions = seedList.size() / 2;

        //TODO allow the seeds to sort with every number
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

        // TODO: Use the seedList to something
        int seedMatchIndex = finalMatch.getTreeAsListBFS().size()-1;
        for(int playerIndex = 0; playerIndex < seedList.size();){
            if(byeList.contains(seedList.get(playerIndex)) || byeList.contains(seedList.get(playerIndex+1))) {
                matches[getParent(seedMatchIndex)].setBlue(new StarterSlot(seedList.get(playerIndex)));
                matches[seedMatchIndex] = null;
                seedMatchIndex--;
                playerIndex = playerIndex + 2;
            }
            else {
                matches[seedMatchIndex].setBlue(new StarterSlot(seedList.get(playerIndex)));
                playerIndex++;
                matches[seedMatchIndex].setOrange(new StarterSlot(seedList.get(seedMatchIndex)));
                seedMatchIndex--;
                playerIndex++;
            }
        }
    }

    void onMatchPlayed(Match match) {
        if (finalMatch.hasBeenPlayed()) {
            status = StageStatus.CONCLUDED;
        } else {
            status = StageStatus.RUNNING;
        }
    }

    private int getParent(int i) {
        i = i + 1;
        if (i == 1) {
            return -1;
        } else {
            return Math.floorDiv(i,2)-1;
        }
    }

    public Match getLeftSide(int i){
        i = i + 1;
        return matches[2*i];
    }

    public Match getRightSide(int i){
        i = 1 + 1;
        return matches[2*i-1];
    }
}
