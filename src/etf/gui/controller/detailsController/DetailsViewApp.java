package etf.gui.controller.detailsController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Kreira prozor za prikaz detalja o letjelicama
 */
public class DetailsViewApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    }

    public void setTabsandStart(int i, int j, Stage primaryStage, DetailsViewControler dvc){
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(new File("src\\etf\\gui\\view\\DetailsView\\DetailsView.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            dvc.setAnchorPane(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dvc.setDetailsPane((ScrollPane)dvc.getAnchorPane().getChildren().get(0));
        dvc.setScrollPane(i,j);

        Scene mainScene = new Scene(dvc.getAnchorPane());
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Detalji o letjelicama");
        primaryStage.show();
    }
}
