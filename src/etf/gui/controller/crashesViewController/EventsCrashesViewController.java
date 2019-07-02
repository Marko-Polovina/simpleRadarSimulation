package etf.gui.controller.crashesViewController;

import etf.fileManagers.CrashManager;
import etf.fileManagers.EventManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventsCrashesViewController {
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

    public void setCrashesLabel(){
        List<CrashManager> detalji = new ArrayList<>();
        detalji = CrashManager.readCrashes();
        if(detalji.size() == 0){
            crashListings.setText("Nema arhiviranih sudara.");
        }else{
            TabPane tabPane = new TabPane();
            for (int k = 0; k < detalji.size(); k++) {
                Tab tab = new Tab();
                CrashManager crash = detalji.get(k);
                String crashDetails = crash.getDetails() + "\n" + crash.getTime() + "\n" + crash.getPosition();
                tab.setText(crash.getTime());
                tab.setContent(new Label(crashDetails));
                tabPane.getTabs().add(tab);
            }
            scrollPane.setContent(tabPane);
        }
    }

    public void browseEvents() {
        new CrashesViewApp().showEvents(this, new Stage());
    }

    public void setEventsLabel(){
        List<List<String>> detalji = new ArrayList<>();
        detalji = EventManager.readEvents();
        if(detalji.size() == 0){
            crashListings.setText("Nema arhiviranih događaja.");
        }else{
            TabPane tabPane = new TabPane();
            for (int k = 0; k < detalji.size(); k++) {
                Tab tab = new Tab();
                List<String> crash = detalji.get(k);
                String crashDetails = new String();
                for(String s : crash){
                    crashDetails = crashDetails + "" + s + "\n";
                }
                tab.setText(crash.get(0));
                tab.setContent(new Label(crashDetails));
                tabPane.getTabs().add(tab);
            }
            scrollPane.setContent(tabPane);
        }
    }
}
