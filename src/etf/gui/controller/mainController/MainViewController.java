package etf.gui.controller.mainController;

import etf.customLogger.CustomLogger;
import etf.fileManagers.ConfigFileManager;
import etf.fileManagers.EventManager;
import etf.gui.controller.crashesViewController.EventsCrashesViewController;
import etf.gui.controller.detailsController.DetailsViewControler;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainViewController implements Initializable {
    private static Path mapLocation = Paths.get("src\\etf\\files\\map.txt");
    CustomLogger cl = new CustomLogger(this);

    private static boolean FlightBanEnforcement = false;
    private static List<String> map = new ArrayList<String>();

    @FXML
    private GridPane FlightArea;

    @FXML
    private CheckBox FlightBan;

    @FXML
    private Button ExitBtn;

    @FXML
    private Button CrashDetailsBtn;

    @FXML
    private Button eventsButton;

    @FXML
    private Label eventsLabel;

    public Label getEventsLabel() {
        return eventsLabel;
    }

    public void setEventsLabel(Label label) {
        this.eventsLabel = label;
    }

    public MainViewController(){}

    public GridPane getFlightArea() {
        return FlightArea;
    }

    public void setFlightArea(GridPane flightArea) {
        FlightArea = flightArea;
    }

    public CheckBox getFlightBan() {
        return FlightBan;
    }

    public void setFlightBan(CheckBox flightBan) {
        FlightBan = flightBan;
    }

    public Button getExitBtn() {
        return ExitBtn;
    }

    public void setExitBtn(Button exitBtn) {
        ExitBtn = exitBtn;
    }

    public Button getCrashDetailsBtn() {
        return CrashDetailsBtn;
    }

    public void setCrashDetailsBtn(Button crashDetailsBtn) {
        CrashDetailsBtn = crashDetailsBtn;
    }

    public void updateMap(){
        String mapLocation = "src\\etf\\files\\map.txt";
        map = null;
        try {
            map = Files.readAllLines(Paths.get(mapLocation));
        } catch (IOException e) {
            cl.logException(e.getMessage(),e);
        }
    }

    public List<String> getMap(){return map;}

    public List<String> getAircraftOnLocation(int locationX, int locationY){
        return map.stream().filter(x->x.contains("#currX!"+locationX+"#currY!"+locationY)).collect(Collectors.toList());
    }

    /**
     * Inicijalizuje sva polja GridPane-a
     * Postavlja ih na Pane-ove koji sadrze Label-e
     * Odabrao sam Pane-ove zbog lakseg postavljanja handlera na njih
     * Postavlja dugme za zatvaranje
     * Postavlja dugme za prikaz svih nesreca i obradu proslijedjuje EventsCrashesViewController
     * Postavlja funkcionalnost toggle-a za zabranu leta
     */
    public void initialize(){
        ConfigFileManager cnf = new ConfigFileManager();
        FlightArea.setGridLinesVisible(true);
        for(int i = 0; i < ConfigFileManager.X_DIMENSION; i++)
            for(int j = 0; j < ConfigFileManager.Y_DIMENSION; j++) {
                Pane pane = new Pane(new Label(""));
                pane.setPrefHeight(50);
                pane.setPrefWidth(50);
                onGridPanePositionClick(pane);
                FlightArea.add(pane, i, j);
            }
        ExitBtn.setOnAction(x -> {
            File dir = new File("src\\etf\\files\\events\\");
            for(File file : dir.listFiles()){
                file.delete();
            }
            Platform.exit();
        });
        CrashDetailsBtn.setOnAction(x -> new EventsCrashesViewController().browseCrashes());
        FlightBan.setOnAction(x -> toggleFlightBan());
        eventsButton.setOnAction(x-> new EventsCrashesViewController().browseEvents());
        EventManager em = new EventManager(this);
        em.setDaemon(true);
        em.start();
        GridDraw gd = new GridDraw(this);
        gd.setDaemon(true);
        gd.start();
    }


    private synchronized void toggleFlightBan(){
        if(FlightBan.isSelected()){
            FlightBanEnforcement = true;
        }else{
            FlightBanEnforcement = false;
        }
        ConfigFileManager.updateFlightBan(FlightBanEnforcement);
    }

    /**
     *
     * @param text      Predefinisani tekst koristi staticki objekat sa tipovima aviona
     * @param color     Gornja klasa sa predefinisanim bojama
     * @param column    Kolona iz logicke matrice(stvarna pozicija)
     * @param row       Red iz logicke matrice(stvarna pozicija)
     */
    public synchronized void setLabel(String text, Color color, int column, int row){
        for(Node field : FlightArea.getChildren()){
            if(field instanceof Pane
                    && GridPane.getRowIndex(field) == row
                    && GridPane.getColumnIndex(field) == column)
            {
                Pane pane = (Pane)FlightArea.getChildren().get(column*ConfigFileManager.X_DIMENSION + row + 1);
                Label extLabel = (Label)pane.getChildren().get(0);
                extLabel.setText(text);
                extLabel.setTextFill(color);
                extLabel.setVisible(true);
            }
        }
    }

    public synchronized Label getFlightAreaLabel(int column, int row){
        Label retVal = null;

        for(Node field : FlightArea.getChildren()){
            if(field instanceof Pane
                    && GridPane.getRowIndex(field) == row
                    && GridPane.getColumnIndex(field) == column)
            {
                Pane pane = (Pane)FlightArea.getChildren().get(column*ConfigFileManager.X_DIMENSION + row + 1);
                retVal = (Label)pane.getChildren().get(0);
            }
        }

        return retVal;
    }

    /**
     *
     * @param column    Kolona iz logicke matrice(stvarna pozicija)
     * @param row       Red iz logicke matrice(stvarna pozicija)
     */
    public synchronized void removeLabel(int column, int row){
        for(Node field : FlightArea.getChildren()){
            if(field instanceof Pane && GridPane.getRowIndex(field) == row && GridPane.getColumnIndex(field) == column)
            {
                Pane pane = (Pane)FlightArea.getChildren().get(column*ConfigFileManager.X_DIMENSION + row + 1);
                Label extLabel = (Label)pane.getChildren().get(0);
                extLabel.setVisible(false);
                extLabel.setText("");
            }
        }
    }


    /**
     * Metoda postavlja handler na pane unutar grida koji reaguje na klik.
     * Na jedan klik na pane se dohvata pozicija tog polja i obrada slucaja se proslijedjuje
     * DetailsViewControlleru koji je zaduzen za nabavljanje i adekvatno prikazivanje informacija
     * @param specifiedField    Polje na koje se postavlja handler
     */
    private void onGridPanePositionClick(Node specifiedField){
        specifiedField.setOnMouseClicked(x->{
            Node node = (Node)x.getSource();
            int i = GridPane.getColumnIndex(node);
            int j = GridPane.getRowIndex(node);
            DetailsViewControler dvc = new DetailsViewControler();
            dvc.setMvc(this);
            dvc.showAircraftDetails(i,j);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }
}
