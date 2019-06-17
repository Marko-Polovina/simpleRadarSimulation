package etf.model.aircraft.helicopters;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Firefighting;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;

public class FireghtingHelicopter extends Helicopter implements Firefighting {
    private int waterAmount;

    public FireghtingHelicopter(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int waterAmount) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.waterAmount = waterAmount;
    }

    public String toString(){
        return ""+this.hashCode()+"#Firefighting Helicopter"+"#wateramount!" + this.waterAmount + super.toString();
    }
}
