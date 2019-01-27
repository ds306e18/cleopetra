package dk.aau.cs.ds306e18.tournament.utility.configuration;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

public class RLBotConfig extends ConfigFileEditor {

    /**
     * No-args constructor for unit-testing
     */
    RLBotConfig() {
    }

    /**
     * Calls the read-function of CFE
     * @param filename the filename to be read
     */
    public RLBotConfig(String filename) {
        read(filename);
    }

    /**
     * Calls the write-function of CFE
     * @param filename the filename to be written to
     */
    public void writeConfig(String filename) {
        write(filename);
    }

    /**
     * Configures the config based on the state of a given Match
     * @param match the match to configure the config for
     * @return the boolean of success
     */
    public boolean setupMatch(Match match) {
        int numParticipants = 0;

        // for blue team, edit numbered parameters by incremented count of participants
        for (Bot bot : match.getBlueTeam().getBots()) {
            // edit participant_config parameter to current bots config path
            editLine(PARAMETER_PARTICIPANT_CONFIG, numParticipants, bot.getConfigPath());
            // edit participant_team parameter to blue-team constant
            editLine(PARAMETER_PARTICIPANT_TEAM, numParticipants, PARAMETER_BLUE_TEAM);
            // edit participant_type parameter to RLBot-participant constant
            editLine(PARAMETER_PARTICIPANT_TYPE, numParticipants, PARAMETER_BOT_TYPE);
            numParticipants++;
        }

        // for orange team, edit numbered parameters by incremented count of participants
        for (Bot bot : match.getOrangeTeam().getBots()) {
            editLine(PARAMETER_PARTICIPANT_CONFIG, numParticipants, bot.getConfigPath());
            editLine(PARAMETER_PARTICIPANT_TEAM, numParticipants, PARAMETER_ORANGE_TEAM);
            editLine(PARAMETER_PARTICIPANT_TYPE, numParticipants, PARAMETER_BOT_TYPE);
            numParticipants++;
        }

        // edit num_participants parameter to count of edited participants
        editLine(PARAMETER_PARTICIPANT_NUM, Integer.toString(numParticipants));

        // when finished, validate syntax and return boolean set by validateConfigSyntax
        validateConfigSyntax();
        return isValid();
    }
}
