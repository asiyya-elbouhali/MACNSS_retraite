package macnss.service;
import macnss.dao.EmployeeDAOImpl;
import macnss.dao.UserDAOImpl;
import macnss.model.Patient;
import macnss.model.User;

import java.sql.Connection;
import java.util.Scanner;

public class PatientService {
    private final Connection connection;
    private final UserDAOImpl UserDAOImpl;


    public PatientService(Connection connection) {
        this.connection = connection;
        this.UserDAOImpl = new UserDAOImpl(connection);
    }

    public void showMenu(User authenticatedUser,FileService FileService) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nClient Menu:");
            System.out.println("1. check your files state");
            System.out.println("2. My Retirement:");
            System.out.println("3. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> FileService.checkClientFiles(authenticatedUser);
                    case 2 ->  {
                        if (authenticatedUser instanceof Patient) {
                             Patient patient = (Patient) authenticatedUser;
                            int patientId = patient.getId();

                             EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
                            double retirementPension = employeeDAO.calculateRetirementPension(patientId);
                            System.out.println("Estimated Retirement Pension:");
                            if (retirementPension > 0) {
                                System.out.println("Congratulations! You are eligible for a retirement pension.");
                                System.out.printf("Your estimated monthly pension is: $%.2f%n", retirementPension);
                            } else {
                                System.out.println("Sorry, you are not eligible for a retirement pension at this time.");
                            }
                        } else {
                            System.out.println("Only employees can calculate retirement pension.");
                        }
                    }
                    case 3 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
    public Patient createPatient() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter patient name: ");
        String name = scanner.nextLine();

        System.out.println("Enter patient Birth date: ");
        String birth_date = scanner.nextLine();

        System.out.println("Enter patient email: ");
        String email = scanner.nextLine();

        System.out.println("Enter patient password: ");
        String password = scanner.nextLine();

        System.out.println("Enter patient matricule: ");
        int matricule = scanner.nextInt();

        Patient patient = new Patient(0, name, email, password, matricule,birth_date);
        return UserDAOImpl.addPatient(patient);
    }

}

