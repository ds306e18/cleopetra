package dk.aau.cs.ds306e18.tournament.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AutoNamingTest {

    private String[] botNames;
    private String result;
    private String expectedResult;

    public AutoNamingTest(String[] botNames, String expectedResult) {
        this.botNames = botNames;
        this.result = AutoNaming.getName(botNames);
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
                { new String[] { "ReliefBot", "Beast from the East" }, "Relief-Beast" },
                { new String[] { "Air Bud", "Skybot" }, "Air-Sky" },
                { new String[] { "Botimus Prime", "Gas Gas Gas" }, "Botimus-Gas" },
                { new String[] { "Self-driving car", "Diablo" }, "Self-driving-Diablo" },
                { new String[] { "Gosling", "Self-driving car" }, "Gosling-Self-driving" },
                { new String[] { "NV Derevo", "AdversityBot" }, "NV-Adversity" },
                { new String[] { "Wildfire V2", "Boolean Algebra Calf" }, "Wildfire-Boolean" },
                { new String[] { "Psyonix Allstar", "Zoomelette" }, "Allstar-Zoomelette" },
        });
    }

    @Test
    public void getNameTest() {
        assertEquals(expectedResult, result);
    }
}