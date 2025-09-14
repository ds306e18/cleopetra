module cleopetra {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires rlbot.framework;
    requires org.tomlj;

    exports dk.aau.cs.ds306e18.tournament;
    opens dk.aau.cs.ds306e18.tournament.model;
    opens dk.aau.cs.ds306e18.tournament.model.format;
    opens dk.aau.cs.ds306e18.tournament.model.match;
    opens dk.aau.cs.ds306e18.tournament.model.stats;
    opens dk.aau.cs.ds306e18.tournament.rlbot;
    opens dk.aau.cs.ds306e18.tournament.rlbot.configuration;
    opens dk.aau.cs.ds306e18.tournament.serialization;

    opens dk.aau.cs.ds306e18.tournament.ui;
    opens dk.aau.cs.ds306e18.tournament.utility;
}
