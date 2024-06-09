package API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;

@SpringBootApplication
@ComponentScan(basePackages = {"API", "ServiceLayer", "DomainLayer"})
@DependsOn("startupListener")
public class Application {

    public static boolean systemInitialize;

    public static void main(String[] args) {
        systemInitialize = false;
        SpringApplication.run(Application.class, args);
    }

}