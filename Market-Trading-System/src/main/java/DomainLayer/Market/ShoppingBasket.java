package DomainLayer.Market;

import DomainLayer.Market.Store.Item;

import java.util.HashMap;

public class ShoppingBasket {

    private HashMap<Integer,Integer> items;
    private long id;
    private int storeId;

    public ShoppingBasket(){
        items = new HashMap<>();
    }

    public int getStoreId() {
        return storeId;
    }

    public long getId() {
        return id;
    }

    public void addItem(int itemId, int quantity){
        items.put(itemId,quantity);
    }

    public void removeItem(int itemId){
        items.remove(itemId);
    }

    public void purchaseShoppingBasket(){
        //TODO: implement
    }

    public void updateItemQuantity(int itemId, int quantity){
        if(quantity == 0)
            removeItem(itemId);
        //TODO: check stock?
        items.put(itemId,quantity);
    }

    @Override
    public String toString() {
        return super.toString(); //TODO: overwrite to return the shopping basket items
    }
}
