package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.oldui.Tabs.general.StageSettings;
import dk.aau.cs.ds306e18.tournament.ui.StageFormatOption;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.effect.BlendMode;
import javafx.util.Callback;

import java.util.Collections;

public class TournamentSettingsTabController {

    @FXML
    private GridPane tournamentSettingsTab;
    @FXML
    private TextField nameTextField;
    @FXML
    private ChoiceBox<TieBreaker> tieBreakerChoiceBox;
    @FXML
    private ListView<Stage> stagesListView;
    @FXML
    private Button addStageBtn;
    @FXML
    private Button removeStageBtn;
    @FXML
    private VBox stageSettingsVBox;
    @FXML
    private Text selectStageText;
    @FXML
    private Text stageSettingsHeadLabel;
    @FXML
    private HBox stageSettingsContent;
    @FXML
    private TextField stageNameTextfield;
    @FXML
    private ChoiceBox<StageFormatOption> formatChoicebox;
    @FXML
    private TextField roundsTextfield;
    @FXML
    private HBox roundsHBox;
    @FXML
    private Button swapUp;
    @FXML
    private Button swapDown;


    /* TODO List
       DONE: Clean code && Comments
       DONE: Look more into having a getJavaFxNode. Nope, not for prototype.
       DONE: Is seeding method still relevant? Nope not for prototype.
       Done: Add arrows to order stages.
       DONE: Disable arrows if a swap is not possible. (Listener)
    */

    @FXML
    private void initialize() {
        /* Retrieve and set tournament name into textfield. */
        nameTextField.setText(Tournament.get().getName());

        /* Retrieve and add choices to choicebox for the Tiebreaker box.
         * Also upon change sets the new tiebreaker rule to the tournament model. */
        tieBreakerChoiceBox.setItems(FXCollections.observableArrayList(new TieBreakerBySeed()));
        tieBreakerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Tournament.get().setTieBreaker(newValue));
        tieBreakerChoiceBox.getSelectionModel().select(0);


        /* Retrieve possible formats and add to a choicebox */
        formatChoicebox.setItems(FXCollections.observableArrayList(StageFormatOption.values()));

        /* Listener for the format choicebox. Used to change a Stage format when a different format is chosen */
        formatChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (getSelectedStage() != null && StageFormatOption.getOption(getSelectedStage().getFormat()) != newValue) {
                getSelectedStage().setFormat(newValue.getNewInstance());
            }

            /* Rounds are only visible if a specific format is chosen. */
            if (getSelectedStage() != null && StageFormatOption.getOption(getSelectedStage().getFormat()) != StageFormatOption.ROUND_ROBIN) {
                roundsHBox.setVisible(false);
            } else {
                roundsHBox.setVisible(true);
            }

        });

        /* By default the stage settings are hidden.
         * This listener is used to show the stage settings when there is at least one Stage added.
         * Also handles disabling and enabling of buttons for stages.
         * */
        stagesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stagesListView.getItems().size() != 0) {
                stageSettingsVBox.setVisible(true);
            } else {
                stageSettingsVBox.setVisible(false);
            }

            /* Handle stage order button disabling / enabling */
            if (getSelectedIndex() == 0) {
                swapUp.setDisable(true);
            } else {
                swapUp.setDisable(false);
            }
            if (getSelectedIndex() == stagesListView.getItems().size() - 1 && getSelectedIndex() != -1) {
                swapDown.setDisable(true);
            } else {
                swapDown.setDisable(false);
            }

            /* Set content inside stage settings of chosen stage */
            setContent();
        });
    }

    @FXML
    void nameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().setName(nameTextField.getText());
    }

    @FXML
    void stageNameTextFieldOnKeyReleased(KeyEvent event) {
        getSelectedStage().setName(stageNameTextfield.getText());
        stagesListView.refresh();
    }

    /**
     * Adds a stage to the stages list and also to the tournament model.
     */
    @FXML
    void addStageBtnOnAction(ActionEvent actionEvent) {
        Tournament.get().addStage(new Stage("New Stage", new SwissStage()));

        stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
        stagesListView.refresh();
    }

    /**
     * Removes a stage from the Stages list and also from the tournament model.
     */
    @FXML
    void removeStageBtnOnAction() {
        if (getSelectedIndex() != -1) {
            Tournament.get().removeStage(getSelectedIndex());
            stagesListView.getItems().remove(getSelectedIndex());
        }
    }

    /**
     * Sets correct text into fields inside the stage settings.
     */
    private void setContent() {
        if (getSelectedStage() != null) {
            stageNameTextfield.setText(getSelectedStage().getName());
            formatChoicebox.getSelectionModel().select(StageFormatOption.getOption(getSelectedStage().getFormat()));
        }
    }

    /**
     * Swaps a stage upwards in the list of stages. Used to allow ordering of stages.
     * This also swaps the stages in the tournament model.
     */
    @FXML
    private void swapStageUpwards() {
        if (getSelectedIndex() != 0 && getSelectedIndex() != -1) {
            Collections.swap(stagesListView.getItems(), getSelectedIndex(), getSelectedIndex() - 1);
            Tournament.get().swapStages(stagesListView.getItems().get(getSelectedIndex()), stagesListView.getItems().get(getSelectedIndex() - 1));

            stagesListView.getSelectionModel().select(getSelectedIndex() - 1);
        }
    }

    /**
     * Swaps a stage downwards in the list of stages. Used to allow ordering of stages.
     * This also swaps the stages in the tournament model.
     */
    @FXML
    private void swapStageDownwards() {
        int listSize = stagesListView.getItems().size();

        if (getSelectedIndex() != listSize - 1 && getSelectedIndex() != -1) {
            Collections.swap(stagesListView.getItems(), getSelectedIndex(), getSelectedIndex() + 1);
            Tournament.get().swapStages(stagesListView.getItems().get(getSelectedIndex()), stagesListView.getItems().get(getSelectedIndex() + 1));
            stagesListView.getSelectionModel().select(getSelectedIndex() + 1);
        }
    }

    private Stage getSelectedStage() {
        return stagesListView.getSelectionModel().getSelectedItem();
    }

    private int getSelectedIndex() {
        return stagesListView.getSelectionModel().getSelectedIndex();
    }

}
