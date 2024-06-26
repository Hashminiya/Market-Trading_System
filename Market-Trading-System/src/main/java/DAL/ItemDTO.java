package DAL;

import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class ItemDTO implements DataItem <Long>, Comparable {

    @Id
    @JsonProperty("itemId")
    private long itemId;

    @JsonProperty("itemName")
    private String itemName;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("storeId")
    private long storeId;

    @JsonProperty("totalPrice")
    private double totalPrice;


    public ItemDTO(long itemId, String itemName, int quantity, long storeId, double totalPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
    }

    public ItemDTO() {

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
