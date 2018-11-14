package dk.aau.cs.ds306e18.tournament.utility;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/*TODO
    num_participants = <count>
    participant_config_X = <path>
    participant_team_X = <teamIndex>
    participant_type_X = rlbot
 */

public class ConfigFileEditor {

    private List<String> config;

    private static String removeValuePattern = "= .*$";

    public void readConfig(String filename) {
        Path in = Paths.get(filename);
        try {
            this.config = Files.readAllLines(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeConfig(String filename) {
        Path out = Paths.get(filename);
        try {
            Files.write(out, this.config, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editLine(String parameter, String value) {
        for (String line : this.config) {
            if (line.startsWith(parameter)) {
                line = removeValue(line) + value;
                return;
            }
        }
    }

    public void editLine(String parameter, int num, String value) {
        for (String line : this.config) {
            if (line.startsWith(parameter + num)) {
                line = removeValue(line) + value;
                return;
            }
        }
    }

    private static String removeValue(String line) {
        return line.replaceAll(removeValuePattern, "= ");
    }
}
