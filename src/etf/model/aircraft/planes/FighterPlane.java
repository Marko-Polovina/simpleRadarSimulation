package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Military;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FighterPlane extends Plane implements Military {
    List<String> armaments;

    public FighterPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, Consumer<Aircraft> movement, int currentX, int currentY) {
        super(characteristics, model, id, height, velocity, personList, movement, currentX, currentY);
    }

    public FighterPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, Consumer<Aircraft> movement, int currentX, int currentY, List<String> armaments) {
        super(characteristics, model, id, height, velocity, personList, movement, currentX, currentY);
        this.armaments = armaments;
    }

    public boolean attackTarget(){
        return true;
    }
}
