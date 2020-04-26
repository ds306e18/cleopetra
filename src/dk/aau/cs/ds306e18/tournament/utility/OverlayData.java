package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.match.Series;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A data class for the RLBot's current OBS overlay script
 */
public class OverlayData implements Serializable {

    public static final String CURRENT_MATCH_FILE_NAME = "current_match.json";

    static class OverlayBotData implements Serializable {

        @SerializedName("name")
        private final String name;

        @SerializedName("config_path")
        private final String configPath;

        public OverlayBotData(String name, String configPath) {
            this.name = name;
            this.configPath = configPath;
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
                .map(bot -> new OverlayBotData(bot.getName(), bot.getConfigPath()))
                .collect(Collectors.toList());
        orangeBots = series.getOrangeTeam().getBots().stream()
                .map(bot -> new OverlayBotData(bot.getName(), bot.getConfigPath()))
                .collect(Collectors.toList());
    }

    /**
     * Write the overlay data to the default location.
     */
    public void write() throws IOException {
        String folder = Tournament.get().getRlBotSettings().getOverlayPath();
        Files.write(new File(folder, CURRENT_MATCH_FILE_NAME).toPath(), new Gson().toJson(this).getBytes());
    }
}
