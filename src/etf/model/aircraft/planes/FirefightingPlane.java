package etf.model.aircraft.planes;

import etf.model.aircraft.Aircraft;
import etf.model.aircraft.Firefighting;
import etf.model.person.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FirefightingPlane extends Plane implements Firefighting {
    private Integer waterAmount;

    public FirefightingPlane(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY, int waterAmount) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
        this.waterAmount = waterAmount;
    }

    public FirefightingPlane(String details, int spawnX, int spawnY) {
        super(details, spawnX, spawnY);
        List<String> detailsHelpArray = Arrays.asList(details.split("#"));
        String waterAmountStringHelp = detailsHelpArray.stream().filter(x->x.startsWith("wateramount")).collect(Collectors.toList()).get(0);
        this.waterAmount = Integer.parseInt(waterAmountStringHelp.split("!")[1]);
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }

    public String toString(){
        return super.toString() + "#Firefighting Plane"+"#wateramount!" + this.waterAmount;
    }
}
