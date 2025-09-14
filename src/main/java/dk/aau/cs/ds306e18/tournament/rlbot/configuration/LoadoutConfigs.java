package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import org.tomlj.Toml;
import rlbot.flat.LoadoutPaintT;
import rlbot.flat.PlayerLoadoutT;

import java.io.File;
import java.io.IOException;
import java.util.function.LongSupplier;

public class LoadoutConfigs {

    public static PlayerLoadoutT tryLoad(File file, TeamColor team) throws IOException {
        if (file == null || !file.exists()) return null;

        var result = Toml.parse(file.toPath());
        if (result.hasErrors()) {
            throw new IOException("Failed to parse loadout file: " + result.errors().get(0));
        }

        LongSupplier zero = () -> 0L;

        var loadout = new PlayerLoadoutT();
        var paint = new LoadoutPaintT();

        var itemTable = team == TeamColor.BLUE ? result.getTable("blue_loadout") : result.getTable("orange_loadout");
        if (itemTable == null) return null;

        var paintTable = itemTable.getTable("paint");
        if (paintTable != null) {
            paint.setCarPaintId(paintTable.getLong("car_paint_id", zero));
            paint.setDecalPaintId(paintTable.getLong("decal_paint_id", zero));
            paint.setWheelsPaintId(paintTable.getLong("wheels_paint_id", zero));
            paint.setBoostPaintId(paintTable.getLong("boost_paint_id", zero));
            paint.setAntennaPaintId(paintTable.getLong("antenna_paint_id", zero));
            paint.setHatPaintId(paintTable.getLong("hat_paint_id", zero));
            paint.setTrailsPaintId(paintTable.getLong("trails_paint_id", zero));
            paint.setGoalExplosionPaintId(paintTable.getLong("goal_explosion_paint_id", zero));
        }

        loadout.setTeamColorId(itemTable.getLong("team_color_id", zero));
        loadout.setCustomColorId(itemTable.getLong("custom_color_id", zero));
        loadout.setCarId(itemTable.getLong("car_id", zero));
        loadout.setDecalId(itemTable.getLong("decal_id", zero));
        loadout.setWheelsId(itemTable.getLong("wheels_id", zero));
        loadout.setBoostId(itemTable.getLong("boost_id", zero));
        loadout.setAntennaId(itemTable.getLong("antenna_id", zero));
        loadout.setHatId(itemTable.getLong("hat_id", zero));
        loadout.setPaintFinishId(itemTable.getLong("paint_finish_id", zero));
        loadout.setCustomFinishId(itemTable.getLong("custom_finish_id", zero));
        loadout.setEngineAudioId(itemTable.getLong("engine_audio_id", zero));
        loadout.setTrailsId(itemTable.getLong("trails_id", zero));
        loadout.setGoalExplosionId(itemTable.getLong("goal_explosion_id", zero));
        loadout.setLoadoutPaint(paint);

        return loadout;
    }
}
