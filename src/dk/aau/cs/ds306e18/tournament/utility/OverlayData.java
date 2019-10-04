package dk.aau.cs.ds306e18.tournament.utility;

import com.google.gson.annotations.SerializedName;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotConfig;

/**
 * A data class for the RLBot's current OBS overlay script
 */
public class OverlayData {

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
}
