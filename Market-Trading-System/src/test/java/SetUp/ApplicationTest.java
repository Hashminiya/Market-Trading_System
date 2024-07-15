package SetUp;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"API.controller","API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@EnableJpaRepositories(basePackages = {"API.controller", "API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@EntityScan(basePackages = {"API.controller", "API.Utils", "ServiceLayer", "DomainLayer", "DAL"})
@DependsOn("startupListener")
public class ApplicationTest {
    public static void main(String[] args) throws Exception {

        try {
            SpringApplication.run(ApplicationTest.class, args);
        } catch (Throwable t) {
            System.err.println("\nApplication failed to start: " + t.getClass().getName() + " :\n" + t.getMessage());
            // You can log the full stack trace here if needed for debugging
            // t.printStackTrace();
            System.exit(1);
        }
    }
}