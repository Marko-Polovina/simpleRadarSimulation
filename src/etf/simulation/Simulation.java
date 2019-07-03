package etf.simulation;

import etf.customLogger.CustomLogger;
import etf.fileManagers.ConfigFileManager;
import etf.model.aircraft.Aircraft;
import etf.model.aircraft.helicopters.FireghtingHelicopter;
import etf.model.aircraft.helicopters.PassangerHelicopter;
import etf.model.aircraft.helicopters.TransportHelicopter;
import etf.model.aircraft.planes.*;
import etf.model.aircraft.unmanned.UnmannedAircraft;
import etf.model.airspace.Airspace;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Simulation extends Thread{
    CustomLogger cl = new CustomLogger(this);
    Airspace airspace;
    static boolean spawnHunters = false;
    static int startX = 0;
    static int startY = 0;
    Object lockHunterCreation = new Object();

    public Simulation(Airspace airspace){
        this.airspace = airspace;
    }

    public static void notifySimulationToCreateHunters(int startXlocal, int startYlocal){
        startX = startXlocal;
        startY = startYlocal;
        spawnHunters = true;
    }

    @Override
    public void run() {
        while (true) {
            if (ConfigFileManager.checkFlightBan()) {
                try {
                    System.out.println("Zabranjen let.");
                    sleep(ConfigFileManager.SPAWN_RATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (spawnHunters) {
                synchronized (lockHunterCreation) {
                    System.out.println("Skonto da trebaju lovci." + startX + " " + startY);
                    List<Aircraft> aircraftList = airspace.getAllAircrafts().stream().filter(x -> !x.isFriendly()).collect(Collectors.toList());
                    if (aircraftList.size() == 0) {
                        System.out.println("Izasao prije pravljenja lovaca.");
                        spawnHunters = false;
                        startX = 0;
                        startY = 0;
                    }else {
                        Aircraft aircraft = aircraftList.get(0);
                        System.out.println("Dosao do pravljenja lovaca." + aircraft.getMoveDirection());
                        airspace.createAndSendHunters(aircraft.getMoveDirection(), startX, startY);
                        spawnHunters = false;
                        startX = 0;
                        startY = 0;
                    }
                }
            } else {
                Aircraft aircraft = null;
                Random rnd = new Random();
                int x, y;
                //odabir pocetne lokacije
                boolean horizontalOrVertival = rnd.nextBoolean(); //true krece se vodoravno, false vertikalno, mogu se desiti situacije (0,0),(99,99),(0,99),(99,0) gdje je kretanje proizvoljno odredjeno
                if (horizontalOrVertival) {
                    boolean leftOrRight = rnd.nextBoolean(); //true lijevo, false desno
                    if (leftOrRight) {
                        x = ConfigFileManager.X_DIMENSION - 1;
                        y = rnd.nextInt(ConfigFileManager.Y_DIMENSION);
                    } else {
                        x = 0;
                        y = rnd.nextInt(ConfigFileManager.Y_DIMENSION);
                    }
                } else {
                    boolean upOrDown = rnd.nextBoolean(); //true gore, false dole
                    if (upOrDown) {
                        y = ConfigFileManager.Y_DIMENSION - 1;
                        x = rnd.nextInt(ConfigFileManager.X_DIMENSION);
                    } else {
                        y = 0;
                        x = rnd.nextInt(ConfigFileManager.X_DIMENSION);
                    }
                }
                //odabir tipa letjelice
                int aircraftType;
                label:
                do {
                    aircraftType = rnd.nextInt(10);
                    //provjera validnosti
                    if (aircraftType == 9) {
                        if (ConfigFileManager.checkEnemyMilitaryAircraftSpawn()) {
                            System.out.println("pravi neprijateljski avion");
                            aircraft = new FighterPlane(ConfigFileManager.ENEMY_AIRCRAFT_PROPERTIES, x, y);
                            break;
                        }
                    } else if (aircraftType == 8) {
                        if (ConfigFileManager.checkFriendlyMilitaryAircraftSpawn()) {
                            System.out.println("pravi prijateljski avion");
                            aircraft = new FighterPlane(ConfigFileManager.FRIENDLY_HUNTER_PROPERTIES, x, y);
                            break;
                        }
                    } else if (aircraftType == 7) {
                        if (ConfigFileManager.checkFriendlyMilitaryAircraftSpawn()) {
                            System.out.println("pravi prijateljski bombarder");
                            aircraft = new Bomber(ConfigFileManager.BOMBER, x, y);
                            break;
                        }
                    } else if (aircraftType == 6) {
                        System.out.println("pravi transportni avion");
                        aircraft = new TransportPlane(ConfigFileManager.TRANSPORT_AIRPLANE_PROPERTIES, x, y);
                    } else if (aircraftType == 5) {
                        System.out.println("pravi transportni helikopter");
                        aircraft = new TransportHelicopter(ConfigFileManager.TRANSPORT_HELICOPTER_PROPERTIES, x, y);
                    } else if (aircraftType == 4) {
                        System.out.println("pravi bespilotnu");
                        aircraft = new UnmannedAircraft(ConfigFileManager.UNMANNED_AIRCRAFT_PROPERTIES, x, y);
                    } else if (aircraftType == 3) {
                        System.out.println("pravi protivpozarni avion");
                        aircraft = new FirefightingPlane(ConfigFileManager.FIREFIGHTING_AIRPLANE_PROPERTIES, x, y);
                    } else if (aircraftType == 2) {
                        System.out.println("pravi putnicki avion");
                        aircraft = new PassangerPlane(ConfigFileManager.PASSANGER_AIRPLANE_PROPERTIES, x, y);
                    } else if (aircraftType == 1) {
                        System.out.println("pravi putnicki helikopter");
                        aircraft = new PassangerHelicopter(ConfigFileManager.PASSANGER_HELICOPTER_PROPERTIES, x, y);
                    } else if (aircraftType == 0) {
                        System.out.println("pravi protivpozarni helikopter");
                        aircraft = new FireghtingHelicopter(ConfigFileManager.FIREFIGHTING_HELICOPTER_PROPERTIES, x, y);
                    }
                }
                while (aircraftType == 9 || aircraftType == 8 || aircraftType == 7);
                //napravilo i pozicioniralo letjelicu koja moze biti bilo kojeg tipa, relativno slucajno
                aircraft.start();

                //cekanje spawnRate-a, po zadataku 5 sekundi, prije pokretanja programa namjesta se u config.properties fajlu
                try {
                    sleep(ConfigFileManager.SPAWN_RATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    cl.logException(e.getMessage(), e);
                }
            }
        }


    }

}
