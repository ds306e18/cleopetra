package dk.aau.cs.ds306e18.tournament;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL resource = getClass().getResource("ui/layout/MainLayout.fxml");
        Parent root = FXMLLoader.load(resource);
        primaryStage.setTitle("DatTournament Runner");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Hola mundo");
        launch(args);
    }
}
