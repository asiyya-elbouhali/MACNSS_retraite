package macnss.service;

import macnss.dao.*;
import macnss.model.*;

import java.sql.Connection;
import java.util.Scanner;

public class CompanyService {
    private   EmployeeService employeeService = new EmployeeService();


    public Company createCompany(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter company name: ");
        String name = scanner.nextLine();

        System.out.println("Enter company email: ");
        String email = scanner.nextLine();

        System.out.println("Enter company password: ");
        String password = scanner.nextLine();
        CompanyDAOImpl companyDAOImpl = new CompanyDAOImpl();
        Company company = new Company(0, name, email, password);
        return companyDAOImpl.addCompany(company);
    }

    public Company companyAuth(Scanner scanner){
        System.out.println("=== Company Sign-In ===");
        System.out.print("Enter your email: ");
        String email ;
        email = scanner.nextLine();
         System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        while (util.tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = scanner.nextLine();
        }
        CompanyDAOImpl companyDAOImpl= new CompanyDAOImpl();
        Company authentificatedCompany = companyDAOImpl.authenticateCompany(email,password);
        if (authentificatedCompany != null) {
           this.showMenu(authentificatedCompany);
             return authentificatedCompany;
        } else {
            System.out.println("Sign-In failed. Invalid email or password.");
            return null;
        }

    }

    public void showMenu(Company authenticatedCompany) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n Company Menu:");
            System.out.println("1. Add a new Employee:");
            System.out.println("2. Show all employees");
            System.out.println("3. Search an Employee:");
            System.out.println("4. Declare an employee:");
            System.out.println("5. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addEmployeeByMatricule(authenticatedCompany);
                    case 2 -> employeeService.showAllEmployees();
                    case 3 ->  {}
                    case 4 -> {
                        int companyId = authenticatedCompany.getId();
                        inputWorkedDays(companyId);
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }


    public void addEmployeeByMatricule(Company authenticatedCompany) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Add Employee by Matricule ===");

        // Prompt the company to enter the employee's matricule
        System.out.print("Enter employee's matricule: ");
        int matricule = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Check if the provided matricule already exists
        Connection connection = null;
        UserDAOImpl userDAO = new UserDAOImpl(connection);
        Patient existingPatient = userDAO.getPatientByMatricule(matricule);

        if (existingPatient != null) {
            // Matricule exists, use the existing patient's information
            System.out.println("Employee with the provided matricule already exists. Using existing patient information.");

            // Create a Patient (Employee) object with existing patient's information
            Patient employee = existingPatient;

            // Prompt the company to enter the employee's salary
            System.out.print("Enter employee's salary: ");
            String salaryInput = scanner.nextLine();
            double salary = 0;
            try {
                salary = Double.parseDouble(salaryInput);
                boolean validSalary = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid salary format. Please enter a valid numeric value.");
            }

            // Now, you can add the employee to the company_employee table with the provided salary
            CompanyDAOImpl companyDAO = new CompanyDAOImpl();
            EmployeeCompany addedEmployee = companyDAO.addEmployee(employee, authenticatedCompany.getId(), matricule, salary);

            if (addedEmployee != null) {
                System.out.println("Employee added successfully to the company_employee table with salary: " + salary);
            } else {
                System.out.println("Failed to add the employee to the company_employee table.");
            }
        } else {
            // Matricule does not exist, so add the employee as a new patient first
            System.out.println("Employee with the provided matricule does not exist. Adding the employee as a new patient.");

            // Use the PatientService to create a new patient
            PatientService patientService=new PatientService(connection );
            Patient newPatient = patientService.createPatient();

            if (newPatient != null) {
                System.out.println("Employee added as a patient. You can now add them to the company.");
                addEmployeeByMatricule(authenticatedCompany); // Recursively call this method to add the employee to the company
            } else {
                System.out.println("Failed to add the employee as a patient.");
            }
        }
    }

    public void inputWorkedDays(int companyId) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter employee's matricule: ");
        int matricule = scanner.nextInt();

        // Retrieve the company_employee ID based on the matricule
        PatientCompanyDAOImpl patientCompanyDAO= new PatientCompanyDAOImpl();
        int companyEmployeeId = patientCompanyDAO.getCompanyEmployeeIdByMatriculeAndCompanyId(matricule, companyId);

        if (companyEmployeeId != -1) {
            System.out.println("Enter days_nb: ");
            int daysNb = scanner.nextInt();

            System.out.println("Enter month: ");
            int month = Integer.parseInt(scanner.next());


            // Retrieve the periode ID based on the provided month
            PeriodDAOImpl periodDAO = new PeriodDAOImpl();
            int periodeId = periodDAO.getPeriodeIdByMonth(month).getId() ;
             System.out.println(periodeId);
            if (periodeId != -1) {
                // Insert the worked days record into the database
                WorkedDaysDAOImpl workedDaysDAO=new WorkedDaysDAOImpl();
                boolean success = workedDaysDAO.insertWorkedDays(companyEmployeeId, daysNb, periodeId);

                if (success) {
                    System.out.println("Worked days recorded successfully.");
                } else {
                    System.out.println("Failed to record worked days.");
                }
            } else {
                System.out.println("Month not found in the periode table.");
            }
        } else {
            System.out.println("Employee not found.");
        }
    }

}
