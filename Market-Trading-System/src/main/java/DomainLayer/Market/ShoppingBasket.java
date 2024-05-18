package DomainLayer.Market;

import DAL.ItemDTO;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;

import java.util.HashMap;

public class ShoppingBasket implements DataItem<Long> {

    private IRepository<Long, ItemDTO> items;
    private int storeId;

    public ShoppingBasket(IRepository iRepository) {
        items = iRepository;
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

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
