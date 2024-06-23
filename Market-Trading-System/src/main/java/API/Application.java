package API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication

@ComponentScan(basePackages = {"API", "ServiceLayer", "DomainLayer", "DAL"})
@EnableJpaRepositories(basePackages = {"API", "ServiceLayer", "DomainLayer", "DAL"})
@EntityScan(basePackages = {"API", "ServiceLayer", "DomainLayer", "DAL"})


@DependsOn("startupListener")
public class Application {

    public static boolean systemInitialize;

    public static void main(String[] args) {
        systemInitialize = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("To initialize the server an admin verification is needed, please log in:\n");

        Properties props = loadProperties();
        String adminUsername = props.getProperty("systemManager.username");
        String adminPassword = props.getProperty("systemManager.password");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter a Admin user name: ");
            String username = scanner.nextLine();
            System.out.print("Enter a Admin password: ");
            String password = scanner.nextLine();
            if (username.equals(adminUsername) && password.equals(adminPassword)) {
                System.out.println("\nLogin successful. Welcome, " + username + "!\n");
                loggedIn = true;
            }
            else {
                System.out.println("\nInvalid credentials. Please try again.\n");
            }
        }
        SpringApplication.run(Application.class, args);
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("Market-Trading-System/src/main/resources/application.properties")) {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}