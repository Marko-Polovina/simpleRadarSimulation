package etf.gui.controller.crashesViewController;

import etf.customLogger.CustomLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 *  Zasto ovo radis, jel nije moglo dinamicki, jel mora fxml
 *  Kreira prozor za ispis informacija o svim sudarima
 *  ukoliko se desi sudar za vrijeme rada aplikacije,
 *  dodace se u fajl na drugom mjestu i kreirace se alert sa potrebnim informacijama
 */
public class CrashesViewApp extends Application {
    CustomLogger cl = new CustomLogger(this);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public void showCrashes(EventsCrashesViewController cvc, Stage primaryStage){
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(new File("src\\etf\\gui\\view\\CrashView\\ListAllCrashes.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        try {
            cvc.setAnchorPane(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        cvc.setScrollPane((ScrollPane)cvc.getAnchorPane().getChildren().get(0));
        cvc.setCrashListings((Label)cvc.getScrollPane().getContent());
        cvc.setCrashesLabel();

        Scene mainScene = new Scene(cvc.getAnchorPane());
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Detalji o sudarima");
        primaryStage.show();
    }

    public void showEvents(EventsCrashesViewController cvc, Stage primaryStage){
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(new File("src\\etf\\gui\\view\\CrashView\\ListAllCrashes.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        try {
            cvc.setAnchorPane(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        cvc.setScrollPane((ScrollPane)cvc.getAnchorPane().getChildren().get(0));
        cvc.setCrashListings((Label)cvc.getScrollPane().getContent());
        cvc.setEventsLabel();

        Scene mainScene = new Scene(cvc.getAnchorPane());
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Detalji o događajima");
        primaryStage.show();
    }
}
