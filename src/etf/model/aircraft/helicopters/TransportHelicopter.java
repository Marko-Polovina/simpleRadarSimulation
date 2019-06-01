package etf.model.aircraft.helicopters;

import etf.model.aircraft.Aircraft;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class TransportHelicopter extends Aircraft {
    private int carryWeight;

    public TransportHelicopter(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int carryWeight) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.carryWeight = carryWeight;
    }

    public int getCarryWeight() {
        return carryWeight;
    }

    public void setCarryWeight(int carryWeight) {
        this.carryWeight = carryWeight;
    }

    public String toString(){
        return ""+this.hashCode()+"#Transport Helicopter"+"#maxcarryweight!"+this.carryWeight+super.toString();
    }
}
