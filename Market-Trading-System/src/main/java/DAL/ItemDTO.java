package DAL;

import java.util.Date;

public class ItemDTO {
    private String storeId;
    private double totalPrice;
    private int quantity;
    private String itemName;
    private String itemId;
    private Date purchaseDate;

    public Date getPurchaseDate() {return purchaseDate;}

    public String getStoreId() {
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

    public String getItemId() {
        return itemId;
    }
}
