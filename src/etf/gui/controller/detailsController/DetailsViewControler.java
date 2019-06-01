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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
        List<String> allAircrafts = null;
        try {
            allAircrafts = Files.readAllLines(Paths.get("src/etf/files/map.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> filteredAircrafts = allAircrafts.stream().filter(x->x.contains("#currX$" + i + "#currY$"+j)).collect(Collectors.toList());
        if(filteredAircrafts.size() != 0) {
            TabPane tabPane = new TabPane();
            for (int k = 0; k < filteredAircrafts.size(); k++) {
                Tab tab = new Tab();
                String aircraft = filteredAircrafts.get(k);
                List<String> splitAircraft = Arrays.asList(aircraft.split("#")); //imas sve rastavljeno po #
                tab.setText(splitAircraft.get(0)); //stavi tip letjelice
                String content = new String();
                for(int t = 1; t < splitAircraft.size(); t++){
                    List<String> hlp = Arrays.asList(splitAircraft.get(t).split("!"));
                    if(hlp!=null){
                        for(String h : hlp){
                            content.concat(h);
                            content.concat(" ");
                        }
                        content.concat("\n");
                    }
                }
                tab.setContent(new Label(content));
                tabPane.getTabs().add(tab);
            }
            detailsPane.setContent(tabPane);
        }
        else{
            detailsPane.setContent(new Label("Izabrano polje nema letjelica."));
        }
    }
}
