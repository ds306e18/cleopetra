package dk.aau.cs.ds306e18.tournament.model;

import rlbot.flat.PlayerClass;

/** Enum over supported bot-types in the rlbot.cfg */
public enum BotType {
    HUMAN("human", PlayerClass.HumanPlayer),
    RLBOT("rlbot", PlayerClass.RLBotPlayer),
    PSYONIX("psyonix", PlayerClass.PsyonixBotPlayer),
    PARTY_MEMBER_BOT("party_member_bot", PlayerClass.PartyMemberBotPlayer),
    CONTROLLER_PASSTHROUGH("controller_passthrough", PlayerClass.NONE)
    ;

    private String configValue;
    private byte flatBufferValue;

    BotType(String configValue, byte flatBufferValue) {
        this.configValue = configValue;
        this.flatBufferValue = flatBufferValue;
    }

    public String getConfigValue() {
        return configValue;
    }

    public byte getFlatBufferBotType() {
        return flatBufferValue;
    }

    @Override
    public String toString() {
        return this.configValue;
    }
}
