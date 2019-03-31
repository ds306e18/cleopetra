package dk.aau.cs.ds306e18.tournament.model;

/**
 * An EditableBot is a bot that can be edited by the user in the info window.
 */
public interface EditableBot extends Bot {
    void setDescription(String description);
    void setName(String name);
    void setDeveloper(String developer);
    void setConfigPath(String configPath);
    void setFunFact(String funFact);
    void setGitHub(String github);
    void setLanguage(String language);
    void setBotType(BotType type);
    void setBotSkill(BotSkill skill);
}
