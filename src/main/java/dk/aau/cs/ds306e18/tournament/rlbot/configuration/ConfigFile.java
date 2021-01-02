package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstraction of a '.cfg'-file.
 * Can parse files like this: https://docs.python.org/3/library/configparser.html#supported-ini-file-structure
 * except indent sections and keys.
 * Inspired by: https://stackoverflow.com/a/15638381
 */
public class ConfigFile {

    // Matches section headers like "[Details]"
    private final Pattern sectionPattern = Pattern.compile("\\[([^\\[\\]]*)\\]\\s*");
    // Matches key-value pairs "key = value", the delimiter can be both '=' or ':' and the value can be omitted for null
    private final Pattern keyValuePattern = Pattern.compile("([^=:]*)(=|:)(.*)");
    // Matches subsequent lines of multi-line values. These always start with indentation
    // It uses "[^\S\r\n]+" as indent pattern to not consume empty lines
    private final Pattern multiLineValuePattern = Pattern.compile("[^\\S\\r\\n]+(.*)");
    // Matches empty lines and comments which start with '#' or ';'
    private final Pattern commentAndEmptyPattern = Pattern.compile("\\s*[#;].*|\\s*");

    private Map<String, Map<String, String>> entries = new HashMap<>();

    /**
     * Create empty ConfigFile.
     */
    public ConfigFile() {

    }

    /**
     * Create ConfigFile based on a the entries of a config file.
     * @param file the file to parse.
     * @throws IOException
     */
    public ConfigFile(File file) throws IOException {
        load(file);
    }

    /**
     * Fills this ConfigFile with the entries from the given config file.
     * @param file the config file to parse.
     * @throws IOException
     */
    public void load(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int lineNumber = 0;
            String line;
            Map<String, String> currentSectionMap = null;
            String key = null;
            StringBuilder value = null;
            Matcher m;
            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Parse potential multiline value (next line has indentation)
                if (key != null) {
                    m = multiLineValuePattern.matcher(line);
                    if (m.matches()) {
                        value.append(" ").append(m.group(1).trim());
                        continue;
                    } else {
                        // No more lines of value. Store key value pair
                        currentSectionMap.put(key, value.toString().trim());
                        key = null;
                        value = null;
                    }
                }

                // Ignore comments and empty lines
                m = commentAndEmptyPattern.matcher(line);
                if (m.matches()) {
                    continue;
                }

                // Section
                m = sectionPattern.matcher(line);
                if (m.matches()) {
                    String section = m.group(1).trim();
                    currentSectionMap = entries.computeIfAbsent(section, k -> new HashMap<>());
                    continue;

                }

                if (currentSectionMap != null) {
                    // Key value pair
                    m = keyValuePattern.matcher(line);
                    if (m.matches()) {
                        key = m.group(1).trim();
                        value = new StringBuilder(m.group(3).trim());

                        // key value pair is stored if next line shows that it is not a multiline value
                        continue;
                    }
                }

                throw new IOException("Incorrect format in config file (error at line " + lineNumber + ": " + file.toString() + ").");
            }

            // Store last key value pair (happens when file does not end with a newline character)
            if (key != null) {
                currentSectionMap.put(key, value.toString().trim());
            }
        }
    }

    /**
     * Write this config to the given file.
     * @param file
     */
    public void write(File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            Set<String> sections = entries.keySet();
            for (String section : sections) {
                // Section name
                bw.write("[");
                bw.write(section);
                bw.write("]");
                bw.newLine();

                Map<String, String> keyValuePairs = entries.get(section);
                Set<String> keys = keyValuePairs.keySet();
                for (String key : keys) {
                    // Key value pair
                    bw.write(key);
                    bw.write(" = ");
                    bw.write(keyValuePairs.get(key));
                    bw.newLine();
                }

                bw.newLine();
            }
        }
    }

    /**
     * @return true if the given section exists.
     */
    public boolean hasSection(String section) {
        Map<String, String> sectionMap = entries.get(section);
        return sectionMap != null;
    }

    /**
     * Creates a section if it does not exist.
     * @param section the name of the section
     */
    public void createSection(String section) {
        entries.computeIfAbsent(section, k -> new HashMap<>());
    }

    /**
     * @return true if the value exists.
     */
    public boolean hasValue(String section, String key) {
        Map<String, String> sectionMap = entries.get(section);
        if (sectionMap == null) return false;
        return sectionMap.containsKey(key);
    }

    /**
     * Sets the given entry to the given value.
     * @param section the name of the section
     * @param key the name of the key
     * @param value the value
     */
    public void set(String section, String key, String value) {
        Map<String, String> sectionMap = entries.computeIfAbsent(section, k -> new HashMap<>());
        sectionMap.put(key, value);
    }

    /**
     * Sets the given entry to the given value.
     * @param section the name of the section
     * @param key the name of the key
     * @param value the value
     */
    public void set(String section, String key, int value) {
        set(section, key, String.valueOf(value));
    }

    /**
     * Sets the given entry to the given value.
     * @param section the name of the section
     * @param key the name of the key
     * @param value the value
     */
    public void set(String section, String key, float value) {
        set(section, key, String.valueOf(value));
    }

    /**
     * Sets the given entry to the given value.
     * @param section the name of the section
     * @param key the name of the key
     * @param value the value
     */
    public void set(String section, String key, double value) {
        set(section, key, String.valueOf(value));
    }

    /**
     * Sets the given entry to the given value.
     * @param section the name of the section
     * @param key the name of the key
     * @param value the value
     */
    public void set(String section, String key, boolean value) {
        set(section, key, String.valueOf(value));
    }

    /**
     * Returns the requested value as a string. If the entry does not exist, {@code defaultValue} is return instead.
     * @param section the name of the section
     * @param key the name of the key
     * @param defaultValue alternative return value
     * @return the requested value or {@code defaultValue} if the entry does not exist.
     */
    public String getString(String section, String key, String defaultValue) {
        Map<String, String> sectionMap = entries.get(section);
        if (sectionMap == null) return defaultValue;
        String value = sectionMap.get(key);
        if (value == null) return defaultValue;
        return value;
    }

    /**
     * Returns the requested value as an integer. If the entry does not exist, {@code defaultValue} is return instead.
     * @param section the name of the section
     * @param key the name of the key
     * @param defaultValue alternative return value
     * @return the requested value or {@code defaultValue} if the entry does not exist.
     */
    public int getInt(String section, String key, int defaultValue) {
        String value = getString(section, key, null);
        if (value == null) return defaultValue;
        return Integer.parseInt(value);
    }

    /**
     * Returns the requested value as a float. If the entry does not exist, {@code defaultValue} is return instead.
     * @param section the name of the section
     * @param key the name of the key
     * @param defaultValue alternative return value
     * @return the requested value or {@code defaultValue} if the entry does not exist.
     */
    public float getFloat(String section, String key, float defaultValue) {
        String value = getString(section, key, null);
        if (value == null) return defaultValue;
        return Float.parseFloat(value);
    }

    /**
     * Returns the requested value as a double. If the entry does not exist, {@code defaultValue} is return instead.
     * @param section the name of the section
     * @param key the name of the key
     * @param defaultValue alternative return value
     * @return the requested value or {@code defaultValue} if the entry does not exist.
     */
    public double getDouble(String section, String key, double defaultValue) {
        String value = getString(section, key, null);
        if (value == null) return defaultValue;
        return Double.parseDouble(value);
    }

    /**
     * Returns the requested value as a boolean. If the entry does not exist, {@code defaultValue} is return instead.
     * @param section the name of the section
     * @param key the name of the key
     * @param defaultValue alternative return value
     * @return the requested value or {@code defaultValue} if the entry does not exist.
     */
    public boolean getBoolean(String section, String key, boolean defaultValue) {
        String value = getString(section, key, null);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }
}
