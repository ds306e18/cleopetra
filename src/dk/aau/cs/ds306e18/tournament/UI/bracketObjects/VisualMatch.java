package dk.aau.cs.ds306e18.tournament.UI.bracketObjects;

import dk.aau.cs.ds306e18.tournament.UI.Tabs.BracketOverview;
import dk.aau.cs.ds306e18.tournament.model.Match;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/** Used to represent a match in the UI. */
public class VisualMatch extends VBox {

    private Label team1;
    private Label team2;
    private Label team1points;
    private Label team2points;

    Match match;

    /** Used to represent a match in the UI. */
    VisualMatch(Match match, BracketOverview bracketOverview){

        this.team1 = new Label();
        this.team2 = new Label();
        this.team1points = new Label();
        this.team2points = new Label();
        this.match = match;

        setAlignment();
        setLabels(match);
        setStyle();
        setOnMouseClicked(e -> bracketOverview.setSelectedMatch(match));

        addContent();
    }

    /** Sets the style for this node. */
    private void setStyle(){
        this.setMinWidth(200);
        this.setHeight(100);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                null, null, new Insets(10))));
        this.setPadding(new Insets(5));
    }

    /** Adds content to this node. */
    private void addContent(){

        HBox line1 = new HBox();
        HBox line2 = new HBox();

        HBox space1 = new HBox();
        HBox space2 = new HBox();

        line1.getChildren().addAll(team1, space1, team1points);
        line2.getChildren().addAll(team2, space2, team2points);

        line1.setHgrow(space1, Priority.ALWAYS);
        line2.setHgrow(space2, Priority.ALWAYS);

        this.getChildren().addAll(line1, line2);

    }

    /** Sets the text on the labels. */
    private void setLabels(Match match){

        this.team1.setText(match.getBlueTeam().getTeamName());
        this.team2.setText(match.getOrangeTeam().getTeamName());
        this.team1points.setText(String.valueOf(match.getBlueScore()));
        this.team2points.setText(String.valueOf(match.getOrangeScore()));
    }

    /** Sets the alignment of the elements in this node. */
    private void setAlignment(){

        team1.setAlignment(Pos.CENTER_LEFT);
        team1points.setAlignment(Pos.CENTER);
        team2.setAlignment(Pos.CENTER_LEFT);
        team2points.setAlignment(Pos.CENTER);
    }
}
