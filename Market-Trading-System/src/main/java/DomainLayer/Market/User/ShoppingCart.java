package DomainLayer.Market.User;

import DomainLayer.Market.IRepository;
import DomainLayer.Market.InMemoryRepository;

public class ShoppingCart {
    private IRepository<ShoppingBasket> baskets;

    public ShoppingCart(){
        baskets = new InMemoryShoppingBasketsRepository<ShoppingBasket>();
    }

    public String viewShoppingCart(){
        StringBuilder res = new StringBuilder();
        List<ShoppingBasket> l = baskets.findAll();
        for(ShoppingBasket s : l){
            res.append(s.toString());
            //TODO: overwrite the toString method in shoppingBasket class
        }
        return res.toString();
    }

    public void modifyShoppingCart(Item i, long basketId){
        ShoppingBasket sb = baskets.findById(basketId);
        sb.modifyItem(i);
        //TODO: replace the item? or maybe better to create few function for the different modification options?
    }

    public void removeBasket(long id){
        baskets.delete(id);
    }

    public void checkoutShoppingCart(){
        //TODO: what data the purchase need to perform the checkout?
        purchase.checkout();
    }
}
