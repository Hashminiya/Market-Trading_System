package DomainLayer.Market.User;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Market.Util.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

@Component
public class ShoppingCart {
    private final IRepository<Long,ShoppingBasket> baskets;

    @Autowired
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
        Optional<ShoppingBasket> sb = baskets.findById(basketId);
        if (sb.isEmpty()){
            throw new IllegalArgumentException("no such basket");
        }
        sb.get().updateItemQuantity(itemId,quantity);
    }

    public Long addItemBasket(long storeId, long itemId, int quantity, IStoreFacade storeFacade) throws Exception {
        ShoppingBasket sb = getShoppingBasket(storeId);
        boolean hasStock = storeFacade.addItemToShoppingBasket(sb, storeId, itemId, quantity);
        if(!hasStock) throw new Exception("Item's quantity isn't in stock");
        return sb.getId();
    }

    public List<ItemDTO> checkoutShoppingCart(String userName, IStoreFacade storeFacade, String code) throws Exception{
        List<ShoppingBasket> l = getBaskets();
        List<ItemDTO> items = new ArrayList<>();
        List<ShoppingBasket> decreasedBaskets = new ArrayList<>();
        int num = 0;
        for(ShoppingBasket sb : l){
            try {
                decreasedBaskets.add(sb);
                if (!storeFacade.checkValidBasket(sb, userName)) {
                    System.out.println("check valid false "+ Thread.currentThread().getName());
                    num = 1;
                    storeFacade.restoreStock(decreasedBaskets);
                    num = 2;
                    throw new RuntimeException("couldn't complete checkout- invalid basket");
                }
                storeFacade.calculateBasketPrice(sb, code);
                List<ItemDTO> basketItems = sb.checkoutShoppingBasket(storeFacade);
                items.addAll(basketItems);
                //}
            }
            catch(Exception e) {
                if(e instanceof RuntimeException) {
                    System.out.println("throw1 " + Thread.currentThread().getName() + " ---- " + num);
                    throw new RuntimeException(e.getMessage());
                }
                storeFacade.restoreStock(decreasedBaskets);
                if(e instanceof InterruptedException) {
                    System.out.println("throw2 "+ Thread.currentThread().getName());
                    throw new InterruptedException("Unable to acquire lock for basket");
                }
                else {
                    System.out.println("throw3 "+ Thread.currentThread().getName());
                    throw new Exception(e.getMessage());
                }
            }
        }
        System.out.println("check valid true "+ Thread.currentThread().getName());
        return items;
    }

    public List<ShoppingBasket> getBaskets() {
        return baskets.findAll();
    }

    public void deleteShoppingBasket(long id){
        //baskets.delete(id);
        baskets.deleteById(id);
    }

    private ShoppingBasket getShoppingBasket(long storeId){
        ShoppingBasket sb;
        List<ShoppingBasket> currentBasket = baskets.findAll().stream().filter(basket -> basket.getStoreId() == storeId).toList();
        if(currentBasket.isEmpty()) {
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
            baskets.deleteById(id);
        }

    }
}
