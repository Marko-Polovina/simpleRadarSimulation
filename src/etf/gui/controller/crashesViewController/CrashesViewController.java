package etf.gui.controller.crashesViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CrashesViewController {
    @FXML
    private Label crashListings;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public Label getCrashListings() {
        return crashListings;
    }

    public void setCrashListings(Label crashListings) {
        this.crashListings = crashListings;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public void browseCrashes() {
        new CrashesViewApp().showCrashes(this, new Stage());
    }

    public void setLabel(){
        String detalji = null;
        //TODO ucitaj datoteku i formatiraj po potrebi
        crashListings.setText(detalji);
    }


    //test local git desktop app

}
