package DomainLayer.Market.Purchase.OutServices;

import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import ServiceLayer.ServiceFactory;

import java.util.Date;

public class PaymentServiceImpl implements IPaymentService {
    private static PaymentServiceImpl instance;

    private PaymentServiceImpl(){}

    public static synchronized PaymentServiceImpl getInstance() {
        if(instance == null) {
            instance = new PaymentServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean validateCreditCard(String cardNumber, Date expiryDate, String cvv,double amount) {
        //connect with swift and check
        return true;
    }

    @Override
    public boolean chargeCreditCard(String cardNumber, Date expiryDate, String cvv, double amount) {
        //connect with swift and charge
        return true;
    }
}
