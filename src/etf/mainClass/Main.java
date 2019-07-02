package etf.mainClass;

import etf.backUpManager.BackUp;
import etf.fileManagers.ConfigFileManager;
import etf.gui.main.MainApp;
import etf.model.airspace.Airspace;
import etf.simulation.Simulation;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args){
        ConfigFileManager cnfm = new ConfigFileManager();
        Airspace airspace = Airspace.getAirspace();
        Simulation simulation = new Simulation(airspace);
        simulation.start();
        BackUp backUp = new BackUp();
        backUp.setDaemon(true);
        backUp.start();
        Application.launch(MainApp.class,args);
    }
}
