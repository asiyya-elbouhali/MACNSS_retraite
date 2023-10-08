package macnss.service;
import macnss.dao.UserDAOImpl;
import macnss.model.User;
import macnss.dao.RefundFileDAOImpl;
import  macnss.service.CompanyService;
import java.sql.Connection;
import java.util.Scanner;

public class AgentService {
    private final Connection connection;
    private final FileService FileService;
     private final RefundFileDAOImpl refundFileDAOImpl;


    public AgentService(Connection connection) {
        this.connection = connection;
        this.FileService = new FileService(connection);
        this.refundFileDAOImpl= new RefundFileDAOImpl(connection);
    }

    public void showMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCompany Menu:");
            System.out.println("1. Add Employee");
            System.out.println("2. Edit employee information");
            System.out.println("3. Show all our employees");
             System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1 -> FileService.addFile(scanner);
                    case 2 -> FileService.updateFileStatus();
                    case 3 -> new AuthenticationService(new UserDAOImpl(this.connection)).addPatient(scanner);
                    case 4 -> {
                        CompanyService companyService = new CompanyService();
                        companyService.createCompany();
                    }
                    case 5 -> {
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



}

