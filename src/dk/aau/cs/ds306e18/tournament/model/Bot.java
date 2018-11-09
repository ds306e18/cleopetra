package dk.aau.cs.ds306e18.tournament.model;

import java.util.Objects;

public class Bot {

    private String name;
    private String developer;
    private String description;
    private String configPath;

    public Bot(String name, String developer, String configPath) {
        this.name = name;
        this.developer = developer;
        this.configPath = configPath;
    }

    public Bot(String name, String developer) {
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

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bot bot = (Bot) o;
        return Objects.equals(getName(), bot.getName()) &&
                Objects.equals(getDeveloper(), bot.getDeveloper()) &&
                Objects.equals(getConfigPath(), bot.getConfigPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDeveloper(), getConfigPath());
    }

}
