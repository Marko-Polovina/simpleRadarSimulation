package etf.fileManagers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CrashManager {
    private String details;
    private String time;
    private String position;

    public CrashManager(String details, String time, String position) {
        this.details = details;
        this.time = time;
        this.position = position;
        List<String> writeIn = new ArrayList<>();
        writeIn.add(details);
        writeIn.add(time);
        writeIn.add(position);
        try {
            Files.write(Paths.get("C:\\Users\\Marko\\Documents\\simpleRadarSimulation\\src\\etf\\files\\alert\\" + time + ".txt"),writeIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<String>> readCrashes() {
        List<List<String>> retVal = new ArrayList<>();
        File dir = new File("C:\\Users\\Marko\\Documents\\simpleRadarSimulation\\src\\etf\\files\\alert\\");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {
                    retVal.add(Files.readAllLines(Paths.get(child.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retVal;
    }

}
