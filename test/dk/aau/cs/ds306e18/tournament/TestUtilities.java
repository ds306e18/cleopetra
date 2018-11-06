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
     * Generates a random tournament with between 2 and 20 teams, with between 2 and 10 players each
     *
     * @return random Tournament object
     */
    public static Tournament generateTournamentOnlyTeams() {
        int minNumPlayers = 2;
        int maxNumPlayers = 10;

        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        // add between 2 to 20 teams, with between minNumPlayers
        for (int i = 0; i < randomIntInRange(2, 20); i++) {
            tournament.addTeam(generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers)));
        }

        return tournament;
    }

    /**
     * Generates a random Round Robin tournament with between 2 and 20 teams, with between 2 and 10 players each
     *
     * @return random Tournament object
     */
    public static Tournament generateRoundRobinTournament() {
        int minNumPlayers = 2;
        int maxNumPlayers = 10;

        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        ArrayList<Team> teams = new ArrayList<>();

        // add between 2 to 20 teams, with between minNumPlayers
        for (int i = 0; i < randomIntInRange(2, 20); i++) {
            Team team = generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers));
            tournament.addTeam(team);
            teams.add(team);
        }

        RoundRobinFormat roundRobinFormat = new RoundRobinFormat();
        roundRobinFormat.start(teams);

        Stage stage = new Stage("Round Robin stage", roundRobinFormat);

        tournament.addStage(stage);

        return tournament;
    }

    /**
     * Generates a random Single Elimination tournament with between 2 and 20 teams, with between 2 and 10 players each
     *
     * @return random Tournament object
     */
    public static Tournament generateSingleEliminationTournament() {
        int minNumPlayers = 2;
        int maxNumPlayers = 10;

        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        ArrayList<Team> teams = new ArrayList<>();

        // add between 2 to 20 teams, with between minNumPlayers
        for (int i = 0; i < randomIntInRange(2, 20); i++) {
            Team team = generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers));
            tournament.addTeam(team);
            teams.add(team);
        }

        SingleEliminationFormat singleEliminationFormat = new SingleEliminationFormat();
        singleEliminationFormat.start(teams);

        Stage stage = new Stage("Single Elimination stage", singleEliminationFormat);

        tournament.addStage(stage);

        return tournament;
    }

    /**
     * Generates a random Single Elimination tournament with between 2 and 20 teams, with between 2 and 10 players each
     *
     * @return random Tournament object
     */
    public static Tournament generateSwissTournament() {
        int minNumPlayers = 2;
        int maxNumPlayers = 10;

        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        ArrayList<Team> teams = new ArrayList<>();

        // add between 2 to 20 teams, with between minNumPlayers
        for (int i = 0; i < randomIntInRange(2, 20); i++) {
            Team team = generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers));
            tournament.addTeam(team);
            teams.add(team);
        }

        SwissFormat swissFormat = new SwissFormat();
        swissFormat.start(teams);

        Stage stage = new Stage("Swiss stage", swissFormat);

        tournament.addStage(stage);

        return tournament;
    }


    /**
     * Generates an arrayList with the requested number of teams containing the requested number of bots with ascending seed values.
     * <p>
     * /** @return the given x factored. x!
     */
    public static int factorial(int x) {

        int result = 0;

        for (int i = x; i > 0; i--)
            result += i;

        return result;
    }

    /**
     * sets all upcoming matches in the given swissStage to have been played.
     */
    public static void setAllMatchesPlayed(Format stage) {

        //Set all matches to played
        List<Match> matches = stage.getUpcomingMatches();
        for (Match match : matches) {
            match.setHasBeenPlayed(true);
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
