package macnss.service;

import macnss.dao.EmployeeDAO;
import macnss.dao.EmployeeDAOImpl;
import macnss.db.DatabaseConnection;
import macnss.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmployeeService {
    private EmployeeDAO employeeDAO;
    private Scanner scanner;

    public EmployeeService() {
        this.employeeDAO = employeeDAO;
        this.scanner = new Scanner(System.in);
    }

    public boolean addEmployee() {

        System.out.println("Enter Employee Name: ");
        String name = scanner.nextLine();

        System.out.println("Enter Email: ");
        String email = scanner.nextLine();

        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        System.out.println("Enter Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter Job Title: ");
        String jobTitle = scanner.nextLine();

        System.out.println("Enter Birth Date: ");
        String birthDate = scanner.nextLine();

         Employee employee = new Employee(  name, email, password, salary, jobTitle, birthDate);

        EmployeeDAOImpl employeeDAOImpl = new EmployeeDAOImpl();
         return employeeDAOImpl.addEmployee(employee);
    }

    public void showAllEmployees() {
        Connection connection= null;
        String selectQuery = "SELECT * FROM employees";
        try  {
            connection= DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                 String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                double salary = resultSet.getDouble("salary");
                String jobTitle = resultSet.getString("job_title");
                String birthDate = resultSet.getString("birth_date");

                 System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("Salary: " + salary);
                System.out.println("Job Title: " + jobTitle);
                System.out.println("Birth Date: " + birthDate);
                System.out.println("------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
