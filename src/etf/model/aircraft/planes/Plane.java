package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;

public class Plane extends Aircraft {
    public Plane(HashMap<String, String> characteristics, String model, String id, int FlightHeight, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY) {
        super(characteristics, model, id, FlightHeight, velocity, personList, friendly, currentX, currentY);
    }

    public Plane(String details, int spawnX, int spawnY){
        super(details, spawnX, spawnY);
    }
}
