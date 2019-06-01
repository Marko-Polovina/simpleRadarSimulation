package etf.gui.controller.mainController;

import etf.gui.controller.crashesViewController.CrashesViewController;
import etf.gui.controller.detailsController.DetailsViewControler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    private static boolean FlightBanEnforcement = false;

    public boolean getFlightBanEnforment(){return FlightBanEnforcement;}

    @FXML
    private GridPane FlightArea;

    @FXML
    private CheckBox FlightBan;

    @FXML
    private Button ExitBtn;

    @FXML
    private Button CrashDetailsBtn;

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

    /**
     * Inicijalizuje sva polja GridPane-a
     * Postavlja ih na Pane-ove koji sadrze Label-e
     * Odabrao sam Pane-ove zbog lakseg postavljanja handlera na njih
     * Postavlja dugme za zatvaranje
     * Postavlja dugme za prikaz svih nesreca i obradu proslijedjuje CrashesViewController
     * Postavlja funkcionalnost toggle-a za zabranu leta
     */
    public void initialize(){
        ArrayList<Label> labelList = new ArrayList<>();
        for(int i = 0; i < 100; i++)
            for(int j = 0; j < 100; j++)
                FlightArea.add(new Pane(new Label("")), i, j);
        for(Node field : FlightArea.getChildren()){
            onGridPanePositionClick(field);
        }
        ExitBtn.setOnAction(x -> Platform.exit());
        CrashDetailsBtn.setOnAction(x -> new CrashesViewController().browseCrashes());
        FlightBan.setOnAction(x -> toggleFlightBan());
    }

    /**
     * Odradi let da prilikom normalnog leta provjerava da li je let dozvoljen, a prilikom prinudnog izlaska
     * ne provjerava zabranu vec podrazumjeva da mora izaci iz prostora leta
     */
    private synchronized void toggleFlightBan(){
        if(FlightBan.isSelected()){
            FlightBanEnforcement = true;
        }else{
            FlightBanEnforcement = false;
        }
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
            if(field instanceof Pane && GridPane.getRowIndex(field) == row && GridPane.getColumnIndex(field) == column)
            {
                Pane pane = (Pane)FlightArea.getChildren().get(column*100 + row + 1);
                Label extLabel = (Label)pane.getChildren().get(0);
                extLabel.setText(text);
                extLabel.setTextFill(color);
                extLabel.setVisible(true);
            }
        }
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
                Pane pane = (Pane)FlightArea.getChildren().get(column*100 + row + 1);
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
            new DetailsViewControler().showAircraftDetails(i,j);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }

    //todo odredi gdje ce se ucitavati fajl map.txt i gdje ce se pozivati setLabel metoda
}
