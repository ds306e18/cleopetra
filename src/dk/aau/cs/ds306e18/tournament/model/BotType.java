package dk.aau.cs.ds306e18.tournament.model;

import rlbot.flat.PlayerType;

/** Enum over supported bot-types in the rlbot.cfg */
public enum BotType {
    HUMAN("human", PlayerType.HumanPlayer),
    RLBOT("rlbot", PlayerType.RLBotPlayer),
    PSYONIX("psyonix", PlayerType.PsyonixBotPlayer),
    PARTY_MEMBER_BOT("party_member_bot", PlayerType.PartyMemberBotPlayer),
    CONTROLLER_PASSTHROUGH("controller_passthrough", PlayerType.NONE),
    SPECTATOR("", PlayerType.NONE);

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
