package DomainLayer.Market.Purchase;

import DAL.ItemDTO;

import java.util.Date;
import java.util.List;

public interface IPurchase {
    void checkout(String creditCard, Date expiryDate, String CVV);
}
