package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotConfig;
import dk.aau.cs.ds306e18.tournament.serialization.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A data class for the RLBot's current OBS overlay script
 */
public class OverlayData implements Serializable {

    public static final Path CURRENT_MATCH_PATH = Paths.get("overlay/current_match.json").toAbsolutePath();

    @SerializedName("blue_team_name")
    private final String blueTeamName;

    @SerializedName("orange_team_name")
    private final String orangeTeamName;

    public OverlayData(Match match) {
        this.blueTeamName = match.getBlueTeamAsString();
        this.orangeTeamName = match.getOrangeTeamAsString();
    }

    /**
     * Write the overlay data to the default location.
     */
    public void write() throws IOException {
        Files.write(CURRENT_MATCH_PATH, new Gson().toJson(this).getBytes());
    }
}
