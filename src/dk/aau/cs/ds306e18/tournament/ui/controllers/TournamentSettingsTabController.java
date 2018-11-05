package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.oldui.Tabs.general.StageSettings;
import dk.aau.cs.ds306e18.tournament.ui.StageFormatOption;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TournamentSettingsTabController {

    @FXML private GridPane tournamentSettingsTab;
    @FXML private TextField nameTextField;
    @FXML private ChoiceBox<TieBreaker> tieBreakerChoiceBox;
    @FXML private ListView<Stage> stagesListView;
    @FXML private Button addStageBtn;
    @FXML private Button removeStageBtn;
    @FXML private VBox stageSettingsVBox;
    @FXML private Text selectStageText;
    @FXML private Text stageSettingsHeadLabel;
    @FXML private HBox stageSettingsContent;
    @FXML private TextField stageNameTextfield;
    @FXML private ChoiceBox<StageFormatOption> formatChoicebox;
    @FXML private ChoiceBox<Object> seedingMethodChoicebox;
    @FXML private TextField roundsTextfield;
    @FXML private HBox roundsHBox;

    @FXML private void initialize() {
        nameTextField.setText(Tournament.get().getName());

        tieBreakerChoiceBox.setItems(FXCollections.observableArrayList(new TieBreakerBySeed()));
        tieBreakerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Tournament.get().setTieBreaker(newValue));
        tieBreakerChoiceBox.getSelectionModel().select(0);

        // Hide rounds by default
        roundsHBox.setVisible(false);

        /* Stage format initial items and setting new format for a stage */
        formatChoicebox.setItems(FXCollections.observableArrayList(StageFormatOption.values()));
        formatChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Stage selectedStage = stagesListView.getSelectionModel().getSelectedItem();
            if (selectedStage != null && StageFormatOption.getOption(selectedStage.getFormat()) != newValue) {
                selectedStage.setFormat(newValue.getNewInstance());
            }

            /* Check to see if rounds should be visible or not */
            if (selectedStage != null && StageFormatOption.getOption(selectedStage.getFormat()) != StageFormatOption.ROUND_ROBIN){
                roundsHBox.setVisible(false);
            } else {
                roundsHBox.setVisible(true);
            }

        });
        formatChoicebox.getSelectionModel().select(0);

        /* Hide stage settings by default and if the listview is empty. */
        stageSettingsVBox.setVisible(false);

        stagesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stagesListView.getItems().size() != 0){
                stageSettingsVBox.setVisible(true);
            } else {
                stageSettingsVBox.setVisible(false);
            }
        });
    }

    @FXML void nameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().setName(nameTextField.getText());
    }

    @FXML void stageNameTextFieldOnKeyReleased(KeyEvent event) {
        Stage selectedItem = stagesListView.getSelectionModel().getSelectedItem();
        selectedItem.setName(stageNameTextfield.getText());
        stagesListView.refresh();
    }

    @FXML void addStageBtnOnAction(ActionEvent actionEvent) {
        Tournament.get().addStage(new Stage("New Stage", new SwissStage()));
        stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
        stagesListView.refresh();
    }

    @FXML void removeStageBtnOnAction(ActionEvent actionEvent) {
        int selectedIndex = stagesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Tournament.get().removeStage(selectedIndex);
            stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
            stagesListView.refresh();
        }
    }
    @FXML void setContent(MouseEvent event) {
        Stage selectedItem = stagesListView.getSelectionModel().getSelectedItem();
        stageNameTextfield.setText(selectedItem.getName());
        formatChoicebox.getSelectionModel().select(StageFormatOption.getOption(selectedItem.getFormat()));
    }



}
