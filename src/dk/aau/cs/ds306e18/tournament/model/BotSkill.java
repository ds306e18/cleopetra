package dk.aau.cs.ds306e18.tournament.model;

/**
 * Enum for bot skill values in the rlbot.cfg
 */
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

    /**
     * Turn a number into a valid skill type
     */
    public static BotSkill getSkillFromNumber(double skill) {
        if (skill <= 0.0) return ROOKIE;
        if (skill <= 0.5) return PRO;
        return ALLSTAR;
    }
}
