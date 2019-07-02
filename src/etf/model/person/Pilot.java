package etf.model.person;

public class Pilot extends Person {
    private String flightLicence;

    public Pilot(String firstName, String lastName, String flightLicence) {
        super(firstName, lastName);
        this.flightLicence = flightLicence;
    }

    public String getFlightLicence() {
        return flightLicence;
    }

    public void setFlightLicence(String flightLicence) {
        this.flightLicence = flightLicence;
    }
}
