package dk.aau.cs.ds306e18.tournament.utility.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

abstract class ConfigFileEditor {

    private static boolean valid = false;
    private static ArrayList<String> config;

    private final static String REMOVE_VALUE_PATTERN = "= .*$";

    /**
     * Reads all lines from a given file and puts them in ArrayList config. Throws ISE if read config is invalid and
     * sets valid-flag if file is read.
     * @param filename the filename to read
     */
    void read(String filename) {
        Path in = Paths.get(filename);
        try {
            config = (ArrayList<String>) Files.readAllLines(in);
            validateConfigSyntax();
            if (!this.isValid()) {
                throw new IllegalStateException("Warning: RLBot config-file read: " + filename + "'s syntax is not valid!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all lines from config to a file with system-default charset. Throws ISE if config is not valid. Not
     * possible through ordinary usage of methods on config.
     * @param filename the filename to be written to
     */
    void write(String filename) {
        if (!this.isValid())
            throw new IllegalStateException("Warning: RLBot config-file to write: " + filename + "'s syntax is not valid!");
        Path out = Paths.get(filename);
        try {
            Files.write(out, config, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits value of first occurrence of line with given parameter
     * @param parameter the given parameter to edit
     * @param value     the value to edit given parameter with
     */
    void editLine(String parameter, String value) {
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
     * @param parameter the given parameter to edit
     * @param num       the number of a numbered parameter
     * @param value     the value to edit given parameter with
     */
    void editLine(String parameter, int num, String value) {
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
     * @param i index of line
     * @return line on index i
     */
    String getLine(int i) {
        return config.get(i);
    }

    /**
     * Gets first line matching parameter, if config is empty, returns null
     * @param parameter is beginning of line
     * @return first line which starts with parameter
     */
    String getLine(String parameter) {
        for (String line : config) {
            if (line.startsWith(parameter)) {
                return line;
            }
        }
        return null;
    }

    /**
     * Gets value of argument, assuming the line is split with "=". Trims whitespace before returning value. If no split
     * is found, just returns line.
     * @param line is the line to extract value from
     * @return value found, trimmed for whitespace
     */
    private String getValue(String line) {
        String[] value = line.split("=");
        return value[value.length - 1].trim();
    }

    /**
     * Composite method of getLine and getValue method, returns null, if no parameter exists
     * @param parameter the given parameter
     * @return the value at first line with parameter
     */
    String getValueOfLine(String parameter) {
        for (String line : config) {
            if (line.startsWith(parameter)) {
                return getValue(line);
            }
        }
        return null;
    }

    /**
     * Takes a parameter-line and regex-substitutes equals and everyting after with an equals and a space for easy
     * appending of value
     * @param line the given line to remove value from
     * @return the given line with value removed
     */
    private String removeValue(String line) {
        return line.replaceAll(REMOVE_VALUE_PATTERN, "= ");
    }


    /**
     * Checks loaded config for valid syntax by iterating through each line. Allows empty lines, and checks for three
     * cases; square bracketed headers, hashtag-comments, and parameters with equals-symbols
     */
    void validateConfigSyntax() {
        for (String line : config) {
            // if line is not whitespace, check syntax
            if (!(line.isEmpty())) {
                switch (line.trim().charAt(0)) {
                    case '[':
                        // if last char, without whitespace, is a closing square bracket, then header and break
                        if (line.trim().charAt(line.trim().length() - 1) == ']') break;
                        valid = false;
                        return;

                    // hashtags are comments and allowed
                    case '#':
                        break;

                    // if none of the above, must be parameter-line, check for existence of an equals-symbol
                    default:
                        if (line.contains("=")) break;
                        valid = false;
                        return;
                }
            }
        }
        valid = true;
    }

    ArrayList<String> getConfig() {
        return config;
    }

    void setConfig(ArrayList<String> config) {
        ConfigFileEditor.config = config;
        validateConfigSyntax();
    }

    boolean isValid() {
        return valid;
    }
}