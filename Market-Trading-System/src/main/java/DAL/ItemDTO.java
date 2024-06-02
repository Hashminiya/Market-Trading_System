package DAL;

import DomainLayer.Market.Util.DataItem;

import java.util.Date;

public class ItemDTO implements DataItem <Long>, Comparable {
    private final long storeId;
    private final double totalPrice;
    private final int quantity;
    private final String itemName;
    private final long itemId;

    public ItemDTO(long itemId, String itemName, int quantity, long storeId, double totalPrice){
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
    }

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

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
