package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import dk.aau.cs.ds306e18.tournament.model.BotSkill;
import dk.aau.cs.ds306e18.tournament.model.BotType;

public class ParticipantInfo {

    private BotSkill skill;
    private BotType type;
    private TeamColor team;
    private BotConfig config;

    public ParticipantInfo(BotSkill skill, BotType type, TeamColor team, BotConfig config) {
        this.skill = skill;
        this.type = type;
        this.team = team;
        this.config = config;
    }

    public BotSkill getSkill() {
        return skill;
    }

    public void setSkill(BotSkill skill) {
        this.skill = skill;
    }

    public BotType getType() {
        return type;
    }

    public void setType(BotType type) {
        this.type = type;
    }

    public TeamColor getTeam() {
        return team;
    }

    public void setTeam(TeamColor team) {
        this.team = team;
    }

    public BotConfig getConfig() {
        return config;
    }

    public void setConfig(BotConfig config) {
        this.config = config;
    }
}
