package DomainLayer.Market;

import API.SpringContext;
import DAL.BasketItem;
import DAL.BasketItemId;
import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.Istate;
import DomainLayer.Market.User.ShoppingCart;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Repositories.BasketItemRepository;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShoppingBasket implements DataItem<Long> {
    @Id
    private long basketId;

    @Transient
    private Map<Long, Integer> itemsQuantity; // map<itemId, quantity>

    @Transient
    private Map<Long, Double> itemsPrice; // map<itemId, price>


    @Transient
    private BasketItemRepository basketItemRepository;

    @Setter
    @Getter
    private double basketTotalPrice;

    @Getter
    private long storeId;

    @Getter
    @Column(name = "user_name")
    private String userName;

    public ShoppingBasket(Long storeId, String userName) {
        this.basketId = IdGenerator.generateId();
        this.storeId = storeId;
        this.itemsQuantity = new HashMap<>();
        this.itemsPrice = new HashMap<>();
        this.userName = userName;
        this.basketItemRepository =SpringContext.getBean(BasketItemRepository.class);

    }

    public ShoppingBasket() {

    }

    @PostLoad
    private void initFields() throws Exception {
        this.itemsQuantity = new HashMap<>();
        this.itemsPrice = new HashMap<>();
        this.basketItemRepository = SpringContext.getBean(BasketItemRepository.class);

        this.itemsQuantity = basketItemRepository.findAll()
                .stream()
                .filter(x -> x.getId().getBasketId() == this.basketId)
                .collect(Collectors.toMap(
                        basketItem -> basketItem.getId().getItemId(),  // Key: item ID
                        basketItem -> basketItem.getQuantity() // Value: item quantity
                ));

        IStoreFacade storeController = (IStoreFacade) SpringContext.getBean(StoreController.class);
        storeController.calculateBasketPrice(this,null);


        //load shopping cart
    }

    public void addItem(long itemId, int quantity) {
        itemsQuantity.put(itemId, itemsQuantity.getOrDefault(itemId, 0) + quantity);
        basketItemRepository.save(new BasketItem(basketId,itemId,quantity));

        // Ensure itemsPrice has a default value or is set elsewhere in your logic
    }

    public void removeItem(long itemId) {
        itemsQuantity.remove(itemId);
        itemsPrice.remove(itemId);
        basketItemRepository.deleteById(new BasketItemId(basketId,itemId));

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
            basketItemRepository.save(new BasketItem(basketId,itemId,quantity));
        }
    }

    public void setItemsPrice(Map<Long, Double> itemsPrice) {
        this.itemsPrice.putAll(itemsPrice);
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

}
