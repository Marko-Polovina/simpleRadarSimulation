package etf.model.airspace;

import etf.model.aircraft.Aircraft;

import java.util.ArrayList;
import java.util.List;

public class Field {
    List<Aircraft> aircrafts;

    public Field(){
        aircrafts = new ArrayList<>();
    }

    public void addAircraft(Aircraft aircraft){aircrafts.add(aircraft);}

    public List<Aircraft> getAircrafts(){return aircrafts;}

    public void removeAircraft(Aircraft aircraft){aircrafts.remove(aircraft);}

    public void removeAircraft(int i){aircrafts.remove(i);}



}
