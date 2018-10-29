package dk.aau.cs.ds306e18.tournament.oldui.Tabs;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class RankingsOverview extends Tab {

    public RankingsOverview(){

        this.setText("Rankings");
        Label tempLabel = new Label("PLACEHOLDER RANKING");

        this.setContent(tempLabel);

    }
}
