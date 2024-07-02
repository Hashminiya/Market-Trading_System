package API;

import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import ServiceLayer.Store.IStoreBuyerService;
import ServiceLayer.Store.IStoreManagementService;
import ServiceLayer.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component("StartupRunner")
public class StartupRunner implements CommandLineRunner {

    private final IUserService userService;
    private final IStoreManagementService storeManagementService;


    public StartupRunner() {
        userService = (IUserService) SpringContext.getBean("userService");
        storeManagementService = (IStoreManagementService) SpringContext.getBean("StoreManagementService");
    }

    @Override
    public void run(String... args){

        System.out.println("-----------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------");

        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));


        //TODO: delete the following 3 lines before submission: the admin is always registered


        ResponseEntity<?> response = userService.register("admin", "admin", 25);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ResponseStatusException(response.getStatusCode(),response.getBody().toString());
        }

        System.out.println("Payment and Supply external services connected successfully");
        System.out.println("The server is running...");

//        CommandParserService commandParserService = SpringContext.getBean(CommandParserService.class);
//        commandParserService.parseAndExecuteCommands();
        runSystem();
    }

    private void runSystem() {
        List<String> tokens = createUsers();
        List<Long> storeIds = createStores(tokens);
        List<Long> itemIds = createItems(tokens, storeIds);
        initShoppingCart(tokens, storeIds, itemIds);
        createPurchase(tokens);
        createDiscounts(tokens, storeIds, itemIds);
        logoutUsers(tokens);
    }

    private void logoutUsers(List<String> tokens) {
        for (String token: tokens) {
            userService.logout(token);
        }
    }

    private List<String> createUsers() {
        userService.register("user1", "user1", 25);
        userService.register("user2", "user2", 30);
        userService.register("user3", "user3", 22);
        userService.register("user4", "user4", 28);
        userService.register("user5", "user5", 35);
//        userService.register("user6", "user6", 26);
//        userService.register("user7", "user7", 29);
//        userService.register("user8", "user8", 31);
//        userService.register("user9", "user9", 24);
//        userService.register("user10", "user10", 27);
        ResponseEntity<String> response1 = userService.login("user1", "user1");
        ResponseEntity<String> response2 = userService.login("user2", "user2");
        ResponseEntity<String> response3 = userService.login("user3", "user3");
        ResponseEntity<String> response4 = userService.login("user4", "user4");
        ResponseEntity<String> response5 = userService.login("user5", "user5");
        List<String> tokens = new ArrayList<>();
        tokens.add(response1.getBody());
        tokens.add(response2.getBody());
        tokens.add(response3.getBody());
        tokens.add(response4.getBody());
        tokens.add(response5.getBody());
        return tokens;
    }

    private List<Long> createStores(List<String> tokens) {
        IStoreManagementService storeManagementService = (IStoreManagementService) SpringContext.getBean("StoreManagementService");
        ResponseEntity<?> storeId1 = storeManagementService.createStore(tokens.get(0), "Electronics Store","High quality electronics and gadgets");
        ResponseEntity<?> storeId2 = storeManagementService.createStore(tokens.get(1), "Camping Gear","All your camping essentials");
        ResponseEntity<?> storeId3 = storeManagementService.createStore(tokens.get(2), "Art Supplies","Wide range of art supplies");
        ResponseEntity<?> storeId4 = storeManagementService.createStore(tokens.get(3), "DIY Tools","Everything you need for your DIY projects");
        ResponseEntity<?> storeId5 = storeManagementService.createStore(tokens.get(4), "Garden Center","All you need for your garden");
//        ResponseEntity<?> storeId6 = storeManagementService.createStore(tokens.get(0), "Shoe Store", "Stylish and comfortable shoes");
//        ResponseEntity<?> storeId7 = storeManagementService.createStore(tokens.get(1), "Audio Store", "High fidelity audio equipment");
//        ResponseEntity<?> storeId8 = storeManagementService.createStore(tokens.get(2), "Home Goods", "Essentials for your home");
//        ResponseEntity<?> storeId9 = storeManagementService.createStore(tokens.get(3), "Sports Store", "All your sports needs");
//        ResponseEntity<?> storeId10 = storeManagementService.createStore(tokens.get(4), "Office Supplies", "Everything for your office");
        List<Long> storeIds = new ArrayList<>();
        storeIds.add((long) storeId1.getBody());
        storeIds.add((long) storeId2.getBody());
        storeIds.add((long) storeId3.getBody());
        storeIds.add((long) storeId4.getBody());
        storeIds.add((long) storeId5.getBody());
//        storeIds.add((long) storeId6.getBody());
//        storeIds.add((long) storeId7.getBody());
//        storeIds.add((long) storeId8.getBody());
//        storeIds.add((long) storeId9.getBody());
//        storeIds.add((long) storeId10.getBody());
//        storeManagementService.assignStoreOwner(tokens.get(0),(long) storeId1.getBody())
        return storeIds;
    }

    private void createDiscounts(List<String> tokens, List<Long> storeIds, List<Long> itemIds) {
        String discountDetails1 = "{\n" +
                "    \"@type\": \"RegularDiscount\",\n" +
                "    \"id\": 10,\n" +
                "    \"percent\": 5.0,\n" +
                "    \"expirationDate\": \"2024-12-31T23:59:59Z\",\n" +
                "    \"storeId\": "+storeIds.get(0)+",\n" +
                "    \"items\": ["+itemIds.get(0)+"],\n" +
                "    \"categories\": [\"Electronics\"],\n" +
                "    \"conditions\": {\n" +
                "        \"@type\": \"ConditionComposite\",\n" +
                "        \"id\": 978132,\n" +
                "        \"conditions\": [\n" +
                "            {\n" +
                "                \"@type\": \"Condition\",\n" +
                "                \"id\": 978132,\n" +
                "                \"itemId\": "+itemIds.get(0)+",\n" +
                "                \"count\": 1\n" +
                "            }\n" +
                "        ],\n" +
                "        \"rule\": \"OR\"\n" +
                "    }\n" +
                "}";

        String discountDetails2 = "{\n" +
                "    \"@type\": \"RegularDiscount\",\n" +
                "    \"id\": 10,\n" +
                "    \"percent\": 5.0,\n" +
                "    \"expirationDate\": \"2024-12-31T23:59:59Z\",\n" +
                "    \"storeId\": "+storeIds.get(0)+",\n" +
                "    \"items\": ["+itemIds.get(0)+"],\n" +
                "    \"categories\": [\"Electronics\"],\n" +
                "    \"conditions\": {\n" +
                "        \"@type\": \"Condition\",\n" +
                "        \"id\": 978132,\n" +
                "        \"conditions\": [\n" +
                "            {\n" +
                "                \"@type\": \"Condition\",\n" +
                "                \"id\": 978132,\n" +
                "                \"itemId\": "+itemIds.get(0)+",\n" +
                "                \"count\": 1\n" +
                "            }\n" +
                "        ],\n" +
                "        \"rule\": \"OR\"\n" +
                "    }\n" +
                "}";
        storeManagementService.addDiscount(tokens.get(0),storeIds.get(0),discountDetails1);
        storeManagementService.addDiscount(tokens.get(0),storeIds.get(0),discountDetails2);

    }

    private void initShoppingCart(List<String> tokens, List<Long> storeIds, List<Long> itemIds) {
        userService.addItemToBasket(tokens.get(0), storeIds.get(0), itemIds.get(0), 1);
//        userService.addItemToBasket(tokens.get(1), storeIds.get(1), itemIds.get(1), 1);
//        userService.addItemToBasket(tokens.get(2), storeIds.get(2), itemIds.get(2), 1);
//        userService.addItemToBasket(tokens.get(3), storeIds.get(3), itemIds.get(3), 1);
//        userService.addItemToBasket(tokens.get(4), storeIds.get(4), itemIds.get(4), 1);
    }

    private List<Long> createItems(List<String> tokens, List<Long> storeIds) {
        IStoreManagementService storeManagementService = (IStoreManagementService) SpringContext.getBean("StoreManagementService");

        List<Long> itemIds = new ArrayList<>();

        // Electronics Store
        itemIds.addAll(createElectronicsItems(tokens.get(0), storeIds.get(0), storeManagementService));

        // Camping Gear
        //itemIds.addAll(createCampingItems(tokens.get(1), storeIds.get(1), storeManagementService));

        // Art Supplies
        //itemIds.addAll(createArtItems(tokens.get(2), storeIds.get(2), storeManagementService));

        // DIY Tools
        //itemIds.addAll(createDiyItems(tokens.get(3), storeIds.get(3), storeManagementService));

        // Garden Center
        //itemIds.addAll(createGardenItems(tokens.get(4), storeIds.get(4), storeManagementService));

        // Shoe Store
        //itemIds.addAll(createShoeItems(tokens.get(0), storeIds.get(5), storeManagementService));

        // Audio Store
        //itemIds.addAll(createAudioItems(tokens.get(1), storeIds.get(6), storeManagementService));

        // Home Goods
        //itemIds.addAll(createHomeItems(tokens.get(2), storeIds.get(7), storeManagementService));

        // Sports Store
        //itemIds.addAll(createSportsItems(tokens.get(3), storeIds.get(8), storeManagementService));

        // Office Supplies
        //itemIds.addAll(createOfficeItems(tokens.get(4), storeIds.get(9), storeManagementService));

        return itemIds;
    }

    private List<Long> createElectronicsItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Smartphone", "Latest model smartphone", 700, 50, new ArrayList<>(Arrays.asList("Electronics", "Gadgets"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Laptop", "High performance laptop", 1200, 30, new ArrayList<>(Arrays.asList("Electronics", "Computers"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Tablet", "Lightweight and powerful tablet", 500, 20, new ArrayList<>(Arrays.asList("Electronics", "Gadgets"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Smartwatch", "Feature-rich smartwatch", 200, 100, new ArrayList<>(Arrays.asList("Electronics", "Wearables"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Headphones", "Noise-cancelling headphones", 150, 40, new ArrayList<>(Arrays.asList("Electronics", "Audio"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Camera", "High resolution digital camera", 800, 15, new ArrayList<>(Arrays.asList("Electronics", "Photography"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Speaker", "Portable Bluetooth speaker", 100, 60, new ArrayList<>(Arrays.asList("Electronics", "Audio"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Monitor", "4K Ultra HD monitor", 300, 25, new ArrayList<>(Arrays.asList("Electronics", "Computers"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Keyboard", "Mechanical keyboard", 80, 70, new ArrayList<>(Arrays.asList("Electronics", "Computers"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Mouse", "Wireless mouse", 50, 90, new ArrayList<>(Arrays.asList("Electronics", "Computers"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Router", "High-speed internet router", 100, 40, new ArrayList<>(Arrays.asList("Electronics", "Networking"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Smart TV", "4K Smart TV", 500, 20, new ArrayList<>(Arrays.asList("Electronics", "Entertainment"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "VR Headset", "Virtual reality headset", 300, 10, new ArrayList<>(Arrays.asList("Electronics", "Gaming"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "External Hard Drive", "1TB external hard drive", 80, 50, new ArrayList<>(Arrays.asList("Electronics", "Storage"))).getBody());
//        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Drone", "Quadcopter drone with camera", 400, 15, new ArrayList<>(Arrays.asList("Electronics", "Gadgets"))).getBody());
        return itemIds;
    }

    private List<Long> createCampingItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Tent", "4-person tent", 100, 20, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Sleeping Bag", "Warm and comfortable sleeping bag", 60, 40, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Backpack", "50L hiking backpack", 80, 25, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Camping Stove", "Portable camping stove", 40, 30, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Flashlight", "LED flashlight", 20, 50, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Camping Chair", "Folding camping chair", 30, 35, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Water Bottle", "Insulated water bottle", 15, 60, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Multi-tool", "Multi-purpose tool", 25, 45, new ArrayList<>(Arrays.asList("Camping", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Camping Table", "Folding camping table", 50, 10, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Lantern", "Battery-powered lantern", 30, 25, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Hiking Boots", "Waterproof hiking boots", 100, 20, new ArrayList<>(Arrays.asList("Camping", "Shoes"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Compass", "Precision compass", 20, 30, new ArrayList<>(Arrays.asList("Camping", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Portable Grill", "Compact portable grill", 70, 15, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Rain Jacket", "Waterproof rain jacket", 60, 25, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Camping Hammock", "Lightweight camping hammock", 40, 20, new ArrayList<>(Arrays.asList("Camping", "Outdoors"))).getBody());
        return itemIds;
    }

    private List<Long> createArtItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Canvas", "Stretched canvas for painting", 20, 100, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Acrylic Paint", "Set of 12 acrylic paints", 30, 50, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Brushes", "Set of 10 brushes", 15, 60, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Sketchbook", "Hardcover sketchbook", 25, 40, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Easel", "Adjustable wooden easel", 50, 15, new ArrayList<>(Arrays.asList("Art", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Palette", "Wooden artist palette", 10, 70, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Markers", "Set of 24 markers", 25, 30, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Charcoal Pencils", "Set of 12 charcoal pencils", 15, 20, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Watercolor Set", "Watercolor paints and brushes", 35, 25, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Drawing Table", "Adjustable drawing table", 100, 10, new ArrayList<>(Arrays.asList("Art", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Oil Paint", "Set of 12 oil paints", 40, 35, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Pastels", "Set of 24 pastels", 30, 25, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Calligraphy Set", "Complete calligraphy set", 50, 15, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Ink", "Set of 5 inks", 20, 40, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Craft Paper", "Set of 50 craft papers", 15, 50, new ArrayList<>(Arrays.asList("Art", "Supplies"))).getBody());
        return itemIds;
    }

    private List<Long> createDiyItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Hammer", "Heavy-duty hammer", 25, 50, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Screwdriver Set", "Set of 20 screwdrivers", 30, 40, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Drill", "Cordless power drill", 100, 20, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Wrench Set", "Set of 10 wrenches", 35, 30, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Tape Measure", "25-foot tape measure", 10, 60, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Level", "24-inch level", 15, 25, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Pliers Set", "Set of 5 pliers", 20, 35, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Saw", "Hand saw", 25, 40, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Utility Knife", "Retractable utility knife", 10, 50, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Toolbox", "Large toolbox", 40, 20, new ArrayList<>(Arrays.asList("DIY", "Storage"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Safety Goggles", "Protective safety goggles", 15, 50, new ArrayList<>(Arrays.asList("DIY", "Safety"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Workbench", "Heavy-duty workbench", 150, 10, new ArrayList<>(Arrays.asList("DIY", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Sandpaper", "Set of 50 sandpapers", 10, 70, new ArrayList<>(Arrays.asList("DIY", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Glue Gun", "High-temperature glue gun", 20, 30, new ArrayList<>(Arrays.asList("DIY", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Paint", "Set of 5 paint cans", 50, 20, new ArrayList<>(Arrays.asList("DIY", "Supplies"))).getBody());
        return itemIds;
    }

    private List<Long> createGardenItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Lawn Mower", "Electric lawn mower", 200, 10, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Garden Hose", "50-foot garden hose", 30, 40, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Pruning Shears", "Stainless steel pruning shears", 20, 50, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Shovel", "Heavy-duty garden shovel", 25, 30, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Rake", "Leaf rake", 15, 20, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Garden Gloves", "Pair of garden gloves", 10, 60, new ArrayList<>(Arrays.asList("Garden", "Clothing"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Watering Can", "2-gallon watering can", 12, 35, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Wheelbarrow", "Heavy-duty wheelbarrow", 70, 15, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Plant Pots", "Set of 5 ceramic plant pots", 40, 25, new ArrayList<>(Arrays.asList("Garden", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Fertilizer", "Organic garden fertilizer", 20, 50, new ArrayList<>(Arrays.asList("Garden", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Hedge Trimmer", "Electric hedge trimmer", 100, 20, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Compost Bin", "Large compost bin", 80, 10, new ArrayList<>(Arrays.asList("Garden", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Greenhouse", "Portable greenhouse", 150, 5, new ArrayList<>(Arrays.asList("Garden", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Garden Cart", "Heavy-duty garden cart", 90, 15, new ArrayList<>(Arrays.asList("Garden", "Tools"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Seed Packets", "Set of 20 seed packets", 25, 50, new ArrayList<>(Arrays.asList("Garden", "Supplies"))).getBody());
        return itemIds;
    }

    private List<Long> createShoeItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Running Shoes", "Comfortable running shoes", 80, 40, new ArrayList<>(Arrays.asList("Shoes", "Sports"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Hiking Boots", "Waterproof hiking boots", 120, 30, new ArrayList<>(Arrays.asList("Shoes", "Camping"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Sandals", "Comfortable summer sandals", 50, 50, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Dress Shoes", "Elegant dress shoes", 100, 20, new ArrayList<>(Arrays.asList("Shoes", "Formal"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Sneakers", "Casual sneakers", 60, 60, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Flip Flops", "Comfortable flip flops", 20, 70, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Work Boots", "Durable work boots", 90, 30, new ArrayList<>(Arrays.asList("Shoes", "Work"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Loafers", "Comfortable loafers", 70, 40, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Climbing Shoes", "High grip climbing shoes", 110, 20, new ArrayList<>(Arrays.asList("Shoes", "Sports"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Soccer Cleats", "Professional soccer cleats", 120, 25, new ArrayList<>(Arrays.asList("Shoes", "Sports"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Ballet Flats", "Comfortable ballet flats", 50, 30, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "High Heels", "Stylish high heels", 80, 20, new ArrayList<>(Arrays.asList("Shoes", "Formal"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Boat Shoes", "Water-resistant boat shoes", 70, 25, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Slippers", "Warm and cozy slippers", 30, 40, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Rain Boots", "Waterproof rain boots", 60, 30, new ArrayList<>(Arrays.asList("Shoes", "Casual"))).getBody());
        return itemIds;
    }

    private List<Long> createAudioItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Headphones", "Noise-cancelling headphones", 150, 40, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Speakers", "High fidelity speakers", 200, 30, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Soundbar", "Dolby Atmos soundbar", 250, 20, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Bluetooth Earbuds", "Wireless Bluetooth earbuds", 100, 50, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Turntable", "Vintage turntable", 300, 15, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Microphone", "Professional studio microphone", 150, 20, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Home Theater System", "5.1 surround sound system", 400, 10, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Portable Speaker", "Waterproof portable speaker", 80, 40, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Subwoofer", "Powerful subwoofer", 200, 20, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Digital Recorder", "Portable digital recorder", 120, 25, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Headphone Amplifier", "High-quality headphone amplifier", 100, 15, new ArrayList<>(Arrays.asList("Audio", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Soundproofing Foam", "Soundproofing foam panels", 50, 30, new ArrayList<>(Arrays.asList("Audio", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Acoustic Guitar", "Professional acoustic guitar", 300, 20, new ArrayList<>(Arrays.asList("Audio", "Instruments"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Electric Guitar", "High-quality electric guitar", 500, 10, new ArrayList<>(Arrays.asList("Audio", "Instruments"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "MIDI Keyboard", "49-key MIDI keyboard", 150, 25, new ArrayList<>(Arrays.asList("Audio", "Instruments"))).getBody());
        return itemIds;
    }

    private List<Long> createHomeItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Sofa", "Comfortable living room sofa", 500, 10, new ArrayList<>(Arrays.asList("Home", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Dining Table", "Wooden dining table", 300, 15, new ArrayList<>(Arrays.asList("Home", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Bed Frame", "King-size bed frame", 400, 20, new ArrayList<>(Arrays.asList("Home", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Mattress", "Memory foam mattress", 300, 30, new ArrayList<>(Arrays.asList("Home", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Coffee Table", "Modern coffee table", 150, 25, new ArrayList<>(Arrays.asList("Home", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Bookshelf", "Large wooden bookshelf", 200, 20, new ArrayList<>(Arrays.asList("Home", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Lamp", "Stylish floor lamp", 80, 40, new ArrayList<>(Arrays.asList("Home", "Lighting"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Curtains", "Set of 2 blackout curtains", 60, 50, new ArrayList<>(Arrays.asList("Home", "Decor"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Rug", "Large area rug", 120, 25, new ArrayList<>(Arrays.asList("Home", "Decor"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Picture Frames", "Set of 5 picture frames", 30, 50, new ArrayList<>(Arrays.asList("Home", "Decor"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Blender", "High-speed blender", 100, 30, new ArrayList<>(Arrays.asList("Home", "Appliances"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Microwave", "900W microwave oven", 150, 20, new ArrayList<>(Arrays.asList("Home", "Appliances"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Toaster", "4-slice toaster", 50, 40, new ArrayList<>(Arrays.asList("Home", "Appliances"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Vacuum Cleaner", "Bagless vacuum cleaner", 200, 15, new ArrayList<>(Arrays.asList("Home", "Appliances"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Iron", "Steam iron", 40, 30, new ArrayList<>(Arrays.asList("Home", "Appliances"))).getBody());
        return itemIds;
    }

    private List<Long> createSportsItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Soccer Ball", "Official size soccer ball", 30, 50, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Basketball", "Professional basketball", 40, 40, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Tennis Racket", "Lightweight tennis racket", 60, 30, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Yoga Mat", "Non-slip yoga mat", 20, 70, new ArrayList<>(Arrays.asList("Sports", "Fitness"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Dumbbells", "Set of 2 dumbbells", 50, 25, new ArrayList<>(Arrays.asList("Sports", "Fitness"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Resistance Bands", "Set of 5 resistance bands", 30, 40, new ArrayList<>(Arrays.asList("Sports", "Fitness"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Jump Rope", "Speed jump rope", 15, 50, new ArrayList<>(Arrays.asList("Sports", "Fitness"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Running Shoes", "Comfortable running shoes", 80, 40, new ArrayList<>(Arrays.asList("Sports", "Shoes"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Baseball Glove", "Professional baseball glove", 60, 20, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Golf Clubs", "Set of 10 golf clubs", 300, 10, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Boxing Gloves", "Pair of boxing gloves", 50, 30, new ArrayList<>(Arrays.asList("Sports", "Fitness"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Bicycle Helmet", "Safety bicycle helmet", 40, 35, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Running Shorts", "Lightweight running shorts", 30, 50, new ArrayList<>(Arrays.asList("Sports", "Clothing"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Football", "Official size football", 35, 40, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Swim Goggles", "Anti-fog swim goggles", 20, 50, new ArrayList<>(Arrays.asList("Sports", "Outdoors"))).getBody());
        return itemIds;
    }

    private List<Long> createOfficeItems(String token, Long storeId, IStoreManagementService storeManagementService) {
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Desk", "Ergonomic office desk", 200, 20, new ArrayList<>(Arrays.asList("Office", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Office Chair", "Comfortable office chair", 150, 30, new ArrayList<>(Arrays.asList("Office", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Monitor", "27-inch monitor", 250, 40, new ArrayList<>(Arrays.asList("Office", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Printer", "All-in-one printer", 100, 20, new ArrayList<>(Arrays.asList("Office", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Desk Lamp", "Adjustable desk lamp", 40, 50, new ArrayList<>(Arrays.asList("Office", "Lighting"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "File Cabinet", "4-drawer file cabinet", 80, 20, new ArrayList<>(Arrays.asList("Office", "Furniture"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Office Supplies Set", "Complete office supplies set", 50, 40, new ArrayList<>(Arrays.asList("Office", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Whiteboard", "Large magnetic whiteboard", 70, 30, new ArrayList<>(Arrays.asList("Office", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Shredder", "High-security paper shredder", 100, 15, new ArrayList<>(Arrays.asList("Office", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Computer", "Desktop computer", 500, 10, new ArrayList<>(Arrays.asList("Office", "Electronics"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Notebook", "Set of 5 notebooks", 20, 50, new ArrayList<>(Arrays.asList("Office", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Pens", "Pack of 50 pens", 10, 100, new ArrayList<>(Arrays.asList("Office", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Paper Clips", "Box of 100 paper clips", 5, 200, new ArrayList<>(Arrays.asList("Office", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Sticky Notes", "Pack of 12 sticky notes", 15, 60, new ArrayList<>(Arrays.asList("Office", "Supplies"))).getBody());
        itemIds.add((long) storeManagementService.addItemToStore(token, storeId, "Calculator", "Scientific calculator", 30, 25, new ArrayList<>(Arrays.asList("Office", "Electronics"))).getBody());
        return itemIds;
    }

    private void createPurchase(List<String> tokens) {
        IUserService userService = (IUserService) SpringContext.getBean("userService");
        userService.checkoutShoppingCart(tokens.get(0), "1234567812345678", new Date(2025), "123", null);
    }
}