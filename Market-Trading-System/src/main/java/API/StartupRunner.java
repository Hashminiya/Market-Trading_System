package API;

import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import ServiceLayer.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Scanner;

@Component("StartupRunner")
public class StartupRunner implements CommandLineRunner {

    private final IUserService userService;

    @Autowired
    public StartupRunner(@Qualifier("userService") IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args){

        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));

        ResponseEntity<?> response = userService.register("admin", "admin",25);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ResponseStatusException(response.getStatusCode(),response.getBody().toString());
        }
        // Continue with the rest of your application logic here
        runSystem();
    }

    private void runSystem() {
        // Your system's main logic goes here
        System.out.println("The server is running...");
    }
}
