package dk.aau.cs.ds306e18.tournament.utility;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ConfigFileEditorTest {

    private List<String> testConfig = Arrays.asList("[Participant Configuration]", "participant_team_0 = 0", "participant_team_1 = 1");

    @Test
    public void readWriteConcurrencyTest() {
        ConfigFileEditor.setConfig(testConfig);
        ConfigFileEditor.writeConfig("testconfig.cfg");

        ConfigFileEditor.readConfig("testconfig.cfg");
        assertEquals(testConfig, ConfigFileEditor.getConfig());
    }

    @Test
    public void editLineTest() {
        ConfigFileEditor.setConfig(testConfig);

        ConfigFileEditor.editLine("participant_team_", 0, "42");
        assertEquals("participant_team_0 = 42", ConfigFileEditor.getConfig().get(1));
    }
}