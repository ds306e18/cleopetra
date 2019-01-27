package dk.aau.cs.ds306e18.tournament.utility.configuration;

/** Enum over supported bot-types in the rlbot.cfg */
public enum BotType {
    HUMAN("human"), RLBOT("rlbot"), PSYONIX("psyonix"), PARTY_MEMBER_BOT("party_member_bot"), CONTROLLER_PASSTHROUGH("controller_passthrough"), SPECTATOR("");

    private String configValue;

    BotType(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValue() {
        return configValue;
    }

    @Override
    public String toString() {
        return this.configValue;
    }
}
