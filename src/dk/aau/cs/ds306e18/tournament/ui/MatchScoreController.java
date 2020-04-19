package dk.aau.cs.ds306e18.tournament.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.text.Text;

import java.io.IOException;

public class MatchScoreController {

    @FXML private Group root;
    @FXML private Text scoreText;

    public void setScoreText(String txt) {
        scoreText.setText(txt);
    }

    public Group getRoot() {
        return root;
    }

    public static MatchScoreController loadNew() {
        try {
            // Load the fxml document into the Controller and JavaFx node.
            FXMLLoader loader = new FXMLLoader(MatchScoreController.class.getResource("layout/MatchScore.fxml"));
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
