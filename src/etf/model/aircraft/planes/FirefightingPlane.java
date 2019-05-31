package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Firefighting;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FirefightingPlane extends Plane implements Firefighting {
    private int waterAmount;

    public FirefightingPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, Consumer<Aircraft> movement, int currentX, int currentY) {
        super(characteristics, model, id, height, velocity, personList, movement, currentX, currentY);
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }
}
