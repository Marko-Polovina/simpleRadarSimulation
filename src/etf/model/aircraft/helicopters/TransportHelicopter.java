package etf.model.aircraft.helicopters;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.PassangerAircraft;
import etf.model.aircraft.Transport;
import etf.model.person.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TransportHelicopter extends Helicopter implements Transport {
    private int carryWeight;

    public TransportHelicopter(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int carryWeight) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.carryWeight = carryWeight;
    }

    public TransportHelicopter(String details, int spawnX, int spawnY){
        super(details, spawnX, spawnY);
        List<String> detailsHelpArray = Arrays.asList(details.split("#"));
        String capacityStringHelp = detailsHelpArray.stream().filter(x->x.startsWith("cargospace")).collect(Collectors.toList()).get(0);
        this.carryWeight = Integer.parseInt(capacityStringHelp.split("!")[1]);

    }

    public int getCarryWeight() {
        return carryWeight;
    }

    public void setCarryWeight(int carryWeight) {
        this.carryWeight = carryWeight;
    }

    public String toString(){
        return super.toString()+"#Transport Helicopter"+"#maxcarryweight!"+this.carryWeight;
    }
}
