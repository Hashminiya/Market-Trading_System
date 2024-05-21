package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.Item;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IPurchaseFacade {
    void checkout(String userID, String creditCard, Date expiryDate, String cvv, List<ItemDTO> purchaseItemsList,double totalAmount);
    HashMap<Long,List<ItemDTO>> getPurchasesByStore(long storeId);
    //HashMap<Long,List<ItemDTO>> getPurchasesByUser(String UserId); Not For this version
    List<ItemDTO> getPurchasedItems();

}
