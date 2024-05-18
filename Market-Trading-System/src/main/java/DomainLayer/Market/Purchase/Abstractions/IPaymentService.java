package DomainLayer.Market.Purchase.Abstractions;

import java.util.Date;

public interface IPaymentService {
    boolean validateCreditCard(String cardNumber, Date expiryDate, String cvv);
    boolean chargeCreditCard(String cardNumber, Date expiryDate, String cvv, double amount);
}