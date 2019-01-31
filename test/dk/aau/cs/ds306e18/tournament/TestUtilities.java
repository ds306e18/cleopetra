package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Stage;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinFormat;
import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.time.Instant.now;

public class TestUtilities {

    private static int minNumPlayers = 2;
    private static int maxNumPlayers = 5;
    private static int numTeams = randomIntInRange(2, 5);

    /**
     * Returns a random int between lower and upper
     *
     * @param lower limit, inclusive of limit
     * @param upper limit, inclusive of limit
     * @return random int
     */
    private static int randomIntInRange(int lower, int upper) {
        //seed java.util random function for greater resolution when called in rapid succession
        return new Random(now().getNano()).nextInt(upper - lower) + lower;
    }

    private static final ArrayList<String> botNames = new ArrayList<String>(Arrays.asList(
            "Boten Anna", "JoeyBot", "MightyBot", "2DayHackBot", "AdversityBot", "Air Bud",
            "Atba2", "Beast from the East", "BeepBoop", "Botimus Prime", "Brick",
            "CunningBot", "Dweller", "Gosling", "Defending!"));

    private static final ArrayList<String> devNames = new ArrayList<>(Arrays.asList(
            "Mathias", "Mikkel", "Ali", "Nicolai", "Falke", "Chris", "Goose", "Tarehart",
            "DTracers"));

    private static final ArrayList<String> teamNames = new ArrayList<>(Arrays.asList(
            "compLexity", "FlipSid3", "Fnatic", "Method", "Team Dignitas", "Team Secret",
            "We Dem Girlz", "Allegiance", "Cloud9", "Evil Geniuses", "G2", "Ghost Gaming",
            "NRG", "Rouge"));

    /**
     * Generates a single bot.
     *
     * @return a bot.
     */
    public static Bot generateBot() {
        Random rand = new Random();
        return new Bot(botNames.get(rand.nextInt(botNames.size())),
                devNames.get((rand.nextInt(devNames.size()))), null); //TODO should create path somehow
    }

    /**
     * Generates an arrayList with the requested number of bots.
     *
     * @param numberOfBots the desired number of bots.
     * @return an arrayList containing the requested number of bots.
     */
    public static ArrayList<Bot> generateBots(int numberOfBots) {

        if (numberOfBots < 0)
            throw new IllegalArgumentException();

        Random rand = new Random();
        ArrayList<Bot> bots = new ArrayList<>();

        for (int i = 0; i < numberOfBots; i++)
            bots.add(generateBot());

        return bots;
    }

    /**
     * Generates a team with the given team size.
     *
     * @param teamSize the requested number of players on the team.
     * @return a team with the requested number of members.
     */
    public static Team generateTeam(int teamSize) {
        Random rand = new Random();
        int seedValue = rand.nextInt(100);
        return generateTeam(teamSize, seedValue);
    }

    /**
     * Generates a team with the given team size and seed.
     *
     * @param teamSize  the requested team size.
     * @param seedValue the teams seed
     * @return a team with the requested number of members with the specified seed.
     */
    public static Team generateTeam(int teamSize, int seedValue) {

        if (teamSize < 0)
            throw new IllegalArgumentException();

        Random rand = new Random();

        return new Team(teamNames.get(rand.nextInt(teamNames.size())), generateBots(teamSize), seedValue, "");
    }

    /**
     * Generates an arrayList with the requested number of teams containing the requested number of bots.
     *
     * @param numberOfTeams the requested number of teams.
     * @param teamSize      the requested number of bots on each team.
     * @return an arrayList containing the requested number of teams.
     */
    public static ArrayList<Team> generateTeams(int numberOfTeams, int teamSize) {

        if (numberOfTeams < 0)
            throw new IllegalArgumentException();

        ArrayList<Team> teams = new ArrayList<>();

        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(generateTeam(teamSize));
        }

        return teams;
    }

    /**
     * Generates a random tournament with numTeams teams, with between minNumPlayers and maxNumPlayers players each
     *
     * @return random Tournament object
     */
    public static Tournament generateTournamentOnlyTeams() {
        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        for (int i = 0; i < numTeams; i++) {
            tournament.addTeam(generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers)));
        }

        return tournament;
    }

    /**
     * Generates a random Round Robin tournament with numTeams teams, with between minNumPlayers and maxNumPlayers players each
     *
     * @return random Tournament object
     */
    public static Tournament generateRoundRobinTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        for (int i = 0; i < numTeams; i++)
            tournament.addTeam(generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers)));

        RoundRobinFormat roundRobinFormat = new RoundRobinFormat();

        Stage stage = new Stage("Round Robin stage", roundRobinFormat);

        tournament.addStage(stage);
        tournament.start();

        return tournament;
    }

    /**
     * Generates a random Single Elimination tournament with numTeams teams, with between minNumPlayers and maxNumPlayers players each
     *
     * @return random Tournament object
     */
    public static Tournament generateSingleEliminationTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        for (int i = 0; i < numTeams; i++)
            tournament.addTeam(generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers)));

        SingleEliminationFormat singleEliminationFormat = new SingleEliminationFormat();

        Stage stage = new Stage("Single Elimination stage", singleEliminationFormat);

        tournament.addStage(stage);
        tournament.start();

        return tournament;
    }

    /**
     * Generates a random Single Elimination tournament with numTeams teams, with between minNumPlayers and maxNumPlayers players each
     *
     * @return random Tournament object
     */
    public static Tournament generateSwissTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        for (int i = 0; i < numTeams; i++)
            tournament.addTeam(generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers)));

        SwissFormat swissFormat = new SwissFormat();

        Stage stage = new Stage("Swiss stage", swissFormat);

        tournament.addStage(stage);
        tournament.start();

        return tournament;
    }

    public static int numberOfMatchesInRoundRobin(int x) {
        return x * (x-1) / 2;
    }

    /**
     * sets all matches in the given list to have been played. The best seeded team wins.
     */
    public static void setAllMatchesToPlayed(List<Match> matches) {
        for (Match match : matches) {
            Team blue = match.getBlueTeam();
            Team orange = match.getOrangeTeam();
            if (blue.getInitialSeedValue() < orange.getInitialSeedValue()) {
                match.setScores(1, 0, true);
            } else {
                match.setScores(0, 1, true);
            }
        }
    }

    /**
     * Generates an arrayList with the requested number of teams containing the requested number of bots with ascending seed values.
     *
     * @param numberOfTeams the requested number of teams.
     * @param teamSize      the requested number of bots on each team.
     * @return an arrayList containing the requested number of teams ascending seed values.
     */
    public static ArrayList<Team> generateSeededTeams(int numberOfTeams, int teamSize) {

        if (numberOfTeams < 0)
            throw new IllegalArgumentException();

        ArrayList<Team> teams = new ArrayList<>();

        for (int i = 1; i <= numberOfTeams; i++) {
            teams.add(generateTeam(teamSize, i));
        }

        return teams;
    }
}
