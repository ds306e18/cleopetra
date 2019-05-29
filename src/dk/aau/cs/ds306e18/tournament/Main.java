package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.rlbot.RLBotCommunication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ini4j.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class Main extends Application {

    public static File lastSavedDirectory = new File(System.getProperty("user.home"));

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Start program with the launcher
        AnchorPane launcherLoader = FXMLLoader.load(Main.class.getResource("ui/layout/Launcher.fxml"));
        Config.getGlobal().setLineSeparator("\n");
        primaryStage.setTitle("CleoPetra Launcher");
        primaryStage.setScene(new Scene(launcherLoader));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("ui/layout/images/logo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Hej verden!");
        launch(args);
    }
}
