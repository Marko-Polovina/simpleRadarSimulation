package etf.model.aircraft;

import etf.fileManagers.ConfigFileManager;
import etf.model.airspace.Airspace;
import etf.model.person.Person;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Aircraft extends Thread implements Serializable {
    private boolean crashed = false;
    private HashMap<String,String> characteristics;
    private String model;
    private String Id;
    private int flightHeight;
    private long velocity;
    private List<Person> personList;
    private boolean friendly;
    private int currentX;
    private int currentY;

    @Override
    public void run() {
        Airspace airspace = Airspace.getAirspace();
        writeInMap(); // nakon sto se napravi upisace se u map.txt file
        int moveDirection = setMoveDirection();
        boolean flightBan = false;
        if(moveDirection == 1){
            while(moveUp(airspace) && !crashed){ //pri svakom kretanju automatski se updejtuje map.txt, na kraju kretanja se brise iz map.tx
                checkCrash();
                flightBan = checkFlightBan();
            if(flightBan)break;
            }
        }else if(moveDirection == 2){
            while(moveDown(airspace) && !crashed){
                checkCrash();
                flightBan = checkFlightBan();
                if(flightBan)break;
            }
        }else if(moveDirection == 3){
            while(moveLeft(airspace) && !crashed){
                checkCrash();
                flightBan = checkFlightBan();
                if(flightBan)break;
            }
        }else if(moveDirection == 4){
            while(moveRight(airspace) && !crashed){
                checkCrash();
                flightBan = checkFlightBan();
                if(flightBan)break;
            }
        }
        if(flightBan){ //potencijalno mijenja smjer kretanja, update map.txt se nastavlja kroz metode za kretanje
            moveDirection = closestBorder();
            if(moveDirection == 1){
                while(moveUp(airspace) && !crashed){checkCrash();}
            }else if(moveDirection == 2){
                while(moveDown(airspace) && !crashed){checkCrash();}
            }else if(moveDirection == 3){
                while(moveLeft(airspace) && !crashed){checkCrash();}
            }else if(moveDirection == 4){
                while(moveRight(airspace) && !crashed){checkCrash();}
            }
        }
        if(crashed){
            airspace.removeAircraft(currentX,currentY,this);
            deleteFromMap();
        }
    }

    private boolean checkFlightBan() {
        return ConfigFileManager.checkFlightBan();
    }

    /**
     *
     * @return 0-default, 1-up, 2-down, 3-left, 4-right
     */
    private int setMoveDirection(){
        int retVal = 0;
        if(currentX == 99){
            retVal = 3;
        }
        else if(currentX == 0){
            retVal = 4;
        }
        else if(currentY == 99){
            retVal = 1;
        }
        else {
            retVal = 2;
        }
        return retVal;
    }

    /**
     *
     * @return 0-default, 1-up, 2-down, 3-left, 4-right
     */
    private int closestBorder() {
        int retVal = 0;

        int distanceFromRight = 99 - currentX;
        int distanceFromLeft = currentX;
        int distanceFromUp = currentY;
        int distanceFromDown = 99 - currentY;

        if(distanceFromUp < distanceFromDown){
            retVal = 1;
        }
        else{
            retVal = 2;
        }
        if(retVal == 1){
            if(distanceFromRight < distanceFromUp && distanceFromRight < distanceFromLeft){
                retVal = 4;
            }
            if(distanceFromLeft < distanceFromUp && distanceFromLeft < distanceFromRight){
                retVal = 3;
            }
        }
        if(retVal == 2){
            if(distanceFromRight < distanceFromDown && distanceFromRight < distanceFromLeft){
                retVal = 4;
            }
            if(distanceFromLeft < distanceFromDown && distanceFromLeft < distanceFromRight){
                retVal = 3;
            }
        }

        return retVal;
    }

    public void setCrashed(boolean crashed){
        this.crashed = crashed;
    }

    public static long randomSpeed() {
        Random r = new Random();
        return (long)((1 + (2) * r.nextDouble())*1000);
    }


    public Aircraft(HashMap<String, String> characteristics, String model, String id, int FlightHeight, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY) {
        this.characteristics = characteristics; //mozda ucitati iz fajla, po tipu letjelice
        this.model = model;
        Id = id;
        this.flightHeight = FlightHeight;
        this.velocity = velocity;
        this.personList = personList; //mozda ucitati iz fajla
        this.currentX = currentX;
        this.currentY = currentY;
        this.friendly = friendly;
    }

    public String toString(){
        String retVal = "id!" + this.Id +"#model!"+this.model+"#flightheight!"+this.flightHeight +"#velocity!"+this.velocity+
                "#currX!"+this.currentX+"#currY!"+this.currentY+"#height!"+this.characteristics.get("height")+"#width!" +
                this.characteristics.get("width");
        return retVal;
    }

    public synchronized void writeInMap() {
        try {
            List<String> allAircrafts = Files.readAllLines(Paths.get("src/etf/files/map.txt"));
            allAircrafts.add(this.toString());
            Files.write(Paths.get("etf/files/map.txt"), allAircrafts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateInMap(){
        try {
            List<String> allAircrafts = removeFromList();
            allAircrafts.add(this.toString());
            Files.write(Paths.get("src/etf/files/map.txt"),allAircrafts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> removeFromList() throws IOException {
        List<String> allAircrafts = Files.readAllLines(Paths.get("src/etf/files/map.txt"));
        String thisAircraft = allAircrafts.stream().filter(x->x.startsWith(""+this.hashCode())).collect(Collectors.toList()).get(0);
        allAircrafts.remove(thisAircraft);
        return allAircrafts;
    }

    public synchronized void deleteFromMap(){
        try {
            List<String> allAircrafts = removeFromList();
            Files.write(Paths.get("src/etf/files/map.txt"),allAircrafts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private synchronized void checkCrash() {
        Airspace airspace = Airspace.getAirspace();
        List<Aircraft> aircraftList = airspace.getField(currentX,currentY).getAircrafts();
        Aircraft crashedAircraft = null;
        for(Aircraft aircraft : aircraftList){
            if(!aircraft.equals(this) && this.flightHeight == aircraft.flightHeight){
                crashedAircraft = aircraft;
            }
        }
        if(crashedAircraft!=null){
            //todo odradi crash report
            crashedAircraft.setCrashed(true);
            this.crashed = true;
            //sledeci put kad udarena letjelica dodje na red uklonice se iz mape
        }
    }


    /**
     *
     * @param airspace
     * @return true - avion se moze nastaviti kretati, false - avion je stigao do kraja
     */
    public boolean moveUp(Airspace airspace){
        boolean retVal;
        airspace.removeAircraft(currentX,currentY,this);
        currentY--;
        if(currentY>-1){
            airspace.addAircraft(currentX,currentY,this);
            updateInMap();
            retVal = true;
        }
        else{
            deleteFromMap();
            retVal = false;
        }
        try {
            sleep(velocity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /**
     *
     * @param airspace
     * @return true - avion se moze nastaviti kretati, false - avion je stigao do kraja
     */
    public boolean moveDown(Airspace airspace){
        boolean retVal;
        airspace.removeAircraft(currentX,currentY,this);
        currentY++;
        if(currentY<100){
            airspace.addAircraft(currentX,currentY,this);
            updateInMap();
            retVal = true;
        }
        else{
            deleteFromMap();
            retVal = false;
        }
        try {
            sleep(velocity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /**
     *
     * @param airspace
     * @return true - avion se moze nastaviti kretati, false - avion je stigao do kraja
     */
    public boolean moveRight(Airspace airspace){
        boolean retVal;
        airspace.removeAircraft(currentX,currentY,this);
        currentX++;
        if(currentX<100){
            airspace.addAircraft(currentX,currentY,this);
            updateInMap();
            retVal = true;
        }
        else{
            deleteFromMap();
            retVal = false;
        }
        try {
            sleep(velocity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /**
     *
     * @param airspace
     * @return true - avion se moze nastaviti kretati, false - avion je stigao do kraja
     */
    public boolean moveLeft(Airspace airspace){
        boolean retVal;
        airspace.removeAircraft(currentX,currentY,this);
        currentX--;
        if(currentX>-1){
            airspace.addAircraft(currentX,currentY,this);
            updateInMap();
            retVal = true;
        }
        else{
            deleteFromMap();
            retVal = false;
        }
        try {
            sleep(velocity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retVal;
    }

}
