package dk.aau.cs.ds306e18.tournament.model;

public interface Bot {
    String getDescription();
    String getName();
    String getDeveloper();
    String getConfigPath();
    BotType getBotType();
    BotSkill getBotSkill();
}
