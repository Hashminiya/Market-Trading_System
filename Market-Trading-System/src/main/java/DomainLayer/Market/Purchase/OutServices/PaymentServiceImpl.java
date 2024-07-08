package DomainLayer.Market.Purchase.OutServices;

import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.ExternalApiUtil;
import ServiceLayer.ServiceFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("PaymentServiceImpl")
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
    public int chargeCreditCard(String cardNumber, Date expiryDate, String cvv, double amount) throws Exception {
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "pay");
            params.put("amount", String.valueOf(amount));
            params.put("currency", "USD");
            params.put("card_number", cardNumber);
            params.put("month", String.valueOf(expiryDate.getMonth() + 1));
            params.put("year", String.valueOf(expiryDate.getYear() + 1900));
            params.put("holder", "Israel Israelovice");
            params.put("cvv", cvv);
            params.put("id", "20444444");

            String response = ExternalApiUtil.sendPostRequest(params);
            int transactionId;
            try {
                transactionId = Integer.parseInt(response);
            }
            catch(Exception e) {
                return -1;
            }
            return transactionId;
    }

    @Override
    public int cancelPayment(int transactionId) throws Exception {
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "cancel_pay");
            params.put("transaction_id", String.valueOf(transactionId));

            String response = ExternalApiUtil.sendPostRequest(params);
            int transactionID;
            try {
                transactionID = Integer.parseInt(response);
            }
            catch(Exception e) {
                return -1;
            }
            return transactionID;
    }
}
