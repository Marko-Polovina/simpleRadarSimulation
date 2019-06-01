package etf.model.aircraft.unmanned;

import etf.model.aircraft.Aircraft;
import etf.model.person.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UnmannedAircraft extends Aircraft {
    public UnmannedAircraft(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, boolean friendly, int currentX, int currentY) {
        super(characteristics, model, id, height, velocity, personList, friendly, currentX, currentY);
    }

    public boolean takeShoots(){
        return true;
    }

    public String toString(){
        return ""+this.hashCode()+"#Unmanned#"+super.toString();
    }
}
