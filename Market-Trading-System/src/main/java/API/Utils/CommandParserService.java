package API.Utils;

import API.InitCommand;
import ServiceLayer.Market.ISystemManagerService;
import ServiceLayer.Store.IStoreManagementService;
import ServiceLayer.User.IUserService;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Service
//TODO : Need to put a name?
public class CommandParserService {
    @Value("classpath:init-commands.txt")
    private Resource commandsFile;

    @Value("${logging.include-exception:false}")
    private boolean includeException;

    private final Map<String, Method> commandMethods = new HashMap<>();
    private final Map<String, Object> services = new HashMap<>();
    private final Map<String, String> tokens = new HashMap<>();
    private final Map<String, Long> storeIds = new HashMap<>();

    @Autowired
    public CommandParserService(IUserService userService,
                                IStoreManagementService storeManagementService,
                                ISystemManagerService systemManagerService) {
        services.put("userService", userService);
        services.put("storeManagementService", storeManagementService);
        services.put("systemManagerService", systemManagerService);
        initCommandMethods();
    }

    private void initCommandMethods() {
        for (Object service : services.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(service);
            for (Method method : targetClass.getMethods()) {
                InitCommand annotation = method.getAnnotation(InitCommand.class);
                if (annotation != null) {
                    String commandName = annotation.name();
                    commandMethods.put(commandName, method);
                    //System.out.println("Added command: " + commandName); // Debug print
                }
            }
        }
    }


    public void parseAndExecuteCommands() {
        System.out.println("Starting to parse and execute initialization commands...");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(commandsFile.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeCommand(line.trim());
            }
        } catch (Exception e) {
            System.err.println("errors raised while running initialization commands");
            System.err.println(e.getMessage());
            if (includeException) {
                e.printStackTrace();
            }
        }
        System.out.println("Finished parsing and executing initialization commands.");
    }

    private void executeCommand(String command) throws Exception {
        String[] parts = command.split("\\(", 2);
        String commandName = parts[0];
        String argsString = parts[1].substring(0, parts[1].length() - 1);
        List<String> args = parseArguments(argsString);

        Method method = commandMethods.get(commandName);
        if (method == null) {
            System.out.println("Unknown command: " + commandName);
            return;
        }

        Object service = services.values().stream()
                .filter(s -> AopUtils.getTargetClass(s).isAssignableFrom(method.getDeclaringClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No service found for command: " + commandName));

        Object[] methodArgs = prepareMethodArgs(commandName, method, args);
        Object result = method.invoke(service, methodArgs);

        handleCommandResult(commandName, args, result);
    }

    private Object[] prepareMethodArgs(String commandName, Method method, List<String> args) {
        Parameter[] parameters = method.getParameters();
        Object[] methodArgs = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            String arg = args.get(i);
            if (type == String.class) {
                if (i == 0 && needsTokenReplacement(commandName)) {
                    methodArgs[i] = tokens.get(arg); // Replace username with token
                } else {
                    methodArgs[i] = arg;
                }
            } else if (type == int.class || type == Integer.class) {
                methodArgs[i] = Integer.parseInt(arg);
            } else if (type == double.class || type == Double.class) {
                methodArgs[i] = Double.parseDouble(arg);
            } else if (type == long.class || type == Long.class) {
                methodArgs[i] = storeIds.containsKey(arg) ? storeIds.get(arg) : Long.parseLong(arg);
            } else if (type == List.class) {
                methodArgs[i] = parseList(arg);
            } else {
                throw new IllegalArgumentException("Unsupported parameter type: " + type.getSimpleName());
            }
        }
        return methodArgs;
    }

    private boolean needsTokenReplacement(String commandName) {
        // List of commands that need token replacement
        return !Arrays.asList("register","login", "guestEntry").contains(commandName);
    }

    private void handleCommandResult(String commandName, List<String> args, Object result) {
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) result;
            if (response.getStatusCode().is2xxSuccessful()) {
                switch (commandName) {
                    case "login":
                        tokens.put(args.get(0), (String) response.getBody());
                        break;
                    case "createStore":
                        storeIds.put(args.get(1), (Long) response.getBody());
                        break;
                    case "logout":
                        tokens.remove(args.get(0));
                        break;
                }
            } else {
                System.out.println("Command failed: " + commandName + " - " + response.getBody());
            }
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