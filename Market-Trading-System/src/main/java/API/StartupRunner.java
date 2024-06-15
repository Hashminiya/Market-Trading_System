package API;

import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import ServiceLayer.Store.IStoreManagementService;
import ServiceLayer.User.IUserService;
import ServiceLayer.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("StartupRunner")
public class StartupRunner implements CommandLineRunner {

    private final IUserService userService;

    @Autowired
    public StartupRunner(@Qualifier("userService") IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args){

        System.out.println("-----------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------");

        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));

        ResponseEntity<?> response = userService.register("admin", "admin", 25);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ResponseStatusException(response.getStatusCode(),response.getBody().toString());
        }
        System.out.println("Payment and Supply external services connected successfully");
        System.out.println("The server is running...");
        runSystem();
    }

    private void runSystem() {
        List<String> tokens = createUsers();
        List<Long> storeIds = createStores(tokens);
        List<Long> itemIds = createItems(tokens, storeIds);
        initShoppingCart(tokens, storeIds, itemIds);
        createPurchase(tokens);
    }

    private List<String> createUsers() {
        IUserService userService = (IUserService) SpringContext.getBean("userService");
        userService.register("user1", "user1", 25);
        userService.register("user2", "user2", 30);
        userService.register("user3", "user3", 22);
        userService.register("user4", "user4", 28);
        userService.register("user5", "user5", 35);
        userService.register("user6", "user6", 26);
        userService.register("user7", "user7", 29);
        userService.register("user8", "user8", 31);
        userService.register("user9", "user9", 24);
        userService.register("user10", "user10", 27);
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
        userService.logout("admin");
        return tokens;
    }

    private List<Long> createStores(List<String> tokens) {
        IStoreManagementService storeManagementService = (IStoreManagementService) SpringContext.getBean("StoreManagementService");
        ResponseEntity<?> storeId1 = storeManagementService.createStore(tokens.get(0), "Electronics Store","High quality electronics and gadgets");
        ResponseEntity<?> storeId2 = storeManagementService.createStore(tokens.get(1), "Camping Gear","All your camping essentials");
        ResponseEntity<?> storeId3 = storeManagementService.createStore(tokens.get(2), "Art Supplies","Wide range of art supplies");
        ResponseEntity<?> storeId4 = storeManagementService.createStore(tokens.get(3), "DIY Tools","Everything you need for your DIY projects");
        ResponseEntity<?> storeId5 = storeManagementService.createStore(tokens.get(4), "Garden Center","All you need for your garden");
        List<Long> storeIds = new ArrayList<>();
        storeIds.add((long) storeId1.getBody());
        storeIds.add((long) storeId2.getBody());
        storeIds.add((long) storeId3.getBody());
        storeIds.add((long) storeId4.getBody());
        storeIds.add((long) storeId5.getBody());
        return storeIds;
    }

    private void initShoppingCart(List<String> tokens, List<Long> storesId, List<Long> itemIds) {
        IUserService userService = (IUserService) SpringContext.getBean("userService");
        userService.addItemToBasket(tokens.get(0), storesId.get(0), itemIds.get(0), 1);
        userService.addItemToBasket(tokens.get(1), storesId.get(1), itemIds.get(1), 1);
        userService.addItemToBasket(tokens.get(2), storesId.get(2), itemIds.get(2), 1);
        userService.addItemToBasket(tokens.get(3), storesId.get(3), itemIds.get(3), 1);
        userService.addItemToBasket(tokens.get(4), storesId.get(4), itemIds.get(4), 1);
    }

    private List<Long> createItems(List<String> tokens, List<Long> storeIds) {
        IStoreManagementService storeManagementService = (IStoreManagementService) SpringContext.getBean("StoreManagementService");

        // Electronics Store
        ResponseEntity<?> itemId1 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Smartphone", "Latest model smartphone", 700, 50, new ArrayList<>());
        ResponseEntity<?> itemId2 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Laptop", "High performance laptop", 1200, 30, new ArrayList<>());
        ResponseEntity<?> itemId3 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Tablet", "Lightweight and powerful tablet", 500, 20, new ArrayList<>());
        ResponseEntity<?> itemId4 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Smartwatch", "Feature-rich smartwatch", 200, 100, new ArrayList<>());
        ResponseEntity<?> itemId5 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Headphones", "Noise-cancelling headphones", 150, 40, new ArrayList<>());
        ResponseEntity<?> itemId6 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Camera", "High resolution digital camera", 800, 15, new ArrayList<>());
        ResponseEntity<?> itemId7 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Speaker", "Portable Bluetooth speaker", 100, 60, new ArrayList<>());
        ResponseEntity<?> itemId8 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Monitor", "4K Ultra HD monitor", 300, 25, new ArrayList<>());
        ResponseEntity<?> itemId9 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Keyboard", "Mechanical keyboard", 80, 70, new ArrayList<>());
        ResponseEntity<?> itemId10 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "Mouse", "Wireless mouse", 50, 90, new ArrayList<>());

        // Camping Gear
        ResponseEntity<?> itemId11 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Tent", "4-person tent", 100, 20, new ArrayList<>());
        ResponseEntity<?> itemId12 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Sleeping Bag", "Warm and comfortable sleeping bag", 60, 40, new ArrayList<>());
        ResponseEntity<?> itemId13 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Backpack", "50L hiking backpack", 80, 25, new ArrayList<>());
        ResponseEntity<?> itemId14 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Camping Stove", "Portable camping stove", 40, 30, new ArrayList<>());
        ResponseEntity<?> itemId15 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Flashlight", "LED flashlight", 20, 50, new ArrayList<>());
        ResponseEntity<?> itemId16 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Camping Chair", "Folding camping chair", 30, 35, new ArrayList<>());
        ResponseEntity<?> itemId17 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Water Bottle", "Insulated water bottle", 15, 60, new ArrayList<>());
        ResponseEntity<?> itemId18 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Multi-tool", "Multi-purpose tool", 25, 45, new ArrayList<>());
        ResponseEntity<?> itemId19 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Camping Table", "Folding camping table", 50, 10, new ArrayList<>());
        ResponseEntity<?> itemId20 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "Lantern", "Battery-powered lantern", 30, 25, new ArrayList<>());

        // Art Supplies
        ResponseEntity<?> itemId21 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Canvas", "Stretched canvas for painting", 20, 100, new ArrayList<>());
        ResponseEntity<?> itemId22 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Acrylic Paint", "Set of 12 acrylic paints", 30, 50, new ArrayList<>());
        ResponseEntity<?> itemId23 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Brushes", "Set of 10 brushes", 15, 60, new ArrayList<>());
        ResponseEntity<?> itemId24 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Sketchbook", "Hardcover sketchbook", 25, 40, new ArrayList<>());
        ResponseEntity<?> itemId25 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Easel", "Adjustable wooden easel", 50, 15, new ArrayList<>());
        ResponseEntity<?> itemId26 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Palette", "Wooden artist palette", 10, 70, new ArrayList<>());
        ResponseEntity<?> itemId27 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Markers", "Set of 24 markers", 25, 30, new ArrayList<>());
        ResponseEntity<?> itemId28 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Charcoal Pencils", "Set of 12 charcoal pencils", 15, 20, new ArrayList<>());
        ResponseEntity<?> itemId29 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Watercolor Set", "Watercolor paints and brushes", 35, 25, new ArrayList<>());
        ResponseEntity<?> itemId30 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "Drawing Table", "Adjustable drawing table", 100, 10, new ArrayList<>());

        // DIY Tools
        ResponseEntity<?> itemId31 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Hammer", "Heavy-duty hammer", 25, 50, new ArrayList<>());
        ResponseEntity<?> itemId32 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Screwdriver Set", "Set of 20 screwdrivers", 30, 40, new ArrayList<>());
        ResponseEntity<?> itemId33 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Drill", "Cordless power drill", 100, 20, new ArrayList<>());
        ResponseEntity<?> itemId34 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Wrench Set", "Set of 10 wrenches", 35, 30, new ArrayList<>());
        ResponseEntity<?> itemId35 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Tape Measure", "25-foot tape measure", 10, 60, new ArrayList<>());
        ResponseEntity<?> itemId36 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Level", "24-inch level", 15, 25, new ArrayList<>());
        ResponseEntity<?> itemId37 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Pliers Set", "Set of 5 pliers", 20, 35, new ArrayList<>());
        ResponseEntity<?> itemId38 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Saw", "Hand saw", 25, 40, new ArrayList<>());
        ResponseEntity<?> itemId39 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Utility Knife", "Retractable utility knife", 10, 50, new ArrayList<>());
        ResponseEntity<?> itemId40 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "Toolbox", "Large toolbox", 40, 20, new ArrayList<>());

        // Garden Center
        ResponseEntity<?> itemId41 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Lawn Mower", "Electric lawn mower", 200, 10, new ArrayList<>());
        ResponseEntity<?> itemId42 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Garden Hose", "50-foot garden hose", 30, 40, new ArrayList<>());
        ResponseEntity<?> itemId43 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Pruning Shears", "Stainless steel pruning shears", 20, 50, new ArrayList<>());
        ResponseEntity<?> itemId44 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Shovel", "Heavy-duty garden shovel", 25, 30, new ArrayList<>());
        ResponseEntity<?> itemId45 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Rake", "Leaf rake", 15, 20, new ArrayList<>());
        ResponseEntity<?> itemId46 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Garden Gloves", "Pair of garden gloves", 10, 60, new ArrayList<>());
        ResponseEntity<?> itemId47 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Watering Can", "2-gallon watering can", 12, 35, new ArrayList<>());
        ResponseEntity<?> itemId48 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Wheelbarrow", "Heavy-duty wheelbarrow", 70, 15, new ArrayList<>());
        ResponseEntity<?> itemId49 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Plant Pots", "Set of 5 ceramic plant pots", 40, 25, new ArrayList<>());
        ResponseEntity<?> itemId50 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "Fertilizer", "Organic garden fertilizer", 20, 50, new ArrayList<>());

        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) itemId1.getBody());
        itemIds.add((long) itemId2.getBody());
        itemIds.add((long) itemId3.getBody());
        itemIds.add((long) itemId4.getBody());
        itemIds.add((long) itemId5.getBody());
        itemIds.add((long) itemId6.getBody());
        itemIds.add((long) itemId7.getBody());
        itemIds.add((long) itemId8.getBody());
        itemIds.add((long) itemId9.getBody());
        itemIds.add((long) itemId10.getBody());
        itemIds.add((long) itemId11.getBody());
        itemIds.add((long) itemId12.getBody());
        itemIds.add((long) itemId13.getBody());
        itemIds.add((long) itemId14.getBody());
        itemIds.add((long) itemId15.getBody());
        itemIds.add((long) itemId16.getBody());
        itemIds.add((long) itemId17.getBody());
        itemIds.add((long) itemId18.getBody());
        itemIds.add((long) itemId19.getBody());
        itemIds.add((long) itemId20.getBody());
        itemIds.add((long) itemId21.getBody());
        itemIds.add((long) itemId22.getBody());
        itemIds.add((long) itemId23.getBody());
        itemIds.add((long) itemId24.getBody());
        itemIds.add((long) itemId25.getBody());
        itemIds.add((long) itemId26.getBody());
        itemIds.add((long) itemId27.getBody());
        itemIds.add((long) itemId28.getBody());
        itemIds.add((long) itemId29.getBody());
        itemIds.add((long) itemId30.getBody());
        itemIds.add((long) itemId31.getBody());
        itemIds.add((long) itemId32.getBody());
        itemIds.add((long) itemId33.getBody());
        itemIds.add((long) itemId34.getBody());
        itemIds.add((long) itemId35.getBody());
        itemIds.add((long) itemId36.getBody());
        itemIds.add((long) itemId37.getBody());
        itemIds.add((long) itemId38.getBody());
        itemIds.add((long) itemId39.getBody());
        itemIds.add((long) itemId40.getBody());
        itemIds.add((long) itemId41.getBody());
        itemIds.add((long) itemId42.getBody());
        itemIds.add((long) itemId43.getBody());
        itemIds.add((long) itemId44.getBody());
        itemIds.add((long) itemId45.getBody());
        itemIds.add((long) itemId46.getBody());
        itemIds.add((long) itemId47.getBody());
        itemIds.add((long) itemId48.getBody());
        itemIds.add((long) itemId49.getBody());
        itemIds.add((long) itemId50.getBody());

        return itemIds;
    }

    private void createPurchase(List<String> tokens) {
        IUserService userService = (IUserService) SpringContext.getBean("userService");
        userService.checkoutShoppingCart(tokens.get(0), "1234567812345678", new Date(2025), "123", null);
    }
}
