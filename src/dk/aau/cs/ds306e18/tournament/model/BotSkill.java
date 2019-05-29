package dk.aau.cs.ds306e18.tournament.model;

/**
 * Enum for bot skill values in the rlbot.cfg
 */
public enum BotSkill {
    ROOKIE(0.0f),
    PRO(0.5f),
    ALLSTAR(1.0f);

    private final float skill;

    BotSkill(float skill) {
        this.skill = skill;
    }

    public String getConfigValue() {
        return "" + skill;
    }

    public float getFlatBufferValue() {
        return skill;
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
