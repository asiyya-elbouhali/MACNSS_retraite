package macnss.model;

import java.time.Period;

public class WoredDays {
    private int id;
    private double nDays;
    private Period period;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getnDays() {
        return nDays;
    }

    public void setnDays(double nDays) {
        this.nDays = nDays;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
