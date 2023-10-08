package macnss.model;

import java.util.List;

public class Company {
    private int id;
    private String name;
    private  String email;
    private  String password;
    private List<Patient> patients;

    public Company(int id, String name, String email, String password) {
        this.name=name;
        this.email=email;
        this.password=password;
    }

    public Company() {

    }


    public String getName(){
        return this.name;
    }

    public  String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }
    public List<Patient> getPatients() {
        return patients;
    }

    public void setName(String name){
        this.name =name;
    }

    public void setPassword(String password){
        this.password=password;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPatients(List<Patient> patients){
        this.patients=patients;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
