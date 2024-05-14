package DomainLayer.Market.Purchase.Abstractions;

public interface IPaymentService {
    boolean validateCreditCard(String cardNumber, String expiryDate, String cvv);
    boolean chargeCreditCard(String cardNumber, String expiryDate, String cvv, double amount);
}