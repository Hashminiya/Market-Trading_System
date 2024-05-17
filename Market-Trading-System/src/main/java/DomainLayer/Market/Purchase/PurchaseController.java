package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PurchaseController implements IPurchaseFacade{
    private HashMap<String,List<ItemDTO>> storeIDtoItems;
    private HashMap<String,List<ItemDTO>> userIDtoItems;
    private BlockingQueue<ItemDTO> purchasedItems;


    private PaymentServiceProxy paymentServiceProxy;
    private SupplyServiceProxy supplyServiceProxy;

    private PaymentServiceImpl paymentServiceImpl;
    private SupplyServiceImpl supplyServiceImpl;

    private boolean isPaymentServiceConnected;
    private boolean isSupplyServiceConnected;

    public PurchaseController(){
        isPaymentServiceConnected =false;
        isSupplyServiceConnected=false;

        storeIDtoItems = new HashMap<>();
        userIDtoItems=new HashMap<>();
        purchasedItems = new PriorityBlockingQueue<ItemDTO>() ; //protected queue
        initServices();
    }

    @Override
    public void initServices() {
        paymentServiceImpl=new PaymentServiceImpl();
        supplyServiceImpl=new SupplyServiceImpl();

        paymentServiceProxy = new PaymentServiceProxy(paymentServiceImpl);
        supplyServiceProxy = new SupplyServiceProxy(supplyServiceImpl);
    }

    @Override
    public boolean isValidServices() {
        return isPaymentServiceConnected & isSupplyServiceConnected;
    }

    @Override
    public boolean checkout(String userID, String creditCard, Date expiryDate, String cvv, List<ItemDTO> purchaseItemsList) {
        Purchase purchase = new Purchase(paymentServiceProxy,supplyServiceProxy);
        boolean success = purchase.checkout(purchaseItemsList,creditCard,expiryDate,cvv);
        if(!success)
            return false;
        for (ItemDTO item:purchaseItemsList) { // save all the purchased items
            if(!userIDtoItems.containsKey(userID))
                userIDtoItems.put(userID,new ArrayList<>());
            userIDtoItems.get(userID).add(item);//save item by userID

            if(!storeIDtoItems.containsKey(item.getStoreId()))
                storeIDtoItems.put(item.getStoreId(), new ArrayList<>());
            storeIDtoItems.get(item.getStoreId()).add(item);
            purchasedItems.add(item);
        }
        return true;
    }

    @Override
    public List<ItemDTO> getPurchasesByStore(String storeId) {
        if(!storeIDtoItems.containsKey(storeId))
            return null;
        return storeIDtoItems.get(storeId);
    }

    @Override
    public List<ItemDTO> getPurchasesByUser(String userId) {
        if(!userIDtoItems.containsKey(userId))
            return null;
        return userIDtoItems.get(userId);
    }

    @Override
    public List<ItemDTO> getPurchasedItems() {
        List<ItemDTO> purchasedItems = new ArrayList<>();
        synchronized (this) {
            while (this.purchasedItems.size() > 0)
                purchasedItems.add(this.purchasedItems.remove());
        }
        return purchasedItems;
    }
}

