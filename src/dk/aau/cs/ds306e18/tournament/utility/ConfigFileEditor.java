package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigFileEditor {

    private static boolean valid;
    private static ArrayList<String> config;

    private final static String REMOVE_VALUE_PATTERN = "= .*$";

    private final static String PARAMETER_PARTICIPANT_CONFIG = "participant_config_";
    private final static String PARAMETER_PARTICIPANT_TEAM = "participant_team_";
    private final static String PARAMETER_PARTICIPANT_TYPE = "participant_type_";
    private final static String PARAMETER_PARTICIPANT_NUM = "num_participant";

    private final static String PARAMETER_BLUE_TEAM = "0";
    private final static String PARAMETER_ORANGE_TEAM = "1";
    private final static String PARAMETER_BOT_TYPE = "rlbot";

    /**
     * Reads all lines from a given file and puts them in ArrayList config
     *
     * @param filename the filename to read
     */
    static void readConfig(String filename) {
        Path in = Paths.get(filename);
        try {
            config = (ArrayList<String>) Files.readAllLines(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all lines from config to a file with system-default charset
     *
     * @param filename the filename to write to
     */
    public static void writeConfig(String filename) {
        Path out = Paths.get(filename);
        try {
            Files.write(out, config, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits value of first occurrence of line with given parameter
     *
     * @param parameter the given parameter to edit
     * @param value     the value to edit given parameter with
     */
    private static void editLine(String parameter, String value) {
        for (int i = 0; i < config.size(); i++) {
            String line = config.get(i);
            if (line.startsWith(parameter)) {
                config.set(i, removeValue(line) + value);
                return;
            }
        }
    }

    /**
     * Edits value of first occurrence of line with given numbered parameter
     *
     * @param parameter the given parameter to edit
     * @param num       the number of a numbered parameter
     * @param value     the value to edit given parameter with
     */
    static void editLine(String parameter, int num, String value) {
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
    static String getLine(int i) {
        return config.get(i);
    }

    /**
     * Gets first line matching parameter, if config is empty, returns null
     *
     * @param parameter is beginning of line
     * @return first line which starts with parameter
     */
    static String getLine(String parameter) {
        for (String line : config) {
            if (line.startsWith(parameter)) {
                return line;
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
    private static String getValue(String line) {
        String[] value = line.split("=");
        return value[value.length - 1].trim();
    }

    /**
     * Composite method of getLine and getValue method, returns null, if no parameter exists
     *
     * @param parameter the given parameter
     * @return the value at first line with parameter
     */
    static String getValueOfLine(String parameter) {
        for (String line : config) {
            if (line.startsWith(parameter)) {
                return getValue(line);
            }
        }
        return null;
    }

    /**
     * Takes a parameter-line and regex-substitutes equals and everyting after with an equals and a space for easy appending of value
     *
     * @param line the given line to remove value from
     * @return the given line with value removed
     */
    private static String removeValue(String line) {
        return line.replaceAll(REMOVE_VALUE_PATTERN, "= ");
    }

    /**
     * Configures the config based on the state of a given Match
     *
     * @param match the match to configure the config for
     */
    public static void configureMatch(Match match) {
        int numParticipantsBlue = 0;
        int numParticipantsOrange = 0;

        for (Bot bot : match.getBlueTeam().getBots()) {
            editLine(PARAMETER_PARTICIPANT_CONFIG, numParticipantsBlue, bot.getConfigPath());
            editLine(PARAMETER_PARTICIPANT_TEAM, numParticipantsBlue, PARAMETER_BLUE_TEAM);
            editLine(PARAMETER_PARTICIPANT_TYPE, numParticipantsBlue, PARAMETER_BOT_TYPE);
            numParticipantsBlue++;
        }

        for (Bot bot : match.getOrangeTeam().getBots()) {
            editLine(PARAMETER_PARTICIPANT_CONFIG, numParticipantsBlue + numParticipantsOrange, bot.getConfigPath());
            editLine(PARAMETER_PARTICIPANT_TEAM, numParticipantsBlue + numParticipantsOrange, PARAMETER_ORANGE_TEAM);
            editLine(PARAMETER_PARTICIPANT_TYPE, numParticipantsBlue + numParticipantsOrange, PARAMETER_BOT_TYPE);
            numParticipantsOrange++;
        }

        editLine(PARAMETER_PARTICIPANT_NUM, Integer.toString(numParticipantsBlue + numParticipantsOrange));
    }

    public static boolean validateConfig() {
        //TODO
        return false;
    }

    static ArrayList<String> getConfig() {
        return config;
    }

    static void setConfig(ArrayList<String> config) {
        ConfigFileEditor.config = config;
    }
}