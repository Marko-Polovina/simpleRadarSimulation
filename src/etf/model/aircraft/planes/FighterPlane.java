package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Military;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FighterPlane extends Plane implements Military {
    List<String> armaments;

    public FighterPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, List<String> armaments) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.armaments = armaments;
    }


    public boolean attackTarget(){
        return true;
    }

    public String toString(){
        String retVal = new String();
        retVal.concat(""+this.hashCode()+"#Fighter Plane#");
        for(String arm : armaments){
            retVal.concat("#weapon!" + arm);
        }
        return retVal + super.toString();
    }
}
