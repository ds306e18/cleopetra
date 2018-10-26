package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SingleEliminationStageTest {

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest01(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(8,1));
        assertEquals(7, bracket.getAllMatches().size());
    }

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest02() {
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(16,1));
        assertEquals(15, bracket.getAllMatches().size());
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch01() {
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(8,1));
        List<Match> matches = bracket.getAllMatches();
        for(int n = 1; n < matches.size(); n++) {
            assertTrue(matches.get(0).dependsOn(matches.get(n)));
        }
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch02() {
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(16,1));
        List<Match> matches = bracket.getAllMatches();
        for(int n = 1; n < matches.size(); n++) {
            assertTrue(matches.get(0).dependsOn(matches.get(n)));
        }
    }

    //The teams should be seeded correctly in first round
    @Test
    public void seedTest01(){
        ArrayList<Team> teamList = TestUtilities.generateSeededTeams(8,1);
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(teamList);
        assertEquals(1, bracket.getAllMatches().get(bracket.getAllMatches().size()-1).getBlueTeam().getInitialSeedValue());
        assertEquals(8, bracket.getAllMatches().get(bracket.getAllMatches().size()-1).getOrangeTeam().getInitialSeedValue());
        assertEquals(4, bracket.getAllMatches().get(bracket.getAllMatches().size()-2).getBlueTeam().getInitialSeedValue());
        assertEquals(5, bracket.getAllMatches().get(bracket.getAllMatches().size()-2).getOrangeTeam().getInitialSeedValue());
        assertEquals(2, bracket.getAllMatches().get(bracket.getAllMatches().size()-3).getBlueTeam().getInitialSeedValue());
        assertEquals(7, bracket.getAllMatches().get(bracket.getAllMatches().size()-3).getOrangeTeam().getInitialSeedValue());
        assertEquals(3, bracket.getAllMatches().get(bracket.getAllMatches().size()-4).getBlueTeam().getInitialSeedValue());
        assertEquals(6, bracket.getAllMatches().get(bracket.getAllMatches().size()-4).getOrangeTeam().getInitialSeedValue());
    }

    //first match should be null, and best seeded team should be placed in next round
    @Test
    public void seedTest02(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateSeededTeams(7,1));
        assertNull(bracket.getMatchesAsArray()[6]);
        assertEquals(1,bracket.getMatchesAsArray()[bracket.getParent(6)].getBlueTeam().getInitialSeedValue());
    }

    //match 3 should be null and snd seed should be placed in next round
    @Test
    public void seedTest03(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateSeededTeams(6,1));
        assertNull(bracket.getMatchesAsArray()[4]);
        assertEquals(2,bracket.getMatchesAsArray()[bracket.getParent(4)].getBlueTeam().getInitialSeedValue());
    }

    //There should only be one match in first around, this should be the worst seeded teams.
    //The winner of this match should meet seed 1
    @Test
    public void seedTest04(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateSeededTeams(5,1));
        assertNull(bracket.getMatchesAsArray()[6]);
        assertNull(bracket.getMatchesAsArray()[4]);
        assertNull(bracket.getMatchesAsArray()[3]);
        assertEquals(4,bracket.getMatchesAsArray()[5].getBlueTeam().getInitialSeedValue());
        assertEquals(5,bracket.getMatchesAsArray()[5].getOrangeTeam().getInitialSeedValue());
    }

    //Should return the correct amount of playable matches
    @Test
    public void upcomingMatchesTest01(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(8,1));
        assertEquals(4, bracket.getUpcomingMatches().size());
        bracket.getUpcomingMatches().get(0).setHasBeenPlayed(true);
        assertEquals(3, bracket.getUpcomingMatches().size());
    }

    //Should return the correct amount of playable matches
    @Test
    public void upcomingMatchesTest02(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(6,1));
        assertEquals(2, bracket.getUpcomingMatches().size());
    }

    //Should return the correct amount of not-playable upcoming matches
    @Test
    public void pendingMatchesTest01(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(8,1));
        assertEquals(3, bracket.getPendingMatches().size());
        bracket.getAllMatches().get(bracket.getAllMatches().size()-1).setHasBeenPlayed(true);
        bracket.getAllMatches().get(bracket.getAllMatches().size()-2).setHasBeenPlayed(true);
        assertEquals(2, bracket.getPendingMatches().size());
    }

    //Should return the correct amount of played matches
    @Test
    public void completedMatchesTest01(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(8,1));
        assertEquals(0, bracket.getCompletedMatches().size());
        bracket.getAllMatches().get(bracket.getAllMatches().size()-1).setHasBeenPlayed(true);
        bracket.getAllMatches().get(bracket.getAllMatches().size()-2).setHasBeenPlayed(true);
        assertEquals(2, bracket.getCompletedMatches().size());
    }

    @Test
    public void getTopTeamsTest01(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateSeededTeams(8,1));
        for(int matchIndex = 6; matchIndex >= 0; matchIndex--){
            if(bracket.getAllMatches().get(matchIndex).getBlueTeam().getInitialSeedValue() < bracket.getAllMatches().get(matchIndex).getOrangeTeam().getInitialSeedValue()) {
                bracket.getAllMatches().get(matchIndex).setBlueScore(1);
                bracket.getAllMatches().get(matchIndex).setHasBeenPlayed(true);
            }
            else{
                bracket.getAllMatches().get(matchIndex).setOrangeScore(1);
                bracket.getAllMatches().get(matchIndex).setHasBeenPlayed(true);
            }
        }
        bracket.getTopTeams(4, new TieBreakerBySeed());
    }
}