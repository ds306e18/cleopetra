package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantInfo that = (ParticipantInfo) o;
        return skill == that.skill &&
                type == that.type &&
                team == that.team &&
                Objects.equals(config, that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill, type, team, config);
    }
}
