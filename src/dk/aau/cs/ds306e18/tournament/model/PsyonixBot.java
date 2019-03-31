package dk.aau.cs.ds306e18.tournament.model;

public class PsyonixBot extends BotFromConfig {

    private BotSkill skill;

    public PsyonixBot(String pathToConfig, BotSkill skill) {
        super(pathToConfig);
        this.skill = skill;
    }

    @Override
    public BotType getBotType() {
        return BotType.PSYONIX;
    }

    @Override
    public BotSkill getBotSkill() {
        return skill;
    }
}
