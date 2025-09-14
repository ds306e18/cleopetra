package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotSkill;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotType;

public interface Bot {
    String getConfigPath();
    String getName();
    BotSkill getBotSkill();
    String getAgentId();
    String getLoadoutFile();
    String getRootDir();
    String getRunCommand();
    boolean isHivemind();
    String getLogoFile();

    String getDescription();
    String getDeveloper();
    String getFunFact();
    String getGitHub();
    String getLanguage();
    BotType getBotType();
}
