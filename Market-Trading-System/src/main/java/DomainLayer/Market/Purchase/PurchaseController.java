package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.User.IUserFacade;
//import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Component("purchaseController")
public class PurchaseController implements IPurchaseFacade {
    private static PurchaseController purchaseControllerInstance;
    private PurchaseRepository purchaseRepo;
    private PaymentServiceProxy paymentServiceProxy;
    private SupplyServiceProxy supplyServiceProxy;
    private IUserFacade userFacade;

    @Autowired
    public PurchaseController(PurchaseRepository purchaseRepo, PaymentServiceProxy paymentServiceProxy, SupplyServiceProxy supplyServiceProxy) {
        this.purchaseRepo = purchaseRepo;
        this.paymentServiceProxy = paymentServiceProxy;
        this.supplyServiceProxy = supplyServiceProxy;

    }

    public static synchronized PurchaseController getInstance(PurchaseRepository purchaseRepo, PaymentServiceProxy paymentServiceProxy, SupplyServiceProxy supplyServiceProxy) {
        if (purchaseControllerInstance == null) {
            purchaseControllerInstance = new PurchaseController(purchaseRepo, paymentServiceProxy, supplyServiceProxy);
        }
        return purchaseControllerInstance;
    }

    @Override
    public void checkout(String userID, String creditCard, Date expiryDate, String cvv, List<ItemDTO> purchaseItemsList,double totalAmount) throws Exception {
        if(purchaseItemsList.isEmpty())
            throw new RuntimeException("No items for checkout");

        Long purchaseId = IdGenerator.generateId();
        Purchase purchase = new Purchase(userID,totalAmount,purchaseId,purchaseItemsList,paymentServiceProxy, supplyServiceProxy);
        purchase.checkout(creditCard, expiryDate, cvv);
        purchaseRepo.save(purchase);
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
    public List<Purchase> getPurchaseHistory(String userName) {
       return purchaseRepo.findAll();
    }

    public void setUserFacade(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void setPaymentServiceProxy(PaymentServiceProxy paymentServiceProxy) {
        this.paymentServiceProxy = paymentServiceProxy;
    }
    public void setSupplyServiceProxy(SupplyServiceProxy supplyServiceProxy) {this.supplyServiceProxy = supplyServiceProxy;}

    public void setPurchaseRepo(PurchaseRepository purchaseRepo) {
        this.purchaseRepo = purchaseRepo;
    }

    @Override
    public void clearPurchases(){
        List<Purchase> purchases = purchaseRepo.findAll();
        for(Purchase purchase: purchases)
            purchaseRepo.delete(purchase);
    }

    public void clear(){
        purchaseControllerInstance = null;
        purchaseRepo = null;

        paymentServiceProxy = null;
        supplyServiceProxy = null;
        userFacade = null;
    }
}


