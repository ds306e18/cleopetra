package dk.aau.cs.ds306e18.tournament.utility.configuration;

public class BotConfig extends ConfigFileEditor {

    private final static String PARAMETER_APPEARANCE = "looks_config";
    private final static String PARAMETER_PYTHON = "python_file";
    private final static String PARAMETER_NAME = "name";

    private final static String PARAMETER_DEVELOPER = "developer";
    private final static String PARAMETER_DESCRIPTION = "description";
    private final static String PARAMETER_FUNFACT = "fun_fact";
    private final static String PARAMETER_GITHUB = "github";
    private final static String PARAMETER_LANGUAGE = "language";

    /**
     * Calls the read-function of CFE
     * @param filename the filename to be read
     */
    public BotConfig(String filename) {
        read(filename);
    }

    /**
     * Calls the write-function of CFE
     * @param filename the filename to be written to
     */
    public void writeConfig(String filename) {
        write(filename);
    }

    public String getAppearanceConfigPath() {
        return getValueOfLine(PARAMETER_APPEARANCE);
    }

    public String getPythonFile() {
        return getValueOfLine(PARAMETER_PYTHON);
    }

    public String getName() {
        return getValueOfLine(PARAMETER_NAME);
    }

    public String getDeveloper() {
        return getValueOfLine(PARAMETER_DEVELOPER);
    }

    public String getDescription() {
        return getValueOfLine(PARAMETER_DESCRIPTION);
    }

    public String getFunFact() {
        return getValueOfLine(PARAMETER_FUNFACT);
    }

    public String getGithub() {
        return getValueOfLine(PARAMETER_GITHUB);
    }

    public String getLanguage() {
        return getValueOfLine(PARAMETER_LANGUAGE);
    }
}
