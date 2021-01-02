package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.Tournament;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Contains an algorithm for auto-naming teams using a piece of the team member's name.
 */
public class AutoNaming {

    private static final Pattern BOT_PATTERN = Pattern.compile("[Bb]ot|BOT");

    private static final Set<String> IGNORED_PIECES = new HashSet<>(Arrays.asList(
            "bot", "psyonix",
            "a", "an", "the", "my", "that", "it",
            "of", "in", "as", "at", "on", "by", "for", "from", "to",
            "and", "or", "but"
    ));

    /**
     * Find a give a unique name of the given team. The name will consists of pieces from the
     * team members' name.
     */
    public static void autoName(Team team, Collection<Team> otherTeams) {
        String teamName = getName(team);
        Set<String> otherTeamNames = otherTeams.stream()
                .filter(t -> t != team)
                .map(Team::getTeamName)
                .collect(Collectors.toSet());
        teamName = uniquify(teamName, otherTeamNames);
        team.setTeamName(teamName);
    }

    /**
     * Checks if a team name is already present in a set of names, and if it is, a postfix "(i)" will
     * be added the end of the team name. E.g. "Team" already exist, then "Team" becomes "Team (1)".
     * The new unique name is returned.
     */
    public static String uniquify(String teamName, Set<String> otherTeamsNames) {
        int i = 1;
        String candidateName = teamName;
        while (otherTeamsNames.contains(candidateName)) {
            i++;
            candidateName = teamName + " (" + i + ")";
        }
        return candidateName;
    }

    /**
     * Finds a team name for the given team. The algorithm will try to find an
     * interesting piece from each bot's name and use it in the team name.
     * The algorithm isn't perfect, and characters other than letters and digits
     * can create weird names.
     * Example: ReliefBot + Beast from the East => Relief-Beast.
     */
    public static String getName(Team team) {
        return getName(team.getBots().stream().map(Bot::getName).collect(Collectors.toList()));
    }

    /**
     * Finds a team name for the given list of bots. The algorithm will try to find an
     * interesting part from each bot's name and use it in the team name.
     * If there is only one unique name, that name will be used.
     * The algorithm isn't perfect, and characters other than letters and digits
     * can create weird names.
     * Example: ReliefBot + Beast from the East => Relief-Beast.
     */
    public static String getName(List<String> botNames) {
        int botCount = botNames.size();
        if (botCount == 0) return "Team";
        if (botCount == 1) return botNames.get(0);

        // Check if there is only one unique name, in that case use that name
        long uniqueNames = botNames.stream().distinct().count();
        if (uniqueNames == 1) return botNames.get(0);

        // Construct names from the short names of bots, separated by "-"
        // Example: ReliefBot + Beast from the East => Relief-Beast
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < botCount; i++) {
            String name = botNames.get(i);
            String shortName = getShortName(name);
            str.append(shortName);
            if (i != botCount - 1) {
                str.append("-");
            }
        }

        return str.toString();
    }

    /**
     * Finds a short name for the given bot name. The short name will be the first interesting "word" in
     * the bot's name. The function knows how to split logically ("RelieftBot" => "Relieft" + "Bot")
     * and ignore common words ("of", "the", etc.). The algorithm isn't perfect, and characters
     * other than letters and digits can create weird names.
     */
    public static String getShortName(String botName) {
        String[] pieces = getPieces(botName);

        Optional<String> shortName = Arrays.stream(pieces)
                // Remove pieces that are "Bot", "Psyonix", or common words like "of", "the", etc.
                .filter(s -> !IGNORED_PIECES.contains(s.toLowerCase()))
                // Remove substrings that are "bot", e.g. "Skybot" => "Sky"
                .map(s -> s.endsWith("bot") ? s.substring(0, s.length() - 3) : s)
                .findFirst();

        return shortName.orElse(botName);
    }

    /**
     * Find all pieces of a name.
     */
    private static String[] getPieces(String botName) {
        /*
        The following regex is used to split the bot name into pieces.
        The string is split the following places:
        - Between two letters/digits separated by spaces, regardless of casing (removing spaces)
            Examples:
                "A B" = ["A", "B"]
                "a b" = ["a", "b"]
                "2 a" = ["2", "a"]
                "A    B" = ["A", "B"]
                "Air Bud" = ["Air", "Bud"]
                "Beast from the East" = ["Beast", "from", "the", "East"]
        - Between a letter/digit and an uppercase letter separated by -'s (removing the -'s)
            Examples:
                "A-B" = ["A", "B"]
                "A-b" = ["A-b"]
                "2-A" = ["2", "A"]
                "2-a" = ["2-a"]
                "2--A" = ["2", "A"]
                "2--a" = ["2--a"]
                "A-2" = ["A-2"]
                "Self-driving" = ["Self-driving"]
        - Between two chars where the first is lowercase or a digit and second is uppercase
            Examples:
                "AaB" = ["Aa", "B"]
                "2B" = ["2", "B"]
                "ReliefBot" = ["Relief", "Bot"]
        Since the split function removes what is matches, the regex uses positive lookbehind (?<=) and
        positive lookahead (?=) to check the characters around the split.
        Other characters than letters and digits will probably create errors, but they are rare in bot names,
        so it should be fine.
        Try it out on https://regex101.com
         */
        return botName.split("(?<=[\\da-z])-*(?=[A-Z])|(?<=[\\da-zA-Z])\\ +(?=[\\da-zA-Z])");
    }
}
