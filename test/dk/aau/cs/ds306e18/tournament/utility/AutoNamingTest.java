package dk.aau.cs.ds306e18.tournament.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AutoNamingTest {

    @RunWith(Parameterized.class)
    static public class GetNameTest {
        private String result;
        private String expectedResult;

        public GetNameTest(String[] botNames, String expectedResult) {
            this.result = AutoNaming.getName(Arrays.asList(botNames));
            this.expectedResult = expectedResult;
        }

        @Parameterized.Parameters
        public static Collection parameters() {
            return Arrays.asList(new Object[][]{
                    {new String[0], "Team"},
                    {new String[]{"ReliefBot", "Beast from the East"}, "Relief-Beast"},
                    {new String[]{"Air Bud", "Skybot"}, "Air-Sky"},
                    {new String[]{"Botimus Prime", "Gas Gas Gas"}, "Botimus-Gas"},
                    {new String[]{"Self-driving car", "Diablo"}, "Self-driving-Diablo"},
                    {new String[]{"Gosling", "Self-driving car"}, "Gosling-Self-driving"},
                    {new String[]{"NV Derevo", "AdversityBot"}, "NV-Adversity"},
                    {new String[]{"Wildfire V2", "Boolean Algebra Calf"}, "Wildfire-Boolean"},
                    {new String[]{"Psyonix Allstar", "Zoomelette"}, "Allstar-Zoomelette"},
                    {new String[]{"Kamael", "Kamael"}, "Kamael"},
            });
        }

        @Test
        public void getNameTest() {
            assertEquals(expectedResult, result);
        }
    }

    @RunWith(Parameterized.class)
    static public class UniquifyTest {
        private String result;
        private String expectedResult;

        public UniquifyTest(String teamName, String[] otherTeams, String expectedResult) {
            this.result = AutoNaming.uniquify(teamName, new HashSet<>(Arrays.asList(otherTeams)));
            this.expectedResult = expectedResult;
        }

        @Parameterized.Parameters
        public static Collection parameters() {
            return Arrays.asList(new Object[][]{
                    { "TSM", new String[]{"NRG", "C9"}, "TSM"},
                    { "Team", new String[]{"Team", "AnotherTeam"}, "Team (2)"},
                    { "Team", new String[]{"Team (1)", "Team (2)"}, "Team"},
                    { "Team", new String[]{"Team", "Team (2)"}, "Team (3)"},
                    { "", new String[]{"", "Another Team"}, " (2)"},
            });
        }

        @Test
        public void uniquifyTest() {
            assertEquals(expectedResult, result);
        }
    }
}