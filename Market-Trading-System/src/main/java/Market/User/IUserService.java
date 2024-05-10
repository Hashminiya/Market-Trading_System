package Market.User;

public interface IUserService{
    public void GuestEntry();
    public void GuestExit(int GuestID);
    public void register(String userName, String password);
    public void login(String userName, String password);
    public void logout();
    public String viewShoppingCart();
    public void modifyShoppingCart();
    public void checkoutShoppingCart();
}
