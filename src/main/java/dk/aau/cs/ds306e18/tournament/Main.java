package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.settings.CleoPetraSettings;
import dk.aau.cs.ds306e18.tournament.ui.LauncherController;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Main extends Application {

    public static final System.Logger LOGGER = System.getLogger(Main.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] %4$s: %5$s %n");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> args = getParameters().getRaw();
        if (!args.isEmpty()) {
            final Path path = Paths.get(args.get(0));
            if (path.toString().endsWith(".rlts") && Files.exists(path)) {
                SaveLoad.loadTournament(new File(path.toUri()));
                Stage systemStage = LauncherController.createSystemStage();
                systemStage.show();
                return;
            }
        }

        // Start program with the launcher
        AnchorPane launcherLoader = FXMLLoader.load(Main.class.getResource("ui/layout/Launcher.fxml"));
        primaryStage.setTitle("CleoPetra Launcher RLBCS'24");
        primaryStage.setScene(new Scene(launcherLoader));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("ui/layout/images/logo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        CleoPetraSettings.setup();
        launch(args);
    }
}
