package ConcurrencyTests;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import API.Utils.SpringContext;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.ItemsCache;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.StorePurchasePolicy.PurchasePolicy;
import DomainLayer.Repositories.DiscountRepository;
import DomainLayer.Repositories.ItemRepository;
import SetUp.ApplicationTest;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@SpringBootTest(classes = ApplicationTest.class)
//@SpringBootTest(classes = ApplicationTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreCT {

    //@Mock
    //private IRepository<Long , IDiscount> discounts;
    //private  purchasePolicies;

    //@InjectMocks
    private Store store;

    private boolean done = false;


    @BeforeEach
    public void setUp() throws Exception{
        //MockitoAnnotations.openMocks(this);
        //discounts = mock(InMemoryRepository.class);
        //purchasePolicies = mock(InMemoryRepository.class);
        if(!done) {
            store = new Store(1L, "userName", "name", "descriptin", SpringContext.getBean(ItemRepository.class), SpringContext.getBean(DiscountRepository.class));
            store.addItem(123L, "item1", 33.5, 3, "description", new ArrayList<>());
            store.addItem(1234L, "item2", 30.0, 2, "description", new ArrayList<>());
            store.addItem(12345L, "item3", 20.5, 1, "description", new ArrayList<>());
            done = true;
        }

    }

    @AfterEach
    public void reset() throws Exception{
        store.updateItem(123L, "item1", 33.5, 3);
        store.updateItem(1234L, "item2", 30.0, 2);
        store.updateItem(12345L, "item3", 20.5, 1);
    }

    @Test
    public void test_updateItem_should_changeItemDetails() throws InterruptedException {
        int threadCount = 3;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        executor.submit(() -> {
            try{
                store.updateItem(123L, "updated", 23.5, 3);
            }catch (InterruptedException e) {}
            finally {
                latch.countDown();
            }
        });
        executor.submit(() -> {
            try{
                store.updateItem(123L, "updated", 23.5, 3);
            }catch (InterruptedException e) {}
            finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(store.getProductRepo().findById(123L).get().getName()).isEqualTo("updated");
        assertThat(store.getProductRepo().findById(123L).get().getPrice()).isEqualTo(23.5);
    }

    @Test
    public void test_checkValidBasket_should_returnTrueAndUpdateItems() throws InterruptedException {
        int threadCount = 2;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        //when(purchasePolicies.findAll()).thenReturn(new ArrayList<>());
        ShoppingBasket basket1 = mock(ShoppingBasket.class);
        ShoppingBasket basket2 = mock(ShoppingBasket.class);
        when(basket1.getId()).thenReturn(111L);
        when(basket2.getId()).thenReturn(222L);
        Map<Long, Integer> items1 = new HashMap<>();
        items1.put(123L, 2);
        items1.put(1234L, 1);
        Map<Long, Integer> items2 = new HashMap<>();
        items2.put(123L, 1);
        items2.put(1234L, 1);
        items2.put(12345L, 1);
        when(basket1.getItems()).thenReturn(items1);
        when(basket2.getItems()).thenReturn(items2);
        final boolean[] success = {true, true};

        Future<Boolean> f1 = executor.submit(() -> {
            boolean b = store.checkValidBasket(basket1, "");
            latch.countDown();
            return b;
        });
        Future<Boolean> f2 = executor.submit(() -> {
            boolean b = store.checkValidBasket(basket2, "");
            latch.countDown();
            return b;
        });

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        try {
            if (!f1.get()) success[0] = false;
            if (!f2.get()) success[1] = false;
        } catch (Exception e) {
            System.out.println("future get failed");
        }
        assertThat(success[0] && success[1]).isEqualTo(true);
        assertThat(store.getProductRepo().findById(123L).get().getQuantity()).isEqualTo(0);
        assertThat(store.getProductRepo().findById(1234L).get().getQuantity()).isEqualTo(0);
        assertThat(store.getProductRepo().findById(12345L).get().getQuantity()).isEqualTo(0);
    }

    @Test
    public void test_checkValidBasket_should_maintainItemsQuantityThroughoutCaches() throws InterruptedException {
        int threadCount = 2;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        //when(purchasePolicies.findAll()).thenReturn(new ArrayList<>());
        ShoppingBasket basket1 = mock(ShoppingBasket.class);
        ShoppingBasket basket2 = mock(ShoppingBasket.class);
        when(basket1.getId()).thenReturn(111L);
        when(basket2.getId()).thenReturn(222L);
        Map<Long, Integer> items1 = new HashMap<>();
        items1.put(123L, 3);
        items1.put(1234L, 1);
        Map<Long, Integer> items2 = new HashMap<>();
        items2.put(123L, 1);
        items2.put(1234L, 1);
        items2.put(12345L, 1);
        when(basket1.getItems()).thenReturn(items1);
        when(basket2.getItems()).thenReturn(items2);

        executor.submit(() -> {
            try{
                store.checkValidBasket(basket1, "");
            }catch (InterruptedException e) {}
            finally {
                latch.countDown();
            }
        });
        executor.submit(() -> {
            try{
                store.checkValidBasket(basket2, "");
            }catch (InterruptedException e) {}
            finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        int item1CurrentQuantity = store.getProductRepo().findById(123L).get().getQuantity();
        int item1Basket1Cache = store.getCache().containsKey(111L) && store.getCache().get(111L).isItemExist(123L) ? store.getCache().get(111L).getQuantity(123L) : 0;
        int item1Basket2Cache = store.getCache().containsKey(222L) && store.getCache().get(222L).isItemExist(123L) ? store.getCache().get(222L).getQuantity(123L) : 0;
        assertThat(item1CurrentQuantity + item1Basket1Cache + item1Basket2Cache).isEqualTo(3);

        int item2CurrentQuantity = store.getProductRepo().findById(1234L).get().getQuantity();
        int item2Basket1Cache = store.getCache().containsKey(111L) && store.getCache().get(111L).isItemExist(1234L) ? store.getCache().get(111L).getQuantity(1234L) : 0;
        int item2Basket2Cache = store.getCache().containsKey(222L) && store.getCache().get(222L).isItemExist(1234L) ? store.getCache().get(222L).getQuantity(1234L) : 0;
        assertThat(item2CurrentQuantity + item2Basket1Cache + item2Basket2Cache).isEqualTo(2);

        int item3CurrentQuantity = store.getProductRepo().findById(12345L).get().getQuantity();
        int item3Basket1Cache = store.getCache().containsKey(111L) && store.getCache().get(111L).isItemExist(12345L) ? store.getCache().get(111L).getQuantity(12345L) : 0;
        int item3Basket2Cache = store.getCache().containsKey(222L) && store.getCache().get(222L).isItemExist(12345L) ? store.getCache().get(222L).getQuantity(12345L) : 0;
        assertThat(item3CurrentQuantity + item3Basket1Cache + item3Basket2Cache).isEqualTo(1);

        if(store.getCache().containsKey(111L))
            store.clearCache(111L);
        if(store.getCache().containsKey(222L))
            store.clearCache(222L);
    }

}
