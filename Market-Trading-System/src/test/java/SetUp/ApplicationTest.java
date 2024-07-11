package SetUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@EnableJpaRepositories(basePackages = {"API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@EntityScan(basePackages = {"API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
//@DependsOn("startupListener")
public class ApplicationTest {

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
            }
            else {
                System.out.println("\nInvalid credentials. Please try again.\n");
            }
        }
        try {
            SpringApplication application = new SpringApplication(ApplicationTest.class);
            //application.addListeners(new ComprehensiveErrorHandler());
            //application.setLogStartupInfo(true);
            //System.setProperty("spring.boot.startup.log-errors", "false");
            application.run(args);
        } catch (Throwable t) {
            System.err.println("\nApplication failed to start: " + t.getClass().getName() + " :\n" + t.getMessage());
            // You can log the full stack trace here if needed for debugging
            // t.printStackTrace();
            System.exit(1);
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("Market-Trading-System/src/test/resources/application-test.properties")) {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    private static boolean checkApplicationPropertiesFile() {
        String filePath = "Market-Trading-System/src/test/resources/application-test.properties";
        System.out.println(filePath);
        File file = new File(filePath);
        return file.exists();
    }
}