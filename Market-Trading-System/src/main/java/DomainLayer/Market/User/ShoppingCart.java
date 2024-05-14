package DomainLayer.Market.User;

public class ShoppingCart {
    private InMemory<ShoppingBasket> baskets;

    public ShoppingCart(){
        baskets = new InMemory<ShoppingBasket>();
    }

    public String viewShoppingCart(){
        List<ShoppingBasket> l = baskets.findAll();
        for(ShoppingBasket s : l){
            res += s.getStoreName();
            res += s.getItems();
            //TODO: replace with s.toString and to overwrite the toString method in shoppingBasket class
        }
        return res;
    }

    public void modifyShoppingCart(Item i, long basketId){
        ShoppingBasket sb = baskets.findById(basketId);
        sb.modifyItem(i);
        //TODO: replace the item? or maybe better to create few function for the different modification options?
    }

    public void checkoutShoppingCart(){
        //TODO: what data the purchase need to perform the checkout?
        purchase.checkout();
    }
}
