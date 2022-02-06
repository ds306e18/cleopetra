package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.match.Series;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A data class to serialize data to overlay scripts. Contains data about team names and the bots on each team.
 */
public class OverlayData implements Serializable {

    public static final String CURRENT_MATCH_FILE_NAME = "current_match.json";

    /**
     * Overlay data for an single bot
     */
    static class OverlayBotData implements Serializable {

        @SerializedName("config_path")
        private final String configPath;

        @SerializedName("name")
        private final String name;

        @SerializedName("developer")
        private final String developer;

        @SerializedName("description")
        private final String description;

        @SerializedName("fun_fact")
        private final String funFact;

        @SerializedName("language")
        private final String language;

        @SerializedName("github")
        private final String github;

        public OverlayBotData(Bot bot) {
            this.configPath = bot.getConfigPath();
            this.name = bot.getName();
            this.developer = bot.getDeveloper();
            this.description = bot.getDescription();
            this.funFact = bot.getFunFact();
            this.language = bot.getLanguage();
            this.github = bot.getGitHub();
        }
    }

    @SerializedName("blue_team_name")
    private final String blueTeamName;

    @SerializedName("orange_team_name")
    private final String orangeTeamName;

    @SerializedName("blue_bots")
    private final List<OverlayBotData> blueBots;

    @SerializedName("orange_bots")
    private final List<OverlayBotData> orangeBots;

    @SerializedName("series_length")
    private final int seriesLength;

    @SerializedName("blue_scores")
    private final List<Integer> blueScores;

    @SerializedName("orange_scores")
    private final List<Integer> orangeScores;

    @SerializedName("series_wins")
    private final List<Integer> seriesWins; // 0: blue won, 1: orange won, -1: unknown winner or draw

    public OverlayData(Series series) {
        blueTeamName = series.getBlueTeamAsString();
        orangeTeamName = series.getOrangeTeamAsString();
        blueBots = series.getBlueTeam().getBots().stream()
                .map(OverlayBotData::new)
                .collect(Collectors.toList());
        orangeBots = series.getOrangeTeam().getBots().stream()
                .map(OverlayBotData::new)
                .collect(Collectors.toList());
        seriesLength = series.getSeriesLength();
        blueScores = series.getBlueScores().stream()
                .map(s -> s.orElse(-1))
                .collect(Collectors.toList());
        orangeScores = series.getOrangeScores().stream()
                .map(s -> s.orElse(-1))
                .collect(Collectors.toList());
        seriesWins = IntStream.range(0, seriesLength)
                .map(m -> {
                    int blueScore = blueScores.get(m);
                    int orangeScore = orangeScores.get(m);
                    if (blueScore == -1 || orangeScore == -1 || blueScore == orangeScore) return -1;
                    return blueScore > orangeScore ? 0 : 1;
                })
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Write the overlay data to the location specified in RLBotSettings.
     */
    public void write() throws IOException {
        String folderString = Tournament.get().getRlBotSettings().getOverlayPath();
        if (folderString.isBlank()) throw new IOException("Overlay path is not set");
        File folder = new File(folderString);
        if (!folder.exists()) throw new IOException("Overlay path does not exist");
        if (folder.isDirectory()) throw new IOException("Overlay path is not a directory");
        Path path = new File(folder, CURRENT_MATCH_FILE_NAME).toPath();
        Files.write(path, new Gson().toJson(this).getBytes());
    }
}
