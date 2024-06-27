package DomainLayer.Market.Store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ItemsCache {

    private Long basketId;
    private Map<Item, Integer> items;
    private ReentrantLock lock = new ReentrantLock();


    public ItemsCache(Long basketId) {
        this.basketId = basketId;
        items = new HashMap<Item, Integer>();
    }

    public Long getBasketId() {return this.basketId;}

    public Map<Item, Integer> getItems() {return this.items;}

    public void addItem(Item item, int quantity) throws InterruptedException {
        lock();
        items.put(item, quantity);
        unlock();
    }

    public void removeItem(Item item) throws InterruptedException {
        lock();
        items.remove(item);
        unlock();
    }

    public int getQuantity(Item item) {
        return items.get(item);
    }

    public boolean isItemExist(long itemId) {
        for(Item item: items.keySet()){
            if(item.getId().equals(itemId))
                return true;
        }
        return false;
    }

    public int getQuantity(long itemId) {
        for(Item item: items.keySet()){
            if(item.getId().equals(itemId))
                return items.get(item);
        }
        return -1;
    }

    public void lock() throws InterruptedException{
        lock.tryLock(10, TimeUnit.SECONDS);
    }

    public void unlock(){
        lock.unlock();
    }
}
