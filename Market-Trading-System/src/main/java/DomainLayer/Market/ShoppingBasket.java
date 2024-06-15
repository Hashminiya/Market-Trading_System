package DomainLayer.Market;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingBasket implements DataItem<Long> {

    private final long basketId;
    private final Map<Long,Integer> itemsQuantity;     //map<itemId,quantity>
    private Map<Long, Double> itemsPrice;        //map<itemId, price>
    private double basketTotalPrice;
    private final long storeId;

    public ShoppingBasket(Long storeId){
        this.basketId = IdGenerator.generateId();
        this.storeId = storeId;
        this.itemsQuantity = new HashMap<>();
        this.itemsPrice = new HashMap<>();
    }

    public void addItem(long itemId, int quantity){
        if(itemsQuantity.containsKey(itemId)){
            itemsQuantity.replace(itemId, itemsQuantity.get(itemId) + quantity);
        }
        itemsQuantity.put(itemId,quantity);
    }

    public void removeItem(long itemId){
        itemsQuantity.remove(itemId);
    }

    public List<ItemDTO> checkoutShoppingBasket(IStoreFacade storeFacade){
        List<ItemDTO> items = new ArrayList<>();
        Map<Long,String> itemsNames = storeFacade.getAllProductsInfoByStore(storeId);
        for(Map.Entry<Long,Integer> entry: itemsQuantity.entrySet()){
            ItemDTO item = new ItemDTO(entry.getKey(),itemsNames.get(entry.getKey()), entry.getValue(), storeId,itemsPrice.get(entry.getKey()));
            items.add(item);
        }
        return items;
    }

    public Map<Long, Integer> getItems(){
        return itemsQuantity;
    }

    public void updateItemQuantity(long itemId, int quantity){
        if(quantity == 0)
            removeItem(itemId);
        else {
            //TODO: check stock?
            itemsQuantity.put(itemId, quantity);
        }
    }

    public void setItemsPrice(Map<Long, Double> itemsPrice){
        this.itemsPrice = itemsPrice;
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
            Double price = itemsPrice.get(itemId);
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
