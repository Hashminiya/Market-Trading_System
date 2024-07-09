//package AcceptanceTests;
//
//import DomainLayer.Market.Purchase.PaymentServiceProxy;
//import DomainLayer.Market.Store.Discount.IDiscount;
//import DomainLayer.Market.Util.IRepository;
//import DomainLayer.Market.Util.InMemoryRepository;
//import DomainLayer.Market.Store.Discount.Discount;
//import ServiceLayer.ServiceFactory;
//import ServiceLayer.Store.StoreManagementService;
//import ServiceLayer.User.UserService;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.junit.jupiter.api.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import javax.ws.rs.core.Response;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class StoreManagementAT{
//
//    private static final String ADMIN_USER_NAME = "SystemManager";
//    private static final String ADMIN_PASSWORD = "SystemManagerPassword";
//    private static final String USERNAME1 = "testUser";
//    private static final String USERNAME2 = "testUser2";
//    private static final String FOUNDER_ID = "founderId";
//    private static final String PASSWORD = "12345678";
//    private static String USERNAME1_TOKEN;
//    private static String USERNAME2_TOKEN;
//    private static long ITEM2_ID;
//    private static long STORE_ID1 ;
//    private static long STORE_ID2 ;
//    private static long ITEM3_ID;
//    private static long ITEM1_ID;
//    private static final String CREDIT_CARD = "1234-5678-9876-5432";
//    private static final Date EXPIRY_DATE = new Date(); // Set an appropriate expiry date
//    private static final String CVV = "123";
//    private static final String DISCOUNT_CODE = "DISCOUNT10";
//    private static final int AGE = 30;
//    private static final String STORE_NAME = "storeName";
//    private static final String STORE_DESCRIPTION = "storeDescription";
//    private static final String MANAGER_ID = "ManagerId";
//    private static  Long ITEM_ID;
//    private static String TOKEN;
//    private static long STORE_ID;
//    private static final String AGE_POLICY_SPECIFIC_ITEM = String.format("{\n" +
//            "    \"@type\": \"AgeRestrictedPurchasePolicy\",\n" +
//            "    \"name\": \"Alcohol 18 and above\",\n" +
//            "    \"id\": 10,\n" +
//            "    \"minAge\": 16,\n"+
//            "    \"items\": [%s],\n" +
//            "    \"categories\": [\"alcohol\"],\n" +
//            "    \"isStore\": false\n" +
//            "}",ITEM_ID); ;
//    private static final String MAXIMUM_QUANTITY_POLICY = String.format("{\n" +
//            "    \"@type\": \"MaximumQuantityPurchasePolicy\",\n" +
//            "    \"name\": \"all store max amount policy\",\n" +
//            "    \"id\": 10,\n" +
//            "    \"maxAmount\": 15,\n"+
//            "    \"items\": null,\n" +
//            "    \"categories\": null ,\n" +
//            "    \"isStore\": true\n" +
//            "}");
//    private static String ALCOHOL_POLICY = "{\n" +
//            "    \"@type\": \"AgeRestrictedPurchasePolicy\",\n" +
//            "    \"name\": \"Alcohol 18 and above\",\n" +
//            "    \"id\": 10002,\n" +
//            "    \"minAge\": 18,\n"+
//            "    \"items\": null,\n" +
//            "    \"categories\": [\"alcohol\"],\n" +
//            "    \"isStore\": false\n" +
//            "}";
//    private static StoreManagementService storeManagementService;
//    private static IRepository<Long, IDiscount> discountRepository;
//    private static UserService userService;
//    private static ServiceFactory serviceFactory;
//
//    @BeforeAll
//    public static void setUp() {
//        SetUp.setUp();
//        serviceFactory = ServiceFactory.getServiceFactory();
//
//        discountRepository = new InMemoryRepository<>();
//        storeManagementService = serviceFactory.getStoreManagementService();
//        storeManagementService.setUserFacade(serviceFactory.getUserFacade());
//        userService = serviceFactory.getUserService();
//        userService.register(FOUNDER_ID, PASSWORD, AGE);
//        userService.register(MANAGER_ID, PASSWORD, AGE);
//        ResponseEntity<String> response1 = userService.login(FOUNDER_ID, PASSWORD);
//        TOKEN = response1.getBody();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        storeManagementService.removeStore(TOKEN, STORE_ID);
//        serviceFactory.clear();
//    }
//
//    @Test
//    @Order(1)
//    public void test_createStore_should_returnOkStatus_for_valid_parameters() {
//        ResponseEntity<?> response = storeManagementService.createStore(TOKEN, "newStoreName", "newStoreDescription");
//        STORE_ID =  (Long) response.getBody();
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(2)
//    public void test_addItemToStore_should_returnOkStatus_and_add_item_for_valid_store_and_valid_item() {
//        ResponseEntity<?> response = storeManagementService.addItemToStore(TOKEN, STORE_ID, "itemName", "description", 10.0, 100, List.of("category"));
//        ITEM_ID = (Long) response.getBody();
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>) storeManagementService.viewInventory(TOKEN, STORE_ID).getBody();
//        assertTrue(inventory.containsKey(ITEM_ID));
//    }
//
//    @Test
//    @Order(3)
//    public void test_updateItem_should_returnOkStatus_and_update_item_for_valid_store_and_exists_item() {
//        ResponseEntity<?> response = storeManagementService.updateItem(TOKEN, STORE_ID, ITEM_ID, "newName", 20.0, 200);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>) storeManagementService.viewInventory(TOKEN, STORE_ID).getBody();
//        assertTrue(inventory.containsKey(ITEM_ID) && inventory.get(ITEM_ID) == 200);
//    }
//
//    @Test
//    @Order(4)
//    public void test_deleteItem_should_returnOkStatus() {
//        ResponseEntity<?> response = storeManagementService.deleteItem(TOKEN, STORE_ID, ITEM_ID);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>) storeManagementService.viewInventory(TOKEN, STORE_ID).getBody();
//        assertFalse(inventory.containsKey(ITEM_ID));
//    }
//
//    @Test
//    @Order(5)
//    public void test_addDiscount_should_returnOkStatus_for_simple_discount() {
//        String discountDetails = "{\n" +
//                "    \"@type\": \"RegularDiscount\",\n" +
//                "    \"id\": 10,\n" +
//                "    \"percent\": 5.0,\n" +
//                "    \"expirationDate\": \"2024-12-31T23:59:59Z\",\n" +
//                "    \"storeId\": 1,\n" +
//                "    \"items\": [1001, 1002],\n" +
//                "    \"categories\": [\"Electronics\"],\n" +
//                "    \"conditions\": {\n" +
//                "        \"@type\": \"ConditionComposite\",\n" +
//                "        \"conditions\": [\n" +
//                "            {\n" +
//                "                \"@type\": \"Condition\",\n" +
//                "                \"itemId\": 3001,\n" +
//                "                \"count\": 2\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"rule\": \"AND\"\n" +
//                "    }\n" +
//                "}";
//        ResponseEntity<?> response = storeManagementService.addDiscount(TOKEN, STORE_ID, discountDetails);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(6)
//    public void test_addDiscount_should_returnOkStatus_for_complex_discount() {
//        String discountDetails = "{\n" +
//                "    \"@type\": \"RegularDiscount\",\n" +
//                "    \"id\": 10,\n" +
//                "    \"percent\": 5.0,\n" +
//                "    \"expirationDate\": \"2024-12-31T23:59:59Z\",\n" +
//                "    \"storeId\": 1,\n" +
//                "    \"items\": [1001, 1002],\n" +
//                "    \"categories\": [\"Electronics\"],\n" +
//                "    \"conditions\": {\n" +
//                "        \"@type\": \"ConditionComposite\",\n" +
//                "        \"conditions\": [\n" +
//                "            {\n" +
//                "                \"@type\": \"Condition\",\n" +
//                "                \"itemId\": 3001,\n" +
//                "                \"count\": 2\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"rule\": \"AND\"\n" +
//                "    }\n" +
//                "}";
//        ResponseEntity<?> response = storeManagementService.addDiscount(TOKEN, STORE_ID, discountDetails);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//    @Test
//    @Order(7)
//    public void test_addPolicy_should_returnOkStatus_for_simple_policy() {
//        String policyDetails = "{\n" +
//                "    \"@type\": \"AgeRestrictedPurchasePolicy\",\n" +
//                "    \"name\": \"Alcohol 18 and above\",\n" +
//                "    \"id\": 10,\n" +
//                "    \"minAge\": 18,\n"+
//                "    \"items\": null,\n" +
//                "    \"categories\": [\"alcohol\"],\n" +
//                "    \"isStore\": false\n" +
//                "}";
//
//        ResponseEntity<?> response = storeManagementService.addPolicy(TOKEN, STORE_ID, policyDetails);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(8)
//    public void test_addPolicy_should_returnOkStatus_for_complex_policy() {
//        String policyDetails = String.format("{\n" +
//                "    \"@type\": \"PurchasePolicyComposite\",\n" +
//                "    \"id\": 1234,\n" +
//                "    \"name\": \"complex policy\",\n" +
//                "    \"policies\": [%s,%s] ,\n" +
//                "    \"logicalRole\": \"OR\"\n" +
//                "}",AGE_POLICY_SPECIFIC_ITEM,MAXIMUM_QUANTITY_POLICY);
//        ResponseEntity<?> response = storeManagementService.addPolicy(TOKEN, STORE_ID, policyDetails);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(9)
//    public void test_viewManagementInfo_should_returnOkStatus() {
//        ResponseEntity<?> response = storeManagementService.viewManagementInfo(TOKEN, STORE_ID);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(10)
//    public void test_viewPurchasesHistory_should_returnOkStatus() {
//        ResponseEntity<?> response = storeManagementService.viewPurchasesHistory(TOKEN, STORE_ID);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(11)
//    public void test_assignStoreManager_should_returnOkStatus() {
//        ResponseEntity<?> response = storeManagementService.assignStoreManager(TOKEN, STORE_ID, MANAGER_ID, Arrays.asList("VIEW_STORE_MANAGEMENT_INFO", "VIEW_PURCHASE_HISTORY", "VIEW_INVENTORY"));
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        ResponseEntity<?> response2 = storeManagementService.viewManagementInfo(TOKEN, STORE_ID);
//        assertTrue(((HashMap<String, List<String>>) response2.getBody()).containsKey(MANAGER_ID));
//    }
//
//    @Test
//    @Order(12)
//    public void test_assignStoreOwner_should_returnErrorStatus() {
//        ResponseEntity<?> response = storeManagementService.assignStoreOwner(TOKEN, STORE_ID, MANAGER_ID);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    @Order(13)
//    public void test_assignStoreOwner_should_returnOkStatus() {
//        userService.register(USERNAME1, PASSWORD, AGE);
//        ResponseEntity<?> response = storeManagementService.assignStoreOwner(TOKEN, STORE_ID, USERNAME1);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(14)
//    public void test_removeStore_should_returnOkStatus() {
//        ResponseEntity<?> response = storeManagementService.removeStore(TOKEN, STORE_ID);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    /*@Test
//    @Order(11)
//    public void test_checkoutShoppingCart_should_return_ok_status() {
//        beforeCheckouts();
//        userService.addItemToBasket(USERNAME1_TOKEN, STORE_ID1, ITEM1_ID, 3);
//        ResponseEntity<String> response = storeManagementService.checkoutShoppingCart(USERNAME1_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
//        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>)
//                storeManagementService.viewInventory(USERNAME1_TOKEN,STORE_ID1).getBody();
//        assertEquals(97, inventory.get(ITEM1_ID));
//    }
//    @Test
//    @Order(12)
//    public void test_checkoutShoppingCart_should_return_error_with_alcohol_item_user_under_18() {
//        storeManagementService.addPolicy(USERNAME1_TOKEN, STORE_ID1,ALCOHOL_POLICY);
//        USERNAME2_TOKEN = userService.login(USERNAME2, PASSWORD).getBody();
//        userService.addItemToBasket(USERNAME2_TOKEN, STORE_ID1, ITEM3_ID, 1);
//        ResponseEntity<String> response = storeManagementService.checkoutShoppingCart(USERNAME2_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
//        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusCode().value());
//    }
//    @Test
//    @Order(13)
//    public void test_checkoutShoppingCart_should_return_okResponse_and_update_inventory_with_2_baskets() {
//
//        HashMap<Long, Integer> inventory_before_action_store1 = (HashMap<Long, Integer>)
//                storeManagementService.viewInventory(USERNAME1_TOKEN,STORE_ID1).getBody();
//        HashMap<Long, Integer> inventory_before_action_store2 = (HashMap<Long, Integer>)
//                storeManagementService.viewInventory(USERNAME1_TOKEN,STORE_ID2).getBody();
//
//        userService.addItemToBasket(USERNAME1_TOKEN, STORE_ID1, ITEM3_ID, 1);
//        userService.addItemToBasket(USERNAME1_TOKEN, STORE_ID2, ITEM2_ID,10);
//
//        ResponseEntity<String> response = storeManagementService.checkoutShoppingCart(USERNAME1_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        HashMap<Long, Integer> inventory_after_action_store1 = (HashMap<Long, Integer>)
//                storeManagementService.viewInventory(USERNAME1_TOKEN,STORE_ID1).getBody();
//        HashMap<Long, Integer> inventory_after_action_store2 = (HashMap<Long, Integer>)
//                storeManagementService.viewInventory(USERNAME1_TOKEN,STORE_ID2).getBody();
//
//        inventory_before_action_store1.put(ITEM3_ID,inventory_before_action_store1.get(ITEM3_ID) - 1);
//        inventory_before_action_store2.put(ITEM2_ID, inventory_before_action_store2.get(ITEM2_ID)- 10);
//        assertEquals(inventory_before_action_store1, inventory_after_action_store1);
//        assertEquals(inventory_before_action_store2, inventory_after_action_store2);
//    }
//    @Test
//    @Order(14)
//    public void test_checkoutShoppingCart_should_return_error_with_false_from_credit_card_services() {
//        PaymentServiceProxy paymentServiceProxy = mock(PaymentServiceProxy.class);
//        when(paymentServiceProxy.chargeCreditCard(anyString(),any(),anyString(),anyDouble())).thenReturn(false);
//        serviceFactory.getPurchaseFacade().setPaymentServiceProxy(paymentServiceProxy);
//
//        HashMap<Long, Integer> inventory_before_action = (HashMap<Long, Integer>)
//                storeManagementService.viewInventory(USERNAME1_TOKEN,STORE_ID1).getBody();
//
//        userService.addItemToBasket(USERNAME1_TOKEN, STORE_ID1, ITEM3_ID, 1);
//
//        ResponseEntity<String> response = storeManagementService.checkoutShoppingCart(USERNAME1_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
//        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusCode().value());
//        HashMap<Long, Integer> inventory_after_action = (HashMap<Long, Integer>)
//                storeManagementService.viewInventory(USERNAME1_TOKEN,STORE_ID1).getBody();
//        assertEquals(inventory_before_action, inventory_after_action);
//    }
//
//
//    private void beforeCheckouts(){
//        userService.register(USERNAME1, PASSWORD, AGE);
//        USERNAME1_TOKEN = userService.login(USERNAME1, PASSWORD).getBody();
//        STORE_ID1 = (Long) storeManagementService.createStore(USERNAME1_TOKEN, "new test store- userAT", "description").getBody();
//        STORE_ID2 = (Long) storeManagementService.createStore(USERNAME1_TOKEN,"second store- userAT ", "descrption").getBody();
//        ITEM1_ID = (Long)  storeManagementService.addItemToStore(USERNAME1_TOKEN, STORE_ID1,"new item", "desctiprion",50,100, List.of("electronics")).getBody();
//        ITEM3_ID = (Long) storeManagementService.addItemToStore(USERNAME1_TOKEN, STORE_ID1, "vodka", "alcoholic drink",100,50, List.of("alcohol")).getBody();
//        ITEM2_ID = (Long)  storeManagementService.addItemToStore(USERNAME1_TOKEN, STORE_ID2,"new item", "desctiprion",50,15, List.of("electronics")).getBody();
//    }*/
//}
