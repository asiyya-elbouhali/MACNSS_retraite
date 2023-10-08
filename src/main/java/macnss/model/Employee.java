package macnss.model;

public class Employee extends User {
    private double salary;
    private String jobTitle;
    private String birth_date;

    public Employee(int id, String name, String email,String password, double salary, String jobTitle, String birth_date) {
        super(id, name, email,password);
        this.salary = salary;
        this.jobTitle = jobTitle;
        this.birth_date = birth_date;
    }
    public Employee(  String name, String email,String password, double salary, String jobTitle, String birth_date) {
        super( name, email,password);
        this.salary = salary;
        this.jobTitle = jobTitle;
        this.birth_date = birth_date;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBirthDate() {
        return birth_date;
    }

    public void setBirthDate(String birthDate) {
        this.birth_date = birth_date;
    }

 }

