package dk.aau.cs.ds306e18.tournament.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TempVisuals extends Application{

    private Insets standardPaddingInsets = new Insets(5, 5, 5, 5);

    private HBox mid1;
    private VBox mid2;

    private enum MatchStatus {
        TBD, BEFORE, PLAYING, AFTER;

        private Label label;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        addContent(primaryStage);

        primaryStage.show();
    }

    private void addContent(Stage stage) {

        HBox contentAll = new HBox();

        TabPane bracket = bracketSection();
        VBox controls = controlSection();

        contentAll.getChildren().addAll(bracket, controls);

        stage.setScene(new Scene(contentAll));
    }

    /**
     * private VBox topSection(){
     * VBox content = new VBox(10);
     * content.setMinWidth(200);
     * <p>
     * //Top
     * VBox topBox = new VBox();
     * Label topLabel = new Label("Navigation");
     * topBox.setStyle("-fx-border-color: black;");
     * topBox.getChildren().addAll(topLabel);
     * <p>
     * content.getChildren().addAll(topBox);
     * <p>
     * return content;
     * }
     */

    private VBox OldbracketSection() {

        VBox content = new VBox();

        Image image = new Image("http://i.imgur.com/dcRQBS7.png");
        ImageView imageView = new ImageView(image);

        content.getChildren().addAll(imageView);

        return content;
    }

    private TabPane bracketSection() {

        TabPane content = new TabPane();
        content.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //Bracket Tab
        Tab bracketTab = new Tab();
        bracketTab.setText("Bracket Overview");
        Image imageBracket = new Image("http://i.imgur.com/dcRQBS7.png");
        ImageView imageViewBracket = new ImageView(imageBracket);
        bracketTab.setContent(imageViewBracket);

        //Rankings Tab
        Tab rankingsTab = new Tab();
        rankingsTab.setText("Rankings");
        //Image imageRankings = new Image("http://www.besttechtools.com/Content/Images/sql/rank.jpg");
        //ImageView imageViewRankings = new ImageView(imageRankings);
        //rankingsTab.setContent(imageViewRankings);
        //TODO: TEMP Rankings Tab
        Button btTBD = new Button();
        btTBD.setText("TBD");
        btTBD.setOnAction(e -> changeSideControl(MatchStatus.TBD));
        Button btBefore = new Button();
        btBefore.setText("Before");
        btBefore.setOnAction(e -> changeSideControl(MatchStatus.BEFORE));
        Button btPlaying = new Button();
        btPlaying.setText("Playing");
        btPlaying.setOnAction(e -> changeSideControl(MatchStatus.PLAYING));
        Button btAfter = new Button();
        btAfter.setText("After");
        btAfter.setOnAction(e -> changeSideControl(MatchStatus.AFTER));
        rankingsTab.setContent(new HBox(btTBD, btBefore, btPlaying, btAfter));

        //Overlay Control
        Tab overlayTab = new Tab();
        overlayTab.setText("Overlay Control");
        Image imageOverlay = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqX6L7Qpa62YOBrRlINaqBMDrUi5sHuWmwEsQU1lDkRMTqzG8K");
        ImageView imageViewOverlay = new ImageView(imageOverlay);
        overlayTab.setContent(imageViewOverlay);

        //Settings Tab
        Tab settingsTab = new Tab();
        settingsTab.setText("Settings");
        Image imageSettings = new Image("https://training.bitrix24.com/images/framework_en/agent_form.png");
        ImageView imageViewSettings = new ImageView(imageSettings);
        settingsTab.setContent(imageViewSettings);

        content.getTabs().addAll(bracketTab, rankingsTab, overlayTab, settingsTab);

        return content;
    }

    private VBox controlSection() {
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
        mid1 = new HBox(10);
        mid2 = new VBox(10);
        changeSideControl(MatchStatus.PLAYING);

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

    private void tbdButton(){

        System.out.println("PRESSED!");
    }

    private void beforeButton(){

        mid1.getChildren().clear();
        mid2.getChildren().clear();


    }

    /**
     private void playingButton(){

     mid1.getChildren().clear();
     mid2.getChildren().clear();

     //mid1
     Label player1 = new Label("Blue Team: Ajani");
     player1.setTextFill(Color.BLUE);
     Label player2 = new Label("Orange Team: Brutus");
     player2.setTextFill(new javafx.scene.paint.Color(1.0, 0.4, 0, 1));
     mid1.getChildren().addAll(player1, player2);
     mid1.setPadding(standardPaddingInsets);
     mid1.setAlignment(Pos.CENTER);

     //mid2
     Label status = new Label("Game Status: Playing");
     Label ingameScore = new Label("Ingame Score");
     mid2.getChildren().addAll(status, ingameScore);
     mid2.setAlignment(Pos.CENTER);

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

     Button endAndFetch = new Button("End and Fetch");
     mid2.getChildren().addAll(endAndFetch);
     }
     */

    private void afterButton(){

    }

    private void changeSideControl(MatchStatus matchStatus){

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
}
