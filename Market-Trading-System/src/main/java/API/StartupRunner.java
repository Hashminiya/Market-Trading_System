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

        System.out.println("-----------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------");

        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));

        ResponseEntity<?> response = userService.register("admin", "admin",25);
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
        initShoppingCart(tokens,storeIds,itemIds);
        createPurchase(tokens);
    }

    private List<String> createUsers() {
        IUserService userService = (IUserService) SpringContext.getBean("userService");
        userService.register("user1", "user1",25);
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
        return tokens;
    }

    private List<Long> createStores(List<String> tokens) {
        IStoreManagementService storeManagementService = (IStoreManagementService) SpringContext.getBean("StoreManagementService");
        ResponseEntity<?> storeId1 = storeManagementService.createStore(tokens.get(0), "store1","store1 description");
        ResponseEntity<?> storeId2 = storeManagementService.createStore(tokens.get(1), "store2","store2 description");
        ResponseEntity<?> storeId3 = storeManagementService.createStore(tokens.get(2), "store3","store3 description");
        ResponseEntity<?> storeId4 = storeManagementService.createStore(tokens.get(3), "store4","store4 description");
        ResponseEntity<?> storeId5 = storeManagementService.createStore(tokens.get(4), "store5","store5 description");
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
        userService.addItemToBasket(tokens.get(0),storesId.get(0), itemIds.get(0), 1);
        userService.addItemToBasket(tokens.get(1),storesId.get(1), itemIds.get(1), 1);
        userService.addItemToBasket(tokens.get(2),storesId.get(2), itemIds.get(2), 1);
        userService.addItemToBasket(tokens.get(3),storesId.get(3), itemIds.get(3), 1);
        userService.addItemToBasket(tokens.get(4),storesId.get(4), itemIds.get(4), 1);
    }

    private List<Long> createItems(List<String> tokens, List<Long> storeIds) {
        IStoreManagementService storeManagementService = (IStoreManagementService) SpringContext.getBean("StoreManagementService");
        ResponseEntity<?> itemId1 = storeManagementService.addItemToStore(tokens.get(0), storeIds.get(0), "item1", "item1 description", 10, 5, new ArrayList<>());
        ResponseEntity<?> itemId2 = storeManagementService.addItemToStore(tokens.get(1), storeIds.get(1), "item2", "item2 description", 15, 3, new ArrayList<>());
        ResponseEntity<?> itemId3 = storeManagementService.addItemToStore(tokens.get(2), storeIds.get(2), "item3", "item3 description", 20, 3, new ArrayList<>());
        ResponseEntity<?> itemId4 = storeManagementService.addItemToStore(tokens.get(3), storeIds.get(3), "item4", "item4 description", 12, 3, new ArrayList<>());
        ResponseEntity<?> itemId5 = storeManagementService.addItemToStore(tokens.get(4), storeIds.get(4), "item5", "item5 description", 13, 4, new ArrayList<>());
        List<Long> itemIds = new ArrayList<>();
        itemIds.add((long) itemId1.getBody());
        itemIds.add((long) itemId2.getBody());
        itemIds.add((long) itemId3.getBody());
        itemIds.add((long) itemId4.getBody());
        itemIds.add((long) itemId5.getBody());
        return itemIds;
    }

    private void createPurchase(List<String> tokens) {
        IUserService userService = (IUserService) SpringContext.getBean("userService");
        userService.checkoutShoppingCart(tokens.get(0),"1234567812345678",new Date(2025),"123",null);
    }
}
