package etf.model.aircraft;

import etf.model.person.Person;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Aircraft extends Thread implements Serializable {
    private HashMap<String,String> characteristics;
    private String model;
    private String Id;
    private int height;
    private long velocity;
    private List<Person> personList;
    private Consumer<Aircraft> movement = null;
    private int currentX;
    private int currentY;

    @Override
    public void run() {
        /*
        primjer setovanja movement metode
        this.setMovement(x->{
            for(int i = currentX; i < 100; i++){
                currentX++;
                try {
                    sleep(velocity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        */
        if(movement!=null)
            movement.accept(this);
    }

    static long randomSpeed() {
        Random r = new Random();
        return (long)((1 + (2) * r.nextDouble())*1000);
    }

    public void setMovement(Consumer<Aircraft> movementDirection){
        this.movement = movementDirection;
    }

    public Aircraft(HashMap<String, String> characteristics, String model, String id, int height, long velocity, List<Person> personList, Consumer<Aircraft> movement, int currentX, int currentY) {
        this.characteristics = characteristics; //mozda ucitati iz fajla, po tipu letjelice
        this.model = model;
        Id = id;
        this.height = height;
        this.velocity = velocity;
        this.personList = personList; //mozda ucitati iz fajla
        this.movement = movement;
        this.currentX = currentX;
        this.currentY = currentY;
    }


}
