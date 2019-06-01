package etf;

import etf.model.airspace.Airspace;

public class Simulation extends Thread{
    Airspace airspace;

    public Simulation(Airspace airspace){
        /*TODO otvori i procitaj sve config fajlove, bice ih nekoliko msm
            sto za info o tipu letjelice sto za kontrolu toka izvrsavanja
        */
        this.airspace = airspace;
    }

    @Override
    public void run() {
        /*TODO odradi petlju za spawnanje stvari, ne zaboravi ih i pokrenuti,
            treba li svaka letjelica znati u kom je zracnom prostoru?*/
    }
}
