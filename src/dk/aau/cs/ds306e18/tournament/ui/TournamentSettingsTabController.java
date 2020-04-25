package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Stage;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Collections;

public class TournamentSettingsTabController {

    public static TournamentSettingsTabController instance;

    @FXML private GridPane tournamentSettingsTab;
    @FXML private TextField nameTextField;
    @FXML private ListView<Stage> stagesListView;
    @FXML private Button addStageBtn;
    @FXML private Button removeStageBtn;
    @FXML private VBox stageSettingsVBox;
    @FXML private Text selectStageText;
    @FXML private Text stageSettingsHeadLabel;
    @FXML private HBox stageSettingsContent;
    @FXML private TextField stageNameTextfield;
    @FXML private Spinner<Integer> defaultSeriesLengthSpinner;
    @FXML private ChoiceBox<StageFormatOption> formatChoicebox;
    @FXML private Button swapUp;
    @FXML private Button swapDown;
    @FXML private VBox formatUniqueSettingsHolder;
    @FXML private Spinner<Integer> teamsInStageSpinner;
    @FXML private Label teamsInStageAll;

    @FXML
    private void initialize() {
        instance = this;

        setUpStageListView();
        updateGeneralTournamentSettings();

        // Setup teams wanted in stage spinner
        teamsInStageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE));
        teamsInStageSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue); // This will throw the exception if the text is not a number
                getSelectedStage().setNumberOfTeamsWanted(value);
            } catch (NumberFormatException e) {
                teamsInStageSpinner.getEditor().setText(oldValue);
            }
        });

        // Setup default series length spinner
        defaultSeriesLengthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 21, 1, 2));
        defaultSeriesLengthSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                Integer.parseInt(newValue); // This will throw the exception if the text is not a number
            } catch (NumberFormatException e) {
                defaultSeriesLengthSpinner.getEditor().setText(oldValue);
            }
        });
        defaultSeriesLengthSpinner.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            int length = Integer.parseInt(defaultSeriesLengthSpinner.getEditor().getText());
            if (length % 2 == 0) length++;
            int safeLength = Math.max(length, 1);
            defaultSeriesLengthSpinner.getEditor().setText("" + safeLength);
            getSelectedStage().getFormat().setDefaultSeriesLength(safeLength);
        });

        // Retrieve possible formats and add to a choicebox
        formatChoicebox.setItems(FXCollections.observableArrayList(StageFormatOption.values()));

        // Listener for the format choicebox. Used to change a Stage format when a different format is chosen
        formatChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Stage selectedStage = getSelectedStage();
            if (selectedStage != null && StageFormatOption.getOption(selectedStage.getFormat()) != newValue) {
                selectedStage.setFormat(newValue.getNewInstance());
                selectedStage.getFormat().setDefaultSeriesLength(defaultSeriesLengthSpinner.getValue());
            }

            updateFormatUniqueSettings();
        });
    }

    /** Sets up the listview for stages. Setting items
     * and adding listener. */
    private void setUpStageListView(){

        /* Assign items to the list in case of a tournament being loaded */
        stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));

        /* By default the stage settings are hidden.
         * This listener is used to show the stage settings when there is at least one Stage added.
         * Also handles disabling and enabling of buttons for stages. */
        stagesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            stageSettingsVBox.setVisible(stagesListView.getItems().size() != 0);

            /* Set content inside stage settings to show chosen stage */
            updateStageSettings();

            updateStageListButtons();
        });
    }

    /** Updates the settings unique to the selected stage's format */
    public void updateFormatUniqueSettings() {

        Stage selectedStage = getSelectedStage();
        formatUniqueSettingsHolder.getChildren().clear();
        if (selectedStage != null) {
            Node formatSettings = selectedStage.getFormat().getSettingsFXNode();
            if (formatSettings != null) {
                formatUniqueSettingsHolder.getChildren().add(selectedStage.getFormat().getSettingsFXNode());
            }
        }
    }

    /** Updates all ui elements */
    public void update() {
        updateGeneralTournamentSettings();
        updateStageSettings();
    }

    /** Updates all general tournament settings like tournament name, tiebreaker rule, and stage list*/
    public void updateGeneralTournamentSettings() {
        Tournament tournament = Tournament.get();
        nameTextField.setText(tournament.getName());

        stagesListView.refresh();
        updateStageListButtons();
    }

    /** Updates the buttons under the stage list */
    public void updateStageListButtons() {
        boolean started = Tournament.get().hasStarted();
        if (stagesListView.getItems().size() == 0) {
            // If the stageListView has no items. Then the remove, up and down buttons is disabled.
            swapUp.setDisable(true);
            swapDown.setDisable(true);
            removeStageBtn.setDisable(true);
        } else {
            // Swap up and down depends on selected index. Remove is always enabled unless tournament has started
            int selectedIndex = getSelectedIndex();
            swapUp.setDisable(selectedIndex == 0 || started);
            swapDown.setDisable(selectedIndex == stagesListView.getItems().size() - 1 && selectedIndex != -1 || started);
            removeStageBtn.setDisable(started);
        }
        addStageBtn.setDisable(started);
    }

    /** Updates all general stage settings like name and teams in stage. */
    public void updateStageSettings() {
        boolean started = Tournament.get().hasStarted();
        Stage selectedStage = getSelectedStage();
        if (selectedStage != null) {
            stageNameTextfield.setText(selectedStage.getName());
            formatChoicebox.getSelectionModel().select(StageFormatOption.getOption(selectedStage.getFormat()));
            formatChoicebox.setDisable(started);

            if (selectedStage.getStageNumber() != 1) {
                teamsInStageAll.setVisible(false);
                teamsInStageSpinner.setVisible(true);
                teamsInStageSpinner.getValueFactory().setValue(selectedStage.getNumberOfTeamsWanted());
            } else {
                teamsInStageAll.setVisible(true);
                teamsInStageSpinner.setVisible(false);
            }
            teamsInStageSpinner.setDisable(started);

            defaultSeriesLengthSpinner.getValueFactory().setValue(selectedStage.getFormat().getDefaultSeriesLength());
            defaultSeriesLengthSpinner.setDisable(started);
        }

        updateFormatUniqueSettings();
        formatUniqueSettingsHolder.setDisable(started);
    }

    @FXML
    void nameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().setName(nameTextField.getText());
    }

    @FXML
    void stageNameTextFieldOnKeyReleased(KeyEvent event) {
        getSelectedStage().setName(stageNameTextfield.getText());
        stagesListView.refresh();
    }

    /**
     * Adds a stage to the stages list and also to the tournament model.
     */
    @FXML
    void addStageBtnOnAction(ActionEvent actionEvent) {
        // increments unique id
        Tournament.get().addStage(new Stage("New Stage", new SingleEliminationFormat()));

        stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));
        stagesListView.refresh();
        stagesListView.getSelectionModel().selectLast();
    }

    /**
     * Removes a stage from the Stages list and also from the tournament model.
     */
    @FXML
    void removeStageBtnOnAction() {
        if (getSelectedIndex() != -1) {
            Tournament.get().removeStage(getSelectedIndex());
            stagesListView.getItems().remove(getSelectedIndex());
        }
    }

    /**
     * Swaps a stage upwards in the list of stages. Used to allow ordering of stages.
     * This also swaps the stages in the tournament model.
     */
    @FXML
    private void swapStageUpwards() {
        if (getSelectedIndex() != 0 && getSelectedIndex() != -1) {
            Collections.swap(stagesListView.getItems(), getSelectedIndex(), getSelectedIndex() - 1);
            Tournament.get().swapStages(stagesListView.getItems().get(getSelectedIndex()), stagesListView.getItems().get(getSelectedIndex() - 1));

            stagesListView.getSelectionModel().select(getSelectedIndex() - 1);
        }
    }

    /**
     * Swaps a stage downwards in the list of stages. Used to allow ordering of stages.
     * This also swaps the stages in the tournament model.
     */
    @FXML
    private void swapStageDownwards() {
        int listSize = stagesListView.getItems().size();

        if (getSelectedIndex() != listSize - 1 && getSelectedIndex() != -1) {
            Collections.swap(stagesListView.getItems(), getSelectedIndex(), getSelectedIndex() + 1);
            Tournament.get().swapStages(stagesListView.getItems().get(getSelectedIndex()), stagesListView.getItems().get(getSelectedIndex() + 1));
            stagesListView.getSelectionModel().select(getSelectedIndex() + 1);
        }
    }

    private Stage getSelectedStage() {
        return stagesListView.getSelectionModel().getSelectedItem();
    }

    private int getSelectedIndex() {
        return stagesListView.getSelectionModel().getSelectedIndex();
    }
}
