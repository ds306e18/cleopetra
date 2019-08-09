package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

public enum TeamColor {
    BLUE(0), ORANGE(1);

    private final int configValue;

    TeamColor(int configValue) {
        this.configValue = configValue;
    }

    public int getConfigValue() {
        return configValue;
    }

    public static TeamColor getFromInt(int index) {
        return index == 1 ? ORANGE : BLUE;
    }
}
