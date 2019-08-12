package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

public class MatchConfigOptions {

    public enum GameMap {
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
        DFH_STADIUM_STORMY("DFH Stadium (Stormy)", "DFHStadium_Stormy"),
        DFH_STADIUM_DAY("DFH Stadium (Day)", "DFHStadium_Day"),
        MANNFIELD_STORMY("Mannfield (Stormy)", "Mannfield_Stormy"),
        MANNFIELD_NIGHT("Mannfield (Night)", "Mannfield_Night"),
        CHAMPIONS_FIELD_DAY("Champions Field (Day)", "ChampionsField_Day"),
        BECKWITH_PARK_STORMY("Beckwith Park (Stormy)", "BeckwithPark_Stormy"),
        BECKWITH_PARK_MIDNIGHT("Beckwith Park (Midnight)", "BeckwithPark_Midnight"),
        URBAN_CENTRAL_NIGHT("Urban Central (Night)", "UrbanCentral_Night"),
        URBAN_CENTRAL_DAWN("Urban Central (Dawn)", "UrbanCentral_Dawn"),
        UTOPIA_COLISEUM_DUSK("Utopia Coliseum (Dusk)", "UtopiaColiseum_Dusk"),
        DFH_STADIUM_SNOWY("DFH Stadium (Snowy)", "DFHStadium_Snowy"),
        MANNFIELD_SNOWY("Mannfield (Snowy)", "Mannfield_Snowy"),
        UTOPIA_COLISEUM_SNOWY("Utopia Coliseum (Snowy)", "UtopiaColiseum_Snowy"),
        BADLANDS("Badlands", "Badlands"),
        BADLANDS_NIGHT("Badlands (Night)", "Badlands_Night"),
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
        THROWBACK_STADIUM("Throwback Stadium", "ThrowbackStadium")
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
    }

    public enum GameMode {
        SOCCER("Soccer", "Soccer"),
        HOOPS("Hoops", "Hoops"),
        DROPSHOT("Dropshot", "Dropshot"),
        HOCKEY("Hockey", "Hockey"),
        RUMBLE("Rumble", "Rumble"),
        ;

        public final String guiName, configName;

        GameMode(String guiName, String configName) {
            this.guiName = guiName;
            this.configName = configName;
        }

        public static GameMode get(String value) {
            for (GameMode gm : values()) {
                if (gm.configName.equals(value)) return gm;
            }
            return SOCCER;
        }

        @Override
        public String toString() {
            return guiName;
        }
    }
}
