package macnss.model;

public class Patient extends User{
    private int matricule;
    private String birth_date;

    public Patient(  int id, String name, String email, String password, int matricule,String birth_date) {
        super(  id,   name, email, password);
        this.matricule = matricule;
        this.birth_date=birth_date;
    }


    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }
    public int getMatricule() {
        return this.matricule;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}
