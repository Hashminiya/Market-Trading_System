package UnitTests.DomainLayer.Purchase;
import API.Utils.SpringContext;
import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Repositories.PurchaseRepository;
import SetUp.ApplicationTest;
import SetUp.cleanUpDB;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ApplicationTest.class)
public class OutServicesUT {

    PaymentServiceImpl paymentServiceImpl;
    SupplyServiceImpl supplyServiceImpl;

    @BeforeEach
    public void setUp() {
        paymentServiceImpl = SpringContext.getBean(PaymentServiceImpl.class);
        supplyServiceImpl = SpringContext.getBean(SupplyServiceImpl.class);
    }

    @AfterEach
    void tearDown()throws Exception{
        resetPurchaseControllerInstance();
    }

    @AfterAll
    public static void tearDownAll() {
        cleanUpDB.clearDB();
    }

    private void resetPurchaseControllerInstance() throws Exception {
        Field instance = PurchaseController.class.getDeclaredField("purchaseControllerInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @Order(1)
    void test_charge_credit_card() throws Exception {
        assertNotEquals(-1,paymentServiceImpl.chargeCreditCard( "1234567890123456",
                new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000),
                "982",200 ));
    }

    @Test
    @Order(1)
    void test_cannot_charge_credit_card() throws Exception {
        assertEquals(-1,paymentServiceImpl.chargeCreditCard( "1234567890123456",
                new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000),
                "984",200 ));
    }

    @Test
    @Order(3)
    void test_cancel_transaction() throws Exception {
        int transactionId = paymentServiceImpl.chargeCreditCard( "1234567890123456",
                new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000),
                "123",200 );
        assertNotEquals(-1, paymentServiceImpl.cancelPayment(transactionId));
    }

    @Test
    @Order(4)
    void test_supply_cart() throws Exception {
        int transactionId = supplyServiceImpl.performCartSupply();
        assertNotEquals(-1,transactionId);
    }

    @Test
    @Order(5)
    void test_cancel_supply_cart() throws Exception {
        int transactionId = supplyServiceImpl.performCartSupply();
        assertNotEquals(-1, supplyServiceImpl.cancelCartSupply(transactionId));
    }
}
