package dk.aau.cs.ds306e18.tournament.UI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application{

    String version = "v 0.1";

    public Launcher startLauncher(String[] args){
        launch(args);
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TournamentSystem Launcher " + version);
        Button btn1 = new Button();
        btn1.setText("Create new Tournament");
        btn1.setOnAction(e->createNewTournament());

        Button btn2 = new Button();
        btn2.setText("Open Local tournament");
        btn2.setOnAction(e->openLocalTournament());

        Button btn3 = new Button();
        btn3.setText("Import from ...");
        btn3.setOnAction(e->importTournament());

        Button btn4 = new Button();
        btn4.setText("Exit");
        btn4.setOnAction(e->exitLauncher());

          Image image = new Image("https://i.imgur.com/KE6nXem.png");
        ImageView imageView = new ImageView(image);

        //Changes the sizes of the elements in the HBox
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        btn1.setPrefSize(200,20);
        btn2.setPrefSize(200,20);
        btn3.setPrefSize(200,20);
        btn4.setPrefSize(100,20);

        StackPane root = new StackPane();

        VBox menu = new VBox();
        menu.getChildren().addAll(imageView, btn1,btn2,btn3,btn4);
        menu.setAlignment(Pos.CENTER);

        root.getChildren().add(menu);

        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();
    }

    private void importTournament() {
    }

    private void openLocalTournament() {

    }

    private void createNewTournament(){

    }
    
    private void exitLauncher(){
        System.exit(0);
    }


}
