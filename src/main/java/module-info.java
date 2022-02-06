module cleopetra {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.controlsfx.controls;

    exports dk.aau.cs.ds306e18.tournament;
    opens dk.aau.cs.ds306e18.tournament.ui;
    opens dk.aau.cs.ds306e18.tournament.utility;
}
