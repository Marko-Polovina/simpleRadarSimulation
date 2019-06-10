package etf.fileManagers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFileManager{
    public synchronized static boolean checkFlightBan(){
        return checkProperty("flightBan");
    }

    public synchronized static boolean checkEnemyMilitaryAircraftSpawn(){
        return checkProperty("enemy");
    }

    public synchronized static boolean checkFriendlyMilitaryAircraftSpawn(){
        return checkProperty("friendly");
    }

    public synchronized static void updateFlightBan(boolean ban){
        String flightBan;
        if(ban == true){
            flightBan = new String("flightBan$-1");
        }else{
            flightBan = new String("flightBan$1");
        }
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get("etf/files/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        allLines.removeIf(x->x.startsWith("flightBan"));
        allLines.add(flightBan);
        try {
            Files.write(Paths.get("etf/files/config.properties"),allLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkProperty(String property){
        boolean retVal = false;
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get("etf/files/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] flightBan = allLines.stream().filter(x->x.startsWith(property)).collect(Collectors.toList()).get(0).split("$");
        if(!flightBan[1].equals("0"))
            retVal = true;
        return retVal;
    }
}
