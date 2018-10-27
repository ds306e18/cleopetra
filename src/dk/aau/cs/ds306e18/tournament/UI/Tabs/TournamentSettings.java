package dk.aau.cs.ds306e18.tournament.UI.Tabs;

import dk.aau.cs.ds306e18.tournament.model.*;
import javafx.collections.FXCollections;
import dk.aau.cs.ds306e18.tournament.UI.FixedTextField;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.swing.text.Utilities;
import java.util.Random;


public class TournamentSettings extends Tab {

    //Fields
    private Insets standardPaddingInsets = new Insets(5, 8, 8, 8);
    private int id = 1;

    /** TournamentSettings extends Tab and creates a tab with a specific layout*/
    public TournamentSettings() {

        this.setText("Tournament Settings");

        HBox contentAll = new HBox();
        VBox mainContent = mainSettings();
        VBox filler = fillerBox();
        VBox secondContent = stageSettings();

        //Content all is the key point
        contentAll.getChildren().addAll(mainContent, filler, secondContent);
        this.setContent(contentAll);

        //ArrayList<Tab> tabs = new ArrayList<>();
        //tabs.add(tournamentSettings);
        //tabs.addAll(new ParticipantSettings().addContent());

    }

    /** Creates an empty box to create space on the tab
     * @return a blank VBox     */
    private VBox fillerBox(){
        VBox content = new VBox();
        content.setMinWidth(100);

        return content;
    }

    /**
     * Creates the main settings VBox, it contains a table with functioning buttons, a header and a dropdown menu
     * @return a specific VBox*/

    private VBox mainSettings(){
        VBox content = new VBox();
        content.setMinWidth(200);

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Tournament settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // Tournament general settings
        VBox tournamentNameBox = new VBox();
        Label tournamentNameLabel = new Label("Tournament name:");
        FixedTextField tournamentNameTextField = new FixedTextField();
        tournamentNameTextField.setText(Tournament.get().getName());
        tournamentNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // Update model when focus is lost
            if (!newValue) Tournament.get().setName(tournamentNameTextField.getText());
        });
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

        // Stage list and buttons
        Label stageHeader = new Label("Stages");
        stageHeader.setFont(new Font("Arial", 15));
        ListView<Stage> stageListView = new ListView<>(FXCollections.observableArrayList(Tournament.get().getStages()));
        HBox stageButtons = new HBox();
        Button stageAddBtn = new Button("Add stage");
        Button stageDeleteBtn = new Button("Remove stage");
        stageButtons.setSpacing(10);
        stageButtons.getChildren().addAll(stageAddBtn, stageDeleteBtn);
        stageAddBtn.setOnAction(e -> {
            Tournament.get().addStage(new Stage("New Stage" + new Random().nextInt(100), new SwissStage()));
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



        content.getChildren().addAll(header, tournamentNameBox, tieBreaker, stageBox);

        return content;
    }

    /**
     * Creates a StageSettings VBox, that contains a header, textfield, checkbox and menufield.
     * @return a specific VBOx with a header, textfield, checkbox
     */
    private VBox stageSettings (){
        VBox content = new VBox();
        content.setMinWidth(300);

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Stage settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // Stage settings
        VBox settings = new VBox();
        Label stageName = new Label("Stage name:");
        FixedTextField stageNameTf = new FixedTextField();
        stageNameTf.setMaxWidth(250);
        Label format = new Label("Format:");
        MenuButton formatMenu = new MenuButton("Choose format");
        Label seedingMethod = new Label("Seeding method:");
        MenuButton seedingMenu = new MenuButton("Choose method");

        Label participants = new Label("Participants");
        participants.setFont(new Font("Arial", 15));
        participants.setPadding(new Insets(25, 0 ,5, 0));

        CheckBox checkBox = new CheckBox("Use top 3 participants from previous stage?");

        HBox buttons = new HBox();
        Button addBtn = new Button("Apply changes");
        Button deleteBtn = new Button("Revert");
        buttons.setSpacing(10);

        buttons.setPadding(new Insets(25, 0, 0 , 0));
        buttons.getChildren().addAll(addBtn, deleteBtn);

        settings.setPadding(standardPaddingInsets);
        settings.getChildren().addAll(stageName, stageNameTf, format, formatMenu,seedingMethod, seedingMenu,participants, checkBox, buttons);


        content.getChildren().addAll(header, settings);

        return content;
    }
}
