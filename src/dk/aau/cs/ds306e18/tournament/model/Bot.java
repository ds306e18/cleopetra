package dk.aau.cs.ds306e18.tournament.model;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
