package DomainLayer.Market.User;

import DomainLayer.Market.DataItem;
import DomainLayer.Market.IRepository;
import DomainLayer.Market.InMemoryRepository;

import java.util.HashMap;
import java.util.List;

public class InMemoryShoppingBasketsRepository<ShoppingBasket> implements IRepository {
    HashMap<Long,ShoppingBasket> shoppingBaskets;

    @Override
    public ShoppingBasket findById(long id) {
        if(!shoppingBaskets.containsKey(id))
            throw new IllegalArgumentException("there is no shoppingBasket with id " + id);
        return shoppingBaskets.get(id);
    }

    @Override
    public DataItem findByName(String name) {
        return null;
    }

    @Override
    public List<ShoppingBasket> findAll() {
        return shoppingBaskets.values().stream().toList();
    }

    @Override
    public void save(ShoppingBasket entity) {
        long id = entity.getId();
        if(shoppingBaskets.containsKey(id))
            throw new IllegalArgumentException("shopping basket with id " + id + "already exist");
        shoppingBaskets.put(id,entity);
    }

    @Override
    public void update(ShoppingBasket entity) {
        long id = entity.getId();
        if(!shoppingBaskets.containsKey(id))
            throw new IllegalArgumentException("shopping basket with id " + id + "does not exist");
        shoppingBaskets.put(id,entity);
    }

    @Override
    public void delete(ShoppingBasket entity) {
        shoppingBaskets.remove(ShoppingBasket.getId());
    }
}
