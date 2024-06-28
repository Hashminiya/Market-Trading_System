package DAL;

import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.util.List;

@Entity
public class ItemDTO implements DataItem<Long>, Comparable<ItemDTO> {

    @Getter
    @Id
    @JsonProperty("itemId")
    private long itemId;

    @JsonProperty("itemName")
    private String itemName;

    @Getter
    @JsonProperty("quantity")
    private int quantity;

    @Getter
    @JsonProperty("storeId")
    private long storeId;

    @Getter
    @JsonProperty("totalPrice")
    private double totalPrice;

    @Getter
    @Transient
    @JsonProperty("categories")
    private List<String> categories;

    @Getter
    @JsonProperty("description")
    private String description;

    public ItemDTO(long itemId, String itemName, int quantity, long storeId, double totalPrice, List<String> categories, String description) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
        this.categories = categories;
        this.description = description;
    }

    public ItemDTO() {

    }

    public Long getId() {
        return itemId;
    }

    public String getName() {
        return itemName;
    }

    @Override
    public int compareTo(ItemDTO o) {
        return Long.compare(this.itemId, o.getItemId());
    }
}
