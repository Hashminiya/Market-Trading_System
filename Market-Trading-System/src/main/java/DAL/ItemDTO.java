package DAL;

import java.util.Date;

public class ItemDTO {
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

    public String getItemName() {
        return itemName;
    }

    public long getItemId() {
        return itemId;
    }
}
