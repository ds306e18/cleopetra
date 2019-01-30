package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchStatus;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.DoubleEliminationNode;
import javafx.scene.Node;

import java.util.*;
import java.util.stream.Collectors;

import static dk.aau.cs.ds306e18.tournament.utility.PowMath.log2;
import static dk.aau.cs.ds306e18.tournament.utility.PowMath.pow2;

public class DoubleEliminationFormat implements Format, MatchPlayedListener {

    private StageStatus status = StageStatus.PENDING;
    private List<Team> teams;
    private int upperBracketRounds;
    private Match finalMatch;
    private Match extraMatch;
    private Match[] upperBracket;
    private Match[] lowerBracket;
    private boolean isExtraMatchNeeded;

    transient private List<StageStatusChangeListener> statusChangeListeners = new LinkedList<>();

    @Override
    public void start(List<Team> seededTeams, boolean doSeeding) {
        teams = new ArrayList<>(seededTeams);
        generateBracket(seededTeams, doSeeding);
        status = StageStatus.RUNNING;
    }

    /** Generates the complete double elimination bracket given a list of teams ordered by seed. If doSeeding is false
     * the teams will just be insert without taking their seed into account. */
    private void generateBracket(List<Team> seededTeams, boolean doSeeding) {
        upperBracketRounds = log2(seededTeams.size());
        generateUpperBracket();
        generateLowerBracket();
        generateGrandFinals();

        // Create byes
        int teamCount = pow2(upperBracketRounds);
        List<Team> teams = new ArrayList<>(seededTeams);
        List<Team> byes = new ArrayList<>();
        while (byes.size() + teams.size() < teamCount) {
            byes.add(new Team("Bye" + byes.size(), null, 999, ""));
        }
        teams.addAll(byes);

        insertTeams(teams, doSeeding);
        removeByes(byes);
        giveMatchesIdentifiers();
    }

    /** Generates the matches in the upper bracket and connects them. All the matches will be empty. */
    private void generateUpperBracket() {
        int matchesInFirstRound = pow2(upperBracketRounds - 1);
        int numberOfMatches = pow2(upperBracketRounds) - 1;
        upperBracket = new Match[numberOfMatches];
        for(int i = numberOfMatches - 1; i >= 0; i--) {
            // Creates empty matches for first round
            if(i >= numberOfMatches - matchesInFirstRound) {
                upperBracket[i] = new Match();
            }
            // Creates the remaining matches which contains winners from their left- and right child-indexes.
            else {
                upperBracket[i] = new Match()
                        .setBlueToWinnerOf(upperBracket[getUpperBracketLeftIndex(i)])
                        .setOrangeToWinnerOf(upperBracket[getUpperBracketRightIndex(i)]);
            }
        }
    }

    /** Generates all the matches in the lower bracket and connects them to the upper bracket matches. Also creates
     * the final match and the extra match. */
    private void generateLowerBracket() {

        // When theres only one match in upper bracket, because there are only two teams, then we cannot make a lower bracket
        if (upperBracket.length == 1)  {
            lowerBracket = new Match[0];
            return;
        }

        int matchesInCurrentRound = pow2(upperBracketRounds - 2);
        int ubLoserIndex = upperBracket.length - 1;
        List<Match> lowerBracketMatches = new ArrayList<>();

        // For the first lower bracket round, use losers from first round in upper bracket
        for (int i = 0; i < matchesInCurrentRound; i++) {
            lowerBracketMatches.add(new Match()
                    .setBlueToLoserOf(upperBracket[ubLoserIndex--])
                    .setOrangeToLoserOf(upperBracket[ubLoserIndex--]));
        }

        int lbWinnerIndex = 0;
        int rounds = 2 * upperBracketRounds - 2;

        // For the remaining lower bracket rounds, alternate between:
        // - odd rounds: using winners from lower bracket against loser from upper bracket.
        // - even rounds: using only winners of prior lower bracket round
        for (int i = 1; i < rounds; i++) {

            if (i % 2 == 1) {

                // Odd round
                // Every other odd round we take winners in the opposite order
                int dir = -1;
                if (i % 4 == 1) {
                    ubLoserIndex = ubLoserIndex - matchesInCurrentRound + 1;
                    dir = 1;
                }

                for (int j = 0; j < matchesInCurrentRound; j++) {
                    lowerBracketMatches.add(new Match()
                            .setBlueToWinnerOf(lowerBracketMatches.get(lbWinnerIndex++))
                            .setOrangeToLoserOf(upperBracket[ubLoserIndex]));

                    ubLoserIndex += dir;
                }

                // Reset behaviour for every other odd round
                if (i % 4 == 1) {
                    ubLoserIndex = ubLoserIndex - matchesInCurrentRound - 1;
                }

            } else {

                // Even round
                // The amount of matches is halved in even rounds as no teams arrive to the loser bracket
                matchesInCurrentRound /= 2;

                for (int j = 0; j < matchesInCurrentRound; j++) {
                    lowerBracketMatches.add(new Match()
                            .setBlueToWinnerOf(lowerBracketMatches.get(lbWinnerIndex++))
                            .setOrangeToWinnerOf(lowerBracketMatches.get(lbWinnerIndex++)));
                }
            }
        }

        // Creates an array of lower bracket matches
        lowerBracket = lowerBracketMatches.toArray(new Match[0]);
    }

    private void generateGrandFinals() {

        // If there are two teams, then there is no lower bracket
        if (lowerBracket.length == 0) {
            // The final is just a rematch
            finalMatch = new Match()
                    .setBlueToLoserOf(upperBracket[0])
                    .setOrangeToWinnerOf(upperBracket[0]);

        } else {
            // The final is the winner of upper bracket versus winner of lower bracket
            finalMatch = new Match()
                    .setBlueToWinnerOf(upperBracket[0])
                    .setOrangeToWinnerOf(lowerBracket[lowerBracket.length-1]);
        }


        // The extra is takes the winner and loser of the final match, but it should not be played if the loser of
        // the final match has already lost twice
        extraMatch = new Match()
                .setBlueToLoserOf(finalMatch)
                .setOrangeToWinnerOf(finalMatch);

        finalMatch.registerMatchPlayedListener(this);
        extraMatch.registerMatchPlayedListener(this);
    }

    /** Inserts the teams in the correct starting positions. If doSeeding is false
     * the teams will just be insert without taking their seed into account. */
    private void insertTeams(List<Team> teams, boolean doSeeding) {

        // Create byes
        int teamCount = pow2(upperBracketRounds);

        // Seeding
        if (doSeeding) {
            teams = Seeding.fairSeedList(teams);
        }

        // Inserting
        int matchIndex = upperBracket.length - 1;
        for (int i = 0; i < teamCount; i += 2, matchIndex--) {
            upperBracket[matchIndex]
                    .setBlue(teams.get(i))
                    .setOrange(teams.get(i + 1));
        }
    }

    /** Removes matches containing byes. */
    private void removeByes(List<Team> byes) {

        // Remove byes from upper bracket
        int ubFirstRoundMatchCount = pow2(upperBracketRounds - 1);
        for (int i = 0; i < ubFirstRoundMatchCount; i++) {
            int ubIndex =  upperBracket.length - i - 1;
            Match m = upperBracket[ubIndex];

            // We know orange is the bye, if there is any
            if (byes.contains(m.getOrangeTeam())) {
                // This is a bye match in upper bracket

                // Resolve match and remove references to this match
                upperBracket[ubIndex] = null;
                if (m.doesWinnerGoToBlue()) {
                    m.getWinnerDestination().setBlue(m.getBlueTeam());
                } else {
                    m.getWinnerDestination().setOrange(m.getBlueTeam());
                }
                if (m.doesLoserGoToBlue()) {
                    m.getLoserDestination().setBlue(m.getOrangeTeam());
                } else {
                    m.getLoserDestination().setOrange(m.getOrangeTeam());
                }
            }
        }

        if (lowerBracket.length > 0) {
            // Remove byes from lower bracket
            int lbFirstRoundMatchCount = pow2(upperBracketRounds - 2);
            for (int i = 0; i < lbFirstRoundMatchCount; i++) {
                Match m = lowerBracket[i];
                // Team orange can only be a bye if blue also is a bye, so we test orange first
                if (byes.contains(m.getOrangeTeam())) {
                    // Both teams are byes
                    // Remove matches from bracket
                    lowerBracket[i] = null;
                    for (int j = 0; j < lbFirstRoundMatchCount * 2; j++) {
                        if (lowerBracket[j] == m.getWinnerDestination()) {
                            lowerBracket[j] = null;
                            break;
                        }
                    }

                    // Sets the winner destination of m's winner destination to receive the loser that m's
                    // winner destination should have received
                    Match wd = m.getWinnerDestination();
                    if (wd.doesWinnerGoToBlue()) {
                        wd.getWinnerDestination().setBlueToLoserOf(wd.getOrangeFromMatch());
                    } else {
                        wd.getWinnerDestination().setOrangeToLoserOf(wd.getOrangeFromMatch());
                    }

                } else if (byes.contains(m.getBlueTeam())) {
                    // Only blue is a bye. Orange is the loser of a match in upper bracket.
                    // Resolve match and remove references to this match
                    lowerBracket[i] = null;
                    m.getWinnerDestination().setBlueToLoserOf(m.getOrangeFromMatch());
                }
            }
        }
    }

    /** Gives the matches identifiers. */
    private void giveMatchesIdentifiers() {

        int nextIdentifier = 1;

        // We give upper bracket the small identifiers as those matches always can be played first
        for (int i = upperBracket.length - 1; i >= 0; i--) {
            if (upperBracket[i] != null) {
                upperBracket[i].setIdentifier(nextIdentifier++);
            }
        }
        for (int i = 0; i < lowerBracket.length; i++) {
            if (lowerBracket[i] != null) {
                lowerBracket[i].setIdentifier(nextIdentifier++);
            }
        }
        finalMatch.setIdentifier(nextIdentifier++);
        extraMatch.setIdentifier(nextIdentifier);
    }

    @Override
    public StageStatus getStatus() {
        return status;
    }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {

        // The easiest way to find the best performing teams in double elimination is to count how many wins and loses
        // each team has and then use the tie breaker to rank them based on that
        HashMap<Team, Integer> pointsMap = new HashMap<>();
        List<Match> allMatches = getAllMatches();
        for (Match match : allMatches) {
            if (match.hasBeenPlayed()) {
                // +1 point to the winner
                Team winner = match.getWinner();
                int wins = 0;
                if (pointsMap.containsKey(winner)) {
                    wins = pointsMap.get(winner);
                }
                pointsMap.put(winner, wins + 1);

                // -1 point to the loser
                Team loser = match.getLoser();
                int loses = 0;
                if (pointsMap.containsKey(loser)) {
                    loses = pointsMap.get(loser);
                }
                pointsMap.put(loser, loses - 1);
            }
        }

        return tieBreaker.compareWithPoints(teams, count, pointsMap);
    }

    /** Recalculates the isExtraMatchNeeded boolean. The extra match is needed if the loser of the final match has only
     * lost once. It will always be set to false if the final match has not been played. */
    public void determineIfExtraMatchNeeded() {
        if (!finalMatch.hasBeenPlayed()) {
            isExtraMatchNeeded = false;
            return;
        }

        HashMap<Team, Integer> loses = getLosesMap();
        Team finalMatchLoser = finalMatch.getLoser();
        if (loses.containsKey(finalMatchLoser)) {
            // The team has either lost once or twice
            // If it is only once, we need an extra match
            isExtraMatchNeeded = loses.get(finalMatchLoser) == 1;
        } else {
            isExtraMatchNeeded = false;
        }
    }

    public boolean isExtraMatchNeeded() {
        return isExtraMatchNeeded;
    }

    /** Returns a hash that maps teams to an integers describing how many times they have lost in this stage. */
    public HashMap<Team, Integer> getLosesMap() {
        HashMap<Team, Integer> losesMap = new HashMap<>();

        for (Match match : getAllMatches()) {
            if (match.hasBeenPlayed()) {
                Team loser = match.getLoser();
                int loses = 0;
                if (losesMap.containsKey(loser)) {
                    loses = losesMap.get(loser);
                }
                losesMap.put(loser, loses + 1);
            }
        }

        return losesMap;
    }

    @Override
    public List<Match> getAllMatches() {
        return extraMatch.getTreeAsListBFS();
    }

    @Override
    public List<Match> getUpcomingMatches() {
        return getAllMatches().stream().filter(m ->
                        m.getStatus().equals(MatchStatus.READY_TO_BE_PLAYED)
                        && !m.hasBeenPlayed()
                        && (m != extraMatch || isExtraMatchNeeded)) // extra match is only an upcoming match if needed
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> getPendingMatches() {
        return getAllMatches().stream().filter(m ->
                        m.getStatus().equals(MatchStatus.NOT_PLAYABLE)
                        && m != extraMatch) // extra match is never pending. It is either not needed, or needed AND playable
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> getCompletedMatches() {
        return getAllMatches().stream().filter(Match::hasBeenPlayed).collect(Collectors.toList());
    }

    public Match getFinalMatch() {
        return finalMatch;
    }

    public Match getExtraMatch() {
        return extraMatch;
    }

    public int getUpperBracketRounds() {
        return upperBracketRounds;
    }

    public Match[] getUpperBracket() {
        return upperBracket;
    }

    public Match[] getLowerBracket() {
        return lowerBracket;
    }

    @Override
    public void registerStatusChangedListener(StageStatusChangeListener listener) {
        statusChangeListeners.add(listener);
    }

    @Override
    public void unregisterStatusChangedListener(StageStatusChangeListener listener) {
        statusChangeListeners.remove(listener);
    }

    /** Let listeners know, that the status has changed */
    private void notifyStatusListeners(StageStatus oldStatus, StageStatus newStatus) {
        for (StageStatusChangeListener listener : statusChangeListeners) {
            listener.onStageStatusChanged(this, oldStatus, newStatus);
        }
    }

    @Override
    public DoubleEliminationNode getBracketFXNode(BracketOverviewTabController bracketOverview) {
        return new DoubleEliminationNode(this, bracketOverview);
    }

    @Override
    public Node getSettingsFXNode() {
        return null;
    }

    /** Repairs match-structure after deserialization. */
    @Override
    public void postDeserializationRepair() {
        repairUpperBracket();
        repairLowerBracket();
        repairGrandFinals();
    }

    /** Reconnect upper bracket post deserialization. */
    private void repairUpperBracket() {
        for (int i = 0; i < upperBracket.length; i++) {
            if (upperBracket[i] != null) {
                // Blue is winner from left match, if such a match exists
                int leftIndex = getUpperBracketLeftIndex(i);
                if (leftIndex < upperBracket.length) {
                    Match leftMatch = upperBracket[leftIndex];
                    if (leftMatch != null) {
                        upperBracket[i].reconnectBlueToWinnerOf(leftMatch);
                    }
                }
                // Orange is winner from right match, if such a match exists
                int rightIndex = getUpperBracketRightIndex(i);
                if (rightIndex < upperBracket.length) {
                    Match rightMatch = upperBracket[rightIndex];
                    if (rightMatch != null) {
                        upperBracket[i].reconnectOrangeToWinnerOf(rightMatch);
                    }
                }
            }
        }
    }

    /** Reconnect lower bracket post deserialization. */
    private void repairLowerBracket() {
        if (lowerBracket.length == 0) {
            return;
        }

        int matchesInCurrentRound = pow2(upperBracketRounds - 2);
        int next = 0; // index of the next match from lower bracket we want to reconnect
        int ubLoserIndex = upperBracket.length - 1;

        // For the first lower bracket round, use losers from first round in upper bracket
        while (next < matchesInCurrentRound) {
            if (lowerBracket[next] != null) {
                lowerBracket[next].reconnectBlueToLoserOf(upperBracket[ubLoserIndex]);
                lowerBracket[next].reconnectOrangeToLoserOf(upperBracket[ubLoserIndex - 1]);
            }
            next++;
            ubLoserIndex -= 2;
        }

        int lbWinnerIndex = 0;
        int rounds = 2 * upperBracketRounds - 2;

        // For the remaining lower bracket rounds, alternate between:
        // - odd rounds: using winners from lower bracket against loser from upper bracket.
        // - even rounds: using only winners of prior lower bracket round
        for (int r = 1; r < rounds; r++) {

            if (r % 2 == 1) {

                // Odd round
                // Every other odd round we take winners in the opposite order
                int dir = -1;
                if (r % 4 == 1) {
                    ubLoserIndex = ubLoserIndex - matchesInCurrentRound + 1;
                    dir = 1;
                }

                for (int j = 0; j < matchesInCurrentRound; j++) {
                    if (lowerBracket[next] == null) {
                        if (r == 1) {
                            // This lower-bracket round one match is gone due to two byes.
                            // We fix this in the following even round
                        } else {
                            System.err.println("Deserialization error: Only the first two rounds of a double-elimination lower bracket may contain a null match.");
                        }

                    } else if (lowerBracket[lbWinnerIndex] == null && r == 1) {

                        // The prior lower-bracket round one match is gone due to a bye. We know the orange team was the bye
                        // and we can calculate which upper-bracket match the blue team should come from
                        int ubAltLoser = upperBracket.length - (lbWinnerIndex * 2 + 2);
                        lowerBracket[next].reconnectBlueToLoserOf(upperBracket[ubAltLoser]);
                        lowerBracket[next].reconnectOrangeToLoserOf(upperBracket[ubLoserIndex]);

                    } else {
                        lowerBracket[next].reconnectBlueToWinnerOf(lowerBracket[lbWinnerIndex]);
                        lowerBracket[next].reconnectOrangeToLoserOf(upperBracket[ubLoserIndex]);
                    }

                    lbWinnerIndex++;
                    ubLoserIndex += dir;
                    next++;
                }

                // Reset behaviour for every other odd round
                if (r % 4 == 1) {
                    ubLoserIndex = ubLoserIndex - matchesInCurrentRound - 1;
                }

            } else {

                // Even round
                // The amount of matches is halved in even rounds as no teams arrive to the loser bracket
                matchesInCurrentRound /= 2;

                for (int j = 0; j < matchesInCurrentRound; j++) {
                    if (lowerBracket[lbWinnerIndex] == null) {
                        // This branch had two byes, so we calculate which loser from upper bracket we need
                        int ubAltLoser = lbWinnerIndex - 1;
                        lowerBracket[next].reconnectBlueToLoserOf(upperBracket[ubAltLoser]);
                    } else {
                        lowerBracket[next].reconnectBlueToWinnerOf(lowerBracket[lbWinnerIndex]);
                    }

                    if (lowerBracket[lbWinnerIndex + 1] == null) {
                        // This branch had two byes, so we calculate which loser from upper bracket we need
                        int ubAltLoser = lbWinnerIndex;
                        lowerBracket[next].reconnectOrangeToLoserOf(upperBracket[ubAltLoser]);
                    } else {
                        lowerBracket[next].reconnectOrangeToWinnerOf(lowerBracket[lbWinnerIndex + 1]);
                    }

                    lbWinnerIndex += 2;
                    next++;
                }
            }
        }
    }

    /** Reconnect final and extra match post deserialization. */
    private void repairGrandFinals() {
        if (lowerBracket.length == 0) {
            // If there is no lower bracket, there are only two teams and final match is just a rematch of upper bracket
            finalMatch.reconnectBlueToLoserOf(upperBracket[0]);
            finalMatch.reconnectOrangeToWinnerOf(upperBracket[0]);
        } else {
            finalMatch.reconnectBlueToWinnerOf(upperBracket[0]);
            finalMatch.reconnectOrangeToWinnerOf(lowerBracket[lowerBracket.length - 1]);
        }
        extraMatch.reconnectBlueToLoserOf(finalMatch);
        extraMatch.reconnectOrangeToWinnerOf(finalMatch);

        // Listeners
        finalMatch.registerMatchPlayedListener(this);
        extraMatch.registerMatchPlayedListener(this);
    }

    /** Given an index of a match in upper bracket, this returns the index of match to the left */
    private int getUpperBracketLeftIndex(int i) {
        return 2 * (i + 1);
    }

    /** Given an index of a match in upper bracket, this returns the index of match to the left */
    private int getUpperBracketRightIndex(int i) {
        return 2 * (i + 1) - 1;
    }

    /** Given an index of a match in upper bracket, this returns the index of parent match */
    private int getUpperBracketParentIndex(int i) {
        if (i == 0) {
            return -1;
        } else {
            return Math.floorDiv(i + 1, 2) - 1;
        }
    }

    @Override
    public void onMatchPlayed(Match match) {

        StageStatus oldStatus = status;
        boolean oldExtraMatchNeeded = isExtraMatchNeeded;
        if (match == finalMatch) {
            // This event was a change to the final. Determine if we need an extra match and set status accordingly
            determineIfExtraMatchNeeded();
            if (!finalMatch.hasBeenPlayed() || isExtraMatchNeeded) {
                status = StageStatus.RUNNING;
            } else {
                status = StageStatus.CONCLUDED;
            }
        } else if (match == extraMatch && extraMatch.hasBeenPlayed() && isExtraMatchNeeded) {
            // The extra match has been played
            status = StageStatus.CONCLUDED;
        } else {
            // The final or extra match has not been played
            status = StageStatus.RUNNING;
        }

        // Notify listeners if status changed
        if (oldStatus != status || oldExtraMatchNeeded != isExtraMatchNeeded) {
            notifyStatusListeners(oldStatus, status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleEliminationFormat that = (DoubleEliminationFormat) o;
        return upperBracketRounds == that.upperBracketRounds &&
                isExtraMatchNeeded == that.isExtraMatchNeeded &&
                status == that.status &&
                Objects.equals(teams, that.teams) &&
                Objects.equals(finalMatch, that.finalMatch) &&
                Objects.equals(extraMatch, that.extraMatch) &&
                Arrays.equals(upperBracket, that.upperBracket) &&
                Arrays.equals(lowerBracket, that.lowerBracket);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(status, teams, upperBracketRounds, finalMatch, extraMatch, isExtraMatchNeeded);
        result = 31 * result + Arrays.hashCode(upperBracket);
        result = 31 * result + Arrays.hashCode(lowerBracket);
        return result;
    }
}