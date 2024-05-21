package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IRepository;

import java.util.Date;
import java.util.List;

public interface IPurchaseFacade {
    public static IPurchaseFacade getInstance(IRepository<Long, Purchase> purchaseRepo, PaymentServiceProxy paymentServiceProxy, PaymentServiceImpl paymentService,
                                              SupplyServiceProxy supplyServiceProxy, SupplyServiceImpl supplyService)
    {
        return PurchaseController.getInstance(purchaseRepo, paymentServiceProxy, paymentService, supplyServiceProxy, supplyService);
    }
    boolean isValidServices();
    boolean checkout(String userID, String creditCard, Date expiryDate , String cvv , List<ItemDTO> purchaseItemsList);
    List<ItemDTO> getPurchasesByStore(long storeId);
    List<ItemDTO> getPurchasesByUser(String UserId);
    List<ItemDTO> getPurchasedItems();

}
