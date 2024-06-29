package DAL;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BasketItemId implements Serializable {

    @Getter
    private Long basketId;
    @Getter
    private Long itemId;

    public BasketItemId() {}

    public BasketItemId(Long basketId, Long itemId) {
        this.basketId = basketId;
        this.itemId = itemId;
    }

    // Getters, setters, equals, and hashCode methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketItemId that = (BasketItemId) o;
        return Objects.equals(basketId, that.basketId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basketId, itemId);
    }
}

