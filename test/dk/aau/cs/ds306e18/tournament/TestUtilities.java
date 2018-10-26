package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.time.Instant.now;

public class TestUtilities {

    /**
     * Returns a random int between lower and upper
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

    /** Generates a single bot.
     *  @return a bot. */
    public static Bot generateBot(){
        Random rand = new Random();
        return new Bot(botNames.get(rand.nextInt(botNames.size())),
                devNames.get((rand.nextInt(devNames.size()))), null); //TODO should create path somehow
    }

    /** Generates an arrayList with the requested number of bots.
     * @param numberOfBots the desired number of bots.
     * @return an arrayList containing the requested number of bots. */
    public static ArrayList<Bot> generateBots(int numberOfBots){

        if(numberOfBots < 0)
            throw new IllegalArgumentException();

        Random rand = new Random();
        ArrayList<Bot> bots = new ArrayList<>();

        for(int i = 0; i < numberOfBots; i++)
            bots.add(generateBot());

        return bots;
    }

    /** Generates a team with the given team size.
     * @param teamSize the requested number of players on the team.
     * @return a team with the requested number of members. */
    public static Team generateTeam(int teamSize){

        if(teamSize < 0)
            throw new IllegalArgumentException();

        Random rand = new Random();

        return new Team(teamNames.get(rand.nextInt(teamNames.size())), generateBots(teamSize), rand.nextInt(100), "");
    }

    /** Generates an arrayList with the requested number of teams containing the requested number of bots.
     * @param numberOfTeams the requested number of teams.
     * @param teamSize the requested number of bots on each team.
     * @return an arrayList containing the requested number of teams. */
    public static ArrayList<Team> generateTeams(int numberOfTeams, int teamSize){

        if(numberOfTeams < 0)
            throw new IllegalArgumentException();

        ArrayList<Team> teams = new ArrayList<>();

        for(int i = 0; i < numberOfTeams; i++){
            teams.add(generateTeam(teamSize));
        }

        return teams;
    }

    /**
     * Generates a random tournament with four teams
     * @return random Tournament object
     */
    public static Tournament generateTournament() {
        int minNumPlayers = 2;
        int maxNumPlayers = 10;

        Tournament tournament = new Tournament();
        tournament.setName("DatTournament");

        // add between 2 to 20 teams, with between minNumPlayers
        for (int i = 0; i < randomIntInRange(2, 20); i++) {
            tournament.addTeam(generateTeam(randomIntInRange(minNumPlayers, maxNumPlayers)));
        }

        //TODO; add stage
        SingleEliminationStage singleEliminationStage = new SingleEliminationStage();
        //tournament.addStage();

        return tournament;
    }
}
