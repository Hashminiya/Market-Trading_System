package DomainLayer.Market.Purchase.OutServices;

import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.ExternalApiUtil;
import DomainLayer.Market.Purchase.SupplyServiceProxy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("SupplyServiceImpl")
public class SupplyServiceImpl implements ISupplyService {
    private static SupplyServiceImpl instance;

    private SupplyServiceImpl() {}

    public static synchronized SupplyServiceImpl getInstance() {
        if(instance == null) {
            instance = new SupplyServiceImpl();
        }
        return instance;
    }

    @Override
    public int performCartSupply() throws Exception {
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "supply");
            params.put("name", "Israel Israelovice");
            params.put("address", "Rager Blvd 12");
            params.put("city", "Beer Sheva");
            params.put("country", "Israel");
            params.put("zip", "8458527");

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
    public int cancelCartSupply(int transactionId) throws Exception {
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "cancel_supply");
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
