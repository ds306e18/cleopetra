package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotConfig;
import dk.aau.cs.ds306e18.tournament.serialization.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A data class for the RLBot's current OBS overlay script
 */
public class OverlayData {

    public static final Path CURRENT_MATCH_PATH = Paths.get("current_match.json").toAbsolutePath();

    @SerializedName("division")
    private final int division = 0; // Not used

    @SerializedName("blue_config_path")
    private final String blueConfig;

    @SerializedName("orange_config_path")
    private final String orangeConfig;

    public OverlayData(BotConfig blueConfig, BotConfig orangeConfig) {
        this.blueConfig = blueConfig.getConfigFile().getAbsolutePath();
        this.orangeConfig = orangeConfig.getConfigFile().getAbsolutePath();
    }

    /**
     * Write the overlay data to the default location. Aborts with no exception if the match is not 1v1 or
     * not ready to be played or if one of the bots are not a bot from a config file.
     */
    public static void write(Series series) throws IOException {
        //TODO; should be expanded when RLBot support overlays for more than one-vs-ones
        if (series.isOneVsOne()) {
            Bot blueBot = series.getBlueTeam().getBots().get(0);
            Bot orangeBot = series.getOrangeTeam().getBots().get(0);
            if (blueBot instanceof BotFromConfig && orangeBot instanceof BotFromConfig) {
                OverlayData overlayData = new OverlayData(
                        ((BotFromConfig) blueBot).getConfig(),
                        ((BotFromConfig) orangeBot).getConfig()
                );
                Serializer.gson.toJson(overlayData);
                Files.write(CURRENT_MATCH_PATH, new Gson().toJson(overlayData).getBytes());
            }
        }
    }
}
