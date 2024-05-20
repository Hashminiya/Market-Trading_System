package DAL;

import java.util.Date;

public class ItemDTO {
    private long storeId;
    private double totalPrice;
    private int quantity;
    private String itemName;
    private long itemId;
    private long itemId;
    private Date purchaseDate;

    public long getStoreId() {
    public Date getPurchaseDate() {return purchaseDate;}

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
