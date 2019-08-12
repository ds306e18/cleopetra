package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

import java.util.function.BiConsumer;

public class RLBotSettingsTabController {

    public static RLBotSettingsTabController instance;
    public ChoiceBox<GameMap> gameMapChoiceBox;
    public ChoiceBox<GameMode> gameModeChoiceBox;
    public ChoiceBox<MatchLength> matchLengthChoiceBox;
    public ChoiceBox<MaxScore> maxScoreChoiceBox;
    public ChoiceBox<Overtime> overtimeChoiceBox;
    public ChoiceBox<GameSpeed> gameSpeedChoiceBox;
    public ChoiceBox<BallMaxSpeed> ballMaxSpeedChoiceBox;
    public ChoiceBox<BallType> ballTypeChoiceBox;
    public ChoiceBox<BallWeight> ballWeightChoiceBox;
    public ChoiceBox<BallSize> ballSizeChoiceBox;
    public ChoiceBox<BallBounciness> ballBouncinessChoiceBox;
    public ChoiceBox<BoostAmount> boostAmountChoiceBox;
    public ChoiceBox<BoostStrength> boostStrengthChoiceBox;
    public ChoiceBox<RumblePowers> rumblePowersChoiceBox;
    public ChoiceBox<Gravity> gravityChoiceBox;
    public ChoiceBox<Demolish> demolishChoiceBox;
    public ChoiceBox<RespawnTime> respawnTimeChoiceBox;

    @FXML private HBox tabRoot;

    @FXML
    private void initialize() {
        instance = this;

        MatchConfig matchConfig = Tournament.get().getRlBotSettings().getMatchConfig();

        setupChoiceBox(gameMapChoiceBox, GameMap.values(), matchConfig.getGameMap(), MatchConfig::setGameMap);
        setupChoiceBox(gameModeChoiceBox, GameMode.values(), matchConfig.getGameMode(), MatchConfig::setGameMode);
        setupChoiceBox(matchLengthChoiceBox, MatchLength.values(), matchConfig.getMatchLength(), MatchConfig::setMatchLength);
        setupChoiceBox(maxScoreChoiceBox, MaxScore.values(), matchConfig.getMaxScore(), MatchConfig::setMaxScore);
        setupChoiceBox(overtimeChoiceBox, Overtime.values(), matchConfig.getOvertime(), MatchConfig::setOvertime);
        setupChoiceBox(gameSpeedChoiceBox, GameSpeed.values(), matchConfig.getGameSpeed(), MatchConfig::setGameSpeed);
        setupChoiceBox(ballMaxSpeedChoiceBox, BallMaxSpeed.values(), matchConfig.getBallMaxSpeed(), MatchConfig::setBallMaxSpeed);
        setupChoiceBox(ballTypeChoiceBox, BallType.values(), matchConfig.getBallType(), MatchConfig::setBallType);
        setupChoiceBox(ballWeightChoiceBox, BallWeight.values(), matchConfig.getBallWeight(), MatchConfig::setBallWeight);
        setupChoiceBox(ballSizeChoiceBox, BallSize.values(), matchConfig.getBallSize(), MatchConfig::setBallSize);
        setupChoiceBox(ballBouncinessChoiceBox, BallBounciness.values(), matchConfig.getBallBounciness(), MatchConfig::setBallBounciness);
        setupChoiceBox(boostAmountChoiceBox, BoostAmount.values(), matchConfig.getBoostAmount(), MatchConfig::setBoostAmount);
        setupChoiceBox(boostStrengthChoiceBox, BoostStrength.values(), matchConfig.getBoostStrength(), MatchConfig::setBoostStrength);
        setupChoiceBox(rumblePowersChoiceBox, RumblePowers.values(), matchConfig.getRumblePowers(), MatchConfig::setRumblePowers);
        setupChoiceBox(gravityChoiceBox, Gravity.values(), matchConfig.getGravity(), MatchConfig::setGravity);
        setupChoiceBox(demolishChoiceBox, Demolish.values(), matchConfig.getDemolish(), MatchConfig::setDemolish);
        setupChoiceBox(respawnTimeChoiceBox, RespawnTime.values(), matchConfig.getRespawnTime(), MatchConfig::setRespawnTime);

        update();
    }

    /**
     * Setup a choice box for match config options.
     */
    private <T> void setupChoiceBox(ChoiceBox<T> choiceBox, T[] values, T current, BiConsumer<MatchConfig, T> onSelect) {
        choiceBox.setItems(FXCollections.observableArrayList(values));
        choiceBox.getSelectionModel().select(current);
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            onSelect.accept(Tournament.get().getRlBotSettings().getMatchConfig(), newValue);
        });
    }

    /** Updates all ui elements */
    public void update() {

    }
}
