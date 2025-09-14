package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

/** Enum over supported bot-types in the rlbot.cfg */
public enum BotType {
    HUMAN("human"),
    RLBOT("rlbot"),
    PSYONIX("psyonix");

    private String configValue;

    BotType(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValue() {
        return configValue;
    }

    public static BotType getTypeFromConfigValue(String typeValue) {
        for (BotType type : values()) {
            if (type.configValue.equals(typeValue)) {
                return type;
            }
        }
        return RLBOT;
    }

    @Override
    public String toString() {
        return this.configValue;
    }
}
