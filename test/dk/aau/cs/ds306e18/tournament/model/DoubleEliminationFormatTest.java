package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.format.DoubleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerBySeed;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DoubleEliminationFormatTest {

    @Test
    public void testConstructor() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(8,1));
    }

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest01(){
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateTeams(8,1));
        assertEquals(14, bracket.getAllMatches().size());
    }

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest02() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateTeams(16,1));
        assertEquals(30, bracket.getAllMatches().size());
    }

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest03() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateTeams(32,1));
        assertEquals(62, bracket.getAllMatches().size());
    }

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest04() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(9,1));
        assertEquals(16, bracket.getAllMatches().size());
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch01() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateTeams(8,1));
        List<Match> matches = bracket.getAllMatches();
        for(int n = 0; n < matches.size()-1; n++) {
            assertTrue(matches.get(matches.size()-1).dependsOn(matches.get(n)));
        }
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch02() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateTeams(16,1));
        List<Match> matches = bracket.getAllMatches();
        for(int n = 0; n < matches.size()-1; n++) {
            assertTrue(matches.get(matches.size()-1).dependsOn(matches.get(n)));
        }
    }

    @Test
    public void dependsOnFinalMatch03() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateTeams(13,1));
        List<Match> matches = bracket.getAllMatches();
        for(int n = 0; n < matches.size()-1; n++) {
            assertTrue(matches.get(matches.size()-1).dependsOn(matches.get(n)));
        }
    }

    @Test
    public void pendingMatchesTest01(){
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(8, 1));
        assertEquals(10, bracket.getPendingMatches().size());
        bracket.getUpperBracketMatches().get(6).setScores(2,0,true);
        bracket.getUpperBracketMatches().get(5).setScores(0,2,true);
        assertEquals(8, bracket.getPendingMatches().size());
    }

    @Test
    public void pendingMatchesTest02(){
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(16, 1));
        assertEquals(22, bracket.getPendingMatches().size());
        bracket.getUpperBracketMatches().get(14).setScores(2,0,true);
        bracket.getUpperBracketMatches().get(13).setScores(0,2,true);
        bracket.getUpperBracketMatches().get(12).setScores(2,0,true);
        bracket.getUpperBracketMatches().get(11).setScores(0,2,true);
        bracket.getLowerBracketMatches().get(0).setScores(2,0,true);
        bracket.getLowerBracketMatches().get(1).setScores(2,0,true);
        assertEquals(18, bracket.getPendingMatches().size());
    }

    @Test
    public void completedMatchesTest01() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(16, 1));
        assertEquals(0, bracket.getCompletedMatches().size());
        bracket.getUpperBracketMatches().get(14).setScores(2,0,true);
        bracket.getUpperBracketMatches().get(13).setScores(0,2,true);
        bracket.getUpperBracketMatches().get(12).setScores(2,0,true);
        bracket.getUpperBracketMatches().get(11).setScores(0,2,true);
        bracket.getLowerBracketMatches().get(0).setScores(2,0,true);
        bracket.getLowerBracketMatches().get(1).setScores(2,0,true);
        assertEquals(6, bracket.getCompletedMatches().size());
    }

    @Test
    public void completedMatchesTest02() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(8, 1));
        for(int i = bracket.getUpperBracketMatches().size()-1; i >= 0; i--){
            bracket.getUpperBracketMatches().get(i).setScores(2,0,true);
        }
        for(int i = 0; i <= bracket.getLowerBracketMatches().size()-1; i++){
            bracket.getLowerBracketMatches().get(i).setScores(2,0,true);
        }
        assertEquals(13, bracket.getCompletedMatches().size());
        bracket.getAllMatches().get(bracket.getAllMatches().size()-1).setScores(2,0,true);
        assertEquals(14, bracket.getCompletedMatches().size());
    }

    @Test
    public void upcomingMatchesTest01() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(8, 1));
        assertEquals(4, bracket.getUpcomingMatches().size());
        bracket.getUpperBracketMatches().get(6).setScores(1,0,true);
        bracket.getUpperBracketMatches().get(5).setScores(3,0,true);
        assertEquals(4, bracket.getUpcomingMatches().size());
        bracket.getLowerBracketMatches().get(0).setScores(0,4,true);
        assertEquals(3, bracket.getUpcomingMatches().size());
    }

    @Test
    public void upcomingMatchesTest02() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(16, 1));
        assertEquals(8, bracket.getUpcomingMatches().size());
        bracket.getUpperBracketMatches().get(14).setScores(1,0,true);
        bracket.getUpperBracketMatches().get(13).setScores(3,0,true);
        assertEquals(8, bracket.getUpcomingMatches().size());
        bracket.getLowerBracketMatches().get(0).setScores(0,4,true);
        assertEquals(7, bracket.getUpcomingMatches().size());
    }

    @Test
    public void topTeamsTest01() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(8, 1));
        for(int i = bracket.getUpperBracketMatches().size()-1; i >= 0; i--){
            bracket.getUpperBracketMatches().get(i).setScores(2,0,true);
        }
        for(int i = 0; i < bracket.getLowerBracketMatches().size(); i++){
            bracket.getLowerBracketMatches().get(i).setScores(2,0,true);
        }
        bracket.getAllMatches().get(bracket.getAllMatches().size()-1).setScores(2,0,true);
        assertEquals(3,bracket.getTopTeams(3, new TieBreakerBySeed()).size());
        assertEquals(6,bracket.getTopTeams(6, new TieBreakerBySeed()).size());
    }

    @Test
    public void topTeamsTest02() {
        DoubleEliminationFormat bracket = new DoubleEliminationFormat();
        bracket.start(TestUtilities.generateSeededTeams(12,1));
        for(int i = bracket.getUpperBracketMatches().size()-1; i >= 0; i--){
            bracket.getUpperBracketMatches().get(i).setScores(2,0,true);
        }
        for(int i = 0; i < bracket.getLowerBracketMatches().size(); i++){
            bracket.getLowerBracketMatches().get(i).setScores(2,0,true);
        }
        bracket.getAllMatches().get(bracket.getAllMatches().size()-1).setScores(2,0,true);
        assertEquals(5, bracket.getTopTeams(5, new TieBreakerBySeed()).size());
    }

}
