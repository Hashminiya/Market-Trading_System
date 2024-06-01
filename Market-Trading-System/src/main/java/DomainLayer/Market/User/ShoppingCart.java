package DomainLayer.Market.User;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Market.Util.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final IRepository<Long,ShoppingBasket> baskets;

    public ShoppingCart(IRepository<Long,ShoppingBasket> baskets){
        this.baskets = baskets;
    }

    public String viewShoppingCart(){
        StringBuilder res = new StringBuilder();
        List<ShoppingBasket> l = baskets.findAll();
        for(ShoppingBasket s : l){
            res.append(s.toString());
        }
        return res.toString();
    }

    public void modifyShoppingCart(long basketId, long itemId, int quantity){
        if (baskets.findById(basketId) == null){
            throw new IllegalArgumentException("no such basket");
        }
        ShoppingBasket sb = getShoppingBasket(basketId);
        sb.updateItemQuantity(itemId,quantity);
    }

    public Long addItemBasket(long basketId, long itemId, int quantity){
        ShoppingBasket sb = getShoppingBasket(basketId);
        sb.addItem(itemId,quantity);
        return sb.getId();
    }

    public List<ItemDTO> checkoutShoppingCart(IStoreFacade storeFacade, String code) throws Exception{
        List<ShoppingBasket> l = getBaskets();
        List<ItemDTO> items = new ArrayList<ItemDTO>();
        for(ShoppingBasket sb : l){
            storeFacade.calculateBasketPrice(sb,code);
            List<ItemDTO> basketItems = sb.checkoutShoppingBasket(storeFacade);
            items.addAll(basketItems);
        }
        return items;
    }

    public List<ShoppingBasket> getBaskets() {
        return baskets.findAll();
    }

    public void deleteShoppingBasket(long id){
        baskets.delete(id);
    }

    private ShoppingBasket getShoppingBasket(long basketId){
        ShoppingBasket sb = baskets.findById(basketId);
        if(sb == null)
            sb = new ShoppingBasket(IdGenerator.generateId());
        baskets.save(sb);
        return sb;
    }

    public double getShoppingCartPrice(){
        double totalPrice = 0;
        for(ShoppingBasket sb: getBaskets()){
            totalPrice += sb.getBasketTotalPrice();
        }
        return totalPrice;
    }
}
