package UnitTests.DomainLayer.Purchase;
import API.Utils.SpringContext;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import SetUp.ApplicationTest;
import SetUp.cleanUpDB;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApplicationTest.class)
public class OutServicesUT {

    PaymentServiceImpl paymentServiceImpl;
    SupplyServiceImpl supplyServiceImpl;

    @BeforeEach
    public void setUp() {
        paymentServiceImpl = SpringContext.getBean(PaymentServiceImpl.class);
        supplyServiceImpl = SpringContext.getBean(SupplyServiceImpl.class);
    }

    @AfterAll
    public static void tearDownAll() {
        cleanUpDB.clearDB();
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
                "982",200 );
        assertNotEquals(-1,transactionId);
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