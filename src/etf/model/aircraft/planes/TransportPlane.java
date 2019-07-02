package etf.model.aircraft.planes;

import etf.model.aircraft.Transport;
import etf.model.person.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TransportPlane extends Plane implements Transport {
    private HashMap<String, Double> cargo = new HashMap<>();
    private Double maxCarryWeight;

    public TransportPlane(HashMap<String, String> characteristics, String model, String id, int FlightHeight, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, HashMap<String, Double> cargo, Double maxCarryWeight) {
        super(characteristics, model, id, FlightHeight, velocity, personList, friendly, currentX, currentY);
        this.cargo = cargo;
        this.maxCarryWeight = maxCarryWeight;
    }

    public TransportPlane(String details, int spawnX, int spawnY) {
        super(details, spawnX, spawnY);
        List<String> detailsHelpArray = Arrays.asList(details.split("#"));
        String seatsStringHelp = detailsHelpArray.stream().filter(x->x.startsWith("cargospace")).collect(Collectors.toList()).get(0);
        this.maxCarryWeight = Double.parseDouble(seatsStringHelp.split("!")[1]);
        this.cargo.put("Cargo", this.maxCarryWeight/10);
    }

    public HashMap<String, Double> getCargo() {
        return cargo;
    }

    public void setCargo(HashMap<String, Double> cargo) {
        this.cargo = cargo;
    }

    public Double getMaxCarryWeight() {
        return maxCarryWeight;
    }

    public void setMaxCarryWeight(Double maxCarryWeight) {
        this.maxCarryWeight = maxCarryWeight;
    }

    public String toString(){
        return super.toString()+"#Transport Airplane"+"#maxcarryweight!"+this.maxCarryWeight;
    }

}
