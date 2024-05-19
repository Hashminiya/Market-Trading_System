package DomainLayer.Market.User;

public interface IUserFacade {
    public void createGuestSession();
    public void terminateGuestSession(String userName);
    public void register(String userName, String password) throws Exception;
    public boolean login(String userName, String password);
    public void logout(String userName);
    public String viewShoppingCart(String token);
    public void modifyShoppingCart(String token);
    public void checkoutShoppingCart(String token);
    public boolean checkPermission(String userName);
    public void assignStoreOwner(String userName, long storeId);
    public void assignStoreManager(String userName, long storeId);
    public List<Permission> getUserPermission(String userName);
}
