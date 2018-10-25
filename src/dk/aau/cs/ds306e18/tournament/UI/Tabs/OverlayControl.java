package dk.aau.cs.ds306e18.tournament.UI.Tabs;

import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OverlayControl extends Tab {

    public OverlayControl(){


        this.setText("Overlay Control");
        Image imageOverlay = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqX6L7Qpa62YOBrRlINaqBMDrUi5sHuWmwEsQU1lDkRMTqzG8K");
        ImageView imageViewOverlay = new ImageView(imageOverlay);
        this.setContent(imageViewOverlay);

    }

}
