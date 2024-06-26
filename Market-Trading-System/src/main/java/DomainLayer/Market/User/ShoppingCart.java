package DomainLayer.Market.User;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Market.Util.InMemoryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

@Component
public class ShoppingCart {
    private final IRepository<Long,ShoppingBasket> baskets;

    public ShoppingCart(IRepository<Long,ShoppingBasket> baskets){
        this.baskets = baskets;
    }

    public String viewShoppingCart(IStoreFacade storeFacade) throws Exception{
        StringBuilder res = new StringBuilder();
        List<ShoppingBasket> l = baskets.findAll();
        for(ShoppingBasket s : l){
            storeFacade.calculateBasketPrice(s, null);
            res.append(s.toString());
        }
        return res.toString();
    }

    public void modifyShoppingCart(long basketId, long itemId, int quantity){
        ShoppingBasket sb = baskets.findById(basketId);
        if (sb == null){
            throw new IllegalArgumentException("no such basket");
        }
        sb.updateItemQuantity(itemId,quantity);
    }

    public Long addItemBasket(long storeId, long itemId, int quantity){
        ShoppingBasket sb = getShoppingBasket(storeId);
        sb.addItem(itemId,quantity);
        return sb.getId();
    }

    public List<ItemDTO> checkoutShoppingCart(String userName, IStoreFacade storeFacade, String code) throws Exception{
        List<ShoppingBasket> l = getBaskets();
        List<ItemDTO> items = new ArrayList<>();
        for(ShoppingBasket sb : l){
            try {
                //if(locks.get(sb.getStoreId()).tryLock(10, TimeUnit.SECONDS)) {
                if (!storeFacade.checkValidBasket(sb, userName)) {
                    throw new RuntimeException("couldn't complete checkout- invalid basket");
                }
                storeFacade.calculateBasketPrice(sb, code);
                List<ItemDTO> basketItems = sb.checkoutShoppingBasket(storeFacade);
                items.addAll(basketItems);
                //}
            }
            catch(InterruptedException e) {
                throw new InterruptedException("Unable to acquire lock for basket");
            } catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        return items;
    }

    public List<ShoppingBasket> getBaskets() {
        return baskets.findAll();
    }

    public void deleteShoppingBasket(long id){
        baskets.delete(id);
    }

    private ShoppingBasket getShoppingBasket(long storeId){
        ShoppingBasket sb;
        List<ShoppingBasket> currentBasket = baskets.findAll().stream().filter(basket -> basket.getStoreId() == storeId).toList();
        if(currentBasket.size() == 0) {
            sb = new ShoppingBasket(storeId);
            baskets.save(sb);
        }
        else
            sb = currentBasket.get(0);
        return sb;
    }

    public double getShoppingCartPrice(){
        double totalPrice = 0;
        for(ShoppingBasket sb: getBaskets()){
            totalPrice += sb.getBasketTotalPrice();
        }
        return totalPrice;
    }
    public void clear(){
        List<Long> ids = baskets.findAll().stream().map(ShoppingBasket::getId).toList();
        for (long id: ids
             ) {
            baskets.delete(id);
        }

    }

    public List<Long> getItems() {
        List<Long> items = new ArrayList<>();
        for(ShoppingBasket sb: getBaskets()){
            items.addAll(sb.getItems().keySet());
        }
        return items;
    }
}
