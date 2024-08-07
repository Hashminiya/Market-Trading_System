package ConcurrencyTests;
import static java.lang.Thread.sleep;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import API.Utils.SpringContext;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Store.*;
import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Store.StorePurchasePolicy.PurchasePolicy;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.ShoppingCart;
import DomainLayer.Market.User.UserController;
import DomainLayer.Repositories.StoreRepository;
import ServiceLayer.Store.StoreManagementService;
import SetUp.ApplicationTest;
import SetUp.cleanUpDB;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.*;

@SpringBootTest(classes = ApplicationTest.class)
//@SpringBootTest(classes = ApplicationTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//TODO : Liran , may cause problems..
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ShoppingCartCT {

    //    @Mock
    private static StoreRepository storesRepo;
    //    @Mock
//    private IRepository<Long, PurchasePolicy> purchasePolicies;
//    @Mock
    private static IUserFacade userFacade;
//    @Mock
//    private IPurchaseFacade purchaseFacadeMock;
//    private InMemoryRepository<Long, Store> storesRepo;

    private static String user1 = "user1";
    //private String user1_token;
    private static String user2 = "user2";


    //@InjectMocks
    private static Long store1;
    private static Long store2;
    private Long item11;
    private Long item12;
    private Long item13;
    private Long item21;
    private Long item22;
    private Long item23;
    private ShoppingCart shoppingCart1;
    private ShoppingCart shoppingCart2;


    private static IStoreFacade storeFacade;

    private boolean done = false;

    @BeforeAll
    public static void setUpAll() throws Exception {
//        MockitoAnnotations.openMocks(this);
        SpringContext.getBean(IStoreFacade.class).setUserFacade(SpringContext.getBean(IUserFacade.class));
        storeFacade = SpringContext.getBean(IStoreFacade.class);
        userFacade = SpringContext.getBean(IUserFacade.class);
        storesRepo = SpringContext.getBean(StoreRepository.class);
        StoreManagementService storeManagementService = SpringContext.getBean(StoreManagementService.class);
        userFacade.register(user1, "123", 23);
        userFacade.register(user2, "123", 23);
        userFacade.login(user1,"123" );
        userFacade.login(user2,"123");
        //when(userFacadeMock.isRegister(user2)).thenReturn(true);
        store1 = storeFacade.createStore(user1, "store1", "description1");
        store2 = storeFacade.createStore(user2, "store2", "description2");

    }

    @AfterAll
    public static void tearDown() {
        if(!cleanUpDB.clearDB()) {
//            storeFacade.clear();
        }
    }

    @BeforeEach
    public void setUp() throws Exception{
//        storesRepo = SpringContext.getBean(StoreRepository.class);
//        MockitoAnnotations.openMocks(this);
        //userFacadeMock = mock(UserController.class);
        //purchaseFacadeMock = mock(PurchaseController.class);
//        SpringContext.getBean(IStoreFacade.class).setUserFacade(SpringContext.getBean(IUserFacade.class));
//        storeFacade = SpringContext.getBean(IStoreFacade.class);
//        userFacade = SpringContext.getBean(IUserFacade.class);
        //storeFacade.setUserFacade(userFacadeMock);
        //storeFacade.setStoersRepo(storesRepo);
        //storeFacade.setPurchaseFacade(purchaseFacadeMock);

        //discounts = mock(InMemoryRepository.class);
        //purchasePolicies = mock(InMemoryRepository.class);

        if(!done){
            //when(userFacadeMock.checkPermission(user1, store1, "ADD_ITEM")).thenReturn(true);
            //when(userFacadeMock.checkPermission(user2, store2, "ADD_ITEM")).thenReturn(true);
            //when(userFacadeMock.checkPermission(user1, store1, "UPDATE_ITEM")).thenReturn(true);
            //when(userFacadeMock.checkPermission(user2, store2, "UPDATE_ITEM")).thenReturn(true);
            item11 = storeFacade.addItemToStore(user1, store1, "item11", 33.5, 3, "description", new ArrayList<>());
            item12 = storeFacade.addItemToStore(user1, store1, "item12", 30.0, 2, "description", new ArrayList<>());
            item13 = storeFacade.addItemToStore(user1, store1, "item13", 20.5, 1, "description", new ArrayList<>());
            item21 = storeFacade.addItemToStore(user2, store2, "item21", 33.5, 3, "description", new ArrayList<>());
            item22 = storeFacade.addItemToStore(user2, store2, "item22", 30.0, 2, "description", new ArrayList<>());
            item23 = storeFacade.addItemToStore(user2, store2, "item23", 20.5, 1, "description", new ArrayList<>());
            done = true;
        }

        shoppingCart1 = userFacade.getShoppingCart(user1);
        shoppingCart2 = userFacade.getShoppingCart(user2);
    }

    @AfterEach
    public void reset() throws Exception{
        storeFacade.updateItem(user1, store1, item11, "item11", 33.5, 3);
        storeFacade.updateItem(user1, store1, item12, "item12", 30.0, 2);
        storeFacade.updateItem(user1, store1, item13, "item13", 20.5, 1);
        storeFacade.updateItem(user2, store2, item21, "item21", 33.5, 3);
        storeFacade.updateItem(user2, store2, item22, "item22", 30.0, 2);
        storeFacade.updateItem(user2, store2, item23, "item23", 20.5, 1);
        storesRepo.findById(store1).get().clearCache();
        storesRepo.findById(store2).get().clearCache();
        shoppingCart1.clear();
        shoppingCart2.clear();
        sleep(1000);
    }


    @Test
    public void test_checkoutShoppingCart_should_endPurchaseSuccessfully() throws Exception {
        shoppingCart1.addItemBasket(store1, item11, 2, storeFacade, user1);
        shoppingCart1.addItemBasket(store1, item12, 1, storeFacade, user1);
        shoppingCart1.addItemBasket(store2, item21, 2, storeFacade, user1);
        shoppingCart1.addItemBasket(store2, item22, 1, storeFacade, user1);
        shoppingCart1.addItemBasket(store2, item23, 1, storeFacade, user1);
        shoppingCart2.addItemBasket(store1, item11, 1, storeFacade, user1);

        int threadCount = 3;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        //when(purchasePolicies.findAll()).thenReturn(new ArrayList<>());
        executor.submit(() -> {
            try{
                shoppingCart1.checkoutShoppingCart(user1, storeFacade, "");
            }catch (Exception e) {
            }
            finally {
                latch.countDown();
            }
        });
        executor.submit(() -> {
            try{
                shoppingCart2.checkoutShoppingCart(user2, storeFacade, "");
            }catch (Exception e) {
            }
            finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(storesRepo.findById(store1).get().getById(item11).getQuantity()).isEqualTo(0);
        assertThat(storesRepo.findById(store1).get().getById(item12).getQuantity()).isEqualTo(1);
        assertThat(storesRepo.findById(store1).get().getById(item13).getQuantity()).isEqualTo(1);
        assertThat(storesRepo.findById(store2).get().getById(item21).getQuantity()).isEqualTo(1);
        assertThat(storesRepo.findById(store2).get().getById(item22).getQuantity()).isEqualTo(1);
        assertThat(storesRepo.findById(store2).get().getById(item23).getQuantity()).isEqualTo(0);

    }

    @Test
    public void test_checkoutShoppingCart_should_abortPurchases() throws Exception {
        shoppingCart1.addItemBasket(store1, item11, 2, storeFacade, user2);
        shoppingCart1.addItemBasket(store1, item12, 1, storeFacade, user2);
        shoppingCart1.addItemBasket(store2, item21, 2, storeFacade, user2);
        shoppingCart1.addItemBasket(store2, item22, 1, storeFacade, user2);
        shoppingCart1.addItemBasket(store2, item23, 1, storeFacade, user2);
        shoppingCart2.addItemBasket(store1, item11, 2, storeFacade, user2);
        int threadCount = 2;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        //when(purchasePolicies.findAll()).thenReturn(new ArrayList<>());
        boolean[] _throw = {false, false};

        Future<Boolean> f1 = executor.submit(() -> {
            try{
                shoppingCart1.checkoutShoppingCart(user1, storeFacade, "");
                return false;
            }catch (Exception e) {
                return true;
            }
            finally {
                latch.countDown();
            }
        });
        Future<Boolean> f2 = executor.submit(() -> {
            try{
                shoppingCart2.checkoutShoppingCart(user2, storeFacade, "");
                return false;
            }catch (Exception e) {
                return true;
            }
            finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        try{
            if(f1.get()) _throw[0] = true;
            if(f2.get()) _throw[1] = true;
        }catch (Exception e){
            System.out.println("future get failed");
        }

        if(!_throw[0] && _throw[1]){
            assertThat(storesRepo.findById(store1).get().getById(item11).getQuantity()).isEqualTo(1);
            assertThat(storesRepo.findById(store1).get().getById(item12).getQuantity()).isEqualTo(1);
            assertThat(storesRepo.findById(store1).get().getById(item13).getQuantity()).isEqualTo(1);
            assertThat(storesRepo.findById(store2).get().getById(item21).getQuantity()).isEqualTo(1);
            assertThat(storesRepo.findById(store2).get().getById(item22).getQuantity()).isEqualTo(1);
            assertThat(storesRepo.findById(store2).get().getById(item23).getQuantity()).isEqualTo(0);
        }
        else {
            if (_throw[0] && !_throw[1]) {
                assertThat(storesRepo.findById(store1).get().getById(item11).getQuantity()).isEqualTo(1);
                assertThat(storesRepo.findById(store1).get().getById(item12).getQuantity()).isEqualTo(2);
                assertThat(storesRepo.findById(store1).get().getById(item13).getQuantity()).isEqualTo(1);
                assertThat(storesRepo.findById(store2).get().getById(item21).getQuantity()).isEqualTo(3);
                assertThat(storesRepo.findById(store2).get().getById(item22).getQuantity()).isEqualTo(2);
                assertThat(storesRepo.findById(store2).get().getById(item23).getQuantity()).isEqualTo(1);
            }
            else{
                System.out.println(_throw[0]);
                System.out.println(_throw[1]);
                assertThat(1).isEqualTo(3);
            }
        }
    }

}