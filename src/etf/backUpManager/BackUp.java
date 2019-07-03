package etf.backUpManager;

import etf.customLogger.CustomLogger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BackUp extends Thread {
    String backUpFolder = "src\\etf\\";
    CustomLogger cl = new CustomLogger(this);
    List<String> allFiles = new ArrayList<>();

    @Override
    public void run() {
        while(true){
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{
                String zipFileName = backUpFolder + "backup_" + LocalDateTime.now().getYear() + "_" + LocalDateTime.now().getMonth()+"+"
                                        + LocalDateTime.now().getDayOfMonth() + "_" + LocalDateTime.now().getHour() + "_"
                                        + LocalDateTime.now().getMinute() + ".zip";
                File dir = new File("src\\etf\\files");
                String dirPath = dir.getAbsolutePath();

                listAllFiles(dir);

                File zipFile = new File(zipFileName);

                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
                byte[] buffer = new byte[1024];
                int len;
                for(String filePath : allFiles){
                    ZipEntry ze = new ZipEntry(filePath);

                    String filePathRelative = "src\\etf\\files\\" + filePath.substring(dirPath.length() + 1, filePath.length());

                    zos.putNextEntry(ze);
                    FileInputStream fis = new FileInputStream(filePathRelative);
                    while((len = fis.read(buffer))> 0){
                        zos.write(buffer, 0 , len);
                    }
                    zos.closeEntry();
                    fis.close();
                }
                zos.close();

                allFiles = null;
                allFiles = new ArrayList<>();
            } catch (FileNotFoundException e) {
                cl.logException(e.getMessage(), e);
            } catch (IOException e) {
                cl.logException(e.getMessage(), e);
            }
        }
    }

    private void listAllFiles(File dir){
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isDirectory())
                listAllFiles(file);
            else{
                allFiles.add(file.getAbsolutePath());
            }
        }
    }
}
