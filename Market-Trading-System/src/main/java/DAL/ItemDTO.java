package DAL;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ItemDTO implements DataItem<Long>, Comparable<ItemDTO> {
    @JsonProperty("itemId")
    private final long itemId;

    @JsonProperty("itemName")
    private final String itemName;

    @JsonProperty("quantity")
    private final int quantity;

    @JsonProperty("storeId")
    private final long storeId;

    @JsonProperty("totalPrice")
    private final double totalPrice;

    @JsonProperty("categories")
    private final List<String> categories;

    @JsonProperty("description")
    private final String description;

    public ItemDTO(long itemId, String itemName, int quantity, long storeId, double totalPrice, List<String> categories, String description) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
        this.categories = categories;
        this.description = description;
    }

    public ItemDTO(Item item, long storeId){
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.quantity = item.getQuantity();
        this.storeId = storeId;
        this.totalPrice = item.getPrice();
        this.categories = item.getCategories();
        this.description = item.getDescription();
    }

    public Long getId() {
        return itemId;
    }

    public long getItemId() {
        return itemId;
    }

    public String getName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getStoreId() {
        return storeId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(ItemDTO o) {
        return Long.compare(this.itemId, o.getItemId());
    }
}
