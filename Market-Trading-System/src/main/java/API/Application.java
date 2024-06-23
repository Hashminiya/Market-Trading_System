package API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"API", "ServiceLayer", "DomainLayer"})
@EnableJpaRepositories(basePackages = {"API", "ServiceLayer", "DomainLayer"})
@EntityScan(basePackages = {"API", "ServiceLayer", "DomainLayer"})
@DependsOn("startupListener")
public class Application {

    public static boolean systemInitialize;

    public static void main(String[] args) {
        systemInitialize = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("To initialize the server an admin verification is needed, please log in:\n");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter a Admin user name: ");
            String username = scanner.nextLine();
            System.out.print("Enter a Admin password: ");
            String password = scanner.nextLine();
            if (username.equals("admin") && password.equals("admin")) {
                System.out.println("\nLogin successful. Welcome, " + username + "!\n");
                loggedIn = true;
            }
            else {
                System.out.println("\nInvalid credentials. Please try again.\n");
            }
        }
        SpringApplication.run(Application.class, args);
    }
}