package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("PaymentServiceProxy")
public class PaymentServiceProxy implements IPaymentService {
    private static PaymentServiceProxy instance;
    private PaymentServiceImpl paymentService;

    public static synchronized PaymentServiceProxy getInstance(PaymentServiceImpl paymentService) {
        if(instance == null) {
            instance = new PaymentServiceProxy(paymentService);
        }
        return instance;
    }

    @Autowired
    private PaymentServiceProxy(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public int chargeCreditCard(String cardNumber, Date expiryDate, String cvv, double amount) throws Exception {
        return paymentService.chargeCreditCard(cardNumber, expiryDate, cvv, amount);
    }

    @Override
    public int cancelPayment(int transactionId) throws Exception {
        return paymentService.cancelPayment(transactionId);
    }
}
