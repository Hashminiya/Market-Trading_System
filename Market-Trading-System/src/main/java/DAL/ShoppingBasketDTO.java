package DAL;

import DomainLayer.Market.Store.Item;

import java.util.Map;

public class ShoppingBasketDTO {

    private  long basketId;
    private  Map<ItemDTO, Integer> itemsAndQuantity; // map<itemId, quantity>
    private double basketTotalPrice;
    private  long storeId;

    public ShoppingBasketDTO() {
    }

    public ShoppingBasketDTO(long basketId, Map<ItemDTO, Integer> itemsAndQuantity, double basketTotalPrice, long storeId) {
        this.basketId = basketId;
        this.itemsAndQuantity = itemsAndQuantity;
        this.basketTotalPrice = basketTotalPrice;
        this.storeId = storeId;
    }

}
