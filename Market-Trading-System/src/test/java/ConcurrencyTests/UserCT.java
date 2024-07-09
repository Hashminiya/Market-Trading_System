//package ConcurrencyTests;
//
//import AcceptanceTests.SetUp;
//import DAL.ItemDTO;
//import DomainLayer.Market.Purchase.IPurchaseFacade;
//import DomainLayer.Market.Purchase.Purchase;
//import DomainLayer.Market.Store.IStoreFacade;
//import DomainLayer.Market.User.IUserFacade;
//import DomainLayer.Market.User.ShoppingCart;
//import ServiceLayer.ServiceFactory;
//import ServiceLayer.Store.StoreManagementService;
//import ServiceLayer.User.UserService;
//import org.assertj.core.api.AbstractIntegerAssert;
//import org.junit.jupiter.api.*;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//public class UserCT {
//
//    private static UserService userService;
//    private static ServiceFactory serviceFactory;
//    private static StoreManagementService storeSevice;
//
//    private static IStoreFacade storeFacade;
//    private static IPurchaseFacade purchaseFacade;
//
//    private static final String ADMIN_USER_NAME ="admin" ;
//
//    private static String user1 = "user1";
//    private static String password1 = "password1";
//    private static int age1 = 19;
//    private static String user1_token;
//    private static String user2 = "user2";
//    private static String password2 = "password2";
//    private static int age2 = 23;
//    private static String user2_token;
//    private static Long store1;
//    private static Long store2;
//    private static Long item11;
//    private static Long item12;
//    private static Long item13;
//    private static Long item21;
//    private static Long item22;
//    private static Long item23;
//    private static final String credit_card = "1234-5678-9876-5432";
//    private static final Date expire_date = new Date(); // Set an appropriate expiry date
//    private static final String cvv = "123";
//
//
//    @BeforeAll
//    public static void setUp() throws Exception {
//        SetUp.setUp();
//
//        serviceFactory = ServiceFactory.getServiceFactory();
//        userService = serviceFactory.getUserService();
//        storeSevice = serviceFactory.getStoreManagementService();
//
//        storeFacade = IStoreFacade.getInstance(null, null, null);
//        purchaseFacade = IPurchaseFacade.getInstance(null, null, null);
//
//        userService.register(user1, password1, age1);
//        userService.register(user2, password2, age2);
//        user1_token = userService.login(user1, password1).getBody();
//        user2_token = userService.login(user2, password2).getBody();
//
//        store1 = (Long) storeSevice.createStore(user1_token, "store1", "description1").getBody();
//        store2 = (Long) storeSevice.createStore(user2_token, "store2", "description2").getBody();
//
//        item11 = (Long) storeSevice.addItemToStore(user1_token, store1, "item11", "description", 33.5, 3, new ArrayList<>()).getBody();
//        item12 = (Long) storeSevice.addItemToStore(user1_token, store1, "item12", "description", 30.0, 2, new ArrayList<>()).getBody();
//        item13 = (Long) storeSevice.addItemToStore(user1_token, store1, "item13", "description", 20.5, 1, new ArrayList<>()).getBody();
//        item21 = (Long) storeSevice.addItemToStore(user2_token, store2, "item21", "description", 33.5, 3, new ArrayList<>()).getBody();
//        item22 = (Long) storeSevice.addItemToStore(user2_token, store2, "item22", "description", 30.0, 2, new ArrayList<>()).getBody();
//        item23 = (Long) storeSevice.addItemToStore(user2_token, store2, "item23", "description", 20.5, 1, new ArrayList<>()).getBody();
//
//    }
//
//    @AfterEach
//    public void reset() {
//        storeSevice.updateItem(user1_token, store1, item11, "item11", 33.5, 3);
//        storeSevice.updateItem(user1_token, store1, item12, "item12", 30.0, 2);
//        storeSevice.updateItem(user1_token, store1, item13, "item13", 20.5, 1);
//        storeSevice.updateItem(user2_token, store2, item21, "item21", 33.5, 3);
//        storeSevice.updateItem(user2_token, store2, item22, "item22", 30.0, 2);
//        storeSevice.updateItem(user2_token, store2, item23, "item23", 20.5, 1);
//        purchaseFacade.clearPurchases();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        serviceFactory.clear();
//    }
//
//    @Test
//    public void test_checkoutShoppingCart_should_endPurchaseSuccessfully() throws Exception {
//        userService.addItemToBasket(user1_token, store1, item11, 2);
//        userService.addItemToBasket(user1_token, store1, item12, 1);
//        userService.addItemToBasket(user1_token, store2, item21, 2);
//        userService.addItemToBasket(user1_token, store2, item22, 1);
//        userService.addItemToBasket(user1_token, store2, item23, 1);
//        userService.addItemToBasket(user2_token, store1, item11, 1);
//
//        int threadCount = 3;
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        final boolean[] _throw = {false, false};
//
//        Future<Boolean> f1 = executor.submit(() -> {
//                ResponseEntity<String> response= userService.checkoutShoppingCart(user1_token, credit_card, expire_date, cvv, "");
//                latch.countDown();
//                return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//        Future<Boolean> f2 = executor.submit(() -> {
//            ResponseEntity<String> response= userService.checkoutShoppingCart(user2_token, credit_card, expire_date, cvv, "");
//            latch.countDown();
//            return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//
//        latch.await(5, TimeUnit.SECONDS);
//        executor.shutdown();
//
//        try{
//            if(f1.get()) _throw[0] = true;
//            if(f2.get()) _throw[1] = true;
//        }catch (Exception e){
//            System.out.println("future get failed");
//        }
//
//        assertThat(_throw[0] || _throw[1]).isEqualTo(false);
//        try {
//            assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item11)).isEqualTo(0);
//            assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item12)).isEqualTo(1);
//            assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item13)).isEqualTo(1);
//            assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item21)).isEqualTo(1);
//            assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item22)).isEqualTo(1);
//            assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item23)).isEqualTo(0);
//
//            List<Purchase> purchases= purchaseFacade.getPurchaseHistory(ADMIN_USER_NAME);
//            for(Purchase purchase: purchases){
//                List<ItemDTO> items_store1 = purchase.getItemByStore(store1);
//                List<ItemDTO> items_store2 = purchase.getItemByStore(store2);
//                for(ItemDTO item: items_store1){
//                    if (item.getItemId() == item11 && Objects.equals(purchase.getUserId(), user1)) {
//                        assertThat(item.getQuantity()).isEqualTo(2);
//                    }
//                    else {
//                        if (item.getItemId() == item11 && Objects.equals(purchase.getUserId(), user2)) {
//                            assertThat(item.getQuantity()).isEqualTo(1);
//                        } else {
//                            if (item.getItemId() == item12) {
//                                assertThat(item.getQuantity()).isEqualTo(1);
//                            } else {
//                                throw new Exception("Item should not exist");
//                            }
//                        }
//                    }
//                }
//                for(ItemDTO item: items_store2){
//                    if (item.getItemId() == item21) {
//                        assertThat(item.getQuantity()).isEqualTo(2);
//                    } else {
//                        if (item.getItemId() == item22) {
//                            assertThat(item.getQuantity()).isEqualTo(1);
//                        } else {
//                            assertThat(item.getQuantity()).isEqualTo(1);
//                        }
//                    }
//                }
//
//            }
//        }
//        catch (Exception e){ throw new Exception(e.getMessage()); }
//
//    }
//
//    @Test
//    public void test_checkoutShoppingCart_should_dropOnePurchase() throws Exception {
//        userService.addItemToBasket(user1_token, store1, item11, 2);
//        userService.addItemToBasket(user1_token, store1, item12, 1);
//        userService.addItemToBasket(user1_token, store2, item21, 2);
//        userService.addItemToBasket(user1_token, store2, item22, 1);
//        userService.addItemToBasket(user1_token, store2, item23, 1);
//        userService.addItemToBasket(user2_token, store1, item11, 2);
//
//        int threadCount = 3;
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        final boolean[] _throw = {false, false};
//
//        Future<Boolean> f1 = executor.submit(() -> {
//            ResponseEntity<String> response = userService.checkoutShoppingCart(user1_token, credit_card, expire_date, cvv, "");
//            latch.countDown();
//            return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//        Future<Boolean> f2 = executor.submit(() -> {
//            ResponseEntity<String> response = userService.checkoutShoppingCart(user2_token, credit_card, expire_date, cvv, "");
//            latch.countDown();
//            return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//
//        latch.await(5, TimeUnit.SECONDS);
//        executor.shutdown();
//
//        try {
//            if (f1.get()) _throw[0] = true;
//            if (f2.get()) _throw[1] = true;
//        } catch (Exception e) {
//            System.out.println("future get failed");
//        }
//
//        assertThat(_throw[0] ^ _throw[1]).isEqualTo(true);
//        try {
//            if (_throw[0]) {
//                assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item11)).isEqualTo(1);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item12)).isEqualTo(2);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item13)).isEqualTo(1);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item21)).isEqualTo(3);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item22)).isEqualTo(2);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item23)).isEqualTo(1);
//            } else {
//                assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item11)).isEqualTo(1);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item12)).isEqualTo(1);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user1, store1).get(item13)).isEqualTo(1);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item21)).isEqualTo(1);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item22)).isEqualTo(1);
//                assertThat(storeFacade.viewInventoryByStoreOwner(user2, store2).get(item23)).isEqualTo(0);
//            }
//            List<Purchase> purchases = purchaseFacade.getPurchaseHistory(ADMIN_USER_NAME);
//            assertThat(purchases.size()).isEqualTo(1);
//            Purchase purchase = purchases.get(0);
//            List<ItemDTO> items_store1 = purchase.getItemByStore(store1);
//            List<ItemDTO> items_store2 = purchase.getItemByStore(store2);
//            for (ItemDTO item : items_store1) {
//                if (item.getItemId() == item11) {
//                    assertThat(item.getQuantity()).isEqualTo(2);
//                } else {
//                    if (item.getItemId() == item12) {
//                        assertThat(item.getQuantity()).isEqualTo(1);
//                    } else {
//                        throw new Exception("Item should not exist");
//                    }
//                }
//            }
//            for (ItemDTO item : items_store2) {
//                if (item.getItemId() == item21) {
//                    assertThat(item.getQuantity()).isEqualTo(2);
//                } else {
//                    assertThat(item.getQuantity()).isEqualTo(1);
//                }
//            }
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }
//
//    @Test
//    public void test_addPermission_should_addBothPermissions() throws Exception {
//        String user3 = "user5";
//        userService.register(user3, password1, age1);
//        String user3_token = userService.login(user3, password1).getBody();
//
//        String user4 = "user4";
//        userService.register(user4, password1, age1);
//        String user4_token = userService.login(user4, password1).getBody();
//
//        List<String> permissions = new ArrayList<>();
//        permissions.add("ADD_ITEM");
//        permissions.add("ASSIGN_MANAGER");
//        storeSevice.assignStoreManager(user1_token, store1, user3, permissions);
//
//        storeSevice.assignStoreManager(user3_token, store1, user4, permissions);
//
//        int threadCount = 3;
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        final boolean[] _throw = {false, false};
//
//        Future<Boolean> f1 = executor.submit(() -> {
//            ResponseEntity<String> response = userService.addPermission(user1_token, user4, store1, "VIEW_INVENTORY");
//            latch.countDown();
//            return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//        Future<Boolean> f2 = executor.submit(() -> {
//            ResponseEntity<String> response = userService.addPermission(user3_token, user4, store1, "VIEW_STORE_MANAGEMENT_INFO");
//            latch.countDown();
//            return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//
//        latch.await(5, TimeUnit.SECONDS);
//        executor.shutdown();
//
//        try {
//            if (f1.get()) _throw[0] = true;
//            if (f2.get()) _throw[1] = true;
//        } catch (Exception e) {
//            System.out.println("future get failed");
//        }
//
//        assertThat(_throw[0] || _throw[1]).isEqualTo(false);
//        IUserFacade userFacade = IUserFacade.getInstance(null, null, null, null);
//        assertThat(userFacade.checkPermission(user4, store1, "VIEW_INVENTORY")).isEqualTo(true);
//        assertThat(userFacade.checkPermission(user4, store1, "VIEW_STORE_MANAGEMENT_INFO")).isEqualTo(true);
//
//    }
//
//    @Test
//    public void test_assignManager_should_addDropOneAttemptToAssign() throws Exception {
//        String user5 = "user5";
//        userService.register(user5, password1, age1);
//        String user5_token = userService.login(user5, password1).getBody();
//
//        String user6 = "user6";
//        userService.register(user6, password1, age1);
//        String user6_token = userService.login(user6, password1).getBody();
//
//        List<String> permissions = new ArrayList<>();
//        permissions.add("ADD_ITEM");
//        permissions.add("ASSIGN_MANAGER");
//
//        storeSevice.assignStoreManager(user1_token, store1, user6, permissions);
//
//        int threadCount = 3;
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        final boolean[] _throw = {false, false};
//
//        Future<Boolean> f1 = executor.submit(() -> {
//            ResponseEntity<String> response = storeSevice.assignStoreManager(user1_token, store1, user5, permissions);
//            latch.countDown();
//            return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//        Future<Boolean> f2 = executor.submit(() -> {
//            ResponseEntity<String> response = storeSevice.assignStoreManager(user6_token, store1, user5, permissions);
//            latch.countDown();
//            return response.getStatusCode().value() == 500 || response.getStatusCode().value() == 401;
//        });
//
//        latch.await(5, TimeUnit.SECONDS);
//        executor.shutdown();
//
//        try {
//            if (f1.get()) _throw[0] = true;
//            if (f2.get()) _throw[1] = true;
//        } catch (Exception e) {
//            System.out.println("future get failed");
//        }
//
//        assertThat(_throw[0] ^ _throw[1]).isEqualTo(true);
//    }
//}
