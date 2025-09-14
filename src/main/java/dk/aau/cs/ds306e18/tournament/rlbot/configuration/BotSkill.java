package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import rlbot.flat.PsyonixSkill;

public enum BotSkill {
    ROOKIE("Rookie", PsyonixSkill.Rookie),
    PRO("Pro", PsyonixSkill.Pro),
    ALLSTAR("AllStar", PsyonixSkill.AllStar);

    private final String name;
    private final int value;

    BotSkill(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static BotSkill get(String name) {
        for (BotSkill m : values()) {
            if (m.name.equals(name)) return m;
        }
        return ALLSTAR;
    }
}
