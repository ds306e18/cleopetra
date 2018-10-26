package dk.aau.cs.ds306e18.tournament.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class Bot {

    private String name;
    private String developer;
    private Path configPath;

    public Bot(String name, String developer, Path configPath) {
        this.name = name;
        this.developer = developer;
        this.configPath = configPath;
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

    public Path getConfigPath() {
        return configPath;
    }

    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
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
