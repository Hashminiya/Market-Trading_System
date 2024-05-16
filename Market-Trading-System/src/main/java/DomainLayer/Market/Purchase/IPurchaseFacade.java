package DomainLayer.Market.Purchase;

import DAL.ItemDTO;

import java.util.Date;
import java.util.List;

public interface IPurchaseFacade {
    boolean initServices();
    boolean isValidServices();
    boolean checkout(int userID, int creditCard, Date expiryDate , int cvv , List<ItemDTO> purchaseItemsList);
    List<Purchase> getPurchasesByStore(int storeId);
    List<Purchase> getPurchasesByUser(int UserId);
}
