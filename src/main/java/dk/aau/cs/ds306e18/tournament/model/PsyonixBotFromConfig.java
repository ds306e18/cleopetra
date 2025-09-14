package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotSkill;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotType;

import java.io.IOException;

public class PsyonixBotFromConfig extends BotFromConfig {

    private BotSkill skill;

    public PsyonixBotFromConfig(String pathToConfig, BotSkill skill) throws IOException {
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
