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

    private static List<String> config;

    private static String removeValuePattern = "= .*$";

    public static List<String> getConfig() {
        return config;
    }

    public static void setConfig(List<String> config) {
        ConfigFileEditor.config = config;
    }

    public static void readConfig(String filename) {
        Path in = Paths.get(filename);
        try {
            config = Files.readAllLines(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeConfig(String filename) {
        Path out = Paths.get(filename);
        try {
            Files.write(out, config, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editLine(String parameter, String value) {
        for (int i = 0; i < config.size(); i++) {
            String line = config.get(i);
            if (line.startsWith(parameter)) {
                config.set(i, removeValue(line) + value);
                return;
            }
        }
    }

    public static void editLine(String parameter, int num, String value) {
        for (int i = 0; i < config.size(); i++) {
            String line = config.get(i);
            if (line.startsWith(parameter + num)) {
                config.set(i, removeValue(line) + value);
                return;
            }
        }
    }

    private static String removeValue(String line) {
        return line.replaceAll(removeValuePattern, "= ");
    }
}
