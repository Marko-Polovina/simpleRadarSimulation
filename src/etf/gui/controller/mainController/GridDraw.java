package etf.gui.controller.mainController;

import etf.customLogger.CustomLogger;
import etf.fileManagers.ConfigFileManager;
import etf.gui.util.AircraftCodes;
import etf.gui.util.ColorCodes;
import etf.model.aircraft.Aircraft;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GridDraw extends Thread {
    private static MainViewController mvc;
    private static Path mapLocation = Paths.get("C:\\Users\\Marko\\Documents\\simpleRadarSimulation\\src\\etf\\files\\map.txt");
    private static CustomLogger cl;
    private StringProperty[][] labelValues = new StringProperty[ConfigFileManager.X_DIMENSION][ConfigFileManager.Y_DIMENSION];

    public GridDraw(MainViewController mvc){
        this.mvc = mvc;
        for(int i = 0; i < ConfigFileManager.X_DIMENSION; i++) {
            for (int j = 0; j < ConfigFileManager.Y_DIMENSION; j++) {
                labelValues[i][j] = new SimpleStringProperty();
                mvc.getFlightAreaLabel(i,j).textProperty().bind(labelValues[i][j]);
            }
        }
    }

    public int getColumn(String aircraft){
       List<String> splitAircraft = Arrays.asList(aircraft.split("#"));
       String locationX = splitAircraft.stream().filter(x->x.startsWith("currX!")).collect(Collectors.toList()).get(0);
       return Integer.parseInt(locationX.split("!")[1]);
    }

    public int getRow(String aircraft){
        List<String> splitAircraft = Arrays.asList(aircraft.split("#"));
        String locationY = splitAircraft.stream().filter(x->x.startsWith("currY!")).collect(Collectors.toList()).get(0);
        return Integer.parseInt(locationY.split("!")[1]);
    }

    public String getTypeCode(String aircraft){
        String retVal = null;
        if(aircraft.contains("friendly!false")){
            retVal =  AircraftCodes.NEPRIJATELJSKI_AVIONI;
        }else if(aircraft.contains("Unmanned")){
            retVal =  AircraftCodes.BESPILOTNA_LETJELICA;
        }else if(aircraft.contains("Fighter Plane")){
            retVal = AircraftCodes.DOMACI_LOVAC;
        }else if(aircraft.contains("Firefighting Plane")){
            retVal = AircraftCodes.PROTIV_POZARNI_AVION;
        }else if(aircraft.equals("Passanger Plane")){
            retVal = AircraftCodes.PUTNICKI_AVION;
        }else if(aircraft.equals("Transport Airplane")){
            retVal = AircraftCodes.TRANSPORTNI_AVION;
        }else if(aircraft.equals("Firefighting Helicopter")){
            retVal = AircraftCodes.PROTIV_POZARNI_HELIKOPTER;
        }else if(aircraft.equals("Passanger Helicopter")){
            retVal = AircraftCodes.PUTNICKI_HELIKOPTER;
        }else if(aircraft.equals("Transport Helicopter")){
            retVal = AircraftCodes.TRANSPORTNI_HELIKOPTER;
        }else if(aircraft.contains("Bomber")){
            retVal = AircraftCodes.BOMBARDER;
        }
        return retVal;
    }

    public Color getTypeColor(String aircraft){
        if(aircraft.contains("friendly!false")){
            return ColorCodes.NEPRIJATELJSKI_AVIONI;
        }else if(aircraft.contains("Unmanned")){
            return ColorCodes.BESPILOTNA_LETJELICA;
        }else if(aircraft.contains("Fighter Plane")){
            return ColorCodes.DOMACI_LOVAC;
        }else if(aircraft.contains("Firefighting Plane")){
            return ColorCodes.PROTIV_POZARNI_AVION;
        }else if(aircraft.equals("Passanger Plane")){
            return ColorCodes.PUTNICKI_AVION;
        }else if(aircraft.equals("Transport Airplane")){
            return ColorCodes.TRANSPORTNI_AVION;
        }else if(aircraft.equals("Firefighting Helicopter")){
            return ColorCodes.PROTIV_POZARNI_HELIKOPTER;
        }else if(aircraft.equals("Passanger Helicopter")){
            return ColorCodes.PUTNICKI_HELIKOPTER;
        }else if(aircraft.equals("Transport Helicopter")){
            return ColorCodes.TRANSPORTNI_HELIKOPTER;
        }else if(aircraft.contains("Bomber")){
            return ColorCodes.BOMBARDER;
        }else{
            return ColorCodes.PUTNICKI_AVION;
        }
    }

    @Override
    public void run(){
        while(true){
            try {
                if(Files.size(mapLocation) == 0){continue;}
                List<String> allAircrafts = mvc.getMap();       //dobavi ga od kontrolera
                //postavi stare labele na prazan string
                for(int i = 0; i < ConfigFileManager.X_DIMENSION; i++) {
                    for (int j = 0; j < ConfigFileManager.Y_DIMENSION; j++) {
                        final int x= i;
                        final int y =j;
                        Platform.runLater(() -> labelValues[x][y].set(""));
                    }
                }
                //updejtuje mapu, ponovo ucita fajl i dobavi je nakon toga
                mvc.updateMap();
                allAircrafts = null;
                allAircrafts = mvc.getMap();

                for(String aircraft : allAircrafts){
                    List<String> details = Arrays.asList(aircraft.split("#"));
                    String aircraftCode = getTypeCode(aircraft);
                    Color colorCode = getTypeColor(aircraft);
                    int locationX = getColumn(aircraft);
                    int locationY = getRow(aircraft);
                    Platform.runLater(()->{
                        Label hlp = new Label();
                        hlp.setText(aircraftCode);
                        hlp.setTextFill(colorCode);
                        labelValues[locationX][locationY].set(aircraftCode);
                        mvc.getFlightAreaLabel(locationX,locationY).setTextFill(colorCode);
                    });
                }
                Thread.sleep(ConfigFileManager.RADAR_REFRESH_RATE);
            } catch (IOException e) {
                cl.logException(e.getMessage(),e);
            } catch (InterruptedException e) {
                cl.logException(e.getMessage(),e);
            }
        }
    }

}
