package etf.gui.controller.detailsController;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DetailsViewControler implements Initializable {
    @FXML
    private ScrollPane detailsPane;

    @FXML
    private AnchorPane anchorPane;

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public ScrollPane getDetailsPane() {
        return detailsPane;
    }

    public void setDetailsPane(ScrollPane detailsPane) {
        this.detailsPane = detailsPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initilaze();
    }

    public void showAircraftDetails(int i, int j) {
        new DetailsViewApp().setTabsandStart(i,j,new Stage(), this);
    }

    private void initilaze(){
    }

    public void setScrollPane(int i, int j) {
        /*TODO kupljenje i postavljane stringa ide ovdje, informacije o letjelicama sa polja, trazi matricu i pristupi polju
                ili jos bolje trazi konkretnu listu letjelica iz tog polja i koristi nju, i i j su koordinate polja
        */
        //Ako lista letjelica nije prazna pravice novi tab za svaku od njih, jos nisam odradio modele
        if(true) {
            TabPane tabPane = new TabPane();
            for (int k = 1; k < 10; k++) {
                Tab tab = new Tab();
                tab.setText("Naslov " + k + i);
                tab.setContent(new Label("Nesto " + k + j));
                tabPane.getTabs().add(tab);
            }
            detailsPane.setContent(tabPane);
        }
        else{
            detailsPane.setContent(new Label("Izabrano polje nema letjelica."));
        }
    }
}
