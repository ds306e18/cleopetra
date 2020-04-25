package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.model.match.Series;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.Instant.now;

public class TestUtilities {

    public static final String[] TEST_BOT_CONFIG_FILES = {
            "test/bots/alpha.cfg",
            "test/bots/bravo.cfg",
            "test/bots/charlie.cfg",
            "test/bots/delta.cfg",
            "test/bots/echo.cfg",
            "test/bots/foxtrot.cfg",
            "test/bots/golf.cfg",
            "test/bots/hotel.cfg",
            "test/bots/india.cfg",
            "test/bots/juliet.cfg",
            "test/bots/kilo.cfg",
            "test/bots/lima.cfg",
            "test/bots/mike.cfg",
            "test/bots/november.cfg",
    };

    public static final String[] TEAM_NAMES = {"Complexity", "FlipSid3", "Fnatic", "Method", "Dignitas",
            "Team Secret", "We Dem Girlz", "Allegiance", "Cloud9", "Evil Geniuses", "G2", "Ghost Gaming",
            "NRG", "Rogue", "The Muffin Men", "FC Barcelona", "Team SoloMid", "The Bricks", "Baguette Squad",
            "Frontline", "Exalty", "ARG", "Echo Zulu", "Ghost", "Triple Commit", "The D00ds", "Plot Twist",
            "Gadget", "Firecrackers", "My Little Pwners", "Avalanche", "ChronoRetreat", "Dala's Warriors"};

    private static final Random rand = new Random();

    /**
     * Returns the given amount of bots from test configs. Each bot will be unique.
     */
    public static List<BotFromConfig> getTestConfigBots(int count) {
        if (0 < count && count < TEST_BOT_CONFIG_FILES.length)
            throw new AssertionError("Only 0-" + TEST_BOT_CONFIG_FILES.length + " test bots are supported.");
        return Arrays.stream(TEST_BOT_CONFIG_FILES)
                .limit(count)
                .map(BotFromConfig::new)
                .collect(Collectors.toList());
    }

    /**
     * Returns a random bot from a test config. Only use it when the randomness does not affect the test.
     */
    private static BotFromConfig randomBotFromConfig() {
        int i = rand.nextInt(TEST_BOT_CONFIG_FILES.length);
        return new BotFromConfig(TEST_BOT_CONFIG_FILES[i]);
    }

    /**
     * Returns the given amount of teams. The teams are seeded and each team is unique due to team name and seed.
     * Note that the bots are chosen randomly.
     */
    public static List<Team> getTestTeams(int count, int teamSize) {
        if (count < 0 || TEAM_NAMES.length <= count)
            throw new AssertionError("Only 0-" + TEAM_NAMES.length + " test teams are supported.");
        return IntStream.range(1, count + 1)
                .mapToObj(seed -> new Team(
                        TEAM_NAMES[seed],
                        Stream.generate(() -> (Bot) randomBotFromConfig())
                                .limit(teamSize)
                                .collect(Collectors.toList()),
                        seed,
                        "Team description"))
                .collect(Collectors.toList());
    }

    /**
     * Generates a tournament with the given number of teams with the given number of bots per team. Note that
     * the bots are randomly chosen, but team are unique due to team names.
     */
    public static Tournament generateTournamentWithTeams(int teamCount, int botsPerTeam) {
        Tournament tournament = new Tournament();
        tournament.setName("DatTestTournament");
        List<Team> teams = getTestTeams(teamCount, botsPerTeam);
        tournament.addTeams(teams);
        return tournament;
    }

    /**
     * Sets all matches in the given list to have been played. The best seeded team wins.
     */
    public static void setAllMatchesToPlayed(List<Series> series) {
        for (Series serie : series) {
            Team teamOne = serie.getTeamOne();
            Team teamTwo = serie.getTeamTwo();
            if (teamOne.getInitialSeedValue() < teamTwo.getInitialSeedValue()) {
                serie.setScores(1, 0, 0);
            } else {
                serie.setScores(0, 1, 0);
            }
            serie.setHasBeenPlayed(true);
        }
    }
}
