package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/*TODO
    num_participants = <count>
    participant_config_X = <path>
    participant_team_X = <teamIndex>
    participant_type_X = rlbot
 */

public class ConfigFileEditor {

    private static ArrayList<String> config;

    private static String removeValuePattern = "= .*$";

    public static ArrayList<String> getConfig() {
        return config;
    }

    public static void setConfig(ArrayList<String> config) {
        ConfigFileEditor.config = config;
    }

    public static void readConfig(String filename) {
        Path in = Paths.get(filename);
        try {
            config = (ArrayList<String>) Files.readAllLines(in);
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

    /**
     * Gets line by index
     *
     * @param i index of line
     * @return line on index i
     */
    public static String getLine(int i) {
        return config.get(i);
    }

    /**
     * Gets first line matching parameter, if config is empty, returns null
     *
     * @param parameter is beginning of line
     * @return first line which starts with parameter
     */
    public static String getLine(String parameter) {
        for (int i = 0; i < config.size(); i++) {
            if (config.get(i).startsWith(parameter)) {
                return config.get(i);
            }
        }
        return null;
    }


    /**
     * Gets value of argument, assuming the line is split with "=". Trims whitespace before returning value. If no split is found, just returns line.
     *
     * @param line is the line to extract value from
     * @return value found, trimmed for whitespace
     */
    public static String getValue(String line) {
        String[] value = line.split("=");
        return value[value.length - 1].trim();
    }

    private static String removeValue(String line) {
        return line.replaceAll(removeValuePattern, "= ");
    }

    public static void configureMatch(Match match) {
        int numParticipantsBlue = 0;
        int numParticipantsOrange = 0;

        for (Bot bot : match.getBlueTeam().getBots()) {
            editLine("participant_config_", numParticipantsBlue, bot.getConfigPath());
            editLine("participant_team_", numParticipantsBlue, "0");
            editLine("participant_type_", numParticipantsBlue, "rlbot");
            numParticipantsBlue++;
        }

        for (Bot bot : match.getOrangeTeam().getBots()) {
            editLine("participant_config_", numParticipantsBlue + numParticipantsOrange, bot.getConfigPath());
            editLine("participant_team_", numParticipantsBlue + numParticipantsOrange, "1");
            editLine("participant_type_", numParticipantsBlue + numParticipantsOrange, "rlbot");
            numParticipantsOrange++;
        }

        editLine("num_participant", Integer.toString(numParticipantsBlue + numParticipantsOrange));
    }
}