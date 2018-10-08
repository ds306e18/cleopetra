package dk.aau.cs.ds306e18.tournament.participants;

import java.nio.file.Path;
import java.util.ArrayList;

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
}
