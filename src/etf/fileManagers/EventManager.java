package etf.fileManagers;

import etf.customLogger.CustomLogger;
import etf.gui.controller.mainController.MainViewController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.*;

public class EventManager extends Thread {
    static CustomLogger cl;
    static String currentLast = null;
    static MainViewController mvc;

    public EventManager(MainViewController mvc){
        this.mvc = mvc;
    }

    @Override
    public void run(){
        SimpleStringProperty labelValue = new SimpleStringProperty();
        mvc.getEventsLabel().textProperty().bind(labelValue);
        while(true){
            File dir = new File("src\\etf\\files\\events\\");
            File[] allFiles = null;
            allFiles = dir.listFiles();
            if(allFiles.length <= 0 && currentLast == null){
                Platform.runLater(()-> labelValue.set("Nema događaja."));
            }
            if(allFiles.length == 1){
                currentLast = allFiles[0].getAbsolutePath();

            }
            if(allFiles.length>=1) {
                if(currentLast == null){
                    currentLast = allFiles[0].getAbsolutePath();
                }
                List<File> files = Arrays.asList(allFiles);
                files.sort((x,y)->{
                    BasicFileAttributes fileX = null;
                    BasicFileAttributes fileY = null;
                    try {
                        fileX = Files.readAttributes(Paths.get(x.getAbsolutePath()),BasicFileAttributes.class);
                        fileY = Files.readAttributes(Paths.get(y.getAbsolutePath()),BasicFileAttributes.class);
                    } catch (IOException e) {
                        cl.logException(e.getMessage(),e);
                    }
                    return fileY.creationTime()
                            .compareTo(fileX.creationTime());
                });
                BasicFileAttributes currentLastFile = null;
                BasicFileAttributes potentialLastFile = null;
                try {
                    currentLastFile = Files.readAttributes(Paths.get(currentLast),BasicFileAttributes.class);
                    potentialLastFile = Files.readAttributes(Paths.get(files.get(0).getAbsolutePath()),BasicFileAttributes.class);
                } catch (IOException e) {
                    cl.logException(e.getMessage(),e);
                }
                boolean greaterThan
                        = currentLastFile.creationTime()
                        .compareTo(potentialLastFile.creationTime()) < 0;
                if(greaterThan){
                    currentLast = files.get(0).getAbsolutePath();
                    Platform.runLater(()-> labelValue.set("Pojavio se strani avion!   " + new File(currentLast).getName()));

                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void noteEvent(String eventDetails, LocalDateTime eventTime){
        String eventFileName = eventTime.getHour()+ "H" + eventTime.getMinute() + "M" + eventTime.getSecond()  + "s.txt";
        List<String> event = new ArrayList<>();
        event.add(eventDetails);
        Path eventFilePath = Paths.get("src\\etf\\files\\events\\" + eventFileName);
        try {
            Files.write(eventFilePath, event);
        } catch (IOException e) {
            e.printStackTrace();
            cl.logException(e.getMessage(),e);
        }
    }

    public static List<List<String>> readEvents() {
        List<List<String>> retVal = new ArrayList<>();

        File dir = new File("src\\etf\\files\\events\\");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {
                    retVal.add(Files.readAllLines(Paths.get(child.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                    cl.logException(e.getMessage(),e);
                }
            }
        }

        return retVal;
    }

}
