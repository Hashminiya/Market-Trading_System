package API;

import ServiceLayer.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IUserService userService;

    @Autowired
    public StartupListener(@Qualifier("userService") IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        System.out.println("---------------------------------------");
        System.out.println("event received");
        System.out.println("---------------------------------------");

        userService.register("admin", "admin",25);

        Scanner scanner = new Scanner(System.in);

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("Enter a Admin user name: ");
            String username = scanner.nextLine();
            System.out.print("Enter a Admin password: ");
            String password = scanner.nextLine();
            ResponseEntity<String> response = userService.login(username, password);
            System.out.println("---------------------------------------");
            System.out.println(response.getStatusCode().value());
            System.out.println("---------------------------------------");
            if (response.getStatusCode().value() == 200) {
                System.out.println("Login successful. Welcome, " + username + "!");
                loggedIn = true;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
        // Perform your startup logic here
        // This method will be called when the Spring application context is initialized or refreshed
        // It ensures that the server will only start after this logic is complete
    }
}

