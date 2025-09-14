package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotSkill;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotType;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class BotFromConfig implements Bot {

    private String pathToConfig;
    private BotConfig config;

    public BotFromConfig(String pathToConfig) throws IOException {
        this.pathToConfig = pathToConfig;
        reload();
    }

    /**
     * Reload the data from the config file that this was bot was based on.
     */
    public void reload() throws IOException {
        config = new BotConfig(new File(pathToConfig));
    }

    @Override
    public String getDescription() {
        return config.getDescription();
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public String getDeveloper() {
        return config.getDeveloper();
    }

    @Override
    public String getConfigPath() {
        return pathToConfig;
    }

    public BotConfig getConfig() {
        return config;
    }

    @Override
    public String getFunFact() {
        return config.getFunFact();
    }

    @Override
    public String getGitHub() {
        return config.getSourceLink();
    }

    @Override
    public String getLanguage() {
        return config.getLanguage();
    }

    @Override
    public BotType getBotType() {
        return BotType.RLBOT;
    }

    @Override
    public BotSkill getBotSkill() {
        return BotSkill.ALLSTAR;
    }

    @Override
    public String getAgentId() {
        return config.getAgentId();
    }

    @Override
    public String getLoadoutFile() {
        return config.getLoadoutFile();
    }

    @Override
    public String getRootDir() {
        return Paths.get(pathToConfig).getParent().resolve(config.getRootDir()).toString();
    }

    @Override
    public String getRunCommand() {
        return config.getRunCommand();
    }

    @Override
    public boolean isHivemind() {
        return config.isHivemind();
    }

    @Override
    public String getLogoFile() {
        return config.getLogoFile();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotFromConfig that = (BotFromConfig) o;
        return Objects.equals(pathToConfig, that.pathToConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathToConfig);
    }
}
