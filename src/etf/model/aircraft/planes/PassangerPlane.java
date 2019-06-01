package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.PassangerAircraft;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class PassangerPlane extends Aircraft implements PassangerAircraft {
    private int passangerSeats;
    private double maxCarryWeight;

    public PassangerPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int passangerSeats, double maxCarryWeight) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.passangerSeats = passangerSeats;
        this.maxCarryWeight = maxCarryWeight;
    }

    public int getPassangerSeats() {
        return passangerSeats;
    }

    public void setPassangerSeats(int passangerSeats) {
        this.passangerSeats = passangerSeats;
    }

    public double getMaxCarryWeight() {
        return maxCarryWeight;
    }

    public void setMaxCarryWeight(double maxCarryWeight) {
        this.maxCarryWeight = maxCarryWeight;
    }

    public String toString(){
        return ""+this.hashCode()+"#Passanger Plane"+"#passangers!" + this.passangerSeats + "#carryweight!" + this.maxCarryWeight +super.toString();
    }
}
