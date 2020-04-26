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
import java.util.List;
import java.util.stream.Collectors;

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

    public OverlayData(Series series) {
        blueTeamName = series.getBlueTeamAsString();
        orangeTeamName = series.getOrangeTeamAsString();
        blueBots = series.getBlueTeam().getBots().stream()
                .map(OverlayBotData::new)
                .collect(Collectors.toList());
        orangeBots = series.getOrangeTeam().getBots().stream()
                .map(OverlayBotData::new)
                .collect(Collectors.toList());
    }

    /**
     * Write the overlay data to the location specified in RLBotSettings.
     */
    public void write() throws IOException {
        String folder = Tournament.get().getRlBotSettings().getOverlayPath();
        Files.write(new File(folder, CURRENT_MATCH_FILE_NAME).toPath(), new Gson().toJson(this).getBytes());
    }
}
