package DAL;

import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.User.ShoppingCart;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartDTO {
    private List<ItemDTO> items;
    private double totalPrice;

    public ShoppingCartDTO() {
    }

    public ShoppingCartDTO(List<ItemDTO> items, double totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }

    // Constructor to initialize from a ShoppingCart entity with storeFacade
    public ShoppingCartDTO(ShoppingCart shoppingCart, IStoreFacade storeFacade) {
        this.items = shoppingCart.getBaskets().stream()
                .flatMap(basket -> basket.checkoutShoppingBasket(storeFacade).stream())
                .collect(Collectors.toList());
        this.totalPrice = shoppingCart.getShoppingCartPrice();
    }

    // Getters and Setters
    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
