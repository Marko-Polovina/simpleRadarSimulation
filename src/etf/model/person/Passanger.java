package etf.model.person;

public class Passanger extends Person {
    private String passport;

    public Passanger(String firstName, String lastName, String passport) {
        super(firstName, lastName);
        this.passport = passport;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
