package DAL;

import DomainLayer.Market.User.ShoppingCart;

import java.util.ArrayList;
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

    // Constructor to initialize from a ShoppingCart entity
    public ShoppingCartDTO(ShoppingCart shoppingCart) {
        this.items = shoppingCart.getBaskets().stream()
                .flatMap(basket -> basket.getItems().entrySet().stream()
                        .map(entry -> {
                            Double price = basket.getItemsPrice().get(entry.getKey());
                            return new ItemDTO(entry.getKey(),
                                    "ItemName", // Replace with actual item name if available
                                    entry.getValue(),
                                    basket.getStoreId(),
                                    price != null ? price : 0.0); // Handle null price

                        }))
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
