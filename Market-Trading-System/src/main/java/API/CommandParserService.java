//package API;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
//@Service
//@Configuration
//public class CommandParserService {
//
//    @Value("classpath:init-commands.txt")
//    private Resource commandsFile;
//
//    private final UserService userService;
//    private final ShopService shopService;
//
//    public CommandParserService(UserService userService, ShopService shopService) {
//        this.userService = userService;
//        this.shopService = shopService;
//    }
//
//    @EventListener(ContextRefreshedEvent.class)
//    public void parseAndExecuteCommands() {
//        System.out.println("Starting to parse and execute initialization commands...");
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(commandsFile.getInputStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                executeCommand(line.trim());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("Finished parsing and executing initialization commands.");
//    }
//
//    private void executeCommand(String command) {
//        // ... (rest of the method remains the same)
//    }
//}
