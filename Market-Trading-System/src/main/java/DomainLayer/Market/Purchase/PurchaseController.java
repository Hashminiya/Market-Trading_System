package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PurchaseController implements IPurchaseFacade {
    private static PurchaseController purchaseControllerInstance;
    private BlockingQueue<ItemDTO> inventoryReduceItems; // queue to remove products from inventory

    private IRepository<Long,Purchase> purchaseRepo;

    private PaymentServiceProxy paymentServiceProxy;
    private SupplyServiceProxy supplyServiceProxy;

    private PaymentServiceImpl paymentServiceImpl;
    private SupplyServiceImpl supplyServiceImpl;


    public PurchaseController(IRepository<Long,Purchase> purchaseRepo,PaymentServiceProxy paymentServiceProxy,PaymentServiceImpl paymentServiceImpl,
                              SupplyServiceProxy supplyServiceProxy,SupplyServiceImpl supplyServiceImpl) {

        this.purchaseRepo=purchaseRepo;

        this.paymentServiceProxy=paymentServiceProxy;
        this.paymentServiceImpl=paymentServiceImpl;
        this.supplyServiceProxy=supplyServiceProxy;
        this.supplyServiceImpl=supplyServiceImpl;

        inventoryReduceItems = new PriorityBlockingQueue<ItemDTO>(); //protected queue
        }

    @Override
    public void checkout(String userID, String creditCard, Date expiryDate, String cvv, List<ItemDTO> purchaseItemsList,double totalAmount) {
        if(purchaseItemsList.size()==0)
            throw new RuntimeException("No items for checkout");

        Long purchaseId = IdGenerator.generateId();
        Purchase purchase = new Purchase(userID,totalAmount,purchaseId,purchaseItemsList,paymentServiceProxy, supplyServiceProxy);
        purchase.checkout(creditCard, expiryDate, cvv);
        purchaseRepo.save(purchase);
        for (ItemDTO item: purchaseItemsList){
            inventoryReduceItems.add(item);
        }
    }

    @Override
    public HashMap<Long,List<ItemDTO>> getPurchasesByStore(long storeId) {
        HashMap<Long,List<ItemDTO>> toRet = new HashMap<>();
        List<Purchase> purchaseList = purchaseRepo.findAll();
        for (Purchase purchase:purchaseList){
           List<ItemDTO> itemsList = purchase.getItemByStore(storeId);
           if(itemsList.size()>0) {
               toRet.put(purchase.getId(), itemsList);
           }
        }
        return toRet;
    }

    @Override
    public synchronized List<ItemDTO> getPurchasedItems() {
        List<ItemDTO> purchasedItems = new ArrayList<>();
        if (inventoryReduceItems.size() == 0)
            throw new RuntimeException("There are no purchased items waiting to reduce from inventory");
        while (inventoryReduceItems.size() > 0)
            purchasedItems.add(inventoryReduceItems.remove());
        return purchasedItems;
    }
}


