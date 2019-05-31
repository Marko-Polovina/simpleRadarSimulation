package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Plane extends Aircraft {
    public Plane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, Consumer<Aircraft> movement, int currentX, int currentY) {
        super(characteristics, model, id, height, velocity, personList, movement, currentX, currentY);
    }
}
