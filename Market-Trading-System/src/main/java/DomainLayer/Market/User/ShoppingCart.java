package DomainLayer.Market.User;

import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;

public class ShoppingCart {
    private IRepository<Long, ShoppingBasket> baskets;//long is the storeID

    public ShoppingCart(IRepository iRepository){
        baskets = iRepository;
    }

    public String viewShoppingCart(){
        String res = "";
        List<ShoppingBasket> l = baskets.findAll();
        for(ShoppingBasket s : l){
            res += s.toString();
            //TODO: overwrite the toString method in shoppingBasket class
        }
        return res;
    }

    public void modifyShoppingCart(Item i, long basketId){
        ShoppingBasket sb = baskets.findById(basketId);
        sb.modifyItem(i);
        //TODO: replace the item? or maybe better to create few function for the different modification options?
    }

    public void addShoppingBasket()

    public void checkoutShoppingCart(){
        //TODO: what data the purchase need to perform the checkout?

    }
}
