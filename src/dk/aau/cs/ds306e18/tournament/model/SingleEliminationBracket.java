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

        // The iterator wil generate the bracket from the fst round to the final, only with StarterSlots.
        for (int roundsLeft = rounds; roundsLeft > 0; roundsLeft--) {
            matchesInCurrentRound = (int) Math.pow(2, roundsLeft);

            //Fst round, fills matches with StarterSlots
            if (roundsLeft == rounds) {
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    bracketList.add(new Match(new StarterSlot(null), new StarterSlot(null)));
                    matchNumberInRound++;
                }
            }

            //Fills all the remaining matches with winnerOf from earlier rounds.
            if (roundsLeft > 1 && roundsLeft <= rounds - 1) {
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    bracketList.add(new Match(new WinnerOf(bracketList.get(matchIndex)), new WinnerOf(bracketList.get(matchIndex + 1))));
                    matchIndex = matchIndex + 2;
                }
            }

            //the final will be a match between the winners of the semi-final.
            else {
                finalMatch = new Match(new WinnerOf(bracketList.get(matchIndex)), new WinnerOf(bracketList.get(matchIndex)));
            }
        }
    }

    public ArrayList<Team> seedBracket(ArrayList<Team> seedList) {

        //Sorts teams compared to seeding
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
