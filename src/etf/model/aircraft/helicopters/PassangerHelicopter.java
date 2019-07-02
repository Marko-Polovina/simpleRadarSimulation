package etf.model.aircraft.helicopters;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.PassangerAircraft;
import etf.model.aircraft.Transport;
import etf.model.person.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PassangerHelicopter extends Helicopter implements PassangerAircraft {
    private int seats;

    public PassangerHelicopter(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int seats) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.seats = seats;
    }

    public PassangerHelicopter(String details, int spawnX, int spawnY){
        super(details, spawnX, spawnY);
        List<String> detailsHelpArray = Arrays.asList(details.split("#"));
        String seatsStringHelp = detailsHelpArray.stream().filter(x->x.startsWith("seats")).collect(Collectors.toList()).get(0);
        this.seats = Integer.parseInt(seatsStringHelp.split("!")[1]);

    }

    public String toString(){
        return super.toString() + "#Passanger Helicopter"+"#passangers!" + this.seats;
    }
}
