package DAL;

import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ItemDTO implements DataItem <Long>, Comparable {
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


    public ItemDTO(long itemId, String itemName, int quantity, long storeId, double totalPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
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


    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
