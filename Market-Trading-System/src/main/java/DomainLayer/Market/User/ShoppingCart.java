package DomainLayer.Market.User;

import API.SpringContext;
import DAL.BasketItem;
import DAL.ItemDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Repositories.BasketItemRepository;
import DomainLayer.Repositories.BasketRepository;
import DomainLayer.Repositories.DbBasketRepository;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

@Component
public class ShoppingCart {
    private final BasketRepository baskets;
    private final BasketItemRepository basketItemRepository;
    private final List<ShoppingBasket> shoppingBaskets;

    @Autowired
    public ShoppingCart(BasketRepository baskets, BasketItemRepository basketItemRepository){
        this.baskets = baskets;
        this.basketItemRepository = basketItemRepository;
        this.shoppingBaskets = new ArrayList<>();
    }

    public String viewShoppingCart(IStoreFacade storeFacade) throws Exception{
        StringBuilder res = new StringBuilder();
        //List<ShoppingBasket> l = baskets.findAll();
        for(ShoppingBasket s : shoppingBaskets){
            storeFacade.calculateBasketPrice(s, null);
            res.append(s.toString());
        }
        return res.toString();
    }

    public void modifyShoppingCart(long basketId, long itemId, int quantity){
        ShoppingBasket sb = getBacketIfExist(basketId);
        if (sb == null){
            throw new IllegalArgumentException("no such basket");
        }
        sb.updateItemQuantity(itemId,quantity);
        BasketItem basketItem = new BasketItem(sb.getId(), itemId, quantity);
        basketItemRepository.save(basketItem);
    }

    public Long addItemBasket(long storeId, long itemId, int quantity, String userName){
        ShoppingBasket sb = getShoppingBasket(storeId,userName);
        BasketItem basketItem = new BasketItem(sb.getId(), itemId, quantity);
        sb.addItem(itemId,quantity);
        basketItemRepository.save(basketItem);
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
        return shoppingBaskets;
        //return baskets.findAll();
    }

    public void deleteShoppingBasket(long id){
        Optional<ShoppingBasket> bas = shoppingBaskets.stream().filter(b -> b.getId() == id).findFirst();
        if (bas.isPresent()){
            shoppingBaskets.remove(bas.get());
        }
        baskets.deleteById(id);
    }

    private ShoppingBasket getShoppingBasket(long storeId, String userName){
        ShoppingBasket sb;
        //List<ShoppingBasket> currentBasket = baskets.findAll().stream().filter(basket -> basket.getStoreId() == storeId).toList();
        List<ShoppingBasket> currentBasket = shoppingBaskets.stream().filter(basket -> basket.getStoreId() == storeId).toList();
        if(currentBasket.isEmpty()) {
            sb = new ShoppingBasket(storeId, userName);
            baskets.save(sb);
            shoppingBaskets.add(sb);
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
        shoppingBaskets.clear();
//        List<Long> ids = baskets.findAll().stream().map(ShoppingBasket::getId).toList();
//        for (long id: ids
//             ) {
//            baskets.deleteById(id);
//        }
    }

    public void setShoppingBaskets(List<ShoppingBasket> shoppingBaskets) {
        this.shoppingBaskets.clear();
        if (shoppingBaskets != null) {
            this.shoppingBaskets.addAll(shoppingBaskets);
        }
    }

    public List<ShoppingBasket> loadBasketsForUser(String userName) {
        return baskets.findByUserName(userName);
    }

    private ShoppingBasket getBacketIfExist(long basketId){
        Optional<ShoppingBasket> bas = shoppingBaskets.stream().filter(b -> b.getId() == basketId).findFirst();
        if (bas.isPresent()){
            return bas.get();
        }
        return null;
    }
}
