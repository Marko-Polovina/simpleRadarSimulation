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
            Field field = airspace.getField(currentX,currentY);
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
            if(moveDirection == 1 || moveDirection == 2 || moveDirection == 3){
                move = moveRight(airspace);
            }else if(moveDirection == 4 || moveDirection == 5 || moveDirection == 6){
                move = moveLeft(airspace);
            }else if(moveDirection == 7 || moveDirection == 8 || moveDirection == 9){
                move = moveUp(airspace);
            }else if(moveDirection == 10 || moveDirection == 11 || moveDirection == 12){
                move = moveDown(airspace);
            }
        }
    }
}
