package API;

import ServiceLayer.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public StartupRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args){


        // Continue with the rest of your application logic here
        runSystem();
    }

    private void runSystem() {
        // Your system's main logic goes here
        System.out.println("System is running...");
    }
}
