package dk.aau.cs.ds306e18.tournament.UI;

import javafx.application.Application;
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
        Button newButton = new Button();
        newButton.setText("Create new Tournament");
        newButton.setOnAction(e-> {
            try {
                createNewTournament(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        Button openButton = new Button();
        openButton.setText("Open Local tournament");
        openButton.setOnAction(e->openLocalTournament());

        Button importButton = new Button();
        importButton.setText("Tournament Running");
        importButton.setOnAction(e-> {
            try {
                importTournament(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        Button exitButton = new Button();
        exitButton.setText("Exit");
        exitButton.setOnAction(e->exitLauncher());

          Image image = new Image("https://i.imgur.com/KE6nXem.png");
        ImageView imageView = new ImageView(image);

        //Changes the sizes of the elements in the HBox
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        newButton.setPrefSize(200,20);
        openButton.setPrefSize(200,20);
        importButton.setPrefSize(200,20);
        exitButton.setPrefSize(100,20);

        StackPane root = new StackPane();

        VBox menu = new VBox();
        menu.getChildren().addAll(imageView, newButton,openButton,importButton,exitButton);
        menu.setAlignment(Pos.CENTER);

        root.getChildren().add(menu);

        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();
    }

    private void importTournament(Stage primaryStage) throws Exception {
        primaryStage.close();
        TempVisuals2 tv2 = new TempVisuals2();
        tv2.startWindow(primaryStage);    }

    private void openLocalTournament() {
        System.out.println("Not implemented");
    }

    private void createNewTournament(Stage primaryStage) throws Exception {
        //Closes the launcher opens the tournamentsettings
        primaryStage.close();
        TournamentSettings ts = new TournamentSettings();
        ts.startWindow(primaryStage);



    }

    private void exitLauncher(){
        System.exit(0);
    }


}
