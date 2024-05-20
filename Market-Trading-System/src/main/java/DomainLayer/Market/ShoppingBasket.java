package DomainLayer.Market;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;

import java.util.HashMap;

public class ShoppingBasket implements DataItem<Long> {

    private long id;
    private HashMap<Long,Integer> itemsQuantity;     //map<itemId,quantity>
    private HashMap<Long, Double> itemsPrice;
    private double price;
    private long storeId;

    public ShoppingBasket(long id, Long storeId){
        this.storeId = storeId;
        this.itemsQuantity = new HashMap<>();
        this.itemsPrice = new HashMap<>();
    }

    public void addItem(long itemId, int quantity){
        itemsQuantity.put(itemId,quantity);
    }

    public void removeItem(long itemId){
        itemsQuantity.remove(itemId);
    }

    public void purchaseShoppingBasket(){
        //TODO: implement
    }

    public HashMap<Long, Integer> getItems(){
        return itemsQuantity;
    }

    public void updateItemQuantity(long itemId, int quantity){
        if(quantity == 0)
            removeItem(itemId);
        //TODO: check stock?
        itemsQuantity.put(itemId,quantity);
    }

    public void setItemsPrice(HashMap<Long, Double> itemsPrice){
        this.itemsPrice = itemsPrice;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return super.toString(); //TODO: overwrite to return the shopping basket items
    }

    @Override
    public Long getId() {
        return storeId;
    }

    @Override
    public String getName() {
        return "";
    }
}
