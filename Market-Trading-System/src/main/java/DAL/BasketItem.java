package DAL;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Cacheable
@Entity
@Table(name = "basket_items")
public class BasketItem {

    @EmbeddedId
    private BasketItemId id;

    private int quantity;

    public BasketItem() {}

    public BasketItem(Long basketId, Long itemId, int quantity) {
        this.id = new BasketItemId(basketId, itemId);
        this.quantity = quantity;
    }

    // Getters and setters

}


