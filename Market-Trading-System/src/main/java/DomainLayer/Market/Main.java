package DomainLayer.Market;

import DomainLayer.Market.User.SystemManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Automatically create a SystemManager instance with relevant data from the User constructor
        SystemManager systemManager = SystemManager.getInstance();

        // Command-line interface
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Market System Manager CLI!");
        System.out.println("Do you want to initialize the system? (yes/no)");
        String userInput = scanner.nextLine().trim().toLowerCase();

        if (userInput.equals("yes")) {
            // Initialize the system
            System.out.println("Initializing the system...");
//            systemManager.InitializeTradingSystem();
        } else {
            System.out.println("System initialization skipped.");
        }

        // Close the scanner
        scanner.close();
    }
}
