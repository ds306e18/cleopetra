package dk.aau.cs.ds306e18.tournament.ui;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * This abstract class is used to make pop-up windows draggable.
 */
public abstract class DraggablePopupWindow {

    private double x = 0;
    private double y = 0;

    protected void windowDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    protected void windowPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
}
