package etf.model.aircraft.planes;

import etf.fileManagers.ConfigFileManager;
import etf.fileManagers.EventManager;
import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Military;
import etf.model.airspace.Airspace;
import etf.model.airspace.Field;
import etf.model.person.Person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FighterPlane extends Plane implements Military {
    List<String> armaments = new ArrayList<>();

    public FighterPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, List<String> armaments) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.armaments = armaments;
    }


    public String toString(){
        String retVal = new String();
        for(String arm : armaments){
            retVal.concat("#weapon!" + arm);
        }
        return super.toString() + retVal + "#Fighter Plane";
    }

    public FighterPlane(String details, int spawnX, int spawnY){
        super(details, spawnX, spawnY);
        List<String> detailsHelpArray = Arrays.asList(details.split("#"));
        String armamentsHelpString = detailsHelpArray.stream().filter(x->x.startsWith("armaments")).collect(Collectors.toList()).get(0);
        String[] armamentsHelpArray = armamentsHelpString.split("!");
        for(int i = 1; i < armamentsHelpArray.length; i++){
            armaments.add(armamentsHelpArray[i]);
        }
    }


    @Override
    public void shootDown(int moveDirection) {
        Object readingLock = new Object();
        Airspace airspace = Airspace.getAirspace();
        ((FighterPlane)this).writeInMap();
        boolean move = true;
        while(move) {
            Aircraft potentialEnemy = null;
            if((currentX != -1 && currentX != ConfigFileManager.X_DIMENSION) && (currentY != -1 && currentY!= ConfigFileManager.Y_DIMENSION)) {
                Field field = airspace.getField(currentX, currentY);
                synchronized (readingLock) {
                    if (airspace.getField(currentX, currentY) != null
                            && airspace.getField(currentX, currentY).getAircrafts().
                            stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).size() > 0) {
                        potentialEnemy = airspace.getField(currentX, currentY).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).get(0);
                    } else if ((currentY + 1 < ConfigFileManager.Y_DIMENSION) && airspace.getField(currentX, currentY + 1) != null && airspace.getField(currentX, currentY + 1).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).size() > 0) {
                        potentialEnemy = airspace.getField(currentX, currentY + 1).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).get(0);
                    } else if ((currentX + 1 < ConfigFileManager.X_DIMENSION) && airspace.getField(currentX + 1, currentY) != null && airspace.getField(currentX + 1, currentY).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).size() > 0) {
                        potentialEnemy = airspace.getField(currentX + 1, currentY).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).get(0);
                    } else if ((currentY - 1 >= 0) && airspace.getField(currentX, currentY - 1) != null && airspace.getField(currentX, currentY - 1).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).size() > 0) {
                        potentialEnemy = airspace.getField(currentX, currentY - 1).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).get(0);
                    } else if ((currentX - 1 >= 0) && airspace.getField(currentX - 1, currentY) != null && airspace.getField(currentX - 1, currentY).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).size() > 0) {
                        potentialEnemy = airspace.getField(currentX - 1, currentY).getAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList()).get(0);
                    }
                }
                if (potentialEnemy != null) {
                    potentialEnemy.setCrashed(true);
                    String details = "SHOOT DOWN" + "\n"
                            + "Friendly : \n"
                            + "ID: " + this.getAircraftId() + "\n"
                            + "Model" + this.getModel() + "\n"
                            + "Enemy : \n"
                            + "ID: " + potentialEnemy.getAircraftId() + "\n"
                            + "Model" + potentialEnemy.getModel() + "\n";

                    EventManager.noteEvent(details, LocalDateTime.now());
                    //rusenje biljezi kao event, nije navedena sta treba raditi
                }
                //1-up, 2-down, 3-left, 4-right
                if (moveDirection == 1) {
                    moveUp(airspace);
                } else if (moveDirection == 2) {
                    moveDown(airspace);
                } else if (moveDirection == 3) {
                    moveLeft(airspace);
                } else if (moveDirection == 4) {
                    moveRight(airspace);
                }
            }else{
                move = false;
                airspace.removeAircraft(this.getCurrentX(),this.getCurrentY(),this);
                deleteFromMap();
            }
        }
    }
}
