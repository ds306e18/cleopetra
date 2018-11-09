package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.ui.controllers.ExitProgramController;
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
        URL resource = getClass().getResource("ui/layout/MainLayout.fxml");
        Parent root = FXMLLoader.load(resource);
        primaryStage.setTitle("DatTournament Runner");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Stage exitStage = new Stage();
        exitStage.initStyle(StageStyle.TRANSPARENT);
        exitStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();

            // Load exit program FXML file
            try {
                AnchorPane exitRoot = FXMLLoader.load(getClass().getResource("ui/layout/Exit.fxml"));

                exitStage.setScene(new Scene(exitRoot));
                exitStage.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        System.out.println("Hej verden!");
        launch(args);
    }
}
