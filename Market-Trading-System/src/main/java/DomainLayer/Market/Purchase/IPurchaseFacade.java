package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IRepository;

import java.util.Date;
import java.util.List;

public interface IPurchaseFacade {
    public static IPurchaseFacade getInstance(PaymentServiceProxy paymentServiceProxy, SupplyServiceProxy supplyServiceProxy)
    {
        return PurchaseController.getInstance(paymentServiceProxy, supplyServiceProxy);
    }
    boolean isValidServices();
    boolean checkout(String userID, String creditCard, Date expiryDate , String cvv , List<ItemDTO> purchaseItemsList);
    List<ItemDTO> getPurchasesByStore(long storeId);
    List<ItemDTO> getPurchasesByUser(String UserId);
    List<ItemDTO> getPurchasedItems();

}
