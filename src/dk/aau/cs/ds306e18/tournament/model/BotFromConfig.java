package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.utility.configuration.BotConfig;

import java.util.Objects;

public class BotFromConfig implements Bot {

    private String pathToConfig;
    private BotConfig config;

    public BotFromConfig(String pathToConfig) {
        this.pathToConfig = pathToConfig;
        this.config = new BotConfig(pathToConfig);
    }

    /**
     * Returns true if the config file that this bot is based on is valid. False otherwise.
     */
    public boolean isValidConfig() {
        return config.isValid();
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

    @Override
    public BotType getBotType() {
        return BotType.RLBOT;
    }

    @Override
    public BotSkill getBotSkill() {
        return BotSkill.ALLSTAR;
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
