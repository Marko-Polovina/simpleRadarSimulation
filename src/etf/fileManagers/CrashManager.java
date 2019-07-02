package etf.fileManagers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CrashManager implements Serializable {

    public static final long serialVersionUID = 9001;

    private String details;
    private String time;
    private String position;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public CrashManager(String details, String time, String position) {
        this.details = details;
        this.time = time;
        this.position = position;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src\\etf\\files\\alert\\" + time + ".ser"));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CrashManager> readCrashes() {
        List<CrashManager> retVal = new ArrayList<>();
        File dir = new File("src\\etf\\files\\alert\\");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(child.getAbsolutePath()));
                    CrashManager cm = (CrashManager) ois.readObject();
                    ois.close();
                    retVal.add(cm);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return retVal;
    }

}
