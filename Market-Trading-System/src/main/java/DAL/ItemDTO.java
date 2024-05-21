package DAL;

import DomainLayer.Market.Util.DataItem;

import java.util.Date;

public class ItemDTO implements DataItem <Long>{
    private long storeId;
    private double totalPrice;
    private int quantity;
    private String itemName;
    private long itemId;

    public long getStoreId() {
        return storeId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getItemId() {
        return itemId;
    }

    @Override
    public Long getId() {
        return itemId;
    }

    @Override
    public String getName() {
        return itemName;
    }
}
