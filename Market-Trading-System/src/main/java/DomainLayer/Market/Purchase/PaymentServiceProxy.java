package DomainLayer.Market.Purchase;

import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;

public class PaymentServiceProxy implements IPaymentService {
    private PaymentServiceImpl paymentService;

    public PaymentServiceProxy(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public boolean validateCreditCard(String cardNumber, String expiryDate, String cvv, double amount) {
        return paymentService.validateCreditCard(cardNumber, expiryDate, cvv, amount);
    }

    @Override
    public boolean chargeCreditCard(String cardNumber, String expiryDate, String cvv, double amount) {
        return paymentService.chargeCreditCard(cardNumber, expiryDate, cvv, amount);
    }
}
