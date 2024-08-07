package API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"API", "API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@EnableJpaRepositories(basePackages = {"API", "API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@EntityScan(basePackages = {"API", "API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@DependsOn("startupListener")
@EnableCaching
public class Application {

    public static boolean systemInitialize;

    public static void main(String[] args) {
        systemInitialize = false;

        if (!checkApplicationPropertiesFile()) {
            System.err.println("application.properties file does not exist.\nThe server can't initialize, Exiting...");
            System.exit(1);
        }

        System.out.println("To initialize the server an admin verification is needed, please log in:\n");
        Scanner scanner = new Scanner(System.in);

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
            } else {
                System.out.println("\nInvalid credentials. Please try again.\n");
            }
        }
        try {
            SpringApplication application = new SpringApplication(Application.class);
            application.run(args);
        } catch (Throwable t) {
            System.err.println("\nApplication failed to start: " + t.getClass().getName() + " :\n" + t.getMessage());
            System.exit(1);
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = new ClassPathResource("application.properties").getInputStream()) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("application.properties file failed to opened");
            System.exit(1);
            //e.printStackTrace();
        }
        return props;
    }

    private static boolean checkApplicationPropertiesFile() {
        try {
            new ClassPathResource("application.properties").getInputStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
