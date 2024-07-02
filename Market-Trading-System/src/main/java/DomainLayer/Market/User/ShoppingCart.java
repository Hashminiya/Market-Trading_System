package DomainLayer.Market.User;

import API.SpringContext;
import DAL.*;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Repositories.BasketItemRepository;
import DomainLayer.Repositories.BasketRepository;

import DomainLayer.Repositories.DbBasketRepository;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import jakarta.persistence.PostLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
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

    public Long addItemBasket(long storeId, long itemId, int quantity, IStoreFacade storeFacade, String userName) throws Exception {
        ShoppingBasket sb = getShoppingBasket(storeId,userName);
        boolean hasStock = storeFacade.addItemToShoppingBasket(sb, storeId, itemId, quantity);
        if(!hasStock) throw new Exception("Item's quantity isn't in stock");
        BasketItem basketItem = new BasketItem(sb.getId(), itemId, quantity);
        basketItemRepository.save(basketItem);

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
        //TODO:: Delete all from DB
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

    public ShoppingCartDTO getShoppingCartDTO(IStoreFacade storeFacade) throws Exception {
        List<ShoppingBasketDTO> basketDTOs = new ArrayList<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            storeFacade.calculateBasketPrice(basket, null);
            List<BasketItemDTO> itemDTOs = basket.getItems().entrySet().stream()
                    .map(entry -> {
                        long itemId = entry.getKey();
                        int quantity = entry.getValue();
                        double price = basket.getBasketTotalPrice();
                        //basket.getItemsPrice() is a map of itemId to price after discount
                        Map<Long, Double> itemIdToPriceAfterDiscount = basket.getItemsPrice();
                        double priceAfterDiscount = itemIdToPriceAfterDiscount.get(itemId);
                        Item item = storeFacade.getItem(itemId);

                        return new BasketItemDTO(itemId, item.getName(), quantity, basket.getStoreId(), item.getPrice(), item.getCategories(), item.getDescription(), priceAfterDiscount);
                    })
                    .collect(Collectors.toList());

            ShoppingBasketDTO basketDTO = new ShoppingBasketDTO(itemDTOs, basket.getBasketTotalPrice(), storeFacade.getStoreName(basket.getStoreId()), basket.getStoreId());
            basketDTOs.add(basketDTO);
        }
        double totalPrice = basketDTOs.stream().mapToDouble(ShoppingBasketDTO::getPrice).sum();
        return new ShoppingCartDTO(basketDTOs, totalPrice);
    }
}
