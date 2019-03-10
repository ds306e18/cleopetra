package dk.aau.cs.ds306e18.tournament.model;

public enum BotSkill {
    ROOKIE("0.0"),
    PRO("0.5"),
    ALLSTAR("1.0");

    private final String configValue;

    BotSkill(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValue() {
        return configValue;
    }
}
