package etf.model.rockets;

import etf.model.airspace.Airspace;

import java.io.Serializable;

import static etf.fileManagers.ConfigFileManager.checkFlightBan;

public class Rockets extends Thread implements Serializable {
    private boolean crashed;
    private int currentX;
    private int currentY;

    public Rockets(int x, int y){
        currentX = x;
        currentY = y;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    @Override
    public void run() {
        Airspace airspace = Airspace.getAirspace();
        writeInMap(); // nakon sto se napravi upisace se u map.txt file
        int moveDirection = setMoveDirection();
        boolean flightBan = false;
        if(moveDirection == 1){
            while(moveUp(airspace) && !crashed){ //pri svakom kretanju automatski se updejtuje map.txt, na kraju kretanja se brise iz map.tx
                checkCrash();
                flightBan = checkFlightBan();
                if(flightBan)break;
            }
        }else if(moveDirection == 2){
            while(moveDown(airspace) && !crashed){
                checkCrash();
                flightBan = checkFlightBan();
                if(flightBan)break;
            }
        }else if(moveDirection == 3){
            while(moveLeft(airspace) && !crashed){
                checkCrash();
                flightBan = checkFlightBan();
                if(flightBan)break;
            }
        }else if(moveDirection == 4) {
            while (moveRight(airspace) && !crashed) {
                checkCrash();
                flightBan = checkFlightBan();
                if (flightBan) break;
            }
        }
        if(crashed) {
            airspace.removeRocket(currentX, currentY, this);
            deleteFromMap();
        }
    }

    private int setMoveDirection() {
        System.out.println("Postavio pravac kretanja");
        return 1;
    }

    private boolean moveDown(Airspace airspace) {
        System.out.println("Pomjerio dole");
        return false;
    }

    private boolean moveUp(Airspace airspace) {
        System.out.println("Pomjerio gore");
        return false;
    }

    private boolean moveLeft(Airspace airspace) {
        System.out.println("Pomjerio lijevo");
        return false;
    }

    private boolean moveRight(Airspace airspace) {
        System.out.println("Pomjerio desno");
        return false;
    }

    private void deleteFromMap() {
        System.out.println("Uklonio iz map.txt");
    }

    private void writeInMap() {
        System.out.println("uspisao u map.txt");
    }

    private void checkCrash(){
        System.out.println("Nije bilo sudara.");
    }



}
