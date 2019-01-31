package dk.aau.cs.ds306e18.tournament.utility.configuration;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

public class RLBotConfig extends ConfigFileEditor {

    /** Parameter- and value-constants of the rlbot.cfg */
    private final static String PARAMETER_PARTICIPANT_CONFIG = "participant_config_";
    private final static String PARAMETER_PARTICIPANT_TEAM = "participant_team_";
    private final static String PARAMETER_PARTICIPANT_TYPE = "participant_type_";
    private final static String PARAMETER_PARTICIPANT_NUM = "num_participant";

    private final static int PARAMETER_BLUE_TEAM = 0;
    private final static int PARAMETER_ORANGE_TEAM = 1;

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

    /** Set path of given participantIndex in RLBotConfig to supplied path-parameter */
    private void setParticipantConfigPath(String path, int participantIndex) {
        editLine(PARAMETER_PARTICIPANT_CONFIG, participantIndex, path);
    }

    /** Sets team-index of given participantIndex in RLBotConfig to supplied teamIndex, converting int to String */
    private void setParticipantTeam(int teamIndex, int participantIndex) {
        editLine(PARAMETER_PARTICIPANT_TEAM, participantIndex, Integer.toString(teamIndex));
    }

    /** Sets participant type of given participantIndex in RLBotConfig to the config value of the given BotType */
    private void setParticipantType(BotType botType, int participantIndex) {
        editLine(PARAMETER_PARTICIPANT_TYPE, participantIndex, botType.getConfigValue());
    }

    /** Sets the number of participants for the RLBotConfig, converting int to String */
    private void setNumberOfParticipants(int participantIndex) {
        editLine(PARAMETER_PARTICIPANT_NUM, Integer.toString(participantIndex));
    }

    /**
     * Configures the config based on the state of a given Match,
     * @param match the match to configure the config for
     * @return the boolean of success
     */
    public boolean setupMatch(Match match) {
        int numParticipants = 0;

        // for blue team, edit numbered parameters by incremented count of participants
        for (Bot bot : match.getBlueTeam().getBots()) {
            // edit participant_config parameter to current bots config path
            setParticipantConfigPath(bot.getConfigPath(), numParticipants);
            // edit participant_team parameter to blue-team constant
            setParticipantTeam(PARAMETER_BLUE_TEAM, numParticipants);
            // edit participant_type parameter to RLBot-participant constant
            setParticipantType(BotType.RLBOT, numParticipants);
            numParticipants++;
        }

        // for orange team, edit numbered parameters by incremented count of participants
        for (Bot bot : match.getOrangeTeam().getBots()) {
            setParticipantConfigPath(bot.getConfigPath(), numParticipants);
            setParticipantTeam(PARAMETER_ORANGE_TEAM, numParticipants);
            setParticipantType(BotType.RLBOT, numParticipants);
            numParticipants++;
        }

        // edit num_participants parameter to count of edited participants
        setNumberOfParticipants(numParticipants);

        // when configured, validate syntax and return boolean set by validateConfigSyntax
        validateConfigSyntax();
        return isValid();
    }
}
