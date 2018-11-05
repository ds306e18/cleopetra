package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchListener;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.ui.tabs.BracketOverview;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class DoubleEliminationFormat extends Elimination implements MatchListener {
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
        status = StageStatus.RUNNING;
        finalMatch.registerListener(this);
    }

    @Override
    public StageStatus getStatus() {
        return null;
    }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {
        return null;
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
        return null;
    }

    @Override
    public List<Match> getPendingMatches() {
        return null;
    }

    @Override
    public List<Match> getCompletedMatches() {
        return null;
    }

    @Override
    public Node getJavaFxNode(BracketOverview bracketOverview) {
        return null;
    }

    @Override
    public void onMatchPlayed(Match match) {

    }

    public void generateLowerBracket(int roundsInUpperBracket) {
        int matchesInCurrentRound = (int) Math.pow(2, roundsInUpperBracket-2), matchNumberInRound, winnerOfMatchIndex = 0;
        upperBracketMatches = finalMatch.getTreeAsListBFS();
        int loserOfMatchIndex = upperBracketMatches.size()-1;
        int roundCount = 1;
        int rounds = roundsInUpperBracket;
        rounds = rounds + rounds - 2;

        // Generate the bracket from the first round to the final. All upperBracketMatches except those in
        // the first uses the winners of previous upperBracketMatches.
        for (int roundsLeft = rounds; roundsLeft > 0; roundsLeft--) {

            if (roundsLeft == rounds) {
                // First round, fill the round with loserOfs matches from the upperBrackets first round
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    lowerBracketMatches.add(new Match().setBlueToLoserOf(upperBracketMatches.get(loserOfMatchIndex)).
                            setOrangeToLoserOf(upperBracketMatches.get(loserOfMatchIndex-1)));
                    loserOfMatchIndex = loserOfMatchIndex - 2;
                }
            }
            else {
                // Fills all the remaining upperBracketMatches with winners from earlier rounds.
                for (matchNumberInRound = 1; matchNumberInRound <= matchesInCurrentRound; matchNumberInRound++) {
                    if(roundCount % 2 == 1) {
                        lowerBracketMatches.add(new Match().setBlueToWinnerOf(lowerBracketMatches.get(winnerOfMatchIndex)).
                                setOrangeToWinnerOf(lowerBracketMatches.get(winnerOfMatchIndex + 1)));
                        winnerOfMatchIndex = winnerOfMatchIndex + 2;
                    }
                    if(roundCount % 2 == 0) {
                        lowerBracketMatches.add(new Match().setBlueToWinnerOf(lowerBracketMatches.get(winnerOfMatchIndex)).
                                setOrangeToLoserOf(upperBracketMatches.get(loserOfMatchIndex)));
                        winnerOfMatchIndex++;
                        loserOfMatchIndex--;
                    }
                }
            }

            if (roundCount % 2 == 0) { matchesInCurrentRound /= 2; }
            roundCount++;
        }

        // The final is the last match in the list
        Match tempMatch = new Match().setBlueToWinnerOf(finalMatch).setOrangeToWinnerOf(lowerBracketMatches.get(lowerBracketMatches.size()-1));
        finalMatch = tempMatch;

        lowerBracketMatchesArray = new Match[lowerBracketMatches.size()];
        lowerBracketMatchesArray = lowerBracketMatches.toArray(lowerBracketMatchesArray);
    }
}
