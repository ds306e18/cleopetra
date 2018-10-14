package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SingleEliminationBracket implements Bracket {

    private Match finalMatch;

    public SingleEliminationBracket(List<Team> teams) {
        int rounds = (int) Math.ceil(Math.log(teams.size()) / Math.log(2));
        generateBracket(rounds);

    }

    @Override
    public ArrayList<Match> getAllMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getUpcommingMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getUnplayableMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getCompletedMatches() {
        return null;
    }

    public void generateBracket(int rounds) {
        int matchesInCurrentRound, matchNumberInRound, matchIndex = 0;
        ArrayList<Match> bracketList = new ArrayList<>();

        // Generate the bracket from the first round to the final. All matches except those in
        // the first uses the winners of previous matches.
        for (int roundsLeft = rounds; roundsLeft > 0; roundsLeft--) {
            matchesInCurrentRound = (int) Math.pow(2, roundsLeft);

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
    }

    public ArrayList<Team> seedBracket(ArrayList<Team> seedList) {

        // Sorts teams compared to seeding
        seedList.sort(new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return Integer.compare(o1.getSeedValue(), o2.getSeedValue());
            }
        });

        ArrayList<Team> temp = new ArrayList<>();

        int slice = 1;
        int interactions = seedList.size() / 2;

        while (slice < interactions) {
            temp = (ArrayList<Team>) seedList.clone();
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

        return seedList;
    }

}
