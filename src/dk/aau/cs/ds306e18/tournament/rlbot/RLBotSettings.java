package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;

import java.util.Objects;

public class RLBotSettings {

    private MatchConfig matchConfig;

    public RLBotSettings() {
        this(new MatchConfig());
    }

    public RLBotSettings(MatchConfig matchConfig) {
        this.matchConfig = matchConfig;
    }

    public MatchConfig getMatchConfig() {
        return matchConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RLBotSettings that = (RLBotSettings) o;
        return Objects.equals(matchConfig, that.matchConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchConfig);
    }
}
