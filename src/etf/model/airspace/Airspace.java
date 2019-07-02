package etf.model.airspace;

import etf.fileManagers.ConfigFileManager;
import etf.fileManagers.EventManager;
import etf.model.aircraft.Aircraft;
import etf.model.aircraft.planes.FighterPlane;
import etf.model.rockets.Rockets;
import etf.simulation.Simulation;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Airspace {
    private static Airspace airspace;
    private Field[][] field = new Field[ConfigFileManager.X_DIMENSION][ConfigFileManager.Y_DIMENSION];
    private List<Aircraft> allAircrafts;

    private Airspace(){
        allAircrafts = new ArrayList<>();
        for(int i = 0; i < ConfigFileManager.X_DIMENSION; i++) {
            for (int j = 0; j < ConfigFileManager.Y_DIMENSION; j++) {
                field[i][j] = new Field();
            }
        }
        this.airspace = this;
    }

    public static Airspace getAirspace() {
        if(airspace!=null)
            return airspace;
        else
            return new Airspace();
    }

    public synchronized List<Aircraft> getAllAircrafts(){return allAircrafts;}

    public synchronized Field getField(int x, int y){return field[x][y];}

    public synchronized void removeAircraft(int x, int y, Aircraft aircraft){
        allAircrafts.remove(aircraft);
        if(x == -1) x=0;
        if(x == ConfigFileManager.X_DIMENSION) x=ConfigFileManager.X_DIMENSION-1;
        if(y == -1) y=0;
        if(y == ConfigFileManager.Y_DIMENSION) y=ConfigFileManager.Y_DIMENSION-1;
        field[x][y].removeAircraft(aircraft);
    }

    public synchronized void moveAircraft(int currX, int currY, int newX, int newY, Aircraft aircraft){
        field[currX][currY].removeAircraft(aircraft);
        field[newX][newY].addAircraft(aircraft);
    }

    public synchronized void addAircraft(int x, int y, Aircraft aircraft){
        field[x][y].addAircraft(aircraft);
        allAircrafts.add(aircraft);
        if(!aircraft.isFriendly() && !aircraft.isNoted()){
            String details = null;
            details =    "ID: " + aircraft.getAircraftId()
                         + "\nIs aircraft friendly : " + aircraft.isFriendly() + "\n"
                         + aircraft.getName() + "\n"
                         + "Model: " + aircraft.getModel()
                         + "\nUsao u domaci vazušni prostor u : "
                         + new LocalDateTimeStringConverter(DateTimeFormatter.ISO_DATE_TIME,null).toString(LocalDateTime.now());
            EventManager.noteEvent(details, LocalDateTime.now());
            aircraft.noteAircraft(); //zapise dogadjaj
            Simulation.notifySimulationToCreateHunters(x,y);
        }
    }

    public void removeRocket(int currentX, int currentY, Rockets rockets) {
        System.out.println("Uklonio raketu");
    }

    //todo zavrsi implementaciju pozicioniranja lovaca, uradjeno kretanje i 'lov'
    public void createAndSendHunters(int moveDirection, int startXEnemyAircraft, int startYEnemyAircraft){
        final int moveDirectionFinal = moveDirection;
        FighterPlane[] fighterPlane = createHunter(moveDirection,startXEnemyAircraft,startYEnemyAircraft);
        addAircraft(fighterPlane[0].getCurrentX(),fighterPlane[0].getCurrentY(),fighterPlane[0]);
        addAircraft(fighterPlane[1].getCurrentX(),fighterPlane[1].getCurrentY(),fighterPlane[1]);
        System.out.println("Napravio lovca 1!" + fighterPlane[0].getCurrentX() + " " + fighterPlane[0].getCurrentY());
        System.out.println("Napravio lovca 2!" + fighterPlane[1].getCurrentX() + " " + fighterPlane[1].getCurrentY());
        for(Aircraft aircraft : allAircrafts){
            aircraft.warneToExit();
        }
        new Thread(()->fighterPlane[0].shootDown(moveDirectionFinal)).start();
        new Thread(()->fighterPlane[1].shootDown(moveDirectionFinal)).start();
        System.out.println("Pokrenuo lovce!");
    }

    // pravi niz od dva lovca koja pravilno pozicionira oko lovljene letjelice
    //todo pojednostavi biranje pravca i spawnanje lovaca sto vise mozes, sad odani malo
    private FighterPlane[] createHunter(int moveDirection,int startEnemyX, int startEnemyY){
        FighterPlane[] retVal = new FighterPlane[2];

        Random rnd = new Random();
        int flightHight = rnd.nextInt(ConfigFileManager.HIGHT_SPAN + 1) * 1000;
        String planeDetails = ConfigFileManager.FRIENDLY_HUNTER_PROPERTIES;
        int spawnXOne = 0,spawnYOne = 0;
        int spawnXTwo = 0,spawnYTwo = 0;
        switch (moveDirection) {
            //1-up, 2-down, 3-left, 4-right
            case 4:
            case 3:
                //jedan od donjih coskova
                if((startEnemyX == 0 && startEnemyY == ConfigFileManager.Y_DIMENSION -1)
                        || (startEnemyX == ConfigFileManager.X_DIMENSION - 1 && startEnemyY == ConfigFileManager.Y_DIMENSION - 1)){
                    spawnXOne = startEnemyX;
                    spawnXTwo = startEnemyX;
                    spawnYOne = startEnemyY - 1;
                    spawnYTwo = startEnemyY;
                }
                //jedan od gornjih coskova
                else if((startEnemyX == (ConfigFileManager.X_DIMENSION - 1) && startEnemyY == 0)
                        || (startEnemyX == 0 && startEnemyY == 0) || startEnemyY == ConfigFileManager.Y_DIMENSION - 1){
                    spawnXOne = startEnemyX;
                    spawnXTwo = startEnemyX;
                    spawnYOne = startEnemyY + 1;
                    spawnYTwo = startEnemyY;
                }
                else{
                    spawnXOne = startEnemyX;
                    spawnXTwo = startEnemyX;
                    spawnYOne = startEnemyY - 1;
                    spawnYTwo = startEnemyY + 1;
                }
                break;
            case 2:
            case 1:
                if(startEnemyY == 0 && startEnemyX == 0){
                    spawnXOne = startEnemyX + 1;
                    spawnXTwo = startEnemyX;
                    spawnYOne = startEnemyY;
                    spawnYTwo = startEnemyY;
                }
                else if(startEnemyY == (ConfigFileManager.X_DIMENSION - 1) && startEnemyX == ConfigFileManager.X_DIMENSION - 1){
                    spawnXOne = startEnemyX - 1;
                    spawnXTwo = startEnemyX;
                    spawnYOne = startEnemyY;
                    spawnYTwo = startEnemyY;
                }
                else{
                    spawnXOne = startEnemyX + 1;
                    spawnXTwo = startEnemyX - 1;
                    spawnYOne = startEnemyY;
                    spawnYTwo = startEnemyY;
                }
                break;
        }
        retVal[0] = new FighterPlane(planeDetails, spawnXOne, spawnYOne);
        retVal[1] = new FighterPlane(planeDetails, spawnXTwo, spawnYTwo);
        return retVal;
    }

}
