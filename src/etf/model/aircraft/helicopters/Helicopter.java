package etf.model.aircraft.helicopters;

import etf.model.aircraft.Aircraft;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;

public class Helicopter extends Aircraft {
    public Helicopter(HashMap<String, String> characteristics, String model, String id, int FlightHeight, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY) {
        super(characteristics, model, id, FlightHeight, velocity, personList, friendly, currentX, currentY);
    }
}
