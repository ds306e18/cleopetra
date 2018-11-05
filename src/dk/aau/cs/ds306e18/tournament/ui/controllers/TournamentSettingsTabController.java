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

public class TournamentSettingsTabController {

    int testID = 0;

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


    /* TODO: Clean code && Comments
       TODO: Maybe add int rounds to Stage model?
       TODO: Fix and store the number of rounds for a stage somewhere.
       TODO: Is seeding method still relevant? if so, create the observable list.
       TODO: Maybe add drag functionality to the listview to choose stage order.
       TODO: Look more into having a getJavaFxNode.
    */

    @FXML
    private void initialize() {
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
            if (selectedStage != null && StageFormatOption.getOption(selectedStage.getFormat()) != StageFormatOption.ROUND_ROBIN) {
                roundsHBox.setVisible(false);
            } else {
                roundsHBox.setVisible(true);
            }

        });
        formatChoicebox.getSelectionModel().select(0);

        /* Hide stage settings by default and if the listview is empty. */
        stageSettingsVBox.setVisible(false);

        stagesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stagesListView.getItems().size() != 0) {
                stageSettingsVBox.setVisible(true);
            } else {
                stageSettingsVBox.setVisible(false);
            }

            setContent();
        });

        /* Testing drag and drop */
        ListCell thisCell = (ListCell) stagesListView.getCellFactory();

        stagesListView.setOnDragDetected(event -> {
            System.out.println("setOnDragDetected");

            if (thisCell.getItem() == null) {
                return;
            }

            ObservableList<Stage> items = stagesListView.getItems();

            Dragboard dragBoard = thisCell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            dragBoard.setContent(content);
            event.consume();
        });


        stagesListView.setOnDragEntered(dragEvent -> {
            System.out.println("setOnDragEntered");

            stagesListView.setBlendMode(BlendMode.DIFFERENCE);
        });


        stagesListView.setOnDragOver(dragEvent -> {
            System.out.println("setOnDragOver");

            dragEvent.acceptTransferModes(TransferMode.MOVE);
            dragEvent.consume();
        });


        stagesListView.setOnDragDropped(dragEvent -> {
            System.out.println("setOnDragDropped");
            if (stagesListView.getItems() == null) {
                return;
            }

            Dragboard db = dragEvent.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<Stage> items = stagesListView.getItems();
                int draggedindex = items.indexOf(db.getString());

            }


            dragEvent.setDropCompleted(true);
        });


        stagesListView.setOnDragExited(dragEvent -> {
            System.out.println("setOnDragExited");

            stagesListView.setBlendMode(null);
        });

        stagesListView.setOnDragDone(dragEvent -> System.out.println("setOnDragDone"));
    }

    @FXML
    void nameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().setName(nameTextField.getText());
    }

    @FXML
    void stageNameTextFieldOnKeyReleased(KeyEvent event) {
        Stage selectedItem = stagesListView.getSelectionModel().getSelectedItem();
        selectedItem.setName(stageNameTextfield.getText());
        stagesListView.refresh();
    }

    @FXML
    void addStageBtnOnAction(ActionEvent actionEvent) {
        Tournament.get().addStage(new Stage("New Stage" + testID, new SwissStage()));
        testID++;
        stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
        stagesListView.refresh();
    }

    @FXML
    void removeStageBtnOnAction(ActionEvent actionEvent) {
        int selectedIndex = stagesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Tournament.get().removeStage(selectedIndex);
            stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
            stagesListView.refresh();
        }
    }

    private void setContent() {
        Stage selectedItem = stagesListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            stageNameTextfield.setText(selectedItem.getName());
            formatChoicebox.getSelectionModel().select(StageFormatOption.getOption(selectedItem.getFormat()));
        }
    }


}
