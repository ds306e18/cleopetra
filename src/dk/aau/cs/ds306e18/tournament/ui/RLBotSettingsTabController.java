package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.utility.configuration.RLBotConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class RLBotSettingsTabController {

    @FXML
    private VBox tabRoot;
    @FXML
    private Button configPathBtn;
    @FXML
    private TextField configPathTextField;
    @FXML
    private CheckBox autoCloseRLBotCheckBox;

    final private FileChooser fileChooser = new FileChooser();

    @FXML
    private void initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CFG files (*.cfg)", "*.cfg"));
        updateConfigPathTextField();
    }

    /**
     * Updates the text shown in the config path text field.
     */
    private void updateConfigPathTextField() {
        String path = Tournament.get().getRlBotSettings().getConfigPath();
        if (path != null && !path.isEmpty()) {
            File file = new File(Tournament.get().getRlBotSettings().getConfigPath());
            String parentparent = file.getParentFile().getParent();
            String shortPath = parentparent == null ? file.getPath() : file.getPath().replace(parentparent, "");
            configPathTextField.setText(shortPath);
        }
    }

    @FXML
    private void configPathBtnOnAction(ActionEvent actionEvent) {
        // Open file chooser
        File file = fileChooser.showOpenDialog((Stage) tabRoot.getScene().getWindow());
        if (file != null) {

            // Next file chooser will now start in the folder of last selected file
            fileChooser.setInitialDirectory(file.getParentFile());
            RLBotConfig RLBotInfo = new RLBotConfig(file.getAbsolutePath());
            // Update settings
            if (RLBotInfo.isValid()) {
                Tournament.get().getRlBotSettings().setConfigPath(file.getAbsolutePath());
                updateConfigPathTextField();
            }
        }
    }
}
