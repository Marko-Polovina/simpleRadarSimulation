package etf.gui.controller.mainController;

import etf.customLogger.CustomLogger;
import etf.fileManagers.CrashManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

public class CrashAlert extends Thread {
    private String currentLast;
    private CustomLogger cl = new CustomLogger(this);

    @Override
    public void run() {
        while(true){
            File dir = new File("src\\etf\\files\\alert\\");
            File[] allFiles = null;
            allFiles = dir.listFiles();
            if(allFiles.length <= 0 && currentLast == null){
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
                    try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(currentLast))){
                        CrashManager cm = (CrashManager) ois.readObject();
                        Platform.runLater(()->{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("SUDAR");
                            alert.setHeaderText(cm.getTime());
                            alert.setContentText(cm.getDetails() + "\n" + cm.getPosition());
                            alert.showAndWait();
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
