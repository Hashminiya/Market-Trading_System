package DAL;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ShoppingCartDTO {

    @JsonProperty("baskets")
    private List<ShoppingBasketDTO> baskets;

    @JsonProperty("totalPrice")
    private double totalPrice;

    public ShoppingCartDTO(List<ShoppingBasketDTO> baskets, double totalPrice) {
        this.baskets = baskets;
        this.totalPrice = totalPrice;
    }

    public List<ShoppingBasketDTO> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<ShoppingBasketDTO> baskets) {
        this.baskets = baskets;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
