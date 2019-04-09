package dk.aau.cs.ds306e18.tournament.utility.configuration;

import org.ini4j.Wini;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for editing configuration files of the INI-format. Does not support multiline values.
 */
abstract class ConfigFileEditor {

    boolean valid = false;
    Wini config = new Wini();

    /**
     * Reads all lines from a given file and puts them in ArrayList config. Throws ISE if read config is invalid and
     * sets valid-flag if file is read.
     *
     * @param filename the filename to read
     */
    protected void read(String filename) {
        Path in = Paths.get(filename);
        try {
            config.load(Files.newInputStream(in));
            validateConfigSyntax();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all lines from config to a file with system-default charset. Throws ISE if config is not valid. Not
     * possible through ordinary usage of methods on config.
     *
     * @param filename the filename to be written to
     */
    void write(String filename) {
        if (!this.isValid())
            throw new IllegalStateException("Warning: RLBot config-file to write: " + filename + "'s syntax is not valid!");
        Path out = Paths.get(filename);
        try {
            config.store(Files.newOutputStream(out));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits value of first occurrence of line with given parameter
     *
     * @param section the section of the given parameter
     * @param key     the given parameter to edit
     * @param value   the value to edit given parameter with
     */
    void editLine(String section, String key, String value) {
        config.get(section).replace(key, value);
    }

    /**
     * Composite method of getLine and getValue method, returns null, if no parameter exists
     *
     * @param key the given parameter
     * @return the value at first line with parameter
     */
    String getValueOfLine(String section, String key) {
        if (config.get(section) != null) {
            return config.get(section).get(key);
        }
        return null;
    }

    /**
     * Checks loaded config for valid syntax by iterating through each line. Allows empty lines, and checks for three
     * cases; square bracketed headers, hashtag-comments, and parameters with equals-symbols
     */
    abstract void validateConfigSyntax();

    public boolean isValid() {
        return valid;
    }

    public Wini getConfig() {
        return config;
    }
}