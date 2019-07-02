package etf.model.aircraft;

import etf.customLogger.CustomLogger;
import etf.fileManagers.ConfigFileManager;
import etf.fileManagers.CrashManager;
import etf.model.airspace.Airspace;
import etf.model.person.Person;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Aircraft extends Thread implements Serializable {
    CustomLogger cl = new CustomLogger(this);
    private static Integer idHelp = 0;

    private boolean crashed = false;
    private HashMap<String,String> characteristics = new HashMap<>();
    private String model;
    private String Id;
    private int flightHeight;
    private long velocity;
    private List<Person> personList;
    private boolean friendly;
    protected int currentX;
    protected int currentY;
    private static Object writterLock;
    private static Object crashLock;

    static{
        writterLock = new Object();
        crashLock = new Object();
    }

    private int moveDirection;
    private boolean noted = false;
    private boolean warnedToExit = false;

    public void warneToExit() {
        this.warnedToExit = true;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public String getAircraftId(){return this.Id;}

    public Aircraft(String details, int spawnX, int spawnY){
        this.velocity = randomSpeed();
        this.friendly = !details.startsWith("NEPRIJATELJSKI_AVIONI");
        String[] detailsHelp = details.split("#");
        String[] modelsHelp = new String[0];
        String characteristicsHelpString = null;
        for(int i = 0; i < detailsHelp.length; i++){
            if(detailsHelp[i].startsWith("model")){
                modelsHelp = detailsHelp[i].split("!");
            }else if(detailsHelp[i].startsWith("characteristics")){
                characteristicsHelpString = detailsHelp[i];
            }
        }
        this.model = modelsHelp[1];
        idHelp++;
        this.Id = idHelp.toString();
        Random rnd = new Random();
        this.flightHeight = (rnd.nextInt(ConfigFileManager.HIGHT_SPAN) + 1)*250;
        currentX = spawnX;
        currentY = spawnY;
        String[] characteristicsHelpArray = characteristicsHelpString.split("!");
        for(int i = 1; i < characteristicsHelpArray.length; i++){
            String hlp1 = characteristicsHelpArray[i].split("@")[0];
            String hlp2 = characteristicsHelpArray[i].split("@")[1];
            characteristics.put(hlp1,hlp2);
        }
        setMoveDirection();
    }


    @Override
    public void run() {
        ConfigFileManager cnf = new ConfigFileManager();
        if(friendly) {
            writeInMap();
            Airspace airspace = Airspace.getAirspace();
            airspace.addAircraft(currentX, currentY, this);
            boolean flightBan = false;
            if (this.moveDirection == 1) {
                while (moveUp(airspace) && !crashed && !warnedToExit) { //pri svakom kretanju automatski se updejtuje map.txt, na kraju kretanja se brise iz map.tx
                    checkCrash();
                    flightBan = checkFlightBan();
                    if (flightBan) break;
                }
            } else if (this.moveDirection == 2) {
                while (moveDown(airspace) && !crashed && !warnedToExit) {
                    checkCrash();
                    flightBan = checkFlightBan();
                    if (flightBan) break;
                }
            } else if (this.moveDirection == 3) {
                while (moveLeft(airspace) && !crashed && !warnedToExit) {
                    checkCrash();
                    flightBan = checkFlightBan();
                    if (flightBan) break;
                }
            } else if (this.moveDirection == 4) {
                while (moveRight(airspace) && !crashed && !warnedToExit) {
                    checkCrash();
                    flightBan = checkFlightBan();
                    if (flightBan) break;
                }
            }
            if (flightBan || warnedToExit) { //potencijalno mijenja smjer kretanja, update map.txt se nastavlja kroz metode za kretanje
                closestBorder(moveDirection);
                moveWithoutCheckingFlightBan(moveDirection,airspace);
            }
            if (crashed) {
                airspace.removeAircraft(currentX, currentY, this);
                deleteFromMap();
            }
        }
        else if (!friendly){
            Airspace airspace = Airspace.getAirspace();
            airspace.addAircraft(currentX, currentY, this);
            writeInMap();
            moveWithoutCheckingFlightBan(moveDirection,airspace);
            if (crashed) {
                airspace.removeAircraft(currentX, currentY, this);
                deleteFromMap();
            }
        }
    }

    private void moveWithoutCheckingFlightBan(int moveDirection, Airspace airspace){
        if (moveDirection == 1) {
            while (moveUp(airspace) && !crashed) {
                checkCrash();
            }
        } else if (moveDirection == 2) {
            while (moveDown(airspace) && !crashed) {
                checkCrash();
            }
        } else if (moveDirection == 3) {
            while (moveLeft(airspace) && !crashed) {
                checkCrash();
            }
        } else if (moveDirection == 4) {
            while (moveRight(airspace) && !crashed) {
                checkCrash();
            }
        }
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    private boolean checkFlightBan() {
        return ConfigFileManager.checkFlightBan();
    }

    /**
     *
     * @return 0-default, 1-up, 2-down, 3-left, 4-right
     */
    public int setMoveDirection(){
        int retVal = 0;
        if(currentX == ConfigFileManager.X_DIMENSION - 1){
            retVal = 3;
        }
        else if(currentX == 0){
            retVal = 4;
        }
        else if(currentY == ConfigFileManager.Y_DIMENSION - 1){
            retVal = 1;
        }
        else {
            retVal = 2;
        }
        this.moveDirection = retVal;
        return retVal;
    }

    /**
     *
     * @return 0-default, 1-up, 2-down, 3-left, 4-right
     * @param currentMoveDirection u slucaju da se letjelica treba okrenuti nazad ne moze, treba polukruzno
     */
    private int closestBorder(int currentMoveDirection) {
        int retVal = 0;

        int distanceFromRight = (ConfigFileManager.X_DIMENSION -1) - currentX;
        int distanceFromLeft = currentX;
        int distanceFromUp = currentY;
        int distanceFromDown = (ConfigFileManager.Y_DIMENSION - 1) - currentY;

        if(distanceFromUp < distanceFromDown){
            retVal = 1;
            if(currentMoveDirection == 2){
                turnAround(retVal, currentMoveDirection);
            }
        }
        else{
            retVal = 2;
            if(currentMoveDirection == 1){
                turnAround(retVal, currentMoveDirection);
            }
        }
        if(retVal == 1){
            if(distanceFromRight < distanceFromUp && distanceFromRight < distanceFromLeft){
                retVal = 4;
                if(currentMoveDirection == 3){
                    turnAround(retVal, currentMoveDirection);
                }
            }
            if(distanceFromLeft < distanceFromUp && distanceFromLeft < distanceFromRight){
                retVal = 3;
                if(currentMoveDirection == 4){
                    turnAround(retVal, currentMoveDirection);
                }
            }
        }
        if(retVal == 2){
            if(distanceFromRight < distanceFromDown && distanceFromRight < distanceFromLeft){
                retVal = 4;
                if(currentMoveDirection == 3){
                    turnAround(retVal, currentMoveDirection);
                }
            }
            if(distanceFromLeft < distanceFromDown && distanceFromLeft < distanceFromRight){
                retVal = 3;
                if(currentMoveDirection == 4){
                    turnAround(retVal, currentMoveDirection);
                }
            }
        }
        this.moveDirection = retVal;
        return retVal;
    }

    private void turnAround(int retVal, int currentMoveDirection) {
        //ako ide gore ili dole i treba promjeniti smjer za 180 prvo ce se pomjeriti po mogucnosti lijevo sem ako je uz lijevi rub
        if((retVal == 1 && currentMoveDirection == 2) || (retVal == 2 && currentMoveDirection == 1)){
            if(currentY==0){
                moveRight(Airspace.getAirspace());
                checkCrash();
            }else{
                moveLeft(Airspace.getAirspace());
                checkCrash();
            }
        }
        //ako ide lijevo ili desno i treba promjeniti smjer za 180 prvo ce se pomjeriti po mogucnosti gore sem ako je uz gornji rub
        else if((retVal ==  3 && currentMoveDirection == 4) || (retVal == 4 && currentMoveDirection == 3)){
            if(currentX==0){
                moveDown(Airspace.getAirspace());
                checkCrash();
            }else{
                moveUp(Airspace.getAirspace());
                checkCrash();
            }
        }
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
        String retVal = "id!" + this.Id +"#model!"+this.model+ "#friendly!" + this.friendly + "#flightheight!"+this.flightHeight +"#velocity!"+this.velocity+
                "#currX!"+this.currentX+"#currY!"+this.currentY+"#height!"+this.characteristics.get("height")+"#width!" +
                this.characteristics.get("width");
        return retVal;
    }

    public void writeInMap() {
        try {
            List<String> allAircrafts = null;
            synchronized (writterLock) {
                allAircrafts = Files.readAllLines(Paths.get("src/etf/files/map.txt"));
                allAircrafts.add(this.toString());
                Files.write(Paths.get("src/etf/files/map.txt"), allAircrafts);
            }
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
    }

    public void updateInMap(){
        try {
            synchronized (writterLock){
                List<String> allAircrafts = removeFromList();
                allAircrafts.add(this.toString());
                Files.write(Paths.get("src/etf/files/map.txt"), allAircrafts);
            }
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
    }

    private List<String> removeFromList() throws IOException {
        List<String> allAircrafts = null;
        try{
            allAircrafts = Files.readAllLines(Paths.get("src/etf/files/map.txt"));
            List<String> compatibleAircrafts = null;
            String thisAircraft = null;
            compatibleAircrafts = allAircrafts.stream().filter(
                    x -> x.startsWith("id!" + this.getAircraftId())).collect(Collectors.toList());
            if(compatibleAircrafts.size()>0){
                thisAircraft = compatibleAircrafts.get(0);
            }
            if(thisAircraft!=null) {
                allAircrafts.remove(thisAircraft);
            }
        }catch (IndexOutOfBoundsException iobe){
            cl.logException(iobe.getMessage(), iobe);
        }
        return allAircrafts;
    }

    public void deleteFromMap(){
        try {
            synchronized (writterLock) {
                List<String> allAircrafts = removeFromList();
                Files.write(Paths.get("src/etf/files/map.txt"), allAircrafts);
            }
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
    }



    private void checkCrash() {
        Airspace airspace = Airspace.getAirspace();
        List<Aircraft> aircraftList = airspace.getField(currentX,currentY).getAircrafts();
        Aircraft crashedAircraft = null;
        for(Aircraft aircraft : aircraftList){
            if(!aircraft.equals(this) && this.flightHeight == aircraft.flightHeight){
                crashedAircraft = aircraft;
            }
        }
        if(crashedAircraft!=null) {
            synchronized (crashLock) {
                crashedAircraft.setCrashed(true);
                this.crashed = true;
                String details = "First Aircraft : " + this.getAircraftId() + " Second Aircraft : " + crashedAircraft.getAircraftId();
                String time = LocalDateTime.now().getHour()+"_"+LocalDateTime.now().getMinute()+"_"+LocalDateTime.now().getSecond();
                String position = "X : " + this.currentY + " Y : " + this.currentY;
                CrashManager cm = new CrashManager(details, time, position); // upisuje sudar odmah
            }
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
            cl.logException(e.getMessage(),e);
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
        if(currentY<ConfigFileManager.Y_DIMENSION){
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
            cl.logException(e.getMessage(),e);
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
        if(currentX<ConfigFileManager.X_DIMENSION){
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
            cl.logException(e.getMessage(),e);
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
            cl.logException(e.getMessage(),e);
        }
        return retVal;
    }

    public String getModel() {
        return model;
    }

    public void noteAircraft(){
        noted = true;
    }

    public boolean isNoted() {
        return noted;
    }

    public int getMoveDirection() {
        return this.moveDirection;
    }
}
