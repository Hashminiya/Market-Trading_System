package DomainLayer.Market.Purchase.OutServices;

import DomainLayer.Market.Purchase.Abstractions.IPaymentService;

public class PaymentServiceImpl implements IPaymentService {
    @Override
    public boolean validateCreditCard(String cardNumber, String expiryDate, String cvv,double amount) {
        //connect with swift and check
        return true;
    }

    @Override
    public boolean chargeCreditCard(String cardNumber, String expiryDate, String cvv, double amount) {
        //connect with swift and charge
        return true;
    }
}
