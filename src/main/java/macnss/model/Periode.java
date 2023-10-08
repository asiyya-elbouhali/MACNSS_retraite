package macnss.model;

public class Periode {
    private  int id;
    private int month;

    public Periode(int id, int month) {
        this.id=id;
        this.month=month;
    }

    public Periode() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
