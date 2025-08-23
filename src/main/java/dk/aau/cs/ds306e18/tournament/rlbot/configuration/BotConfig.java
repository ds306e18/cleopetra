package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import org.tomlj.Toml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Class for reading from a bot config file.
 */
public class BotConfig {

    public final static String SETTINGS_HEADER = "settings";
    public final static String AGENT_ID = "agent_id";
    public final static String NAME = "name";
    public final static String LOADOUT_FILE = "loadout_file";
    public final static String ROOT_DIR = "root_dir";
    public final static String RUN_COMMAND = "run_command";
    public final static String RUN_COMMAND_LINUX = "run_command_linux";
    public final static String HIVEMIND = "hivemind";
    public final static String LOGO_FILE = "logo_file";

    public final static String DETAILS_HEADER = "details";
    public final static String DEVELOPER = "developer";
    public final static String DESCRIPTION = "description";
    public final static String FUN_FACT = "fun_fact";
    public final static String SOURCE_LINK = "source_link";
    public final static String LANGUAGE = "language";
    public final static String TAGS = "tags";

    private File configFile;
    private String agentId;
    private String name;
    private String loadoutFile;
    private String rootDir;
    private String runCommand;
    private String runCommandLinux;
    private boolean hivemind;
    private String logoFile;

    private String developer;
    private String description;
    private String funFact;
    private String sourceLink;
    private String language;
    private List<String> tags = new ArrayList<>();

    public BotConfig(File configFile) throws IOException {
        this.configFile = configFile;
        if (!configFile.toString().endsWith("bot.toml")) {
            throw new IOException("File '" + configFile + "' does not have the expected 'bot.toml' extension.");
        }
        var parseResult = Toml.parse(configFile.toPath());
        if (parseResult.hasErrors()) {
            throw new IOException("Could not parse toml file '" + configFile + "'. Errors: " + parseResult.errors().get(0).toString());
        }

        Supplier<String> emptySupplier = () -> "";
        agentId = parseResult.getString(SETTINGS_HEADER + "." + AGENT_ID, emptySupplier);
        name = parseResult.getString(SETTINGS_HEADER + "." + NAME, emptySupplier);
        loadoutFile = parseResult.getString(SETTINGS_HEADER + "." + LOADOUT_FILE, emptySupplier);
        rootDir = parseResult.getString(SETTINGS_HEADER + "." + ROOT_DIR, emptySupplier);
        runCommand = parseResult.getString(SETTINGS_HEADER + "." + RUN_COMMAND, emptySupplier);
        runCommandLinux = parseResult.getString(SETTINGS_HEADER + "." + RUN_COMMAND_LINUX, emptySupplier);
        hivemind = parseResult.getBoolean(SETTINGS_HEADER + "." + HIVEMIND, () -> false);
        logoFile = parseResult.getString(SETTINGS_HEADER + "." + LOGO_FILE, emptySupplier);

        developer = parseResult.getString(DETAILS_HEADER + "." + DEVELOPER, emptySupplier);
        description = parseResult.getString(DETAILS_HEADER + "." + DESCRIPTION, emptySupplier);
        funFact = parseResult.getString(DETAILS_HEADER + "." + FUN_FACT, emptySupplier);
        sourceLink = parseResult.getString(DETAILS_HEADER + "." + SOURCE_LINK, emptySupplier);
        language = parseResult.getString(DETAILS_HEADER + "." + LANGUAGE, () -> "");
        var tomlTags = parseResult.getArrayOrEmpty(DETAILS_HEADER + "." + TAGS);
        for (int i = 0; i < tomlTags.size(); i++) {
            tags.add(tomlTags.getString(i));
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getName() {
        return name;
    }

    public String getLoadoutFile() {
        return loadoutFile;
    }

    public String getRootDir() {
        return rootDir;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public String getRunCommandLinux() {
        return runCommandLinux;
    }

    public boolean isHivemind() {
        return hivemind;
    }

    public String getLogoFile() {
        return logoFile;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getDescription() {
        return description;
    }

    public String getFunFact() {
        return funFact;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getTags() {
        return List.copyOf(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotConfig botConfig = (BotConfig) o;
        return Objects.equals(configFile, botConfig.configFile) &&
                Objects.equals(agentId, botConfig.agentId) &&
                Objects.equals(name, botConfig.name) &&
                Objects.equals(loadoutFile, botConfig.loadoutFile) &&
                Objects.equals(rootDir, botConfig.rootDir) &&
                Objects.equals(runCommand, botConfig.runCommand) &&
                Objects.equals(runCommandLinux, botConfig.runCommandLinux) &&
                Objects.equals(hivemind, botConfig.hivemind) &&
                Objects.equals(logoFile, botConfig.logoFile) &&
                Objects.equals(developer, botConfig.developer) &&
                Objects.equals(description, botConfig.description) &&
                Objects.equals(funFact, botConfig.funFact) &&
                Objects.equals(sourceLink, botConfig.sourceLink) &&
                Objects.equals(language, botConfig.language) &&
                Objects.equals(tags, botConfig.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configFile, agentId, name, loadoutFile, rootDir, runCommand, runCommandLinux, hivemind, logoFile, developer, description, funFact, sourceLink, language, tags);
    }
}
