package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.List;

public class SingleEliminationStage implements Format, MatchListener {

    private StageStatus status = StageStatus.PENDING;
    private ArrayList<Team> seededTeams;
    private Match finalMatch;
    private int rounds;

    @Override
    public void start(List<Team> seededTeams) {
        this.seededTeams = new ArrayList<>(seededTeams);
        rounds = (int) Math.ceil(Math.log(seededTeams.size()) / Math.log(2));
        generateBracket(rounds);
        seedBracket(seededTeams);
        status = StageStatus.RUNNING;
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
    }

    private void seedBracket(List<Team> seededTeams) {

        ArrayList<Team> seedList = new ArrayList<>(seededTeams);

        int slice = 1;
        int interactions = seedList.size() / 2;

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

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {
        return null; // TODO: Returns a list of the teams that performed best this stage. They should be sorted after performance, with best team first.
    }
}
