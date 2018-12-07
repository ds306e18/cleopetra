package dk.aau.cs.ds306e18.tournament;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Start program with the launcher
        AnchorPane launcherLoader = FXMLLoader.load(Main.class.getResource("ui/layout/Launcher.fxml"));

        primaryStage.setTitle("CleoPetra Launcher");
        primaryStage.setScene(new Scene(launcherLoader));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Hej verden!");
        launch(args);
    }
}
