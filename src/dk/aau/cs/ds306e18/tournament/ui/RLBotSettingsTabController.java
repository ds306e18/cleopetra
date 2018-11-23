package dk.aau.cs.ds306e18.tournament.ui;

import com.google.common.base.CharMatcher;
import dk.aau.cs.ds306e18.tournament.RLBotSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class RLBotSettingsTabController {

    @FXML private VBox tabRoot;
    @FXML private Button configPathBtn;
    @FXML private TextField configPathTextField;
    @FXML private CheckBox autoCloseRLBotCheckBox;

    final private FileChooser fileChooser = new FileChooser();

    @FXML
    private void initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CFG files (*.cfg)", "*.cfg"));
    }

    @FXML
    private void configPathBtnOnAction(ActionEvent actionEvent) {
        // Open file chooser
        File file = fileChooser.showOpenDialog((Stage) tabRoot.getScene().getWindow());
        if (file != null) {

            // Next file chooser will now start in the folder of last selected file
            fileChooser.setInitialDirectory(file.getParentFile());

            // Update settings
            RLBotSettings.setConfigPath(file.getAbsolutePath());

            // Update text field
            String parentparent = file.getParentFile().getParent();
            String shortPath = parentparent == null ? file.getPath() : file.getPath().replace(parentparent, "");
            configPathTextField.setText(shortPath);
        }
    }
}
