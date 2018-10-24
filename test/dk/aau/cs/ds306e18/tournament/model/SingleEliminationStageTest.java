package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SingleEliminationStageTest {


    //Error when running bracket.getAllMatches().size()
    @Test
    public void amountOfMatchesTest01(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(8,1));
        assertEquals(7, bracket.getAllMatches().size());
    }

    @Test
    public void amountOfMatchesTest02() {
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(16,1));
        assertEquals(15, bracket.getAllMatches().size());
    }

    @Test
    public void dependsOnFinalMatch01() {
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(8,1));
        ArrayList<Match> matches = bracket.getAllMatches();
        for(int n = 1; n < matches.size(); n++) {
            assertTrue(matches.get(0).dependsOn(matches.get(n)));
        }
    }

    @Test
    public void dependsOnFinalMatch02() {
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(16,1));
        ArrayList<Match> matches = bracket.getAllMatches();
        for(int n = 1; n < matches.size(); n++) {
            assertTrue(matches.get(0).dependsOn(matches.get(n)));
        }
    }

    @Test
    public void seedTest01(){
        ArrayList<Team> teamList = TestUtilities.generateTeams(8,1);
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(teamList);
    }

    @Test
    public void seedTest02(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(16,1));
    }

    @Test
    public void seedTest03(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(6,1));
    }

    @Test
    public void seedTest04(){
        SingleEliminationStage bracket = new SingleEliminationStage();
        bracket.start(TestUtilities.generateTeams(19,1));
    }

}