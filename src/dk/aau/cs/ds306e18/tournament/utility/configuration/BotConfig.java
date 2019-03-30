package dk.aau.cs.ds306e18.tournament.utility.configuration;

public class BotConfig extends ConfigFileEditor {

    private final static String SECTION_LOCATIONS = "Locations";
    private final static String PARAMETER_APPEARANCE = "looks_config";
    private final static String PARAMETER_PYTHON = "python_file";
    private final static String PARAMETER_NAME = "name";

    private final static String SECTION_DETAILS = "Details";
    private final static String PARAMETER_DEVELOPER = "developer";
    private final static String PARAMETER_DESCRIPTION = "description";
    private final static String PARAMETER_FUNFACT = "fun_fact";
    private final static String PARAMETER_GITHUB = "github";
    private final static String PARAMETER_LANGUAGE = "language";

    /**
     * Calls the read-function of CFE
     *
     * @param filename the filename to be read
     */
    public BotConfig(String filename) {
        read(filename);
    }

    @Override
    void validateConfigSyntax() {
        // valid if the PARAMETER_APPEARANCE and PARAMETER_NAME is present. The PARAMETER_PYTHON is not
        // required for psyonix bots so the config can be valid without
        valid = (
                config.get(SECTION_LOCATIONS) != null
                && config.get(SECTION_LOCATIONS).get(PARAMETER_APPEARANCE) != null
                && config.get(SECTION_LOCATIONS).get(PARAMETER_NAME) != null
        );
    }

    public String getAppearanceConfigPath() {
        return getValueOfLine(SECTION_LOCATIONS, PARAMETER_APPEARANCE);
    }

    public String getPythonFile() {
        return getValueOfLine(SECTION_LOCATIONS, PARAMETER_PYTHON);
    }

    public String getName() {
        return getValueOfLine(SECTION_LOCATIONS, PARAMETER_NAME);
    }

    public String getDeveloper() {
        return getValueOfLine(SECTION_DETAILS, PARAMETER_DEVELOPER);
    }

    public String getDescription() {
        return getValueOfLine(SECTION_DETAILS, PARAMETER_DESCRIPTION);
    }

    public String getFunFact() {
        return getValueOfLine(SECTION_DETAILS, PARAMETER_FUNFACT);
    }

    public String getGithub() {
        return getValueOfLine(SECTION_DETAILS, PARAMETER_GITHUB);
    }

    public String getLanguage() {
        return getValueOfLine(SECTION_DETAILS, PARAMETER_LANGUAGE);
    }
}
