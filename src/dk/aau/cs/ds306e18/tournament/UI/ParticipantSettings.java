package dk.aau.cs.ds306e18.tournament.UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class ParticipantSettings  extends NavigationFrame{

    private Insets standardPaddingInsets = new Insets(5, 8, 8, 8);
    private int id = 1;

    @Override
    ArrayList<Tab> addContent() {

        Tab participantSettings = new Tab();
        participantSettings.setText("Participant Settings");

        HBox contentAll = new HBox();
        VBox participantBox = participantBox();
//        VBox filler = fillerBox();
        VBox teamBox = teamsBox();
        VBox botInfo = botBox();
//        VBox secondContent = stageSettings();

        //Content all is the key point
        contentAll.getChildren().addAll(participantBox, teamBox, botInfo);

        participantSettings.setContent(contentAll);

        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(participantSettings);

        return tabs;
    }

    private VBox teamsBox(){

        VBox teamBox = new VBox();
        Label tournamentName = new Label("Team name:");
        TextField textField = new TextField();

        teamBox.setPadding(standardPaddingInsets);
        teamBox.getChildren().addAll(tournamentName, textField);

        return teamBox;
    }

    private VBox botBox(){
        VBox botBox = new VBox();
        Label botName = new Label("Bot name:");
        TextField namefield = new TextField();
        Label devName = new Label("Developer:");
        TextField devfield = new TextField();
        Label configPath = new Label("Config path:");
        TextField configField = new TextField();

        Label description = new Label("Description");
        TextArea descField = new TextArea();


        botBox.getChildren().addAll(botName,namefield,devName,devfield,configPath,configField,description,descField);

        return botBox;
    }

    private VBox participantBox(){
        VBox content = new VBox();

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Participant settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // team overview table
        Label stageHeader = new Label("Teams");
        stageHeader.setFont(new Font("Arial", 15));

        TableView table = new TableView();
        table.setEditable(true);

        TableColumn stageIdCol = new TableColumn("ID");
        stageIdCol.setResizable(false);
        stageIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn stageNameCol = new TableColumn("Team name");
        stageNameCol.setResizable(false);
        stageNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(stageIdCol, stageNameCol);

        HBox buttons = new HBox();
        Button addBtn = new Button("Add Team");
        Button deleteBtn = new Button("Delete Team");
        buttons.setSpacing(10);

        addBtn.setOnAction(e -> {
            dk.aau.cs.ds306e18.tournament.UI.Stage tempStage = new dk.aau.cs.ds306e18.tournament.UI.Stage(id, "Team " + id);
            id += 1;
            table.getItems().add(tempStage);
        });

        deleteBtn.setOnAction(e -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                Object selectedItem = table.getSelectionModel().getSelectedItem();
                table.getItems().remove(selectedItem);
                id -= 1;
            }
        });

        buttons.getChildren().addAll(addBtn, deleteBtn);


        VBox stageBox = new VBox();
        stageBox.setSpacing(5);
        stageBox.setPadding(standardPaddingInsets);
        stageBox.getChildren().addAll(stageHeader, table, buttons);

        content.getChildren().addAll(header, stageBox);

        return content;
    }

}
