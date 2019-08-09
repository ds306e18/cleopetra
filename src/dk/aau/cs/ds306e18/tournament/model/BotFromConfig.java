package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotConfig;

import java.io.File;
import java.util.Objects;

public class BotFromConfig implements Bot {

    private String pathToConfig;
    private BotConfig config;
    private boolean configLoadedCorrectly = false;

    public BotFromConfig(String pathToConfig) {
        this.pathToConfig = pathToConfig;
        reload();
    }

    /**
     * Returns true if the config file that this bot is based on was loaded correctly and is valid. False otherwise.
     */
    public boolean loadedCorrectly() {
        return configLoadedCorrectly && config != null;
    }

    /**
     * Reload the config file that this was bot was based on.
     * @return true if the config file that this bot is based on was loaded correctly and is valid. False otherwise.
     */
    public boolean reload() {
        try {
            config = new BotConfig(new File(pathToConfig));
            configLoadedCorrectly = true;
        } catch (Exception e) {
            configLoadedCorrectly = false;
            throw new RuntimeException("Could not load bot from config " + pathToConfig + ", reason: " + e.getMessage());
        }
        return loadedCorrectly();
    }

    @Override
    public String getDescription() {
        if (configLoadedCorrectly) return config.getDescription();
        else return "Could not load bot from config";
    }

    @Override
    public String getName() {
        if (configLoadedCorrectly) return config.getName();
        else return "Could not load bot from config";
    }

    @Override
    public String getDeveloper() {
        if (configLoadedCorrectly) return config.getDeveloper();
        else return "Could not load bot from config";
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
        if (configLoadedCorrectly) return config.getFunFact();
        else return "Could not load bot from config";
    }

    @Override
    public String getGitHub() {
        if (configLoadedCorrectly) return config.getGithub();
        else return "Could not load bot from config";
    }

    @Override
    public String getLanguage() {
        if (configLoadedCorrectly) return config.getLanguage();
        else return "Could not load bot from config";
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
