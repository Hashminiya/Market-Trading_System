package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Repositories.PurchaseRepository;
//import DomainLayer.Market.Util.IRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IPurchaseFacade {
    public static IPurchaseFacade getInstance(PurchaseRepository purchaseRepo, PaymentServiceProxy paymentServiceProxy, SupplyServiceProxy supplyServiceProxy)
    {
        return PurchaseController.getInstance(purchaseRepo, paymentServiceProxy, supplyServiceProxy);
    }
    void checkout(String userID, String creditCard, Date expiryDate, String cvv, List<ItemDTO> purchaseItemsList,double totalAmount);
    HashMap<Long,List<ItemDTO>> getPurchasesByStore(long storeId);
    //HashMap<Long,List<ItemDTO>> getPurchasesByUser(String UserId); Not For this version
    List<ItemDTO> getPurchasedItems();
    List<Purchase> getPurchaseHistory(String userName);
    public void setUserFacade(IUserFacade userFacade);

    void clearPurchases();

    public void clear();
    public void setPaymentServiceProxy(PaymentServiceProxy paymentServiceProxy);

}
