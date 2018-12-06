package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;

import java.util.ArrayList;
import java.util.List;

abstract class Elimination implements Format, MatchPlayedListener {
    protected StageStatus status = StageStatus.PENDING;
    protected ArrayList<Team> seededTeams;
    protected transient Match finalMatch;
    protected Match[] upperBracketMatchesArray;
    protected int rounds;

    /**
     * Generates a single-elimination bracket structure. References between the empty matches are made by winnerOf and starterSlots.
     * Matches are accessed through finalMatch (the root).
     *
     * @param rounds the amount of rounds in the bracket
     */
    void generateUpperBracket(int rounds) {
        int matchesInCurrentRound, matchNumberInRound, matchIndex = 0;
        ArrayList<Match> bracketList = new ArrayList<>();

        // Generate the bracket from the first round to the final. All matches except those in
        // the first uses the winners of previous matches.
        for (int roundsLeft = rounds; roundsLeft > 0; roundsLeft--) {
            matchesInCurrentRound = (int) Math.pow(2, roundsLeft - 1);

            if (roundsLeft == rounds) {
                // First round, all matches are empty
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    bracketList.add(new Match());
                }
            } else {
                // Fills all the remaining matches with winners from earlier rounds.
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    Match match = new Match().setBlueToWinnerOf(bracketList.get(matchIndex)).setOrangeToWinnerOf(bracketList.get(matchIndex + 1));
                    bracketList.add(match);
                    matchIndex = matchIndex + 2;
                }
            }
        }

        // The final is the last match in the list
        finalMatch = bracketList.get(bracketList.size() - 1);
        upperBracketMatchesArray = new Match[bracketList.size()];
        upperBracketMatchesArray = finalMatch.getTreeAsListBFS().toArray(upperBracketMatchesArray);
    }

    /**
     * Seeds the single-elimination bracket with teams to give better placements.
     * If there are an insufficient amount of teams, the team(s) with the best seeding(s) will be placed in the next round instead.
     *
     * @param seededTeams a list containing the teams in the tournament
     * @param rounds      the amount of rounds in the bracket
     */
    void seedUpperBracket(List<Team> seededTeams, int rounds) {

        ArrayList<Team> seedList = new ArrayList<>(seededTeams);

        //Create needed amount of byes to match with the slots
        ArrayList<Team> byeList = new ArrayList<>();
        int amountOfByes = (int) Math.pow(2, rounds) - seedList.size();
        while (byeList.size() < amountOfByes) {
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
        int seedMatchIndex = finalMatch.getTreeAsListBFS().size() - 1;
        int teamIndex = 0, teamCount = seedList.size();
        while (teamIndex < teamCount) {
            // If the matchup would be between a team and a bye, the team will be placed at its parent match as a starterSlot
            // The match in the first round will be deleted(null)
            if (byeList.contains(seedList.get(teamIndex)) || byeList.contains(seedList.get(teamIndex + 1))) {
                int matchCheckIndex = getParent(seedMatchIndex);
                if (getLeftSide(matchCheckIndex) == seedMatchIndex) {
                    upperBracketMatchesArray[getParent(seedMatchIndex)].setBlue(seedList.get(teamIndex));
                } else upperBracketMatchesArray[getParent(seedMatchIndex)].setOrange(seedList.get(teamIndex));
                upperBracketMatchesArray[seedMatchIndex] = null;
                seedMatchIndex--;
                teamIndex = teamIndex + 2;
            }
            // If there are no byes in the matchup, place the teams vs each other as intended
            else {
                upperBracketMatchesArray[seedMatchIndex].setBlue(seedList.get(teamIndex));
                teamIndex++;
                upperBracketMatchesArray[seedMatchIndex].setOrange(seedList.get(teamIndex));
                seedMatchIndex--;
                teamIndex++;
            }
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

    public int getRounds() {
        return rounds;
    }

}
