package macnss.service;
import macnss.dao.UserDAOImpl;
import macnss.model.User;

import java.sql.Connection;
import java.util.Scanner;

public class ClientService {
    private final Connection connection;

    private final FileService FileService;


    public ClientService(Connection connection) {
        this.connection = connection;
        this.FileService = new FileService(connection);
    }

    public void showMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nClient Menu:");
            System.out.println("1. check your files state");
            System.out.println("2. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> FileService.checkClientFiles(authenticatedUser);
                    case 2 -> {
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

