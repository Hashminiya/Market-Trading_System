package DAL;

import jakarta.persistence.*;

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

    public BasketItemId getId() {
        return id;
    }

    public void setId(BasketItemId id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


