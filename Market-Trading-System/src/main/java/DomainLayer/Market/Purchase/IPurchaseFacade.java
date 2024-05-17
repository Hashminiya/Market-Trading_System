package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Store.Item;

import java.util.Date;
import java.util.List;

public interface IPurchaseFacade {
    void initServices();
    boolean isValidServices();
    boolean checkout(String userID, String creditCard, Date expiryDate , String cvv , List<ItemDTO> purchaseItemsList);
    List<ItemDTO> getPurchasesByStore(long storeId);
    List<ItemDTO> getPurchasesByUser(String UserId);
    List<ItemDTO> getPurchasedItems();

}
