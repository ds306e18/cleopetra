package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.rlbot.MatchRunner;
import dk.aau.cs.ds306e18.tournament.rlbot.RLBotSettings;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.function.BiConsumer;

public class RLBotSettingsTabController {

    public static RLBotSettingsTabController instance;
    public Button resetAllButton;
    public ChoiceBox<GameMap> gameMapChoiceBox;
    public ChoiceBox<GameMode> gameModeChoiceBox;
    public RadioButton skipReplaysRadioButton;
    public RadioButton instantStartRadioButton;
    public RadioButton writeOverlayDataRadioButton;
    public TextField overlayPathTextField;
    public Button chooseOverlayPathButton;
    public RadioButton useRLBotPackPythonRadioButton;
    public Button rlbotRunnerOpenButton;
    public Button rlbotRunnerCloseButton;
    public Button rlbotRunnerStopMatchButton;
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
    public RadioButton renderingRadioButton;
    public RadioButton stateSettingRadioButton;
    public RadioButton autoSaveReplaysRadioButton;

    @FXML private HBox tabRoot;

    @FXML
    private void initialize() {
        instance = this;

        RLBotSettings settings = Tournament.get().getRlBotSettings();
        MatchConfig matchConfig = settings.getMatchConfig();

        // General match settings
        setupChoiceBox(gameMapChoiceBox, GameMap.values(), matchConfig.getGameMap(), MatchConfig::setGameMap);
        setupChoiceBox(gameModeChoiceBox, GameMode.values(), matchConfig.getGameMode(), MatchConfig::setGameMode);
        skipReplaysRadioButton.setSelected(matchConfig.isSkipReplays());
        skipReplaysRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().getMatchConfig().setSkipReplays(newValue);
        });
        instantStartRadioButton.setSelected(matchConfig.isInstantStart());
        instantStartRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().getMatchConfig().setInstantStart(newValue);
        });
        renderingRadioButton.setSelected(matchConfig.isRenderingEnabled());
        renderingRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().getMatchConfig().setRenderingEnabled(newValue);
        });
        stateSettingRadioButton.setSelected(matchConfig.isStateSettingEnabled());
        stateSettingRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().getMatchConfig().setStateSettingEnabled(newValue);
        });
        autoSaveReplaysRadioButton.setSelected(matchConfig.isAutoSaveReplays());
        autoSaveReplaysRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().getMatchConfig().setAutoSaveReplays(newValue);
        });

        // Mutators
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

        // Other settings
        boolean writeOverlay = settings.writeOverlayDataEnabled();
        writeOverlayDataRadioButton.setSelected(writeOverlay);
        writeOverlayDataRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().setWriteOverlayData(newValue);
            overlayPathTextField.setDisable(!newValue);
            chooseOverlayPathButton.setDisable(!newValue);
        });
        chooseOverlayPathButton.setDisable(!writeOverlay);
        overlayPathTextField.setDisable(!writeOverlay);
        overlayPathTextField.setText(settings.getOverlayPath());
        overlayPathTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String path = overlayPathTextField.textProperty().get();
                settings.setOverlayPath(path);
            }
        });

        useRLBotPackPythonRadioButton.setSelected(settings.useBotPackPythonIfAvailable());
        useRLBotPackPythonRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().setUseBotPackPythonIfAvailable(newValue);
        });

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

    public void onActionResetAllButton(ActionEvent actionEvent) {
        Tournament.get().getRlBotSettings().setMatchConfig(new MatchConfig());
        update();
    }

    /** Updates all ui elements */
    public void update() {

        RLBotSettings settings = Tournament.get().getRlBotSettings();
        MatchConfig matchConfig = settings.getMatchConfig();

        // General match settings
        gameMapChoiceBox.getSelectionModel().select(matchConfig.getGameMap());
        gameModeChoiceBox.getSelectionModel().select(matchConfig.getGameMode());
        skipReplaysRadioButton.setSelected(matchConfig.isSkipReplays());
        instantStartRadioButton.setSelected(matchConfig.isInstantStart());

        // Mutators
        matchLengthChoiceBox.getSelectionModel().select(matchConfig.getMatchLength());
        maxScoreChoiceBox.getSelectionModel().select(matchConfig.getMaxScore());
        overtimeChoiceBox.getSelectionModel().select(matchConfig.getOvertime());
        gameSpeedChoiceBox.getSelectionModel().select(matchConfig.getGameSpeed());
        ballMaxSpeedChoiceBox.getSelectionModel().select(matchConfig.getBallMaxSpeed());
        ballTypeChoiceBox.getSelectionModel().select(matchConfig.getBallType());
        ballWeightChoiceBox.getSelectionModel().select(matchConfig.getBallWeight());
        ballSizeChoiceBox.getSelectionModel().select(matchConfig.getBallSize());
        ballBouncinessChoiceBox.getSelectionModel().select(matchConfig.getBallBounciness());
        boostAmountChoiceBox.getSelectionModel().select(matchConfig.getBoostAmount());
        boostStrengthChoiceBox.getSelectionModel().select(matchConfig.getBoostStrength());
        rumblePowersChoiceBox.getSelectionModel().select(matchConfig.getRumblePowers());
        gravityChoiceBox.getSelectionModel().select(matchConfig.getGravity());
        demolishChoiceBox.getSelectionModel().select(matchConfig.getDemolish());
        respawnTimeChoiceBox.getSelectionModel().select(matchConfig.getRespawnTime());

        // Other settings
        writeOverlayDataRadioButton.setSelected(settings.writeOverlayDataEnabled());
    }

    public void onActionRLBotRunnerOpen(ActionEvent actionEvent) {
        MatchRunner.startRLBotRunner();
    }

    public void onActionRLBotRunnerClose(ActionEvent actionEvent) {
        MatchRunner.closeRLBotRunner();
    }

    public void onActionRLBotRunnerStopMatch(ActionEvent actionEvent) {
        MatchRunner.stopMatch();
    }

    public void onActionChooseOverlayPath(ActionEvent actionEvent) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose overlay folder");
        dirChooser.setInitialDirectory(Main.lastSavedDirectory);
        Window window = chooseOverlayPathButton.getScene().getWindow();
        File folder = dirChooser.showDialog(window);

        if (folder != null) {
            String path = folder.toString();
            overlayPathTextField.setText(path);
            Tournament.get().getRlBotSettings().setOverlayPath(path);
        }
    }
}
