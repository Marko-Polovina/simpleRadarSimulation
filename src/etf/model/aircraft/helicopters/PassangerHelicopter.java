package etf.model.aircraft.helicopters;

import etf.model.aircraft.Aircraft;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;

public class PassangerHelicopter extends Aircraft {
    private int seats;

    public PassangerHelicopter(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int seats) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.seats = seats;
    }

    public String toString(){
        return ""+this.hashCode()+"#Passanger Helicopter"+"#passangers!" + this.seats + super.toString();
    }
}
