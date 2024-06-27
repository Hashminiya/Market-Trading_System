package DomainLayer.Market;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IdGenerator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class ShoppingBasket implements DataItem<Long> {

    @Id
    private long basketId;
//    @ElementCollection
//    @CollectionTable(name = "basket_items_quantity", joinColumns = @JoinColumn(name = "basket_id"))
//    @MapKeyColumn(name = "item_id")
//    @Column(name = "quantity")
    @Transient
    private Map<Long, Integer> itemsQuantity; // map<itemId, quantity>
    @Transient
    private Map<Long, Double> itemsPrice; // map<itemId, price>
    private double basketTotalPrice;
    private long storeId;
    @Column(name = "user_name")
    private String userName;

    public ShoppingBasket(Long storeId, String userName) {
        this.basketId = IdGenerator.generateId();
        this.storeId = storeId;
        this.itemsQuantity = new HashMap<>();
        this.itemsPrice = new HashMap<>();
        this.userName = userName;
    }

    public ShoppingBasket() {

    }

    public void addItem(long itemId, int quantity) {
        itemsQuantity.put(itemId, itemsQuantity.getOrDefault(itemId, 0) + quantity);
        // Ensure itemsPrice has a default value or is set elsewhere in your logic
    }

    public void removeItem(long itemId) {
        itemsQuantity.remove(itemId);
        itemsPrice.remove(itemId);
    }

    public List<ItemDTO> checkoutShoppingBasket(IStoreFacade storeFacade) {
        List<ItemDTO> items = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : itemsQuantity.entrySet()) {
            Long itemId = entry.getKey();
            Item item = storeFacade.getItem(itemId);
            items.add(new ItemDTO(itemId,
                    item.getName(),
                    entry.getValue(),
                    storeId,
                    itemsPrice.getOrDefault(itemId, 0.0),
                    item.getCategories(),
                    item.getDescription()));
        }
        return items;
    }

    public Map<Long, Integer> getItems() {
        return itemsQuantity;
    }

    public Map<Long, Double> getItemsPrice() {
        return itemsPrice;
    }

    public void updateItemQuantity(long itemId, int quantity) {
        if (quantity == 0) {
            removeItem(itemId);
        } else {
            itemsQuantity.put(itemId, quantity);
        }
    }

    public void setItemsPrice(Map<Long, Double> itemsPrice) {
        this.itemsPrice.putAll(itemsPrice);
    }

    public void setBasketTotalPrice(double basketTotalPrice) {
        this.basketTotalPrice = basketTotalPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shopping Basket ID: ").append(basketId).append("\n")
                .append("Store ID: ").append(storeId).append("\n")
                .append("Items:\n");

        sb.append(String.format("%-10s%-10s%-10s%-10s\n", "Item ID", "Quantity", "Price", "Total"));
        for (Map.Entry<Long, Integer> entry : itemsQuantity.entrySet()) {
            Long itemId = entry.getKey();
            Integer quantity = entry.getValue();
            Double price = itemsPrice.getOrDefault(itemId, 0.0); // Handle missing price
            Double total = price * quantity;
            sb.append(String.format("%-10d%-10d%-10.2f%-10.2f\n", itemId, quantity, price, total));
        }

        sb.append("Total Basket Price: ").append(String.format("%.2f", basketTotalPrice)).append("\n");

        return sb.toString();
    }

    @Override
    public Long getId() {
        return basketId;
    }

    @Override
    public String getName() {
        return "";
    }

    public double getBasketTotalPrice() {
        return basketTotalPrice;
    }

    public long getStoreId() {
        return storeId;
    }
}
