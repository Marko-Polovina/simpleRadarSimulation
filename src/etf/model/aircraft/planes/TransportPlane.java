package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Transport;
import etf.model.person.Person;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class TransportPlane extends Aircraft implements Transport {
    private HashMap<String, Double> Cargo;
    private Double maxCarryWeight;

    public TransportPlane(HashMap<String, String> characteristics, String model, String id, int FlightHeight, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, HashMap<String, Double> cargo, Double maxCarryWeight) {
        super(characteristics, model, id, FlightHeight, velocity, personList, friendly, currentX, currentY);
        Cargo = cargo;
        this.maxCarryWeight = maxCarryWeight;
    }

    public HashMap<String, Double> getCargo() {
        return Cargo;
    }

    public void setCargo(HashMap<String, Double> cargo) {
        Cargo = cargo;
    }

    public Double getMaxCarryWeight() {
        return maxCarryWeight;
    }

    public void setMaxCarryWeight(Double maxCarryWeight) {
        this.maxCarryWeight = maxCarryWeight;
    }

    public String toString(){
        return ""+this.hashCode()+"#Transport Airplane"+"#maxcarryweight!"+this.maxCarryWeight+super.toString();
    }
}
