package dk.aau.cs.ds306e18.tournament.UI.Tabs;

import dk.aau.cs.ds306e18.tournament.UI.TextField;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class ParticipantSettings  extends Tab{

    private Insets standardPaddingInsets = new Insets(5, 8, 8, 8);
    private int id = 1;

    public ParticipantSettings() {

        this.setText("Participant Settings");

        HBox contentAll = new HBox();
        VBox participantBox = participantBox();
        VBox teamBox = teamsBox();
        VBox botInfo = botBox();

        //Content all is the key point
        contentAll.getChildren().addAll(participantBox, teamBox, botInfo);

        this.setContent(contentAll);

        //ArrayList<Tab> tabs = new ArrayList<>();
        //tabs.add(participantSettings);

    }

    private VBox teamsBox(){

        VBox header = makeHeader("Team Settings");

        VBox teamBox = new VBox();
        Label tournamentName = new Label("Team name:");
        dk.aau.cs.ds306e18.tournament.UI.TextField textField = new dk.aau.cs.ds306e18.tournament.UI.TextField();

        TableView table = new TableView();
        table.setEditable(true);

        TableColumn stageIdCol = new TableColumn("ID");
        stageIdCol.setResizable(false);
        stageIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn stageNameCol = new TableColumn("Bot name");
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

        Label stageHeader = new Label("Bots");
        stageHeader.setFont(new Font("Arial", 15));
        table.setMaxHeight(200);

        buttons.getChildren().addAll(addBtn, deleteBtn);

        teamBox.setPadding(standardPaddingInsets);
        teamBox.setSpacing(5);
        teamBox.getChildren().addAll(header, tournamentName, textField,stageHeader, table, buttons);

        return teamBox;
    }

    private VBox botBox(){

        VBox header = makeHeader("Bot Settings");

        VBox botBox = new VBox();
        Label botName = new Label("Bot name:");
        dk.aau.cs.ds306e18.tournament.UI.TextField namefield = new dk.aau.cs.ds306e18.tournament.UI.TextField();
        Label devName = new Label("Developer:");
        dk.aau.cs.ds306e18.tournament.UI.TextField devfield = new dk.aau.cs.ds306e18.tournament.UI.TextField();
        Label configPath = new Label("Config path:");
        dk.aau.cs.ds306e18.tournament.UI.TextField configField = new TextField();

        Label description = new Label("Description");
        TextArea descField = new TextArea();
        descField.setMaxWidth(250);
        botBox.setSpacing(5);
        botBox.setPadding(standardPaddingInsets);

        botBox.getChildren().addAll(header,botName,namefield,devName,devfield,configPath,configField,description,descField);

        return botBox;
    }

    private VBox participantBox(){
        VBox content = new VBox();

        // Header
        VBox header = makeHeader("Participation Settings");

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

    VBox makeHeader(String string){
        VBox header = new VBox();
        Label headerText = new Label(string);
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        return header;
    }
}
