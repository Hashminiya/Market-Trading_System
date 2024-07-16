package SetUp;

import API.Utils.*;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import ServiceLayer.User.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

//@Component("StartupRunner")
public class StartupRunner implements CommandLineRunner {

    private final IUserService userService;

    @Value("${data.initialization:false}")
    private boolean initData;

    @Value("${data.admin.register:false}")
    private boolean registerAdmin;

    public StartupRunner() {
        userService = (IUserService) SpringContext.getBean("userService");
    }

    @Override
    public void run(String... args) {

        System.out.println("-----------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------");

        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));

        if (registerAdmin) {
            ResponseEntity<?> response = userService.register("admin", "admin", 25);
            if (!response.getStatusCode().is2xxSuccessful()) {
                //throw new ResponseStatusException(response.getStatusCode(),response.getBody().toString());
                System.err.println("Failed to register admin:");
                System.err.println(response.getBody());
                System.err.println("Exiting...");
                System.exit(1);
            }
        }


        System.out.println("Payment and Supply external services connected successfully");
        System.out.println("The server is running...");

    }
}