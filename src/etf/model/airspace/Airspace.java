package etf.model.airspace;

import etf.model.aircraft.Aircraft;
import etf.model.rockets.Rockets;

import java.util.ArrayList;
import java.util.List;

public class Airspace {
    private static Airspace airspace;
    private Field[][] field = new Field[100][100];
    private List<Aircraft> allAircrafts;

    private Airspace(){
        allAircrafts = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                field[i][j] = new Field();
            }
        }
        this.airspace = this;
    }

    public static Airspace getAirspace() {
        if(airspace!=null)
            return airspace;
        else
            return new Airspace();
    }

    public synchronized List<Aircraft> getAllAircrafts(){return allAircrafts;}

    public synchronized Field getField(int x, int y){return field[x][y];}

    public synchronized void removeAircraft(int x, int y, Aircraft aircraft){
        allAircrafts.remove(aircraft);
        field[x][y].removeAircraft(aircraft);
    }

    public synchronized void moveAircraft(int currX, int currY, int newX, int newY, Aircraft aircraft){
        field[currX][currY].removeAircraft(aircraft);
        field[newX][newY].addAircraft(aircraft);
    }

    public synchronized void addAircraft(int x, int y, Aircraft aircraft){
        field[x][y].addAircraft(aircraft);
        if(!aircraft.isFriendly()){
            //todo notify simulaciju o ulasku strane letjelice u prostor
        }
    }

    public void removeRocket(int currentX, int currentY, Rockets rockets) {
        System.out.println("Uklonio raketu");
    }
}
