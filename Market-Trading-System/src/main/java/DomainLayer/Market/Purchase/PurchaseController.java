package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PurchaseController implements IPurchaseFacade {
    private static PurchaseController purchaseControllerInstance;
    private HashMap<Long, List<ItemDTO>> storeIDtoItems;
    private HashMap<String, List<ItemDTO>> userIDtoItems;
    private BlockingQueue<ItemDTO> purchasedItems;

    private PaymentServiceProxy paymentServiceProxyInstance;
    private PaymentServiceImpl paymentServiceInstance;
    private SupplyServiceProxy supplyServiceProxyInstance;
    private SupplyServiceImpl supplyServiceInstance;
    private boolean isPaymentServiceConnected;
    private boolean isSupplyServiceConnected;


    private PurchaseController(IRepository<Long, Purchase> purchaseRepo, PaymentServiceProxy paymentServiceProxy, PaymentServiceImpl paymentService, SupplyServiceProxy supplyServiceProxy, SupplyServiceImpl supplyService) {
        isPaymentServiceConnected = false;
        isSupplyServiceConnected = false;

        storeIDtoItems = new HashMap<>();
        userIDtoItems = new HashMap<>();
        purchasedItems = new PriorityBlockingQueue<ItemDTO>(); //protected queue
        paymentServiceProxyInstance = paymentServiceProxy;
        supplyServiceProxyInstance = supplyServiceProxy;
        paymentServiceInstance = paymentService;
        supplyServiceInstance = supplyService;
        // TODO : Yagil
        // --- = purchaseRepo;

    }

    public static PurchaseController getInstance(IRepository<Long, Purchase> purchaseRepo, PaymentServiceProxy paymentServiceProxy, PaymentServiceImpl paymentService,
                                                 SupplyServiceProxy supplyServiceProxy, SupplyServiceImpl supplyService) {
        if (purchaseControllerInstance == null) {
            purchaseControllerInstance = new PurchaseController(purchaseRepo, paymentServiceProxy, paymentService, supplyServiceProxy, supplyService);
        }
        return purchaseControllerInstance;
    }

    @Override
    public boolean isValidServices() {
        return isPaymentServiceConnected & isSupplyServiceConnected;
    }

    @Override
    public boolean checkout(String userID, String creditCard, Date expiryDate, String cvv, List<ItemDTO> purchaseItemsList) {
        if(purchaseItemsList.size()==0)
            throw new RuntimeException("No items for checkout");
        Purchase purchase = new Purchase(paymentServiceProxyInstance, paymentServiceProxyInstance);
        boolean success = purchase.checkout(purchaseItemsList, creditCard, expiryDate, cvv);
        if (!success)
            throw new RuntimeException("Checkout Failed");
        for (ItemDTO item : purchaseItemsList) { // save all the purchased items
            if (!userIDtoItems.containsKey(userID))
                userIDtoItems.put(userID, new ArrayList<>());
            userIDtoItems.get(userID).add(item);//save item by userID

            if (!storeIDtoItems.containsKey(item.getStoreId()))
                storeIDtoItems.put(item.getStoreId(), new ArrayList<>());
            storeIDtoItems.get(item.getStoreId()).add(item);//save item by storeID
            purchasedItems.add(item);
        }
        return true;
    }

    @Override
    public List<ItemDTO> getPurchasesByStore(long storeId) {
        if (!storeIDtoItems.containsKey(storeId))
            throw new RuntimeException(String.format("In store: %d no product has been bought", storeId));
        return storeIDtoItems.get(storeId);
    }

    @Override
    public List<ItemDTO> getPurchasesByUser(String userId) {
        if (!userIDtoItems.containsKey(userId))
            throw new RuntimeException(String.format("user: %s did not buy anything", userId));
        return userIDtoItems.get(userId);
    }

    @Override
    public synchronized List<ItemDTO> getPurchasedItems() {
        List<ItemDTO> purchasedItems = new ArrayList<>();
        if (this.purchasedItems.size() == 0)
            throw new RuntimeException("There are no purchased items waiting to reduce from inventory");
        while (this.purchasedItems.size() > 0)
            purchasedItems.add(this.purchasedItems.remove());
        return purchasedItems;
    }
}




