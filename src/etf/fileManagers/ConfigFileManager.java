package etf.fileManagers;

import etf.customLogger.CustomLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFileManager{
    public static double GRID_PANE_HEIGHT;
    public static double GRID_PANE_WIDTH;
    public static int X_DIMENSION;
    public static int Y_DIMENSION;
    public static String TRANSPORT_AIRPLANE_PROPERTIES;
    public static String TRANSPORT_HELICOPTER_PROPERTIES;
    public static String FRIENDLY_HUNTER_PROPERTIES;
    public static String ENEMY_AIRCRAFT_PROPERTIES;
    public static String UNMANNED_AIRCRAFT_PROPERTIES;
    public static String FIREFIGHTING_AIRPLANE_PROPERTIES;
    public static String PASSANGER_AIRPLANE_PROPERTIES;
    public static String PASSANGER_HELICOPTER_PROPERTIES;
    public static String FIREFIGHTING_HELICOPTER_PROPERTIES;
    public static String BOMBER;
    public static int SPAWN_RATE;
    private static CustomLogger cl;
    public static int HIGHT_SPAN;
    public static int RADAR_REFRESH_RATE;

    static
    {
        List<String> properties = null;
        try {
            properties = Files.readAllLines(Paths.get("src/etf/files/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        TRANSPORT_AIRPLANE_PROPERTIES = properties.stream().filter(x->x.startsWith("TRANSPORTNI_AVION")).collect(Collectors.toList()).get(0);
        TRANSPORT_HELICOPTER_PROPERTIES = properties.stream().filter(x->x.startsWith("TRANSPORTNI_HELIKOPTER")).collect(Collectors.toList()).get(0);
        FRIENDLY_HUNTER_PROPERTIES = properties.stream().filter(x->x.startsWith("DOMACI_LOVAC")).collect(Collectors.toList()).get(0);
        ENEMY_AIRCRAFT_PROPERTIES = properties.stream().filter(x->x.startsWith("NEPRIJATELJSKI_AVIONI")).collect(Collectors.toList()).get(0);
        UNMANNED_AIRCRAFT_PROPERTIES = properties.stream().filter(x->x.startsWith("BESPILOTNA_LETJELICA")).collect(Collectors.toList()).get(0);
        FIREFIGHTING_AIRPLANE_PROPERTIES = properties.stream().filter(x->x.startsWith("PROTIV_POZARNI_AVION")).collect(Collectors.toList()).get(0);
        PASSANGER_AIRPLANE_PROPERTIES = properties.stream().filter(x->x.startsWith("PUTNICKI_AVION")).collect(Collectors.toList()).get(0);
        PASSANGER_HELICOPTER_PROPERTIES = properties.stream().filter(x->x.startsWith("PUTNICKI_HELIKOPTER")).collect(Collectors.toList()).get(0);
        FIREFIGHTING_HELICOPTER_PROPERTIES = properties.stream().filter(x->x.startsWith("PROTIV_POZARNI_HELIKOPTER")).collect(Collectors.toList()).get(0);
        BOMBER = properties.stream().filter(x->x.startsWith("BOMBARDER")).collect(Collectors.toList()).get(0);
        X_DIMENSION = Integer.parseInt(properties.stream().filter(x->x.startsWith("n")).collect(Collectors.toList()).get(0).split("!")[1]);
        Y_DIMENSION = Integer.parseInt(properties.stream().filter(x->x.startsWith("m")).collect(Collectors.toList()).get(0).split("!")[1]);
        SPAWN_RATE = Integer.parseInt(properties.stream().filter(x->x.startsWith("spawnRate")).collect(Collectors.toList()).get(0).split("!")[1]);
        HIGHT_SPAN = Integer.parseInt(properties.stream().filter(x->x.startsWith("hightSpan")).collect(Collectors.toList()).get(0).split("!")[1]);
        RADAR_REFRESH_RATE = Integer.parseInt(properties.stream().filter(x->x.startsWith("radarRefreshRate")).collect(Collectors.toList()).get(0).split("!")[1]);
        GRID_PANE_HEIGHT = Double.parseDouble(properties.stream().filter(x->x.startsWith("gridPaneHeight")).collect(Collectors.toList()).get(0).split("!")[1]);
        GRID_PANE_WIDTH = Double.parseDouble(properties.stream().filter(x->x.startsWith("gridPaneWidth")).collect(Collectors.toList()).get(0).split("!")[1]);

    }

    public synchronized static boolean checkFlightBan(){
        return checkProperty("flightBan");
    }

    public synchronized static boolean checkEnemyMilitaryAircraftSpawn(){
        boolean retVal = false;
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get("src/etf/files/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        String[] enemyBan = allLines.stream().filter(x->x.startsWith("enemy")).collect(Collectors.toList()).get(0).split("!");
        if(!enemyBan[1].equals("-1"))
            retVal = true;
        return retVal;
    }

    public synchronized static boolean checkFriendlyMilitaryAircraftSpawn(){
        boolean retVal = false;
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get("src/etf/files/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        String[] friendlyBan = allLines.stream().filter(x->x.startsWith("friendly")).collect(Collectors.toList()).get(0).split("!");
        if(!friendlyBan[1].equals("-1"))
            retVal = true;
        return retVal;    }


    public synchronized static void updateFlightBan(boolean ban){
        String flightBan;
        if(ban == true){
            flightBan = new String("flightBan!-1");
        }else{
            flightBan = new String("flightBan!1");
        }
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get("src/etf/files/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        allLines.removeIf(x->x.startsWith("flightBan"));
        allLines.add(flightBan);
        try {
            Files.write(Paths.get("src/etf/files/config.properties"),allLines);
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
    }

    private static boolean checkProperty(String property){
        boolean retVal = false;
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get("src/etf/files/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
        String[] flightBan = allLines.stream().filter(x->x.startsWith(property)).collect(Collectors.toList()).get(0).split("!");
        if(!flightBan[1].equals("1"))
            retVal = true;
        return retVal;
    }

}
