package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;

import java.util.Objects;

/**
 * Settings related to the tournament or RLBot
 */
public class RLBotSettings {

    private MatchConfig matchConfig = new MatchConfig();
    private boolean writeOverlayData = false;
    private String overlayPath = "";

    public RLBotSettings() {

    }

    public RLBotSettings(MatchConfig matchConfig) {
        this.matchConfig = matchConfig;
    }

    public MatchConfig getMatchConfig() {
        return matchConfig;
    }

    public void setMatchConfig(MatchConfig matchConfig) {
        this.matchConfig = matchConfig;
    }

    public boolean writeOverlayDataEnabled() {
        return writeOverlayData;
    }

    public void setWriteOverlayData(boolean writeOverlayData) {
        this.writeOverlayData = writeOverlayData;
    }

    public String getOverlayPath() {
        return overlayPath;
    }

    public void setOverlayPath(String overlayPath) {
        this.overlayPath = overlayPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RLBotSettings that = (RLBotSettings) o;
        return writeOverlayData == that.writeOverlayData &&
                Objects.equals(matchConfig, that.matchConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchConfig, writeOverlayData);
    }
}
