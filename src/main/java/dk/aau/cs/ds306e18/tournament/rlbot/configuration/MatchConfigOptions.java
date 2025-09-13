package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Holds all the valid values for fields the MatchConfig file. Each one is represented by a enum consisting of two
 * strings: one to display in the UI, and one for it value in the config file.
 */
public class MatchConfigOptions {

    public enum GameMap {
        RANDOM_STANDARD("Random Standard Map", ""),
        AQUA_DOME("Aqua Dome", "Underwater_P"),
        AQUA_DOME_SHALLOWS("Aqua Dome (Shallows)", "Underwater_GRS_P"),
        ARCTAGON("Arctagon", "ARC_P"),
        BADLANDS("Badlands", "Wasteland_P"),
        BADLANDS_NIGHT("Badlands (Night)", "Wasteland_Night_P"),
        BARRICADE("Barricade", "Labs_PillarHeat_P"),
        BASIN("Basin", "Labs_Basin_P"),
        BECKWITH_PARK("Beckwith Park", "Park_P"),
        BECKWITH_PARK_MIDNIGHT("Beckwith Park (Midnight)", "Park_Night_P"),
        BECKWITH_PARK_SNOWY("Beckwith Park (Snowy)", "Park_Snowy_P"),
        BECKWITH_PARK_STORMY("Beckwith Park (Stormy)", "Park_Rainy_P"),
        BECKWITH_PARK_GOTHAM_NIGHT("Beckwith Park Gotham Night", "Park_Bman_P"),
        CALAVERA("Calavera", "KO_Calavera_P"),
        CARBON("Carbon", "KO_Carbon_P"),
        CHAMPIONS_FIELD("Champions Field", "cs_p"),
        CHAMPIONS_FIELD_DAY("Champions Field (Day)", "cs_day_p"),
        CHAMPIONS_FIELD_NFL("Champions Field (NFL)", "BB_P"),
        CHAMPIONS_FIELD_NIKE_FC("Champions Field (Nike FC)", "swoosh_p"),
        COLOSSUS("Colossus", "Labs_PillarWings_P"),
        CORRIDOR("Corridor", "Labs_Corridor_P"),
        COSMIC("Cosmic", "Labs_Cosmic_V4_P"),
        DFH_STADIUM("DFH Stadium", "Stadium_P"),
        DFH_STADIUM_10TH_ANNIVERSARY("DFH Stadium (10th Anniversary)", "stadium_10a_p"),
        DFH_STADIUM_CIRCUIT("DFH Stadium (Circuit)", "Stadium_Race_Day_P"),
        DFH_STADIUM_DAY("DFH Stadium (Day)", "stadium_day_p"),
        DFH_STADIUM_SNOWY("DFH Stadium (Snowy)", "Stadium_Winter_P"),
        DFH_STADIUM_STORMY("DFH Stadium (Stormy)", "Stadium_Foggy_P"),
        DEADEYE_CANYON("Deadeye Canyon", "Outlaw_P"),
        DEADEYE_CANYON_OASIS("Deadeye Canyon (Oasis)", "outlaw_oasis_p"),
        DOUBLE_GOAL("Double Goal", "Labs_DoubleGoal_V2_P"),
        DRIFT_WOODS("Drift Woods", "Woods_P"),
        DRIFT_WOODS_NIGHT("Drift Woods (Night)", "woods_night_p"),
        DROPSHOT_CORE707("DropShot (Core707)", "ShatterShot_P"),
        ESTADIO_VIDA_DUSK("Estadio Vida (Dusk)", "ff_dusk_p"),
        FARMSTEAD("Farmstead", "farm_p"),
        FARMSTEAD_NIGHT("Farmstead (Night)", "Farm_Night_P"),
        FARMSTEAD_PITCHED("Farmstead (Pitched)", "farm_grs_p"),
        FARMSTEAD_SPOOKY("Farmstead (Spooky)", "Farm_HW_P"),
        FORBIDDEN_TEMPLE("Forbidden Temple", "CHN_Stadium_P"),
        FORBIDDEN_TEMPLE_DAY("Forbidden Temple (Day)", "CHN_Stadium_Day_P"),
        FORBIDDEN_TEMPLE_FIRE_AND_ICE("Forbidden Temple (Fire And Ice)", "fni_stadium_p"),
        FUTURA_GARDEN("Futura Garden", "UF_Day_P"),
        GALLEON("Galleon", "Labs_Galleon_P"),
        GALLEON_RETRO("Galleon (Retro)", "Labs_Galleon_Mast_P"),
        HOLYFIELD("Holyfield", "Labs_Holyfield_Space_P"),
        HOOPS_DUNK_HOUSE("Hoops (Dunk House)", "HoopsStadium_P"),
        HOOPS_THE_BLOCK("Hoops (The Block)", "HoopsStreet_P"),
        HOURGLASS("Hourglass", "Labs_PillarGlass_P"),
        LOOPHOLE("Loophole", "Labs_Holyfield_P"),
        MANNFIELD("Mannfield", "EuroStadium_P"),
        MANNFIELD_DUSK("Mannfield (Dusk)", "eurostadium_dusk_p"),
        MANNFIELD_NIGHT("Mannfield (Night)", "EuroStadium_Night_P"),
        MANNFIELD_SNOWY("Mannfield (Snowy)", "eurostadium_snownight_p"),
        MANNFIELD_STORMY("Mannfield (Stormy)", "EuroStadium_Rainy_P"),
        NEOTOKYO("NeoTokyo", "NeoTokyo_Standard_P"),
        NEOTOKYO_ARCADE("NeoTokyo (Arcade)", "NeoTokyo_Arcade_P"),
        NEOTOKYO_COMIC("NeoTokyo (Comic)", "NeoTokyo_Toon_P"),
        NEOTOKYO_HACKED("NeoTokyo (Hacked)", "neotokyo_hax_p"),
        NEON_FIELDS("Neon Fields", "music_p"),
        OCTAGON("Octagon", "Labs_Octagon_02_P"),
        PILLARS("Pillars", "Labs_CirclePillars_P"),
        QUADRON("Quadron", "KO_Quadron_P"),
        RIVALS_ARENA("Rivals Arena", "cs_hw_p"),
        SALTY_SHORES("Salty Shores", "beach_P"),
        SALTY_SHORES_NIGHT("Salty Shores (Night)", "beach_night_p"),
        SALTY_SHORES_SALTYFEST("Salty Shores (SaltyFest)", "Beach_Night_GRS_P"),
        SOVEREIGN_HEIGHTS("Sovereign Heights", "Street_P"),
        STARBASE_ARC("Starbase Arc", "arc_standard_p"),
        STARBASE_ARC_AFTERMATH("Starbase Arc (Aftermath)", "ARC_Darc_P"),
        THROWBACK_STADIUM("Throwback Stadium", "ThrowbackStadium_P"),
        THROWBACK_STADIUM_SNOWY("Throwback Stadium (Snowy)", "ThrowbackHockey_p"),
        TOKYO_UNDERPASS("Tokyo Underpass", "NeoTokyo_P"),
        UNDERPASS("Underpass", "Labs_Underpass_P"),
        URBAN_CENTRAL("Urban Central", "TrainStation_P"),
        URBAN_CENTRAL_DAWN("Urban Central (Dawn)", "TrainStation_Dawn_P"),
        URBAN_CENTRAL_HAUNTED("Urban Central (Haunted)", "Haunted_TrainStation_P"),
        URBAN_CENTRAL_NIGHT("Urban Central (Night)", "TrainStation_Night_P"),
        UTOPIA_COLISEUM("Utopia Coliseum", "UtopiaStadium_P"),
        UTOPIA_COLISEUM_DUSK("Utopia Coliseum (Dusk)", "UtopiaStadium_Dusk_P"),
        UTOPIA_COLISEUM_GILDED("Utopia Coliseum (Gilded)", "UtopiaStadium_Lux_P"),
        UTOPIA_COLISEUM_SNOWY("Utopia Coliseum (Snowy)", "UtopiaStadium_Snow_P"),
        UTOPIA_RETRO("Utopia Retro", "Labs_Utopia_P"),
        WASTELAND("Wasteland", "wasteland_s_p"),
        WASTELAND_NIGHT("Wasteland (Night)", "Wasteland_Night_S_P"),
        WASTELAND_PITCHED("Wasteland (Pitched)", "wasteland_grs_p"),
        ;

        public final String guiName, configName;

        GameMap(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static GameMap get(String value) {
            for (GameMap gm : values()) {
                if (gm.configName.equals(value)) return gm;
            }
            return CHAMPIONS_FIELD;
        }

        @Override
        public String toString() {
            return guiName;
        }

        public static final ArrayList<GameMap> standardMaps = new ArrayList<>(Arrays.asList(
                AQUA_DOME,
                AQUA_DOME_SHALLOWS,
                BECKWITH_PARK,
                BECKWITH_PARK_MIDNIGHT,
                BECKWITH_PARK_SNOWY,
                BECKWITH_PARK_STORMY,
                CHAMPIONS_FIELD,
                CHAMPIONS_FIELD_DAY,
                DEADEYE_CANYON,
                DEADEYE_CANYON_OASIS,
                DFH_STADIUM,
                DFH_STADIUM_10TH_ANNIVERSARY,
                DFH_STADIUM_CIRCUIT,
                DFH_STADIUM_DAY,
                DFH_STADIUM_SNOWY,
                DFH_STADIUM_STORMY,
                DRIFT_WOODS,
                DRIFT_WOODS_NIGHT,
                ESTADIO_VIDA_DUSK,
                FARMSTEAD,
                FARMSTEAD_NIGHT,
                FARMSTEAD_PITCHED,
                FORBIDDEN_TEMPLE,
                FORBIDDEN_TEMPLE_DAY,
                FORBIDDEN_TEMPLE_FIRE_AND_ICE,
                FUTURA_GARDEN,
                MANNFIELD,
                MANNFIELD_DUSK,
                MANNFIELD_NIGHT,
                MANNFIELD_SNOWY,
                MANNFIELD_STORMY,
                NEON_FIELDS,
                NEOTOKYO,
                NEOTOKYO_ARCADE,
                NEOTOKYO_HACKED,
                RIVALS_ARENA,
                SALTY_SHORES,
                SALTY_SHORES_NIGHT,
                SALTY_SHORES_SALTYFEST,
                SOVEREIGN_HEIGHTS,
                STARBASE_ARC,
                STARBASE_ARC_AFTERMATH,
                URBAN_CENTRAL,
                URBAN_CENTRAL_DAWN,
                URBAN_CENTRAL_NIGHT,
                UTOPIA_COLISEUM,
                UTOPIA_COLISEUM_DUSK,
                UTOPIA_COLISEUM_GILDED,
                UTOPIA_COLISEUM_SNOWY,
                WASTELAND,
                WASTELAND_NIGHT,
                WASTELAND_PITCHED
        ));

        public static GameMap getRandomStandardMap() {
            int index = new Random().nextInt(standardMaps.size());
            return standardMaps.get(index);
        }
    }

    public enum GameMode {
        SOCCER("Soccer", rlbot.flat.GameMode.Soccar),
        HOOPS("Hoops", rlbot.flat.GameMode.Hoops),
        DROPSHOT("Dropshot", rlbot.flat.GameMode.Dropshot),
        HOCKEY("Hockey", rlbot.flat.GameMode.Snowday),
        RUMBLE("Rumble", rlbot.flat.GameMode.Rumble),
        ;

        public final String guiName;
        public final int value;

        GameMode(String guiName, int value) {
            this.guiName = guiName;
            this.value = value;
        }

        public static GameMode get(int value) {
            for (GameMode gm : values()) {
                if (gm.value == value) return gm;
            }
            return SOCCER;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    /* ------------------------- MUTATORS --------------------------- */

    public enum MatchLength {
        FIVE_MINUTES("5 minutes", "5 Minutes"),
        TEN_MINUTES("10 minutes", "10 Minutes"),
        TWENTY_MINUTES("20 minutes", "20 Minutes"),
        UNLIMITED("Unlimited", "Unlimited"),
        ;

        public final String guiName, configName;

        MatchLength(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static MatchLength get(String value) {
            for (MatchLength m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return FIVE_MINUTES;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum MaxScore {
        UNLIMITED("Unlimited", "Unlimited"),
        ONE_GOAL("1 goal", "1 Goal"),
        THREE_GOALS("3 goals", "3 Goals"),
        FIVE_GOALS("5 goals", "5 Goals"),
        ;

        public final String guiName, configName;

        MaxScore(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static MaxScore get(String value) {
            for (MaxScore m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return UNLIMITED;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum Overtime {
        UNLIMITED("Unlimited", "Unlimited"),
        MAX_FIVE_MIN_FIRST_SCORE("Max 5 min, first Score", "+5 Max, First Score"),
        MAX_FIVE_MIN_RANDOM_TEAM("Max 5 min, random team", "+5 Max, Random Team"),
        ;

        public final String guiName, configName;

        Overtime(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static Overtime get(String value) {
            for (Overtime m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return UNLIMITED;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum GameSpeed {
        DEFAULT("Default", "Default"),
        SLOW_MO("Slo-mo", "Slo-mo"),
        TIME_WARP("Time warp", "Time warp"),
        ;

        public final String guiName, configName;

        GameSpeed(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static GameSpeed get(String value) {
            for (GameSpeed m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum BallMaxSpeed {
        DEFAULT("Default", "Default"),
        SLOW("Slow", "Slow"),
        FAST("Fast", "Fast"),
        SUPER_FAST("Super fast", "Super Fast"),
        ;

        public final String guiName, configName;

        BallMaxSpeed(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static BallMaxSpeed get(String value) {
            for (BallMaxSpeed m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum BallType {
        DEFAULT("Default", "Default"),
        CUBE("Cube", "Cube"),
        PUCK("Puck", "Puck"),
        BASKETBALL("Basketball", "Basketball"),
        ;

        public final String guiName, configName;

        BallType(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static BallType get(String value) {
            for (BallType m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum BallWeight {
        DEFAULT("Default", "Default"),
        SUPER_LIGHT("Super light", "Super Light"),
        LIGHT("Light", "Light"),
        HEAVY("Heavy", "Heavy"),
        ;

        public final String guiName, configName;

        BallWeight(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static BallWeight get(String value) {
            for (BallWeight m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum BallSize {
        DEFAULT("Default", "Default"),
        SMALL("Small", "Small"),
        LARGE("Large", "Large"),
        GIGANTIC("Gigantic", "Gigantic"),
        ;

        public final String guiName, configName;

        BallSize(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static BallSize get(String value) {
            for (BallSize m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum BallBounciness {
        DEFAULT("Default", "Default"),
        LOW("Low", "Low"),
        HIGH("High", "High"),
        SUPER_HIGH("Super high", "Super High"),
        ;

        public final String guiName, configName;

        BallBounciness(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static BallBounciness get(String value) {
            for (BallBounciness m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum BoostAmount {
        DEFAULT("Default", "Default"),
        UNLIMITED("Unlimited", "Unlimited"),
        RECHARGE_SLOW("Recharge (slow)", "Recharge (Slow)"),
        RECHARGE_FAST("Recharge (fast)", "Recharge (Fast)"),
        NO_BOOST("No boost", "No Boost"),
        ;

        public final String guiName, configName;

        BoostAmount(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static BoostAmount get(String value) {
            for (BoostAmount m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum BoostStrength {
        TIMES_ONE("1x", "1x"),
        TIMES_ONE_AND_HALF("1.5x", "1.5x"),
        TIMES_TWO("2x", "2x"),
        TIMES_TEN("10x", "10x"),
        ;

        public final String guiName, configName;

        BoostStrength(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static BoostStrength get(String value) {
            for (BoostStrength m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return TIMES_ONE;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum RumblePowers {
        NONE("None", "None"),
        DEFAULT("Default", "Default"),
        SLOW("Slow", "Slow"),
        CIVILIZED("Civilized", "Civilized"),
        DESTRUCTION_DERBY("Destruction derby", "Destruction Derby"),
        SPRING_LOADED("Spring loaded", "Spring Loaded"),
        SPIKES_ONLY("Spikes only", "Spikes Only"),
        SPIKE_RUSH("Spike rush", "Spike Rush");

        public final String guiName, configName;

        RumblePowers(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static RumblePowers get(String value) {
            for (RumblePowers m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return NONE;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum Gravity {
        DEFAULT("Default", "Default"),
        LOW("Low", "Low"),
        HIGH("High", "High"),
        SUPER_HIGH("Super high", "Super High"),
        ;

        public final String guiName, configName;

        Gravity(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static Gravity get(String value) {
            for (Gravity m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum Demolish {
        DEFAULT("Default", "Default"),
        DISABLED("Disabled", "Disabled"),
        FRIENDLY_FIRE("Friendly fire", "Friendly Fire"),
        ON_CONTACT("On contact", "On Contact"),
        ON_CONTACT_AND_FRIENDLY_FIRE("On contact (FF)", "On Contact (FF)"),
        ;

        public final String guiName, configName;

        Demolish(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static Demolish get(String value) {
            for (Demolish m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }

    public enum RespawnTime {
        THREE_SECONDS("3 seconds", "3 Seconds"),
        TWO_SECONDS("2 seconds", "2 Seconds"),
        ONE_SECOND("1 second", "1 Second"),
        DISABLE_GOAL_RESET("No goal reset", "Disable Goal Reset"),
        ;

        public final String guiName, configName;

        RespawnTime(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static RespawnTime get(String value) {
            for (RespawnTime m : values()) {
                if (m.configName.equals(value)) return m;
            }
            return THREE_SECONDS;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }
}
