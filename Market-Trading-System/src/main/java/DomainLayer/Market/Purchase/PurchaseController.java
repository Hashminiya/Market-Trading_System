package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PurchaseController implements IPurchaseFacade {
    private static PurchaseController purchaseControllerInstance;
    private final BlockingQueue<ItemDTO> inventoryReduceItems; // queue to remove products from inventory
    private final IRepository<Long,Purchase> purchaseRepo;

    private final PaymentServiceProxy paymentServiceProxy;
    private final SupplyServiceProxy supplyServiceProxy;
    private IUserFacade userFacade;


    private PurchaseController(IRepository<Long,Purchase> purchaseRepo,PaymentServiceProxy paymentServiceProxy, SupplyServiceProxy supplyServiceProxy) {
        this.purchaseRepo = purchaseRepo;
        this.paymentServiceProxy = paymentServiceProxy;
        this.supplyServiceProxy = supplyServiceProxy;

        inventoryReduceItems = new PriorityBlockingQueue<ItemDTO>(); //protected queue
    }

    public static synchronized PurchaseController getInstance(IRepository<Long, Purchase> purchaseRepo, PaymentServiceProxy paymentServiceProxy, SupplyServiceProxy supplyServiceProxy) {
        if (purchaseControllerInstance == null) {
            purchaseControllerInstance = new PurchaseController(purchaseRepo, paymentServiceProxy, supplyServiceProxy);
        }
        return purchaseControllerInstance;
    }

    @Override
    public void checkout(String userID, String creditCard, Date expiryDate, String cvv, List<ItemDTO> purchaseItemsList,double totalAmount) {
        if(purchaseItemsList.isEmpty())
            throw new RuntimeException("No items for checkout");

        Long purchaseId = IdGenerator.generateId();
        Purchase purchase = new Purchase(userID,totalAmount,purchaseId,purchaseItemsList,paymentServiceProxy, supplyServiceProxy);
        purchase.checkout(creditCard, expiryDate, cvv);
        purchaseRepo.save(purchase);
        inventoryReduceItems.addAll(purchaseItemsList);
    }

    @Override
    public HashMap<Long,List<ItemDTO>> getPurchasesByStore(long storeId) {
        HashMap<Long,List<ItemDTO>> toRet = new HashMap<>();
        List<Purchase> purchaseList = purchaseRepo.findAll();
        for (Purchase purchase:purchaseList){
           List<ItemDTO> itemsList = purchase.getItemByStore(storeId);
           if(!itemsList.isEmpty()) {
               toRet.put(purchase.getId(), itemsList);
           }
        }
        return toRet;
    }

    @Override
    public synchronized List<ItemDTO> getPurchasedItems() {
        List<ItemDTO> purchasedItems = new ArrayList<>();
        if (inventoryReduceItems.isEmpty())
            throw new RuntimeException("There are no purchased items waiting to reduce from inventory");
        while (!inventoryReduceItems.isEmpty())
            purchasedItems.add(inventoryReduceItems.remove());
        return purchasedItems;
    }

    @Override
    public List<Purchase> getPurchaseHistory(String userName) {
        if(userFacade.isAdmin(userName)) {
            return purchaseRepo.findAll();
        }
        else{
            throw new RuntimeException(String.format("%s unauthorized to preform this action",userName));
        }
    }

    public void setUserFacade(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }
}


