package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.format.DoubleEliminationFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}
