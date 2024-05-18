package DomainLayer.Market.User;

public interface IUserFacade {
    public void createGuestSession();
    public void terminateGuestSession();
    public void register(String userName, String password);
    public boolean login(String userName, String password);
    public void logout(String userName);
    public String viewShoppingCart(String token);
    public void modifyShoppingCart(String token);
    public void checkoutShoppingCart(String token);
    public void assignStoreOwner(String userName, long storeId);
    public void assignStoreManager(String userName, long storeId);
    public boolean isPermitted(String permission, String userID);
}
