package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Firefighting;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FirefightingPlane extends Aircraft implements Firefighting {
    private int waterAmount;

    public FirefightingPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int waterAmount) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.waterAmount = waterAmount;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }

    public String toString(){
        return ""+this.hashCode()+"#Firefighting Plane"+"#wateramount!" + this.waterAmount + super.toString();
    }
}
