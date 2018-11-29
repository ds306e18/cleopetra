package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerByGoalDiff;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerBySeed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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

    @FXML private GridPane tournamentSettingsTab;
    @FXML private TextField nameTextField;
    @FXML private ChoiceBox<TieBreaker> tieBreakerChoiceBox;
    @FXML private ListView<Stage> stagesListView;
    @FXML private Button addStageBtn;
    @FXML private Button removeStageBtn;
    @FXML private VBox stageSettingsVBox;
    @FXML private Text selectStageText;
    @FXML private Text stageSettingsHeadLabel;
    @FXML private HBox stageSettingsContent;
    @FXML private TextField stageNameTextfield;
    @FXML private ChoiceBox<StageFormatOption> formatChoicebox;
    @FXML private Button swapUp;
    @FXML private Button swapDown;
    @FXML private VBox formatUniqueSettingsHolder;
    @FXML private Spinner<Integer> teamsInStageSpinner;
    @FXML private Label teamsInStageAll;

    @FXML
    private void initialize() {
        /* Assign items to the list in case of a tournament being loaded */
        stagesListView.setItems(FXCollections.observableArrayList(Tournament.get().getStages()));

        /* Retrieve and set tournament name into textfield. */
        nameTextField.setText(Tournament.get().getName());

        /* By default the remove stage button is disabled. */
        removeStageBtn.setDisable(true);

        /* Retrieve and add choices to choicebox for the Tiebreaker box.
         * Also upon change sets the new tiebreaker rule to the tournament model. */
        ObservableList<TieBreaker> tieBreakers = FXCollections.observableArrayList(new TieBreakerBySeed(), new TieBreakerByGoalDiff());

        tieBreakerChoiceBox.setItems(tieBreakers);
        tieBreakerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!(Tournament.get().getTieBreaker().getClass().isInstance(newValue))) {
                Tournament.get().setTieBreaker(newValue);
            }
        });
        for (TieBreaker aTiebreaker : tieBreakers){
            TieBreaker chosenTiebreaker = Tournament.get().getTieBreaker();
            if (chosenTiebreaker.equals(aTiebreaker)){
                tieBreakerChoiceBox.getSelectionModel().select(aTiebreaker);
            } else {
                tieBreakerChoiceBox.getSelectionModel().select(0);
            }
        }

        /* By default the stage settings are hidden.
         * This listener is used to show the stage settings when there is at least one Stage added.
         * Also handles disabling and enabling of buttons for stages. */
        stagesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            stageSettingsVBox.setVisible(stagesListView.getItems().size() != 0);

            /* Handle stage order button disabling / enabling */
            int selectedIndex = getSelectedIndex();
            swapUp.setDisable(selectedIndex == 0);
            swapDown.setDisable(selectedIndex == stagesListView.getItems().size() - 1 && selectedIndex != -1);

            /* Set content inside stage settings to show chosen stage */
            showStageValues();

            /* If the stageListView has no items. Then the remove, up and down buttons is disabled. */
            if(stagesListView.getItems().size() == 0) {
                removeStageBtn.setDisable(true);
                swapUp.setDisable(true);
                swapDown.setDisable(true);
            } else
                removeStageBtn.setDisable(false);
        });

        /* Setup teams wanted in stage spinner */
        teamsInStageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE));
        teamsInStageSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                int value = Integer.valueOf(newValue); //This will throw the exception if the value not only contains numbers
                getSelectedStage().setNumberOfTeamsWanted(value);
            } catch (NumberFormatException e) {
                teamsInStageSpinner.getEditor().setText("2"); //Setting default value
            }
        });

        /* Retrieve possible formats and add to a choicebox */
        formatChoicebox.setItems(FXCollections.observableArrayList(StageFormatOption.values()));

        /* Listener for the format choicebox. Used to change a Stage format when a different format is chosen */
        formatChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Stage selectedStage = getSelectedStage();
            if (selectedStage != null && StageFormatOption.getOption(selectedStage.getFormat()) != newValue) {
                selectedStage.setFormat(newValue.getNewInstance());
            }

            // Show settings unique to format
            formatUniqueSettingsHolder.getChildren().clear();
            if (selectedStage != null) {
                Node formatSettings = selectedStage.getFormat().getSettingsFXNode();
                if (formatSettings != null) {
                    formatUniqueSettingsHolder.getChildren().add(selectedStage.getFormat().getSettingsFXNode());
                }
            }
        });
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
        Tournament.get().addStage(new Stage("New Stage", new SwissFormat()));

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
     * Show the correct values in the stage settings panel.
     */
    private void showStageValues() {
        Stage selectedStage = getSelectedStage();
        if (selectedStage != null) {
            stageNameTextfield.setText(selectedStage.getName());
            formatChoicebox.getSelectionModel().select(StageFormatOption.getOption(selectedStage.getFormat()));
            if (selectedStage.getId() != 1) {
                teamsInStageAll.setVisible(false);
                teamsInStageSpinner.setVisible(true);
                teamsInStageSpinner.getValueFactory().setValue(selectedStage.getNumberOfTeamsWanted());
            } else {
                teamsInStageAll.setVisible(true);
                teamsInStageSpinner.setVisible(false);
            }
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
