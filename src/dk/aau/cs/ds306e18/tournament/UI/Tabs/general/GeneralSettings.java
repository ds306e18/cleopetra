package dk.aau.cs.ds306e18.tournament.UI.Tabs.general;

import dk.aau.cs.ds306e18.tournament.model.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.function.Consumer;

public class GeneralSettings extends VBox {

    private Insets standardPaddingInsets = new Insets(8, 8, 8, 8);
    private Consumer<Stage> onStageSelected;

    public GeneralSettings() {
        setMinWidth(260);

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Tournament settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // Tournament name
        VBox tournamentNameBox = new VBox();
        Label tournamentNameLabel = new Label("Tournament name:");
        TextField tournamentNameTextField = new TextField(Tournament.get().getName());
        tournamentNameTextField.setOnKeyReleased(e -> Tournament.get().setName(tournamentNameTextField.getText()));
        tournamentNameBox.setPadding(standardPaddingInsets);
        tournamentNameBox.getChildren().addAll(tournamentNameLabel, tournamentNameTextField);

        // Tiebreaker
        BorderPane tieBreaker = new BorderPane();
        Label tieBreakerLabel = new Label("Tiebreak by: ");
        ChoiceBox<TieBreaker> tieBreakerChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(new TieBreakerBySeed()));
        tieBreakerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Tournament.get().setTieBreaker(newValue));
        tieBreakerChoiceBox.getSelectionModel().select(0);
        tieBreaker.setPadding(standardPaddingInsets);
        tieBreaker.setLeft(tieBreakerLabel);
        tieBreaker.setRight(tieBreakerChoiceBox);

        // Stage list
        Label stageHeader = new Label("Stages");
        stageHeader.setFont(new Font("Arial", 15));
        ListView<Stage> stageListView = new ListView<>(FXCollections.observableArrayList(Tournament.get().getStages()));
        stageListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (onStageSelected != null) onStageSelected.accept(newValue);
        });

        // Stage buttons
        HBox stageButtons = new HBox();
        Button stageAddBtn = new Button("Add stage");
        Button stageDeleteBtn = new Button("Remove stage");
        stageButtons.setSpacing(10);
        stageButtons.getChildren().addAll(stageAddBtn, stageDeleteBtn);
        stageAddBtn.setOnAction(e -> {
            Tournament.get().addStage(new Stage("New Stage", new SwissStage()));
            stageListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
            stageListView.refresh();
        });
        stageDeleteBtn.setOnAction(e -> {
            int selectedIndex = stageListView.getFocusModel().focusedIndexProperty().intValue();
            Tournament.get().removeStage(selectedIndex);
            stageListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
            stageListView.refresh();
        });
        VBox stageBox = new VBox();
        stageBox.setSpacing(5);
        stageBox.setPadding(standardPaddingInsets);
        stageBox.getChildren().addAll(stageHeader, stageListView, stageButtons);

        getChildren().addAll(header, tournamentNameBox, tieBreaker, stageBox);
    }

    public void setOnStageSelected(Consumer<Stage> onStageSelected) {
        this.onStageSelected = onStageSelected;
    }
}
