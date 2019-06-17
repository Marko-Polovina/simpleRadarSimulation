package etf.gui.main;

import etf.Simulation;
import etf.customLogger.CustomLogger;
import etf.gui.controller.mainController.MainViewController;
import etf.model.airspace.Airspace;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

//ime nije najsretnije
//glavni prozor gui-ja
//treba napraviti fajl iz koga ce ucitavati pozicije aviona
//enum za tip letjelice ili samo keyword iz teksta koja se kopira u boju i skracenicu?
public class MainApp extends Application {
    CustomLogger cl = new CustomLogger(this);


    public static void main(String[] args) {
        Airspace airspace = Airspace.getAirspace();
        Simulation simulation = new Simulation(airspace);
        simulation.start();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(new File("src\\etf\\gui\\view\\mainView\\MainView.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        AnchorPane pane = null;
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        Scene mainScene = new Scene(pane);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Radarska kontrola");
        primaryStage.show();
    }
}
