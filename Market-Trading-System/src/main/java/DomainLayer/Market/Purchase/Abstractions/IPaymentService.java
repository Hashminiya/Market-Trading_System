package DomainLayer.Market.Purchase.Abstractions;

import java.util.Date;

public interface IPaymentService {
    int chargeCreditCard(String cardNumber, Date expiryDate, String cvv, double amount) throws Exception;
    int cancelPayment(int transactionId) throws Exception;
}