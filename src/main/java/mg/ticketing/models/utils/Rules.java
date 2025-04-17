package mg.ticketing.models.utils;

public class Rules {
    int hoursAnnulation;
    int hoursBooking;

    public Rules(){}
    public Rules(int hoursAnnulation, int hoursBooking) {
        this.hoursAnnulation = hoursAnnulation;
        this.hoursBooking = hoursBooking;
    }

    public int getHoursAnnulation() {
        return hoursAnnulation;
    }
    public void setHoursAnnulation(int hoursAnnulation) {
        this.hoursAnnulation = hoursAnnulation;
    }
    public int getHoursBooking() {
        return hoursBooking;
    }
    public void setHoursBooking(int hoursBooking) {
        this.hoursBooking = hoursBooking;
    }

    public Rules getRules(int hoursAnnulation, int hoursBooking) {
        return new Rules(hoursAnnulation, hoursBooking);
    }

    public Rules getRules() {
        return new Rules(4, 5);
    }
}
