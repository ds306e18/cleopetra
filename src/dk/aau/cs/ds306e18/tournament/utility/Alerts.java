package dk.aau.cs.ds306e18.tournament.utility;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Alerts {

    /**
     * Shows an information notification in the bottom right corner of the screen. Fades out after 3 seconds.
     * @param title The title of the notification.
     * @param text A short and precise explanation of the information wished to provide.
     */
    public static void infoNotification (String title, String text){
        Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT)
                .showInformation();
    }

    /**
     * Shows an error notification in the bottom right corner of the screen. Fades out after 5 seconds.
     * @param title The title of the notification
     * @param text A short and precise explanation of the error.
     */
    public static void errorNotification (String title, String text){
        Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .showError();
    }

    /**
     * Shows a confirmation box that requires the user to choose an option (OK or Cancel)
     * @param title The title of the alert.
     * @param text A short and precise explanation of the confirmation text.
     * @return Returns true if the user clicked OK, otherwise returns false.
     */
    public static boolean confirmAlert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        return alert.showAndWait().get() == ButtonType.OK;
    }
}
