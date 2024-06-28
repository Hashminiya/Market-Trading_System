package API;

import ServiceLayer.Store.IStoreManagementService;
import ServiceLayer.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandParserService {

    @Value("classpath:init-commands.txt")
    private Resource commandsFile;

    private IUserService userService;
    private IStoreManagementService storeManagementService;
    private Map<String,String> tokens;
    private Map<String,Long> storeIds;

    @Autowired
    public CommandParserService(IUserService userService, IStoreManagementService storeManagementService) {
        this.userService = userService;
        this.storeManagementService = storeManagementService;
        tokens = new HashMap<>();
        storeIds = new HashMap<>();
    }

    public void parseAndExecuteCommands() {
        System.out.println("Starting to parse and execute initialization commands...");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(commandsFile.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeCommand(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished parsing and executing initialization commands.");
    }

    private void executeCommand(String command) {
        String[] parts = command.split("\\(", 2);
        String commandName = parts[0];
        String argsString = parts[1].substring(0, parts[1].length() - 1);
        List<String> args = parseArguments(argsString);

        switch (commandName) {
            case "register":
                userService.register(args.get(0), args.get(1), Integer.parseInt(args.get(2)));
                break;
            case "login":
                tokens.put(args.get(0), userService.login(args.get(0), args.get(1)).getBody());
                break;
            case "createStore":
                storeIds.put(args.get(1) ,(long)storeManagementService.createStore(tokens.get(args.get(0)), args.get(1), args.get(2)).getBody());
                break;
            case "addItemToStore":
                List<String> categories = parseList(args.get(6));
                storeManagementService.addItemToStore(tokens.get(args.get(0)),storeIds.get(args.get(1)), args.get(2), args.get(3),
                        Double.parseDouble(args.get(4)), Integer.parseInt(args.get(5)), categories);
                break;
            case "assignStoreManager":
                List<String> permissions = parseList(args.get(3));
                storeManagementService.assignStoreManager(tokens.get(args.get(0)), storeIds.get(args.get(1)), args.get(2), permissions);
                break;
            default:
                System.out.println("Unknown command: " + commandName);
        }
    }

    private List<String> parseArguments(String argsString) {
        List<String> args = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        boolean insideQuotes = false;
        boolean insideArray = false;

        for (char c : argsString.toCharArray()) {
            if (c == ',' && !insideQuotes && !insideArray) {
                args.add(currentArg.toString().trim());
                currentArg = new StringBuilder();
            } else if (c == '"') {
                insideQuotes = !insideQuotes;
                currentArg.append(c);
            } else if (c == '[') {
                insideArray = true;
                currentArg.append(c);
            } else if (c == ']') {
                insideArray = false;
                currentArg.append(c);
            } else {
                currentArg.append(c);
            }
        }
        args.add(currentArg.toString().trim());

        return args;
    }

    private List<String> parseList(String listString) {
        // Remove brackets and split by comma
        String[] list = listString.substring(1, listString.length() - 1).split(",");
        return Arrays.stream(list)
                .map(String::trim)
                .toList();
    }
}