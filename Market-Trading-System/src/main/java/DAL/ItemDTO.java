package DAL;

public class ItemDTO {
    private int storeId;
    private int totalPrice;
    private int quantity;
    private int itemName;
    private int itemId;

    public int getStoreId() {
        return storeId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getItemName() {
        return itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
