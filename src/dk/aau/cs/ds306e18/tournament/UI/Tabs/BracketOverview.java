package dk.aau.cs.ds306e18.tournament.UI.Tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class BracketOverview extends Tab{

    //TODO Should be deleted and the other match status' should be used.
    private enum MatchStatus {
        TBD, BEFORE, PLAYING, AFTER;

        private Label label;
    }

    private Insets standardPaddingInsets = new Insets(5, 5, 5, 5);
/**
     TournamentRunning(){

        Tab bracketTab = bracketTab();
        Tab rankingsTab = rankingsTab();
        Tab overlayControl = overlayControlTab();
//        Tab settings = settingsTab();

        bracketTab.setText("Bracket Overview");
        rankingsTab.setText("Ranking");
        overlayControl.setText("Overlay Control");
//        settings.setText("Settings");

        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(bracketTab);
        tabs.add(rankingsTab);
        tabs.add(overlayControl);
//        tabs.add(settings);
//        tabs.addAll(new TournamentSettings().addContent());

    }
*/
public BracketOverview(){

         this.setText("Bracket Overview");
         HBox content = new HBox();

        //Bracket overview
        HBox bracketOverview = bracketOverview();

        //Control panel
        VBox controlpanel = bracketControlpanel();
        content.getChildren().addAll(bracketOverview, controlpanel);
        this.setContent(content);

    }

    private HBox bracketOverview(){

        HBox content = new HBox();

        Image imageBracket = new Image("http://i.imgur.com/dcRQBS7.png");
        ImageView imageViewBracket = new ImageView(imageBracket);

        content.getChildren().add(imageViewBracket);

        return content;
    }

    private VBox bracketControlpanel(){

        VBox content = new VBox();
        content.setMinWidth(200);
        //content.setStyle("-fx-border-style: solid solid none solid; -fx-border-color: none none none black;");
        content.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
                null, null, null)));

        //Top
        VBox topBox = new VBox();
        Label topLabel = new Label("Selected Match");
        //topBox.setStyle("-fx-border-color: white white black white;");
        topBox.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                null, null, null)));
        topBox.setAlignment(Pos.CENTER);
        topBox.setMinWidth(280);
        topBox.getChildren().addAll(topLabel);
        topBox.setPadding(standardPaddingInsets);

        //midSection 1 //midSection 2
        HBox mid1 = new HBox(10);
        VBox mid2 = new VBox(10);
        bracketControlpanelChange(MatchStatus.PLAYING, mid1, mid2);

        //midSections3 SPACE
        VBox mid3 = new VBox();

        // Bottom
        HBox bottomBox = new HBox(10);
        HBox bottomBox2 = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox2.setAlignment(Pos.CENTER);
        Button nextMatch = new Button("Next match");
        Button nextStage = new Button("Next stage");
        Button previousMatch = new Button("Previous match");
        Button previousStage = new Button("Previous stage");
        bottomBox2.getChildren().addAll(previousMatch, previousStage);
        bottomBox.getChildren().addAll(nextMatch, nextStage);
        bottomBox.setStyle("-fx-border-color: black white white white;"); //TODO BUG
        bottomBox.setPadding(standardPaddingInsets);

        VBox bottomBoxContainer = new VBox();
        bottomBoxContainer.getChildren().addAll(bottomBox, bottomBox2);

        content.setVgrow(mid3, Priority.ALWAYS);
        content.getChildren().addAll(topBox, mid1, mid2, mid3, bottomBoxContainer);

        return content;
    }

    /** TODO: used for getting the right box for the control panel. */
    private void bracketControlpanelChange(MatchStatus matchStatus, HBox mid1, VBox mid2){
        mid1.getChildren().clear();
        mid2.getChildren().clear();

        //mid1
        Label player1 = new Label((matchStatus == matchStatus.TBD) ? "Blue Team: TBD" : "Blue Team: Ajani");
        player1.setTextFill(Color.BLUE);
        Label player2 = new Label((matchStatus == matchStatus.TBD) ? "Orange Team: TBD" : "Orange Team: Brutus");
        player2.setTextFill(new javafx.scene.paint.Color(1.0, 0.4, 0, 1));
        mid1.getChildren().addAll(player1, player2);
        mid1.setPadding(standardPaddingInsets);
        mid1.setAlignment(Pos.CENTER);
        mid2.setAlignment(Pos.CENTER);

        //mid2
        Label status = new Label((matchStatus == matchStatus.TBD) ? "Game Status: TBD" : "Game Status: Playing");
        if(matchStatus == matchStatus.TBD) {
            return;
        }

        if(matchStatus == matchStatus.BEFORE){
            Button buttonStartInRL = new Button("Start in RL");
            Button buttonSetResult = new Button("Set result");
            HBox tempHBox = new HBox(buttonStartInRL, buttonSetResult);
            tempHBox.setAlignment(Pos.CENTER);
            tempHBox.setSpacing(10);
            mid2.getChildren().addAll(tempHBox);
            return;
        }

        Label ingameScore = new Label((matchStatus == matchStatus.PLAYING) ? "Ingame Score" : "Winner: Blue Team");
        mid2.getChildren().addAll(status, ingameScore);

        HBox row1 = new HBox();
        row1.setAlignment(Pos.CENTER);
        VBox row11 = new VBox();
        Label row11Label = new Label("Blue");
        row11.getChildren().addAll(row11Label);
        row11.setAlignment(Pos.CENTER);
        row11.setPrefWidth(55);
        row11.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                null, null, null)));
        VBox row12 = new VBox();
        Label row12Label = new Label("Orange");
        row12.setAlignment(Pos.CENTER);
        row12.getChildren().addAll(row12Label);
        row12.setPrefWidth(55);
        row12.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                null, null, null)));
        row1.getChildren().addAll(row11, row12);
        HBox row2 = new HBox();
        row2.setAlignment(Pos.CENTER);
        VBox row21 = new VBox();
        Label row21Label = new Label("5");
        row21.getChildren().addAll(row21Label);
        row21.setStyle("-fx-padding: 5");
        row21.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                null, null, null)));
        VBox row22 = new VBox();
        Label row22Label = new Label("1");
        row22.getChildren().addAll(row22Label);
        row22.setStyle("-fx-padding: 5");
        row2.getChildren().addAll(row21, row22);
        VBox rowCollecter = new VBox(); //For no space
        rowCollecter.getChildren().addAll(row1, row2);
        mid2.getChildren().addAll(rowCollecter);

        HBox buttonHBox = new HBox();
        if(matchStatus == matchStatus.PLAYING){
            Button endAndFetch = new Button("End and Fetch");
            mid2.getChildren().addAll(endAndFetch);
            return;
        }

        Button newButton = new Button("Edit result");
        Button newSButton = new Button("Restart in RL");
        HBox tempHBox = new HBox(newButton, newSButton);
        tempHBox.setAlignment(Pos.CENTER);
        tempHBox.setSpacing(10);
        mid2.getChildren().addAll(tempHBox);
    }




    /** TODO This function is a placeholder for buttons. */
    private void tempHello(MatchStatus matchStatus){
        System.out.println("Hello");
    }
}