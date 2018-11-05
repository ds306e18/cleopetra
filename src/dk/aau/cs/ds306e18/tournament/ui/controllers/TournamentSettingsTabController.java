package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.*;
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
    @FXML private ChoiceBox<Format> formatChoicebox;
    @FXML private ChoiceBox<Object> seedingMethodChoicebox;
    @FXML private TextField roundsTextfield;

    @FXML private void initialize() {
        nameTextField.setText(Tournament.get().getName());

        tieBreakerChoiceBox.setItems(FXCollections.observableArrayList(new TieBreakerBySeed()));
        tieBreakerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Tournament.get().setTieBreaker(newValue));
        tieBreakerChoiceBox.getSelectionModel().select(0);
    }

    @FXML void nameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().setName(nameTextField.getText());
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
        int selectedIndex = stagesListView.getSelectionModel().getSelectedIndex();
        stageNameTextfield.setText(Tournament.get().getStages().get(selectedIndex).getName());
    }

    @FXML
    void stageNameTextFieldOnKeyReleased(KeyEvent event) {

    }

}
