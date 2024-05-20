package DomainLayer.Market.Purchase;

import DAL.ItemDTO;

import java.util.Date;
import java.util.List;

public interface IPurchase {
    boolean checkout(List<ItemDTO> itemList, String creditCard, Date expiryDate, String CVV);
}
