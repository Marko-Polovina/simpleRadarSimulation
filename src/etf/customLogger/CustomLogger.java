package etf.customLogger;

import java.io.IOException;
import java.util.logging.*;

public class CustomLogger {
    private Logger logger;
    private static Handler handler;


    static
    {
        try {
            handler = new FileHandler("log.log", true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public CustomLogger(Object klasa) {
        logger = Logger.getLogger(klasa.getClass().toString());
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);
    }

    synchronized public void logException(String dogadjaj, Throwable ex){
        logger.log(Level.SEVERE,dogadjaj, ex);
    }
}
