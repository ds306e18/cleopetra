package dk.aau.cs.ds306e18.tournament.model;

import java.util.Objects;

/**
 * A CustomBot is a bot that is based on a config file, but can be edited by the user in the info window.
 */
public class CustomBot implements EditableBot, Cloneable {

    private String name;
    private String developer;
    private String description;
    private String configPath;
    private BotType botType;
    private BotSkill botSkill;

    public CustomBot(String name, String developer, String configPath, String description, BotType botType) {
        this.name = name;
        this.developer = developer;
        this.configPath = configPath;
        this.description = description;
        this.botType = botType;
    }

    public CustomBot(String name, String developer, String configPath, String description) {
        this.name = name;
        this.developer = developer;
        this.configPath = configPath;
        this.description = description;
    }

    public CustomBot(String name, String developer, String configPath) {
        this.name = name;
        this.developer = developer;
        this.configPath = configPath;
    }

    public CustomBot(String name, String developer) {
        this.name = name;
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public BotType getBotType() {
        return botType;
    }

    public void setBotType(BotType botType) {
        this.botType = botType;
    }

    @Override
    public void setBotSkill(BotSkill skill) {
        botSkill = skill;
    }

    @Override
    public BotSkill getBotSkill() {
        return botSkill;
    }

    public String toString() {
        return name;
    }

    @Override
    public CustomBot clone() {
        return new CustomBot(name, developer, configPath, description, botType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomBot bot = (CustomBot) o;
        return Objects.equals(getName(), bot.getName()) &&
                Objects.equals(getDeveloper(), bot.getDeveloper()) &&
                Objects.equals(getConfigPath(), bot.getConfigPath()) &&
                Objects.equals(getDescription(), bot.getDescription()) &&
                Objects.equals(getBotType(), bot.getBotType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDeveloper(), getConfigPath(), getDescription(), getBotType());
    }
}
