package dk.aau.cs.ds306e18.tournament.model;

public interface EditableBot extends Bot {
    void setDescription(String description);
    void setName(String name);
    void setDeveloper(String developer);
    void setConfigPath(String configPath);
    void setBotType(BotType type);
    void setBotSkill(BotSkill skill);
}
