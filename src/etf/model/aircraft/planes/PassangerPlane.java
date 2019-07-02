package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.PassangerAircraft;
import etf.model.person.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PassangerPlane extends Plane implements PassangerAircraft {
    private int passangerSeats;
    private double maxCarryWeight;

    public PassangerPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int passangerSeats, double maxCarryWeight) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.passangerSeats = passangerSeats;
        this.maxCarryWeight = maxCarryWeight;
    }

    public PassangerPlane(String details, int spawnX, int spawnY) {
        super(details, spawnX, spawnY);
        List<String> detailsHelpArray = Arrays.asList(details.split("#"));
        String seatsStringHelp = detailsHelpArray.stream().filter(x->x.startsWith("seats")).collect(Collectors.toList()).get(0);
        this.passangerSeats = Integer.parseInt(seatsStringHelp.split("!")[1]);
        String cappacityStringHelp = detailsHelpArray.stream().filter(x->x.startsWith("maxcarry")).collect(Collectors.toList()).get(0);
        this.maxCarryWeight = Integer.parseInt(cappacityStringHelp.split("!")[1]);
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
        return super.toString() + "#Passanger Plane"+"#passangers!" + this.passangerSeats + "#carryweight!" + this.maxCarryWeight;
    }
}
