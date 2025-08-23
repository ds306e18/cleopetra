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
        DFH_STADIUM("DFH Stadium", "DFHStadium"),
        MANNFIELD("Mannfield", "Mannfield"),
        CHAMPIONS_FIELD("Champions Field", "ChampionsField"),
        URBAN_CENTRAL("Urban Central", "UrbanCentral"),
        BECKWITH_PARK("Beckwith Park", "BeckwithPark"),
        UTOPIA_COLISEUM("Utopia Coliseum", "UtopiaColiseum"),
        WASTELAND("Wasteland", "Wasteland"),
        NEO_TOKYO("Neo Tokyo", "NeoTokyo"),
        AQUADOME("Aquadome", "AquaDome"),
        STARBASE_ARC("Starbase Arc", "StarbaseArc"),
        FARMSTEAD("Farmstead", "Farmstead"),
        SALTY_SHORES("Salty Shores", "SaltyShores"),
        DFH_STADIUM_STORMY("DFH Stadium (stormy)", "DFHStadium_Stormy"),
        DFH_STADIUM_DAY("DFH Stadium (day)", "DFHStadium_Day"),
        MANNFIELD_STORMY("Mannfield (stormy)", "Mannfield_Stormy"),
        MANNFIELD_NIGHT("Mannfield (night)", "Mannfield_Night"),
        CHAMPIONS_FIELD_DAY("Champions Field (day)", "ChampionsField_Day"),
        BECKWITH_PARK_STORMY("Beckwith Park (stormy)", "BeckwithPark_Stormy"),
        BECKWITH_PARK_MIDNIGHT("Beckwith Park (midnight)", "BeckwithPark_Midnight"),
        URBAN_CENTRAL_NIGHT("Urban Central (night)", "UrbanCentral_Night"),
        URBAN_CENTRAL_DAWN("Urban Central (dawn)", "UrbanCentral_Dawn"),
        UTOPIA_COLISEUM_DUSK("Utopia Coliseum (dusk)", "UtopiaColiseum_Dusk"),
        DFH_STADIUM_SNOWY("DFH Stadium (snowy)", "DFHStadium_Snowy"),
        MANNFIELD_SNOWY("Mannfield (snowy)", "Mannfield_Snowy"),
        UTOPIA_COLISEUM_SNOWY("Utopia Coliseum (snowy)", "UtopiaColiseum_Snowy"),
        BADLANDS("Badlands", "Badlands"),
        BADLANDS_NIGHT("Badlands (night)", "Badlands_Night"),
        TOKYO_UNDERPASS("Tokyo Underpass", "TokyoUnderpass"),
        ARCTAGON("Arctagon", "Arctagon"),
        PILLARS("Pillars", "Pillars"),
        COSMIC("Cosmic", "Cosmic"),
        DOUBLE_GOAL("Double Goal", "DoubleGoal"),
        OCTAGON("Octagon", "Octagon"),
        UNDERPASS("Underpass", "Underpass"),
        UTOPIA_RETRO("Utopia Retro", "UtopiaRetro"),
        HOOPS_DUNK_HOUSE("Dunk House", "Hoops_DunkHouse"),
        DROPSHOT_CORE707("Core 707", "DropShot_Core707"),
        THROWBACK_STADIUM("Throwback Stadium", "ThrowbackStadium"),
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
                DFH_STADIUM,
                MANNFIELD,
                CHAMPIONS_FIELD,
                URBAN_CENTRAL,
                BECKWITH_PARK,
                UTOPIA_COLISEUM,
                WASTELAND,
                NEO_TOKYO,
                AQUADOME,
                STARBASE_ARC,
                FARMSTEAD,
                SALTY_SHORES,
                DFH_STADIUM_STORMY,
                DFH_STADIUM_DAY,
                MANNFIELD_STORMY,
                MANNFIELD_NIGHT,
                CHAMPIONS_FIELD_DAY,
                BECKWITH_PARK_STORMY,
                BECKWITH_PARK_MIDNIGHT,
                URBAN_CENTRAL_NIGHT,
                URBAN_CENTRAL_DAWN,
                UTOPIA_COLISEUM_DUSK,
                DFH_STADIUM_SNOWY,
                MANNFIELD_SNOWY,
                UTOPIA_COLISEUM_SNOWY
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
        SPIKE_RUSH("Spike rush", "Spike Rush")
        ;

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
